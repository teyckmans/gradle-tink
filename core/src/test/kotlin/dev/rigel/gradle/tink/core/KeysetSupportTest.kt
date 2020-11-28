package dev.rigel.gradle.tink.core

import org.hamcrest.CoreMatchers.*
import org.hamcrest.MatcherAssert.assertThat
import java.io.File
import java.nio.file.Files
import java.util.*
import kotlin.test.Test;
import kotlin.test.AfterTest;

class KeysetSupportTest {

    private val keysetSupport = KeysetSupport()

    private val jsonKeysetInfo = TinkKeysetInfo(
            "AES128_CTR_HMAC_SHA256",
            File("build/keyset.json"),
            KeyStorageFormat.JSON
    )
    private val binaryKeysetInfo = TinkKeysetInfo(
            "AES256_CTR_HMAC_SHA256",
            File("build/keyset.bin"),
            KeyStorageFormat.BINARY
    )
    private val randomFilenamePart = UUID.randomUUID().toString()
    private val testInputFile = File("build/" + randomFilenamePart + "_input.txt")
    private val testEncryptOutputFile = File("build/" + randomFilenamePart + "_encrypt_output.txt")
    private val testDecryptOutputFile = File("build/" + randomFilenamePart + "_decrypt_output.txt")

    private fun withKey(keyInfo: TinkKeysetInfo, action: (keysetInfo: TinkKeysetInfo) -> Unit) {
        Files.deleteIfExists(keyInfo.keyFile.toPath())

        val keysetHandle = keysetSupport.createKey(keyInfo)

        assertThat(keysetHandle, notNullValue())

        assertThat(keyInfo.keyFile.exists(), `is`(true))

        action(keyInfo)
    }

    private fun canEncryptDecryptFile(keyInfo: TinkKeysetInfo) {
        withKey(keyInfo) { keysetInfo ->
            testInputFile.writeText("some random text file contents.\nnot really exciting is it\n")

            keysetSupport.encryptFile(
                    keysetInfo,
                    testInputFile,
                    testEncryptOutputFile,
                    " ".toByteArray()
            )

            keysetSupport.decryptFile(
                    keysetInfo,
                    testEncryptOutputFile,
                    testDecryptOutputFile,
                    " ".toByteArray()
            )

            assertThat(testInputFile.readText(), equalTo(testDecryptOutputFile.readText()))
        }
    }

    @Test fun `can create a json keyset and encrypt and decrypt a file with it`() {
        canEncryptDecryptFile(jsonKeysetInfo)
    }

    @Test fun `can create a binary keyset and encrypt and decrypt a file with it`() {
        canEncryptDecryptFile(binaryKeysetInfo)
    }

    @Test fun `can create a json keyset and encrypt and decrypt strings with it`() {
        withKey(jsonKeysetInfo) { keysetInfo ->
            val originalValue = "this is the original"

            val encryptedValue = keysetSupport.encryptString(keysetInfo, originalValue, " ".toByteArray())

            assertThat(encryptedValue, `is`(not(equalTo(originalValue))))

            val decryptedValue = keysetSupport.decryptString(keysetInfo, encryptedValue, " ".toByteArray())

            assertThat(decryptedValue, equalTo(originalValue))
        }
    }

    @Test fun `can create a json keyset and encrypt and decrypt property values with it`() {
        withKey(jsonKeysetInfo) { keysetInfo ->
            val mySensitiveProperty = "this is a super secret password"
            val myOtherSensitiveProperty = "top secret stuff here"

            testInputFile.writeText("""
                mySensitiveProperty=$mySensitiveProperty
                somethingSecond=222
                myOtherSensitiveProperty=$myOtherSensitiveProperty
                
                somethingLast=999
            """.trimIndent())

            val sensitiveReadBeforeEncrypt = keysetSupport.readPropertyValue(keysetInfo, testInputFile, "mySensitiveProperty")
            assertThat(sensitiveReadBeforeEncrypt, equalTo(mySensitiveProperty))

            val otherSensitiveReadBeforeEncrypt = keysetSupport.readPropertyValue(keysetInfo, testInputFile, "myOtherSensitiveProperty")
            assertThat(otherSensitiveReadBeforeEncrypt, equalTo(myOtherSensitiveProperty))

            keysetSupport.encryptPropertyValue(keysetInfo, testInputFile, "mySensitiveProperty")
            keysetSupport.encryptPropertyValue(keysetInfo, testInputFile, "myOtherSensitiveProperty")

            val linesAfterEncrypting = testInputFile.readLines()
            assertThat(linesAfterEncrypting[0], startsWith("mySensitiveProperty="))
            assertThat(linesAfterEncrypting[0], not(endsWith(mySensitiveProperty)))
            assertThat(linesAfterEncrypting[1], startsWith("somethingSecond="))
            assertThat(linesAfterEncrypting[2], startsWith("myOtherSensitiveProperty="))
            assertThat(linesAfterEncrypting[2], not(endsWith(myOtherSensitiveProperty)))
            assertThat(linesAfterEncrypting[3], equalTo(""))
            assertThat(linesAfterEncrypting[4], startsWith("somethingLast="))

            val sensitiveReadAfterEncrypt = keysetSupport.readPropertyValue(keysetInfo, testInputFile, "mySensitiveProperty")
            assertThat(sensitiveReadAfterEncrypt, equalTo(mySensitiveProperty))

            val otherSensitiveReadAfterEncrypt = keysetSupport.readPropertyValue(keysetInfo, testInputFile, "myOtherSensitiveProperty")
            assertThat(otherSensitiveReadAfterEncrypt, equalTo(myOtherSensitiveProperty))

            keysetSupport.decryptPropertyValue(keysetInfo, testInputFile, "mySensitiveProperty")
            keysetSupport.decryptPropertyValue(keysetInfo, testInputFile, "myOtherSensitiveProperty")

            val linesAfterDecrypting = testInputFile.readLines()
            assertThat(linesAfterDecrypting[0], equalTo("mySensitiveProperty=$mySensitiveProperty"))
            assertThat(linesAfterDecrypting[1], equalTo("somethingSecond=222"))
            assertThat(linesAfterDecrypting[2], equalTo("myOtherSensitiveProperty=$myOtherSensitiveProperty"))
            assertThat(linesAfterDecrypting[3], equalTo(""))
            assertThat(linesAfterDecrypting[4], equalTo("somethingLast=999"))
        }
    }

    @Test fun `can wrap and unwrap`() {
        val originalValue = "this is the original"

        assertThat(keysetSupport.isWrapped(originalValue), `is`(false))

        val wrapped = keysetSupport.wrapStringValue(originalValue)
        assertThat(keysetSupport.wrapStringValue(wrapped), equalTo(wrapped))

        assertThat(keysetSupport.isWrapped(wrapped), `is`(true))

        val unwrapped = keysetSupport.unwrapStringValue(wrapped)
        assertThat(keysetSupport.unwrapStringValue(unwrapped), equalTo(unwrapped))

        assertThat(keysetSupport.isWrapped(unwrapped), `is`(false))
        assertThat(unwrapped, equalTo(originalValue))
    }

    @AfterTest fun cleanup() {
        Files.deleteIfExists(jsonKeysetInfo.keyFile.toPath())
        Files.deleteIfExists(binaryKeysetInfo.keyFile.toPath())
        Files.deleteIfExists(testInputFile.toPath())
        Files.deleteIfExists(testEncryptOutputFile.toPath())
        Files.deleteIfExists(testDecryptOutputFile.toPath())
    }
}