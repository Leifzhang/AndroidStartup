package com.kronos.startup.ksp.compiler.task

import com.google.devtools.ksp.getAllSuperTypes
import com.google.devtools.ksp.symbol.KSAnnotation
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.kronos.startup.ksp.compiler.getMember
import com.kronos.startup.ksp.compiler.toClassName
import com.kronos.startup.ksp.compiler.toValue
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
    var processList = arrayListOf<String>()

    init {
        type.annotations.forEach {
            val annotation = it.annotationType.resolve().toClassName()
            if (annotation.canonicalName == "com.kronos.startup.annotation.startup.Async") {
                isAsync = true
            }
            if (annotation.canonicalName == "com.kronos.startup.annotation.startup.Await") {
                isAwait = true
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


    fun isProcOther(): Boolean {
        return strategy == "other"
    }
}