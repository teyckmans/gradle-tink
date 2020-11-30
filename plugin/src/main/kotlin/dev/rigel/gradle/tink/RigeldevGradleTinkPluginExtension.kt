package dev.rigel.gradle.tink

import dev.rigel.gradle.tink.core.TinkKeyTemplate
import dev.rigel.gradle.tink.core.TinkKeyTemplates
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets

/**
 * Allows to configure the parameters of the dev.rigel.gradle.tink.core.KeysetSupport class.
 *
 * Additionally you can register custom dev.rigel.gradle.tink.core.TinkKeyTemplate(s).
 */
open class RigeldevGradleTinkPluginExtension {

    /**
     * Used then converting Strings from/to ByteArrays.
     */
    val charset: Charset = StandardCharsets.UTF_8

    /**
     * Prefix used when encrypting values in a properties file.
     */
    val encryptedValuePrefix : String = "ENC["

    /**
     * Suffix used when encrypting values in a properties file.
     */
    val encryptedValueSuffix : String = "]"

    /**
     * Register custom dev.rigel.gradle.tink.core.TinkKeyTemplate(s).
     */
    fun register(vararg tinkKeyTemplates: TinkKeyTemplate) {
        TinkKeyTemplates.register(*tinkKeyTemplates)
    }
}