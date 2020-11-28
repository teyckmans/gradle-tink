package dev.rigel.gradle.tink.core

import com.google.crypto.tink.BinaryKeysetWriter
import com.google.crypto.tink.CleartextKeysetHandle
import com.google.crypto.tink.JsonKeysetWriter
import com.google.crypto.tink.KeysetHandle
import java.io.File

class TinkKeysetWriter(private val keyStorageFormat: KeyStorageFormat) {

    fun writeKeyset(keySetHandle: KeysetHandle, file: File) {
        val keySetWriter = when(keyStorageFormat) {
            KeyStorageFormat.BINARY -> {
                BinaryKeysetWriter.withFile(file)
            }
            KeyStorageFormat.JSON -> {
                JsonKeysetWriter.withFile(file)
            }
        }

        CleartextKeysetHandle.write(keySetHandle, keySetWriter)
    }
}