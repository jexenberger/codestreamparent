package io.codestream.util

import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty

fun KMutableProperty<*>.findAnnotationsByType(types: Collection<KClass< out Annotation>>) = this.annotations.filter { types.contains(it::class) }
