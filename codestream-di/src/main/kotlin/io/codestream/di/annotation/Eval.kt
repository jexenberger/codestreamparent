package io.codestream.di.annotation

@Target(AnnotationTarget.PROPERTY, AnnotationTarget.VALUE_PARAMETER)
annotation class Eval(val value: String, val engine: String = "js")