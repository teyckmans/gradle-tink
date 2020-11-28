package dev.rigel.gradle.tink

import dev.rigel.gradle.tink.core.KeyStorageFormat
import dev.rigel.gradle.tink.core.KeysetSupport
import dev.rigel.gradle.tink.core.TinkKeysetInfo
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.options.Option

open class CreateKeySetTask : GradleTinkTask() {

    @Option(option = "template", description = "Template name (default: AES256_CTR_HMAC_SHA256)")
    @get:Input
    var keyTemplate = "AES256_CTR_HMAC_SHA256"

    @Option(option = "keysetFile", description = "Filename to save the generation key into")
    @get:Input
    var keysetFile = ""

    @Option(option = "keyStorageFormat", description = "Options are json or binary")
    @get:Input
    var keyStorageFormat = "json"

    @TaskAction
    fun createKeySet() {
        val targetFile = this.project.file(keysetFile)

        val keysetHandle = KeysetSupport().createKey(
                TinkKeysetInfo(
                        this.keyTemplate,
                        targetFile,
                        KeyStorageFormat.valueOf(this.keyStorageFormat.toUpperCase())
                )
        )
    }
}