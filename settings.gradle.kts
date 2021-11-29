pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        maven { setUrl("https://dl.bintray.com/kotlin/kotlin-eap") }
    }
}

include(":startup")
include(":sample")
include(":startup-annotation")
include(":startup-ksp-compiler")
include(":startup-dag-view")
