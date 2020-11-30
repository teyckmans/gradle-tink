package dev.rigel.gradle.tink.tasks.property

import dev.rigel.gradle.tink.core.KeysetSupport
import dev.rigel.gradle.tink.core.TinkKeysetInfo
import dev.rigel.gradle.tink.tasks.OperationTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.options.Option
import java.io.File

abstract class PropertyOperationTask : OperationTask() {

    @Option(option = "propertiesFile", description = "Property file to use.")
    @get:Input
    var propertiesFilename = ""

    @Option(option = "propertyKey", description = "Property key to encrypt/decrypt")
    @get:Input
    var propertyKey = ""

    override fun doOperation(tinkKeysetInfo: TinkKeysetInfo, keysetSupport: KeysetSupport, associatedData: ByteArray) {
        val propertiesFile = this.project.file(this.propertiesFilename)

        check(propertiesFile.exists()) {
            "--propertiesFile does not exist was: $propertiesFile"
        }
        check(propertyKey != "") {
            "--propertyKey can't be empty"
        }

        doPropertyOperation(tinkKeysetInfo, keysetSupport, associatedData, propertiesFile, propertyKey)
    }

    abstract fun doPropertyOperation(tinkKeysetInfo: TinkKeysetInfo,
                                     keysetSupport: KeysetSupport,
                                     associatedData: ByteArray,
                                     propertiesFile: File,
                                     propertyKey: String)
}