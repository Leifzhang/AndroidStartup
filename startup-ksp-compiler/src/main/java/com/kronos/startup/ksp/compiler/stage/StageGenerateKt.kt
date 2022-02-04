package com.kronos.startup.ksp.compiler.stage

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import com.kronos.startup.ksp.compiler.utils.fixClassName
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy

/**
 * @Author LiABao
 * @Since 2022/1/25
 */
class StageGenerateKt(
    name: String,
    private val groupList: MutableList<ClassName>,
    private val codeGenerator: CodeGenerator
) {

    private val className = name.fixClassName()
    private val specBuilder = FileSpec.builder("com.kronos.lib.startup.step", className)

    private val groupFun: FunSpec.Builder = FunSpec.builder("invoke").apply {
        addModifiers(KModifier.OVERRIDE)
        val className = ClassName("com.kronos.lib.startup", "StartupTaskProcessGroup")
        returns(MUTABLE_LIST.parameterizedBy(className))
        addStatement("val list = mutableListOf<StartupTaskProcessGroup>()")
        groupList.forEach {
            addStatement("list.add(%T())", it)
        }
    }

    fun generateKt() {
        groupFun.addStatement("return list")
        val helloWorld = TypeSpec.classBuilder(className)
            .addSuperinterface(
                ClassName(
                    "com.kronos.lib.startup.step",
                    "StepTaskBuilder"
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