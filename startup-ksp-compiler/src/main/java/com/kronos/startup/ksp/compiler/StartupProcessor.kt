package com.kronos.startup.ksp.compiler

import com.google.devtools.ksp.processing.*
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSType
import com.google.devtools.ksp.symbol.Origin
import com.kronos.startup.annotation.StartupGroup
import com.kronos.startup.annotation.startup.Startup
import com.kronos.startup.ksp.compiler.group.GenerateGroupKt
import com.kronos.startup.ksp.compiler.group.GenerateProcGroupKt
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
    private val taskGroupMap = hashMapOf<String, MutableList<ClassName>>()
    private val procTaskGroupMap =
        hashMapOf<String, MutableList<Pair<ClassName, ArrayList<String>>>>()

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
        if (strategy.equals("other", true)) {
            val key = PROC_MODULE_KEY
            if (procTaskGroupMap[key] == null) {
                procTaskGroupMap[key] = mutableListOf()
            }
            val list = procTaskGroupMap[key] ?: return
            list.add(type.toClassName() to (startGroupAnnotation.getMember("processName")))
        } else {
            val key = strategy
            if (taskGroupMap[key] == null) {
                taskGroupMap[key] = mutableListOf()
            }
            val list = taskGroupMap[key] ?: return
            list.add(type.toClassName())
        }
    }


    override fun finish() {
        super.finish()
        // logger.error("className:${moduleName}")
        try {
            val taskGenerate = GenerateTaskKt(taskMap, codeGenerator)
            taskGenerate.taskGroupMap.forEach {
                val list = taskGroupMap.getValueByDefault(it.key) {
                    mutableListOf()
                }
                list.addAll(it.value)
            }
            taskGenerate.procTaskGroupMap.forEach {
                val list = procTaskGroupMap.getValueByDefault(it.key) {
                    mutableListOf()
                }
                list.addAll(it.value)
            }
            taskGroupMap.forEach { it ->
                val generateKt = GenerateGroupKt(
                    "${moduleName.upCaseKeyFirstChar()}${it.key.upCaseKeyFirstChar()}",
                    codeGenerator
                )
                it.value.forEach { className ->
                    generateKt.addStatement(className)
                }
                generateKt.generateKt()
            }
            procTaskGroupMap.forEach {
                val generateKt = GenerateProcGroupKt(
                    "${moduleName.upCaseKeyFirstChar()}${it.key.upCaseKeyFirstChar()}",
                    codeGenerator
                )
                it.value.forEach { pair ->
                    generateKt.addStatement(pair.first, pair.second)
                }
                generateKt.generateKt()
            }
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