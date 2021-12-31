package com.kronos.startup.ksp.compiler

import com.google.devtools.ksp.processing.*
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSType
import com.google.devtools.ksp.symbol.Origin
import com.kronos.startup.annotation.StartupGroup
import com.squareup.kotlinpoet.ClassName

class StartupProcessor(
    val codeGenerator: CodeGenerator,
    private val logger: KSPLogger,
    val moduleName: String
) : SymbolProcessor {
    private lateinit var startupType: KSType
    private var isload = false
    private val taskGroupMap = hashMapOf<String, MutableList<ClassName>>()
    private val procTaskGroupMap =
        hashMapOf<String, MutableList<Pair<ClassName, ArrayList<String>>>>()

    override fun process(resolver: Resolver): List<KSAnnotated> {
        logger.info("StartupProcessor start")

        val symbols = resolver.getSymbolsWithAnnotation(StartupGroup::class.java.name)
        startupType = resolver.getClassDeclarationByName(
            resolver.getKSNameFromString(StartupGroup::class.java.name)
        )?.asType() ?: kotlin.run {
            logger.error("JsonClass type not found on the classpath.")
            return emptyList()
        }
        symbols.asSequence().forEach {
            add(it)
        }
        return emptyList()
    }

    private fun add(type: KSAnnotated) {
        logger.check(type is KSClassDeclaration && type.origin == Origin.KOTLIN, type) {
            "@JsonClass can't be applied to $type: must be a Kotlin class"
        }

        if (type !is KSClassDeclaration) return

        //class type

        val routerAnnotation = type.findAnnotationWithType(startupType) ?: return
        val groupName = routerAnnotation.getMember<String>("group")
        val strategy = routerAnnotation.arguments.firstOrNull {
            it.name?.asString() == "strategy"
        }?.value.toString().toValue() ?: return
        if (strategy.equals("other", true)) {
            val key = groupName
            if (procTaskGroupMap[key] == null) {
                procTaskGroupMap[key] = mutableListOf()
            }
            val list = procTaskGroupMap[key] ?: return
            list.add(type.toClassName() to (routerAnnotation.getMember("processName")))
        } else {
            val key = "${groupName}${strategy}"
            if (taskGroupMap[key] == null) {
                taskGroupMap[key] = mutableListOf()
            }
            val list = taskGroupMap[key] ?: return
            list.add(type.toClassName())
        }
    }

    private fun String.toValue(): String {
        var lastIndex = lastIndexOf(".") + 1
        if (lastIndex <= 0) {
            lastIndex = 0
        }
        return subSequence(lastIndex, length).toString().lowercase().upCaseKeyFirstChar()
    }

    override fun finish() {
        super.finish()
        // logger.error("className:${moduleName}")
        try {
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

fun String.upCaseKeyFirstChar(): String {
    return if (Character.isUpperCase(this[0])) {
        this
    } else {
        StringBuilder().append(Character.toUpperCase(this[0])).append(this.substring(1)).toString()
    }
}

const val KEY_MODULE_NAME = "MODULE_NAME"