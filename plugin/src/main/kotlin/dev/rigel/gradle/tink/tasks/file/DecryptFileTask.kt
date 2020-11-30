package dev.rigel.gradle.tink.tasks.file

import dev.rigel.gradle.tink.core.KeysetSupport
import dev.rigel.gradle.tink.core.TinkKeysetInfo
import java.io.File

open class DecryptFileTask : FileOperationTask() {
    override fun doFileOperation(tinkKeysetInfo: TinkKeysetInfo, keysetSupport: KeysetSupport, associatedData: ByteArray, inputFile: File, outputFile: File) {
        keysetSupport.decryptFile(
                tinkKeysetInfo,
                inputFile,
                outputFile,
                associatedData
        )
    }
}