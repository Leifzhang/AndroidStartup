import org.jetbrains.kotlin.gradle.plugin.getKotlinPluginVersion

plugins {
    kotlin("jvm")
    id("com.google.devtools.ksp") version "1.5.30-1.0.0"
}
dependencies {
    val kotlinVersion = getKotlinPluginVersion()
    implementation("org.jetbrains.kotlin:kotlin-stdlib:${kotlinVersion}")
    implementation(project(":startup-annotation"))
    implementation("com.squareup:kotlinpoet:1.10.2")

    implementation("com.squareup:kotlinpoet-ksp:1.10.2")
    //一定要加这个  否则会出问题
    compileOnly("org.jetbrains.kotlin:kotlin-compiler-embeddable:${kotlinVersion}")
    compileOnly("dev.zacsweers.autoservice:auto-service-ksp:0.3.2")
    compileOnly("com.google.devtools.ksp:symbol-processing-api:1.5.31-1.0.1")
}