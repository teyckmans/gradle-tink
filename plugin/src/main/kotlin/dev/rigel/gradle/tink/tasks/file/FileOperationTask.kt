package dev.rigel.gradle.tink.tasks.file

import dev.rigel.gradle.tink.core.KeysetSupport
import dev.rigel.gradle.tink.core.TinkKeysetInfo
import dev.rigel.gradle.tink.tasks.OperationTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.options.Option
import java.io.File

abstract class FileOperationTask : OperationTask() {

    @Option(option = "inputFile", description = "File to encrypt.")
    @get:Input
    var inputFilename = ""

    @Option(option = "outputFile", description = "File to save the encryption result to.")
    @get:OutputFile
    var outputFilename = ""

    override fun doOperation(tinkKeysetInfo: TinkKeysetInfo, keysetSupport: KeysetSupport, associatedData: ByteArray) {
        val inputFile = this.project.file(this.inputFilename)

        check(inputFile.exists()) {
            "--inputFile does not exist was: $inputFile"
        }

        val outputFile = if (this.outputFilename == "") {
            inputFile
        } else {
            this.project.file(this.outputFilename)
        }

        doFileOperation(tinkKeysetInfo, keysetSupport, associatedData, inputFile, outputFile)
    }

    abstract fun doFileOperation(tinkKeysetInfo: TinkKeysetInfo,
                               keysetSupport: KeysetSupport,
                               associatedData: ByteArray,
                               inputFile: File,
                               outputFile: File)
}