package io.codestream.util

import kotlin.reflect.KClass

val Annotation.annotationClass : KClass<Annotation> get() = this::class.supertypes[1] as KClass<Annotation>
