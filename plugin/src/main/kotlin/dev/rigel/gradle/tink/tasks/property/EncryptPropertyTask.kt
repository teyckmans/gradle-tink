package dev.rigel.gradle.tink.tasks.property

import dev.rigel.gradle.tink.core.KeysetSupport
import dev.rigel.gradle.tink.core.TinkKeysetInfo
import java.io.File

open class EncryptPropertyTask : PropertyOperationTask() {
    override fun doPropertyOperation(tinkKeysetInfo: TinkKeysetInfo, keysetSupport: KeysetSupport, associatedData: ByteArray, propertiesFile: File, propertyKey: String) {
        keysetSupport.encryptPropertyValue(
                tinkKeysetInfo,
                propertiesFile,
                propertyKey,
                associatedData
        )
    }
}