package dev.rigel.gradle.tink.tasks

import dev.rigel.gradle.tink.RigeldevGradleTinkPluginExtension
import dev.rigel.gradle.tink.core.KeysetSupport
import org.gradle.api.DefaultTask

abstract class GradleTinkTask : DefaultTask() {

    init {
        this.group = "tink"
    }

    protected fun keysetSupport(): KeysetSupport {
        val extension = this.project.extensions.getByType(RigeldevGradleTinkPluginExtension::class.java)
        return KeysetSupport(
                extension.charset,
                extension.encryptedValuePrefix,
                extension.encryptedValueSuffix
        )
    }
}