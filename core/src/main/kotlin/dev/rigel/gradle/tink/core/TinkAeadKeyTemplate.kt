package dev.rigel.gradle.tink.core

import com.google.crypto.tink.Aead
import com.google.crypto.tink.KeysetHandle
import com.google.crypto.tink.aead.AeadConfig


abstract class TinkAeadKeyTemplate(templateName: String) : TinkKeyTemplate(templateName) {
    override fun doRegister() {
        AeadConfig.register()
    }

    override fun encrypt(keysetHandle: KeysetHandle,
                         plainText: ByteArray,
                         associatedData: ByteArray): ByteArray {
        return keysetHandle
                .getPrimitive(Aead::class.java)
                .encrypt(plainText, associatedData)
    }

    override fun decrypt(keysetHandle: KeysetHandle,
                         plainText: ByteArray,
                         associatedData: ByteArray): ByteArray {
        return keysetHandle
                .getPrimitive(Aead::class.java)
                .decrypt(plainText, associatedData)
    }
}