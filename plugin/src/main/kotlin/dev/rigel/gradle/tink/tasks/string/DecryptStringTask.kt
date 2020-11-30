package dev.rigel.gradle.tink.tasks.string

import dev.rigel.gradle.tink.core.KeysetSupport
import dev.rigel.gradle.tink.core.TinkKeysetInfo

open class DecryptStringTask: StringOperationTask(
        { it.readLine("> Provide value to decrypt:") }
) {
    override fun doStringOperation(tinkKeysetInfo: TinkKeysetInfo, keysetSupport: KeysetSupport, associatedData: ByteArray, inputString: String) {
        val decryptedString = keysetSupport.decryptString(
                tinkKeysetInfo,
                inputString,
                associatedData
        )

        println("[decrypted] $decryptedString")
    }
}