package com.kronos.startup.ksp.compiler

import com.google.devtools.ksp.processing.*
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSType
import com.google.devtools.ksp.symbol.Origin
import com.kronos.startup.annotation.Lifecycle
import com.kronos.startup.annotation.StartupGroup
import com.kronos.startup.annotation.nameToLifeCycle
import com.kronos.startup.annotation.startup.Startup
import com.kronos.startup.ksp.compiler.group.GenerateProcGroupKt
import com.kronos.startup.ksp.compiler.group.TaskBuilder
import com.kronos.startup.ksp.compiler.stage.StageGenerateKt
import com.kronos.startup.ksp.compiler.task.GenerateTaskKt
import com.kronos.startup.ksp.compiler.task.StartupTaskBuilder
import com.kronos.startup.ksp.compiler.utils.getValueByDefault
import com.squareup.kotlinpoet.ClassName

class StartupProcessor(
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger,
    private val moduleName: String
) : SymbolProcessor {
    private lateinit var startupGroupType: KSType
    private lateinit var startupType: KSType
    private val procTaskGroupMap =
        hashMapOf<Lifecycle, MutableList<TaskBuilder>>()

    private val taskMap = mutableListOf<StartupTaskBuilder>()

    init {
        mLogger = logger
    }

    override fun process(resolver: Resolver): List<KSAnnotated> {
        logger.info("StartupProcessor start")
        val startupSymbols = resolver.getSymbolsWithAnnotation(Startup::class.java.name)
        startupType = resolver.getClassDeclarationByName(
            resolver.getKSNameFromString(Startup::class.java.name)
        )?.asType() ?: kotlin.run {
            logger.error("Startup type not found on the classpath.")
            return emptyList()
        }
        startupSymbols.asSequence().forEach {
            addStartUp(it)
        }
        val symbols = resolver.getSymbolsWithAnnotation(StartupGroup::class.java.name)
        startupGroupType = resolver.getClassDeclarationByName(
            resolver.getKSNameFromString(StartupGroup::class.java.name)
        )?.asType() ?: kotlin.run {
            logger.error("StartupGroup type not found on the classpath.")
            return emptyList()
        }

        symbols.asSequence().forEach {
            addGroup(it)
        }

        return emptyList()
    }


    private fun addStartUp(type: KSAnnotated) {
        logger.check(type is KSClassDeclaration && type.origin == Origin.KOTLIN, type) {
            "@JsonClass can't be applied to $type: must be a Kotlin class"
        }

        if (type !is KSClassDeclaration) return

        val startupAnnotation = type.findAnnotationWithType(startupType) ?: return
        taskMap.add(StartupTaskBuilder(type, startupAnnotation))

    }

    private fun addGroup(type: KSAnnotated) {
        logger.check(type is KSClassDeclaration && type.origin == Origin.KOTLIN, type) {
            "@JsonClass can't be applied to $type: must be a Kotlin class"
        }

        if (type !is KSClassDeclaration) return

        //class type

        val startGroupAnnotation = type.findAnnotationWithType(startupGroupType) ?: return
        val strategy = startGroupAnnotation.arguments.firstOrNull {
            it.name?.asString() == "strategy"
        }?.value.toString().toValue()

        val lifecycle = type.annotations.firstOrNull {
            val annotation = it.annotationType.resolve().toClassName()
            annotation.canonicalName == "com.kronos.startup.annotation.Step"
        }?.let {
            it.arguments.firstOrNull { ksValue ->
                ksValue.name?.asString() == "lifecycle"
            }?.value.toString()
        }.nameToLifeCycle()
        if (procTaskGroupMap[lifecycle] == null) {
            procTaskGroupMap[lifecycle] = mutableListOf()
        }
        val mustAfter = type.annotations.firstOrNull {
            val annotation = it.annotationType.resolve().toClassName()
            annotation.canonicalName == "com.kronos.startup.annotation.startup.MustAfter"
        }
        val list = procTaskGroupMap[lifecycle] ?: return
        val processName = if (strategy.equals("main", true)) {
            arrayListOf("")
        } else {
            startGroupAnnotation.getMember("processName")
        }
        list.add(TaskBuilder(type.toClassName(), (mustAfter != null), processName))
    }


    override fun finish() {
        super.finish()
        try {
            val taskGenerate = GenerateTaskKt(taskMap, codeGenerator)
            taskGenerate.procTaskGroupMap.forEach {
                val list = procTaskGroupMap.getValueByDefault(it.key) {
                    mutableListOf()
                }
                list.addAll(it.value)
            }
            var index = 1
            val nameList = mutableListOf<ClassName>()
            procTaskGroupMap.forEach {
                val generateKt = GenerateProcGroupKt(
                    "${moduleName.upCaseKeyFirstChar()}${PROC_MODULE_KEY.upCaseKeyFirstChar()}${index++}",
                    it.key,
                    codeGenerator
                )
                it.value.forEach { value ->
                    generateKt.addStatement(value)
                }
                nameList.add(generateKt.generateKt())
            }
            val stageGenerator = StageGenerateKt(
                "${moduleName.upCaseKeyFirstChar()}StageBuilder",
                nameList,
                codeGenerator
            )
            stageGenerator.generateKt()
        } catch (e: Exception) {
            logger.error(
                "Error preparing :" + " ${e.stackTrace.joinToString("\n")}"
            )
        }
    }

    companion object {
        var mLogger: KSPLogger? = null
    }

}


class StartupProcessorProvider : SymbolProcessorProvider {
    override fun create(
        environment: SymbolProcessorEnvironment
    ): SymbolProcessor {
        return StartupProcessor(
            environment.codeGenerator,
            environment.logger,
            environment.options[KEY_MODULE_NAME] ?: "application"
        )
    }
}

fun String.toValue(): String {
    var lastIndex = lastIndexOf(".") + 1
    if (lastIndex <= 0) {
        lastIndex = 0
    }
    return subSequence(lastIndex, length).toString().lowercase().upCaseKeyFirstChar()
}

fun String.upCaseKeyFirstChar(): String {
    return if (this.isEmpty() || Character.isUpperCase(this[0])) {
        this
    } else {
        StringBuilder().append(Character.toUpperCase(this[0])).append(this.substring(1)).toString()
    }
}

const val KEY_MODULE_NAME = "MODULE_NAME"
const val PROC_MODULE_KEY = "Proc"