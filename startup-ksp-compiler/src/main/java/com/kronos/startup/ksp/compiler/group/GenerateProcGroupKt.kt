package com.kronos.startup.ksp.compiler.group

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import com.kronos.startup.annotation.Lifecycle
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
    lifecycle: Lifecycle,
    private val codeGenerator: CodeGenerator
) {

    private val className = name.replace("[^0-9a-zA-Z_]+", "")
    private val specBuilder = FileSpec.builder("com.kronos.lib.startup.group", className)
    private val groupFun: FunSpec.Builder = FunSpec.builder("group").apply {
        addModifiers(KModifier.OVERRIDE)
        addParameter("builder", ClassName("com.kronos.lib.startup.Startup", "Builder"))
        addParameter("process", String::class)
        val className = ClassName("com.kronos.lib.startup", "StartupTask")
        returns(MUTABLE_LIST.parameterizedBy(className))
        addStatement("val list = mutableListOf<StartupTask>()")
    }

    private val stageFun: FunSpec.Builder = FunSpec.builder("lifecycle").apply {
        addModifiers(KModifier.OVERRIDE)
        returns(Lifecycle::class)
        addStatement(
            "return %T",
            ClassName("com.kronos.startup.annotation.Lifecycle", lifecycle.value)
        )
    }

    fun addStatement(builder: TaskBuilder) {
        if (builder.processes.isEmpty()) {
            addClassName(builder)
        } else {
            builder.processes.forEach {
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

    fun generateKt(): ClassName {
        groupFun.addStatement("return list")
        val helloWorld = TypeSpec.classBuilder(className)
            .addSuperinterface(
                ClassName(
                    "com.kronos.lib.startup",
                    "StartupTaskProcessGroup"
                )
            )
            .addFunction(groupFun.build())
            .addFunction(stageFun.build())
            .build()
        specBuilder.addType(helloWorld)
        val spec = specBuilder.build()
        val file = codeGenerator.createNewFile(Dependencies.ALL_FILES, spec.packageName, spec.name)
        file.use {
            val content = spec.toString().toByteArray()
            it.write(content)
        }
        return ClassName("com.kronos.lib.startup.group", className)
    }
}