package com.kronos.startup.ksp.compiler.task

import com.google.devtools.ksp.getAllSuperTypes
import com.google.devtools.ksp.symbol.KSAnnotation
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.kronos.startup.annotation.Lifecycle
import com.kronos.startup.annotation.nameToLifeCycle
import com.kronos.startup.ksp.compiler.StartupProcessor.Companion.mLogger
import com.kronos.startup.ksp.compiler.getMember
import com.kronos.startup.ksp.compiler.toClassName
import com.kronos.startup.ksp.compiler.toValue
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier

/**
 *
 *  @Author LiABao
 *  @Since 2022/1/7
 *
 */
class StartupTaskBuilder(type: KSClassDeclaration, startupAnnotation: KSAnnotation?) {

    val className = type.toClassName()
    var isAsync = false
    var isAwait = false
    var strategy: String
    var processList: ArrayList<String> = arrayListOf()
    val dependOnClassList = mutableListOf<ClassName>()
    val dependOnStringList = mutableListOf<String>()
    var mustAfter: Boolean = false
    var lifecycle: Lifecycle = Lifecycle.OnApplicationCrate

    init {
        type.annotations.forEach {
            val annotation = it.annotationType.resolve().toClassName()
            if (annotation.canonicalName == "com.kronos.startup.annotation.startup.Async") {
                isAsync = true
            }
            if (annotation.canonicalName == "com.kronos.startup.annotation.startup.Await") {
                isAwait = true
            }
            if (annotation.canonicalName == "com.kronos.startup.annotation.startup.MustAfter") {
                mustAfter = true
            }
            if (annotation.canonicalName == "com.kronos.startup.annotation.startup.DependOn") {
                val value = it.getMember<ArrayList<ClassName>>("dependOn")
                dependOnClassList.addAll(value)
                val dependOnTag = it.getMember<ArrayList<String>>("dependOnTag")
                dependOnStringList.addAll(dependOnTag)
            }
            if (annotation.canonicalName == "com.kronos.startup.annotation.Step") {
                val value = it.arguments.firstOrNull {
                    it.name?.asString() == "lifecycle"
                }?.value.toString().nameToLifeCycle()
                lifecycle = value
                mLogger?.warn("stage:$value")
            }
        }

        type.getAllSuperTypes().forEach {
            it.toClassName()
        }
        strategy = startupAnnotation?.arguments?.firstOrNull {
            it.name?.asString() == "strategy"
        }?.value.toString().toValue()

        val list = startupAnnotation?.getMember<ArrayList<String>>("processName")
        list?.let { processList.addAll(it) }
    }

    fun getAsyncFun(): FunSpec? {
        if (!isAsync) {
            return null
        }
        return FunSpec.builder("mainThread").addModifiers(KModifier.OVERRIDE)
            .returns(Boolean::class)
            .addStatement("return false").build()
    }

    fun getAwaitFun(): FunSpec? {
        if (!isAwait) {
            return null
        }
        return FunSpec.builder("await").addModifiers(KModifier.OVERRIDE)
            .returns(Boolean::class)
            .addStatement("return true").build()
    }

    fun getDependenciesFun(): FunSpec? {
        if (dependOnClassList.isEmpty() && dependOnStringList.isEmpty()) {
            return null
        }
        val funSpec = FunSpec.builder("addDependencies").addModifiers(KModifier.OVERRIDE)
        dependOnClassList.forEach {
            funSpec.addStatement("dependOn(%T)", it)
        }
        dependOnStringList.forEach {
            funSpec.addStatement("dependOnName(%S)", it)
        }
        return funSpec.build()
    }


}