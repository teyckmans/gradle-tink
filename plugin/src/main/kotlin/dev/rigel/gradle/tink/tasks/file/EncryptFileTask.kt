package dev.rigel.gradle.tink.tasks.file

import dev.rigel.gradle.tink.core.KeysetSupport
import dev.rigel.gradle.tink.core.TinkKeysetInfo
import java.io.File

open class EncryptFileTask : FileOperationTask() {

    override fun doFileOperation(tinkKeysetInfo: TinkKeysetInfo, keysetSupport: KeysetSupport, associatedData: ByteArray, inputFile: File, outputFile: File) {
        keysetSupport.encryptFile(
                tinkKeysetInfo,
                inputFile,
                outputFile,
                associatedData
        )
    }
}