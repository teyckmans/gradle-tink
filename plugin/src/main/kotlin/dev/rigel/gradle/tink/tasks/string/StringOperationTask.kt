package dev.rigel.gradle.tink.tasks.string

import dev.rigel.gradle.tink.core.KeysetSupport
import dev.rigel.gradle.tink.core.TinkKeysetInfo
import dev.rigel.gradle.tink.tasks.OperationTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.options.Option
import java.io.Console

abstract class StringOperationTask(private val consoleMethod: (Console) -> String) : OperationTask() {

    @Option(option = "value", description = "Value to encrypt/decrypt (using this parameter is NOT secure as it leads to sensitive data being captured in your command line history!)")
    @get:Input
    var value = ""

    override fun doOperation(tinkKeysetInfo: TinkKeysetInfo, keysetSupport: KeysetSupport, associatedData: ByteArray) {
        val inputString = if (value == "") {
            val console = System.console()

            check(console != null) {
                "No console to read input value from."
            }

            consoleMethod.invoke(console)
        } else {
            logger.warn("Using the --value parameter is NOT secure as it leads to sensitive data being captured in your command line history!")
            this.value
        }

        doStringOperation(tinkKeysetInfo, keysetSupport, associatedData, inputString)
    }

    abstract fun doStringOperation(tinkKeysetInfo: TinkKeysetInfo,
                                   keysetSupport: KeysetSupport,
                                   associatedData: ByteArray,
                                   inputString: String)
}