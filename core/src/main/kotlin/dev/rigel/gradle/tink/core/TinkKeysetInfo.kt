package dev.rigel.gradle.tink.core

import java.io.File

data class TinkKeysetInfo(val keyTemplateName: String,
                          val keyFile: File,
                          val keyStorageFormat: KeyStorageFormat = KeyStorageFormat.JSON)
