package com.kronos.startup.ksp.compiler

import com.google.devtools.ksp.processing.*
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSType
import com.google.devtools.ksp.symbol.Origin
import com.kronos.startup.annotation.Startup
import com.squareup.kotlinpoet.ClassName

class StartupProcessor(
    val codeGenerator: CodeGenerator,
    private val logger: KSPLogger,
    val moduleName: String
) : SymbolProcessor {
    private lateinit var startupType: KSType
    private var isload = false
    private val taskGroupMap = hashMapOf<String, MutableList<ClassName>>()


    override fun process(resolver: Resolver): List<KSAnnotated> {
        if (isload) {
            return emptyList()
        }
        logger.info("StartupProcessor start")

        val symbols = resolver.getSymbolsWithAnnotation(Startup::class.java.name)
        startupType = resolver.getClassDeclarationByName(
            resolver.getKSNameFromString(Startup::class.java.name)
        )?.asType() ?: kotlin.run {
            logger.error("JsonClass type not found on the classpath.")
            return emptyList()
        }
        symbols.asSequence().forEach {
            add(it)
        }
        // logger.error("className:${moduleName}")
        try {
            isload = true
            taskGroupMap.forEach { it ->
                val generateKt = GenerateKt(
                    "${moduleName.upCaseKeyFirstChar()}${it.key.upCaseKeyFirstChar()}",
                    codeGenerator
                )
                it.value.forEach { className ->
                    generateKt.addStatement(className)
                }
                generateKt.generateKt()
            }
        } catch (e: Exception) {
            logger.error(
                "Error preparing :" + " ${e.stackTrace.joinToString("\n")}"
            )
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
        if (taskGroupMap[groupName] == null) {
            taskGroupMap[groupName] = mutableListOf()
        }
        val list = taskGroupMap[groupName] ?: return
        list.add(type.toClassName())
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