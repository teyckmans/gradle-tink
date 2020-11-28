package dev.rigel.gradle.tink

import org.gradle.api.DefaultTask

open class GradleTinkTask : DefaultTask() {

    init {
        this.group = "tink"
    }
}