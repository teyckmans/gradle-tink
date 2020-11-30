package dev.rigel.gradle.tink.tasks.string

import dev.rigel.gradle.tink.core.KeysetSupport
import dev.rigel.gradle.tink.core.TinkKeysetInfo

open class EncryptStringTask : StringOperationTask(
        { it.readPassword("> Provide value to encrypt:").toString() }
) {
    override fun doStringOperation(tinkKeysetInfo: TinkKeysetInfo, keysetSupport: KeysetSupport, associatedData: ByteArray, inputString: String) {
        val encryptedString = keysetSupport.encryptString(
                tinkKeysetInfo,
                inputString,
                associatedData
        )

        println("[encrypted] $encryptedString")
    }
}