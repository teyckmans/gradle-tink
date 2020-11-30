package dev.rigel.gradle.tink.core

import com.google.crypto.tink.KeyTemplate
import com.google.crypto.tink.KeysetHandle

/**
 * Named Tink KeyTemplate.
 * Knows:
 * - what TinkConfig to register
 * - how to retrieve a KeyTemplate.
 * - how to encrypt using a KeysetHandle.
 * - how to decrypt using a KeysetHandle.
 */
abstract class TinkKeyTemplate(val templateName: String) {

    fun keyTemplate(): KeyTemplate {
        doRegister()
        return retrieveKeyTemplate()
    }

    protected abstract fun doRegister()

    protected abstract fun retrieveKeyTemplate() : KeyTemplate

    abstract fun encrypt(keysetHandle: KeysetHandle,
                         plainText: ByteArray,
                         associatedData: ByteArray = byteArrayOf()): ByteArray

    abstract fun decrypt(keysetHandle: KeysetHandle,
                         plainText: ByteArray,
                         associatedData: ByteArray = byteArrayOf()): ByteArray
}
