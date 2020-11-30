package dev.rigel.gradle.tink.tasks

import dev.rigel.gradle.tink.core.KeyStorageFormat
import dev.rigel.gradle.tink.core.KeysetSupport
import dev.rigel.gradle.tink.core.TinkKeysetInfo
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.options.Option

open class CreateKeySetTask : GradleTinkTask() {

    @Option(option = "keyTemplate", description = "Keyset template name (default: AES256_CTR_HMAC_SHA256)")
    @get:Input
    var keyTemplate = "AES256_CTR_HMAC_SHA256"

    @Option(option = "keysetFile", description = "Filename to store the generation keyset to.")
    @get:Input
    var keysetFilename = ""

    @Option(option = "keyStorageFormat", description = "Options are json or binary (default: json)")
    @get:Input
    var keyStorageFormat = "json"

    @TaskAction
    fun createKeySet() {
        val keysetFile = this.project.file(keysetFilename)

        super.keysetSupport().createKey(
                TinkKeysetInfo(
                        this.keyTemplate,
                        keysetFile,
                        KeyStorageFormat.valueOf(this.keyStorageFormat.toUpperCase())
                )
        )
    }
}