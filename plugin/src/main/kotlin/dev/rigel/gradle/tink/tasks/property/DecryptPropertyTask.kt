package dev.rigel.gradle.tink.tasks.property

import dev.rigel.gradle.tink.core.KeysetSupport
import dev.rigel.gradle.tink.core.TinkKeysetInfo
import java.io.File

open class DecryptPropertyTask : PropertyOperationTask() {
    override fun doPropertyOperation(tinkKeysetInfo: TinkKeysetInfo, keysetSupport: KeysetSupport, associatedData: ByteArray, propertiesFile: File, propertyKey: String) {
        keysetSupport.decryptPropertyValue(
                tinkKeysetInfo,
                propertiesFile,
                propertyKey,
                associatedData
        )
    }
}