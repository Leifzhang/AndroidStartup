package com.kronos.startup.ksp.compiler

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy

/**
 * @Author LiABao
 * @Since 2021/11/26
 */
class GenerateGroupKt(
    name: String,
    private val codeGenerator: CodeGenerator
) {

    private val className = "StartupTaskGroup${name.replace("[^0-9a-zA-Z_]+", "")}"
    private val specBuilder = FileSpec.builder("com.kronos.lib.startup.group", className)
    private val groupFun: FunSpec.Builder = FunSpec.builder("group").apply {
        addModifiers(KModifier.OVERRIDE)
        val className = ClassName("com.kronos.lib.startup", "StartupTask")
        returns(MUTABLE_LIST.parameterizedBy(className))
        addStatement("val list = mutableListOf<StartupTask>()")
    }

    fun addStatement(className: ClassName) {
        groupFun.addStatement("list.add(%T())", className)
    }

    fun generateKt() {
        groupFun.addStatement("return list")
        val helloWorld = TypeSpec.classBuilder(className)
            .addSuperinterface(
                ClassName(
                    "com.kronos.lib.startup",
                    "StartupTaskGroup"
                )
            )
            .addFunction(groupFun.build())
            .build()
        specBuilder.addType(helloWorld)
        val spec = specBuilder.build()
        val file = codeGenerator.createNewFile(Dependencies.ALL_FILES, spec.packageName, spec.name)
        file.use {
            val content = spec.toString().toByteArray()
            it.write(content)
        }
    }
}