pluginManagement {

    repositories {
        google()
        maven { setUrl("https://maven.aliyun.com/repository/central/") }
        gradlePluginPortal()
        maven { setUrl("https://dl.bintray.com/kotlin/kotlin-eap") }
    }

    plugins {
        id("com.google.devtools.ksp") version ("1.5.30-1.0.0") apply (false)

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
