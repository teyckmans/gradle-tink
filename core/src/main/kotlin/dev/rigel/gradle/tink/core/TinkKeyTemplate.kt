package dev.rigel.gradle.tink.core

import com.google.crypto.tink.KeyTemplate
import com.google.crypto.tink.KeysetHandle

/**
 * Knows what TinkConfig to register and how to retrieve a specific KeyTemplate identified by a name.
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
