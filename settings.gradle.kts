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


include(":network-sdk-demo")
project(":network-sdk-demo").projectDir = file("./sample-sdk/network-sdk-demo")
include(":report-sdk-demo")
project(":report-sdk-demo").projectDir = file("./sample-sdk/report-sdk-demo")
