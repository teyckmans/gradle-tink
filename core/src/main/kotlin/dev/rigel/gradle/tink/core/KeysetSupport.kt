package dev.rigel.gradle.tink.core

import com.google.crypto.tink.*
import java.io.File
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets
import java.util.*

class KeysetSupport(val charset: Charset = StandardCharsets.UTF_8,
                    val encryptedValuePrefix : String = "ENC[",
                    val encryptedValueSuffix : String = "]") {

    fun createKey(keysetInfo: TinkKeysetInfo): KeysetHandle {
        val keyTemplate = TinkKeyTemplates.tinkKeyTemplate(keysetInfo.keyTemplateName)
                .keyTemplate()

        val keysetHandle = KeysetHandle
                .generateNew(keyTemplate)

        TinkKeysetWriter(keysetInfo.keyStorageFormat)
                .writeKeyset(keysetHandle, keysetInfo.keyFile)

        println("Keyset written to ${keysetInfo.keyFile} [keyTemplate: ${keysetInfo.keyTemplateName}, keyStorageFormat: ${keysetInfo.keyStorageFormat}]")

        return keysetHandle
    }

    private fun doWithKeyset(keysetInfo: TinkKeysetInfo, action: (tinkKeyTemplate: TinkKeyTemplate, keysetHandle: KeysetHandle) -> ByteArray): ByteArray {
        val tinkKeyTemplate = TinkKeyTemplates.tinkKeyTemplate(keysetInfo.keyTemplateName)

        val tinkKeysetReader = TinkKeysetReader(keysetInfo.keyStorageFormat)

        val keysetHandle = tinkKeysetReader.readKeyset(keysetInfo.keyFile)

        return action(tinkKeyTemplate, keysetHandle)
    }

    /**
     * encrypt a value.
     */
    fun encryptValue(keysetInfo: TinkKeysetInfo,
                     inputValue: ByteArray,
                     associatedData: ByteArray = byteArrayOf()): ByteArray {
        return doWithKeyset(keysetInfo) { tinkKeyTemplate, keysetHandle ->
            tinkKeyTemplate.encrypt(
                    keysetHandle,
                    inputValue,
                    associatedData
            )
        }
    }

    /**
     * decrypt a value.
     */
    fun decryptValue(keysetInfo: TinkKeysetInfo,
                     inputValue: ByteArray,
                     associatedData: ByteArray = byteArrayOf()): ByteArray {
        return doWithKeyset(keysetInfo) { tinkKeyTemplate, keysetHandle ->
            tinkKeyTemplate.decrypt(
                    keysetHandle,
                    inputValue,
                    associatedData
            )
        }
    }

    /**
     * helper to encrypt a string value.
     */
    fun encryptString(keysetInfo: TinkKeysetInfo,
                      inputString: String,
                      associatedData: ByteArray = byteArrayOf()): String {
        val cypherbytes = encryptValue(keysetInfo, inputString.toByteArray(charset), associatedData)

        return Base64.getEncoder().encodeToString(cypherbytes)
    }

    fun decryptString(keysetInfo: TinkKeysetInfo,
                      inputString: String,
                      associatedData: ByteArray = byteArrayOf()): String {
        return decryptValue(keysetInfo, Base64.getDecoder().decode(inputString), associatedData)
                .toString(charset)
    }

    /**
     * helper to encrypt a file.
     */
    fun encryptFile(keysetInfo: TinkKeysetInfo,
                    inputFile: File,
                    outputFile: File,
                    associatedData: ByteArray = byteArrayOf()
    ) {
        val ciphertext = encryptValue(keysetInfo, inputFile.readBytes(), associatedData)
        outputFile.writeBytes(ciphertext)
    }

    /**
     * helper to decrypt a file.
     */
    fun decryptFile(keysetInfo: TinkKeysetInfo,
                    inputFile: File,
                    outputFile: File,
                    associatedData: ByteArray = byteArrayOf()
    ) {
        val ciphertext = decryptValue(keysetInfo, inputFile.readBytes(), associatedData)
        outputFile.writeBytes(ciphertext)
    }


    /**
     * helper to encrypt a value in a property file.
     */
    fun encryptPropertyValue(keysetInfo: TinkKeysetInfo,
                             propertyFile: File,
                             propertyKey: String,
                             associatedData: ByteArray = byteArrayOf()) {
        val currentValue = PropertiesSupport.readConfig(propertyFile, propertyKey)

        check(currentValue != null) { "$propertyKey is not available in $propertyFile"}
        check(!isWrapped(currentValue)) { "$propertyKey is already encrypted in $propertyFile" }

        PropertiesSupport.changeConfig(propertyFile, propertyKey) { propertyValue ->
            wrapStringValue(encryptString(keysetInfo, propertyValue, associatedData))
        }
    }

    /**
     * helper to read a value in a property file.
     *
     * If the value is encrypted this method will return the decrypted value
     * otherwise the plaintext value is returned.
     */
    fun readPropertyValue(keysetInfo: TinkKeysetInfo,
                          propertyFile: File,
                          propertyKey: String,
                          associatedData: ByteArray = byteArrayOf()): String {
        val currentValue = PropertiesSupport.readConfig(propertyFile, propertyKey)

        check(currentValue != null) { "$propertyKey is not available in $propertyFile"}

        return if (isWrapped(currentValue)) {
            decryptString(keysetInfo, unwrapStringValue(currentValue), associatedData)
        } else {
            currentValue
        }
    }

    /**
     * helper to decrypt a value in a property file.
     */
    fun decryptPropertyValue(keysetInfo: TinkKeysetInfo,
                             propertyFile: File,
                             propertyKey: String,
                             associatedData: ByteArray = byteArrayOf()) {
        val currentValue = PropertiesSupport.readConfig(propertyFile, propertyKey)

        check(currentValue != null) { "$propertyKey is not available in $propertyFile"}
        check(isWrapped(currentValue)) { "$propertyKey is already not encrypted in $propertyFile" }

        PropertiesSupport.changeConfig(propertyFile, propertyKey) { propertyValue ->
            decryptString(keysetInfo, unwrapStringValue(propertyValue), associatedData)
        }
    }

    fun wrapStringValue(value: String): String {
        return if (isWrapped(value)) {
            value
        } else {
            return encryptedValuePrefix + value + encryptedValueSuffix
        }
    }

    fun unwrapStringValue(value: String): String {
        return if (isWrapped(value)) {
            value.substring(encryptedValuePrefix.length, value.length - encryptedValueSuffix.length)
        } else {
            value
        }
    }

    fun isWrapped(value: String): Boolean {
        return value.startsWith(encryptedValuePrefix)
                && value.endsWith(encryptedValueSuffix)
    }
}