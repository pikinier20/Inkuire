rootProject.name = "Inkuire"
include("dokka-plugin")
include("integration-tests")
include("engine")
include("common")
include("intellij-plugin")

pluginManagement {
    plugins {
        id("org.jetbrains.kotlin.jvm") version "1.4.0"
    }
}
