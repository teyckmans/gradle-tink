package dev.rigel.gradle.tink.tasks

import dev.rigel.gradle.tink.core.KeyStorageFormat
import dev.rigel.gradle.tink.core.KeysetSupport
import dev.rigel.gradle.tink.core.TinkKeysetInfo
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.options.Option

abstract class OperationTask : GradleTinkTask() {

    @Option(option = "keyTemplate", description = "Template name (default: AES256_CTR_HMAC_SHA256)")
    @get:Input
    var keyTemplate = "AES256_CTR_HMAC_SHA256"

    @Option(option = "keysetFile", description = "Keyset file to use.")
    @get:Input
    var keysetFilename = ""

    @Option(option = "keyStorageFormat", description = "Keyset storage format Options are json or binary (default: json)")
    @get:Input
    var keyStorageFormat = "json"

    @Option(option = "associatedDataFile", description = "When encrypting a plaintext one can optionally provide associated data that should be authenticated but not encrypted.")
    @get:Input
    var associatedDataFilename = ""

    protected fun tinkKeysetInfo(): TinkKeysetInfo {
        val keysetFile = this.project.file(this.keysetFilename)

        check(keyTemplate.isNotEmpty()) {
            "--keyTemplate can't be empty."
        }
        check(keysetFile.exists()) {
            "--keysetFile does not exist was: $keysetFile"
        }
        check(keyStorageFormat.toLowerCase() == "json"
                || keyStorageFormat.toLowerCase() == "binary") {
            "--keyStorageFormat can only be json or binary but was: $keyStorageFormat"
        }



        return TinkKeysetInfo(
                this.keyTemplate,
                keysetFile,
                KeyStorageFormat.valueOf(this.keyStorageFormat.toUpperCase())
        )
    }

    protected fun associatedData(): ByteArray {
        return if (associatedDataFilename == "") {
            byteArrayOf()
        } else {
            val associatedDataFile = this.project.file(this.associatedDataFilename)

            check(associatedDataFile.exists()) {
                "--associatedDataFile does not exist was: $associatedDataFile"
            }

            associatedDataFile.readBytes()
        }
    }

    @TaskAction
    fun operationTask() {
        val tinkKeysetInfo = this.tinkKeysetInfo()
        val keysetSupport = super.keysetSupport()
        val associatedData = this.associatedData()

        this.doOperation(tinkKeysetInfo, keysetSupport, associatedData)
    }

    abstract fun doOperation(tinkKeysetInfo: TinkKeysetInfo,
                             keysetSupport: KeysetSupport,
                             associatedData: ByteArray)
}