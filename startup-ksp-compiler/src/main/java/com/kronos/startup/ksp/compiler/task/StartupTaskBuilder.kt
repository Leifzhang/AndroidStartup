package com.kronos.startup.ksp.compiler.task

import com.google.devtools.ksp.getAllSuperTypes
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.kronos.startup.ksp.compiler.toClassName

/**
 *
 *  @Author LiABao
 *  @Since 2022/1/7
 *
 */
class StartupTaskBuilder(type: KSClassDeclaration) {
    val className = type.toClassName()

    init {
        type.getAllSuperTypes().forEach {
            it.toClassName()
        }
    }
}