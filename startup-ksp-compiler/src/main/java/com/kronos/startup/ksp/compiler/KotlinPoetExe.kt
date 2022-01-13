package com.kronos.startup.ksp.compiler


import com.google.devtools.ksp.isLocal
import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.symbol.*
import com.google.devtools.ksp.symbol.Variance.*
import com.google.devtools.ksp.symbol.Variance.STAR
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.STAR as KpStar

/**
 *
 *  @Author LiABao
 *  @Since 2021/3/8
 *
 */


internal fun KSType.toClassName(): ClassName {
    val decl = declaration
    check(decl is KSClassDeclaration)
    return decl.toClassName()
}

internal fun KSType.toTypeName(typeParamResolver: TypeParameterResolver): TypeName {
    val type = when (val decl = declaration) {
        is KSClassDeclaration -> decl.toTypeName(arguments.map { it.toTypeName(typeParamResolver) })
        is KSTypeParameter -> typeParamResolver[decl.name.getShortName()]
        is KSTypeAlias -> decl.type.resolve().toTypeName(typeParamResolver)
        else -> error("Unsupported type: $declaration")
    }

    return type.copy(nullable = isMarkedNullable)
}

internal fun KSClassDeclaration.toTypeName(argumentList: List<TypeName> = emptyList()): TypeName {
    val className = toClassName()
    return if (argumentList.isNotEmpty()) {
        className.parameterizedBy(argumentList)
    } else {
        className
    }
}

internal interface TypeParameterResolver {
    val parametersMap: Map<String, TypeVariableName>
    operator fun get(index: String): TypeVariableName
}

internal fun KSClassDeclaration.toClassName(): ClassName {
    require(!isLocal()) {
        "Local/anonymous classes are not supported!"
    }
    val pkgName = packageName.asString()
    val typesString = qualifiedName!!.asString().removePrefix("$pkgName.")

    val simpleNames = typesString
        .split(".")
    return ClassName(pkgName, simpleNames).apply {

    }
}

internal fun KSTypeArgument.toTypeName(typeParamResolver: TypeParameterResolver): TypeName {
    val typeName = type?.resolve()?.toTypeName(typeParamResolver) ?: return KpStar
    return when (variance) {
        COVARIANT -> WildcardTypeName.producerOf(typeName)
        CONTRAVARIANT -> WildcardTypeName.consumerOf(typeName)
        STAR -> KpStar
        INVARIANT -> typeName
    }
}

internal fun KSTypeReference.toTypeName(typeParamResolver: TypeParameterResolver): TypeName {
    val type = resolve()
    return type.toTypeName(typeParamResolver)
}

internal fun FileSpec.writeTo(codeGenerator: CodeGenerator, logger: KSPLogger) {
    //  logger.warn("start dependencies")
    // logger.error("dependencies:$dependencies")

    // Don't use writeTo(file) because that tries to handle directories under the hood
    logger.info("codeGenerator:${codeGenerator.generatedFile}")
}
