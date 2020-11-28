package dev.rigel.gradle.tink.core

import com.google.crypto.tink.*
import java.io.File

class TinkKeysetReader(private val keyStorageFormat: KeyStorageFormat) {

    fun readKeyset(file: File): KeysetHandle {
        val keysetReader = when(keyStorageFormat) {
            KeyStorageFormat.BINARY -> {
                BinaryKeysetReader.withFile(file)
            }
            KeyStorageFormat.JSON -> {
                JsonKeysetReader.withFile(file)
            }
        }

        return CleartextKeysetHandle.read(keysetReader)
    }

}