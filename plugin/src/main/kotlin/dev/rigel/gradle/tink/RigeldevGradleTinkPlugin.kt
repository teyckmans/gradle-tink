/*
 * This Kotlin source file was generated by the Gradle 'init' task.
 */
package dev.rigel.gradle.tink

import dev.rigel.gradle.tink.tasks.CreateKeySetTask
import dev.rigel.gradle.tink.tasks.file.DecryptFileTask
import dev.rigel.gradle.tink.tasks.file.EncryptFileTask
import dev.rigel.gradle.tink.tasks.property.DecryptPropertyTask
import dev.rigel.gradle.tink.tasks.property.EncryptPropertyTask
import dev.rigel.gradle.tink.tasks.string.DecryptStringTask
import dev.rigel.gradle.tink.tasks.string.EncryptStringTask
import org.gradle.api.Project
import org.gradle.api.Plugin

/**
 * Plugin that provides encrypt/decrypt tasks powered by the Google Tink library.
 */
class RigeldevGradleTinkPlugin: Plugin<Project> {
    override fun apply(project: Project) {

        project.extensions.create("tink", RigeldevGradleTinkPluginExtension::class.java)
        // key tasks
        project.tasks.register("createKeyset", CreateKeySetTask::class.java)
        // TODO rotate

        // file tasks
        project.tasks.register("encryptFile", EncryptFileTask::class.java)
        project.tasks.register("decryptFile", DecryptFileTask::class.java)

        // string tasks
        project.tasks.register("encryptString", EncryptStringTask::class.java)
        project.tasks.register("decryptString", DecryptStringTask::class.java)

        // property tasks
        project.tasks.register("encryptProperty", EncryptPropertyTask::class.java)
        project.tasks.register("decryptProperty", DecryptPropertyTask::class.java)
    }
}
