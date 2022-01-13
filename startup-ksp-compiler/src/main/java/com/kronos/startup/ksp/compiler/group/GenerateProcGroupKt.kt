package com.kronos.startup.ksp.compiler.group

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy

/**
 *
 *  @Author LiABao
 *  @Since 2021/12/9
 *
 */
class GenerateProcGroupKt(
    name: String,
    private val codeGenerator: CodeGenerator
) {

    private val className = "StartupProcTaskGroup${name.replace("[^0-9a-zA-Z_]+", "")}"
    private val specBuilder = FileSpec.builder("com.kronos.lib.startup.group", className)
    private val groupFun: FunSpec.Builder = FunSpec.builder("group").apply {
        addModifiers(KModifier.OVERRIDE)
        addParameter("builder", ClassName("com.kronos.lib.startup.Startup", "Builder"))
        addParameter("process", String::class)
        val className = ClassName("com.kronos.lib.startup", "StartupTask")
        returns(MUTABLE_LIST.parameterizedBy(className))
        addStatement("val list = mutableListOf<StartupTask>()")
    }


    fun addStatement(builder: TaskBuilder, processes: ArrayList<String>) {
        if (processes.isEmpty()) {
            addClassName(builder)
        } else {
            processes.forEach {
                if (it.isEmpty()) {
                    groupFun.beginControlFlow(
                        "if(process.%T())",
                        ClassName("com.kronos.lib.startup.utils", "isMainProc")
                    )
                } else {
                    groupFun.beginControlFlow("if(process.contains(%S))", it)
                }
                addClassName(builder)
                groupFun.endControlFlow()
            }
        }
    }

    private fun addClassName(builder: TaskBuilder) {
        if (!builder.mustAfter) {
            groupFun.addStatement("list.add(%T())", builder.className)
        } else {
            groupFun.addStatement("builder.mustAfterAnchorTask(%T())", builder.className)
        }
    }

    fun generateKt() {
        groupFun.addStatement("return list")
        val helloWorld = TypeSpec.classBuilder(className)
            .addSuperinterface(
                ClassName(
                    "com.kronos.lib.startup",
                    "StartupTaskProcessGroup"
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