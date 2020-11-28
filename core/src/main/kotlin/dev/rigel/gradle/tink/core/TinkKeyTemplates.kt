package dev.rigel.gradle.tink.core

import com.google.crypto.tink.KeyTemplate
import com.google.crypto.tink.aead.AesCtrHmacAeadKeyManager

/**
 * Registry for mapping a name to a TinkKeyTemplate.
 */
object TinkKeyTemplates {
    private val keyTemplateNameMapping = mutableMapOf<String, TinkKeyTemplate>()

    init {
        register(
                object : TinkAeadKeyTemplate("AES128_CTR_HMAC_SHA256") {
                    override fun retrieveKeyTemplate(): KeyTemplate {
                        return AesCtrHmacAeadKeyManager.aes128CtrHmacSha256Template()
                    }
                },
                object : TinkAeadKeyTemplate("AES256_CTR_HMAC_SHA256") {
                    override fun retrieveKeyTemplate(): KeyTemplate {
                        return AesCtrHmacAeadKeyManager.aes256CtrHmacSha256Template()
                    }
                }
        )
    }

    fun tinkKeyTemplate(keyTemplateName: String): TinkKeyTemplate {
        return keyTemplateNameMapping[keyTemplateName]!!
    }

    fun register(vararg tinkKeyTemplates: TinkKeyTemplate) {
        for (tinkKeyTemplate in tinkKeyTemplates) {
            keyTemplateNameMapping[tinkKeyTemplate.templateName] = tinkKeyTemplate
        }
    }
}