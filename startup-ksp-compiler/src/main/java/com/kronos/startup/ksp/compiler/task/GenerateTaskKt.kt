package com.kronos.startup.ksp.compiler.task

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import com.kronos.startup.ksp.compiler.group.TaskBuilder
import com.squareup.kotlinpoet.*

/**
 * @Author LiABao
 * @Since 2022/1/7
 */
class GenerateTaskKt(
    builders: MutableList<StartupTaskBuilder>,
    private val codeGenerator: CodeGenerator
) {
    val procTaskGroupMap = hashMapOf<String, MutableList<Pair<TaskBuilder, ArrayList<String>>>>()


    init {
        builders.forEach {
            addToMap(it)
            generateSealedClass(it.className)
            generateTaskClass(it)
        }
    }

    private fun addToMap(taskBuilder: StartupTaskBuilder) {
        val name = taskBuilder.className
        val className = ClassName(name.packageName, name.simpleName + "Task")

        val key = taskBuilder.strategy
        if (procTaskGroupMap[key] == null) {
            procTaskGroupMap[key] = mutableListOf()
        }
        val list = procTaskGroupMap[key] ?: return
        val processName = if (key.equals("main", true)) {
            arrayListOf("")
        } else {
            taskBuilder.processList
        }
        list.add(TaskBuilder(className, taskBuilder.mustAfter) to processName)
    }

    private fun generateTaskClass(taskBuilder: StartupTaskBuilder) {
        val name = taskBuilder.className
        val className = name.simpleName + "Task"
        val specBuilder = FileSpec.builder(
            name.packageName,
            className
        )
        val typeSpec = TypeSpec.classBuilder(className)
            .primaryConstructor(FunSpec.constructorBuilder().build())
            .addAnnotation(ClassName("com.kronos.startup.annotation.startup", "MustAfter"))
            .superclass(
                ClassName(
                    "com.kronos.lib.startup.generate",
                    "GenerateStartupTask"
                )
            )
        val providerName = ClassName(STARTUP_PACKAGE, name.simpleName + "Provider")
        val property = PropertySpec.builder(
            "taskNameProvider",
            ClassName(STARTUP_ANNOTATION, "TaskNameProvider")
        ).addModifiers(KModifier.OVERRIDE)
            .getter(FunSpec.getterBuilder().addStatement("return %T", providerName).build())
        val runFun: FunSpec.Builder = FunSpec.builder("run").apply {
            addModifiers(KModifier.OVERRIDE)
            addParameter("context", ClassName("android.content", "Context"))
            addStatement("val task = %T()", name)
            addStatement("task.run(context)")
        }
        taskBuilder.getAsyncFun()?.apply {
            typeSpec.addFunction(this)
        }
        taskBuilder.getAwaitFun()?.apply {
            typeSpec.addFunction(this)
        }
        taskBuilder.getDependenciesFun()?.apply {
            typeSpec.addFunction(this)
        }
        typeSpec.addProperty(property.build())
        typeSpec.addFunction(runFun.build())
        specBuilder.addType(typeSpec.build())
        val spec = specBuilder.build()
        val file = codeGenerator.createNewFile(Dependencies.ALL_FILES, spec.packageName, spec.name)
        file.use {
            val content = spec.toString().toByteArray()
            it.write(content)
        }
    }


    private fun generateSealedClass(name: ClassName) {
        val className = name.simpleName + "Provider"
        val specBuilder = FileSpec.builder(
            STARTUP_PACKAGE,
            className
        )
        val helloWorld = TypeSpec.objectBuilder(className)
            .addSuperinterface(
                ClassName(
                    STARTUP_ANNOTATION,
                    "TaskNameProvider"
                )
            )
            .build()
        specBuilder.addType(helloWorld)
        val spec = specBuilder.build()
        val file = codeGenerator.createNewFile(Dependencies.ALL_FILES, spec.packageName, spec.name)
        file.use {
            val content = spec.toString().toByteArray()
            it.write(content)
        }
    }

    companion object {
        private const val STARTUP_PACKAGE = "com.kronos.lib.startup"
        private const val STARTUP_ANNOTATION = "com.kronos.startup"
    }

}