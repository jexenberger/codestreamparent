/*
 *  Copyright 2018 Julian Exenberger
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *    `*   `[`http://www.apache.org/licenses/LICENSE-2.0`](http://www.apache.org/licenses/LICENSE-2.0)
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package io.codestream.util.transformation

import io.codestream.util.SystemException
import io.codestream.util.crypto.Secret
import java.io.File
import java.math.BigDecimal
import kotlin.reflect.KClass
import kotlin.reflect.full.isSubclassOf
import kotlin.reflect.full.isSuperclassOf


object TransformerService {

    internal val CONVERSION_REGISTRY: MutableMap<Pair<KClass<*>, KClass<*>>, TypeTransformer<*, *>> = mutableMapOf()


    init {
        addConverter(String::class, Secret::class, LambdaTransformer<String, Secret> { Secret(it) })
        addConverter(Secret::class, String::class, LambdaTransformer<Secret, String> { it.value })
        addConverter(String::class, File::class, LambdaTransformer<String, File> { File(it) })
        addConverter(List::class, Array<Int>::class, LambdaTransformer<List<Any>, Array<Int>> {
            it.map { value -> TransformerService.convert<Int>(value) }.toTypedArray()
        })
        addConverter(List::class, Array<String>::class, LambdaTransformer<List<Any>, Array<String>> {
            it.map { value -> value.toString() }.toTypedArray()
        })
        addConverter(Iterable::class, Iterator::class, LambdaTransformer<Iterable<Any>, Iterator<Any>> { it.iterator() })
        addConverter(String::class, Iterator::class, LambdaTransformer<String, Iterator<Any>> {
            it.split(",").map { it.trim() }.iterator()
        })
        addConverter(Array<Any>::class, Iterator::class, LambdaTransformer<Array<Any>, Iterator<Any>> { it.iterator() })
        addConverter(Array<String>::class, Iterator::class, LambdaTransformer<Array<String>, Iterator<String>> { it.iterator() })
        addConverter(Array<Int>::class, Iterator::class, LambdaTransformer<Array<Int>, Iterator<Int>> { it.iterator() })
        addConverter(Array<Double>::class, Iterator::class, LambdaTransformer<Array<Double>, Iterator<Double>> { it.iterator() })
        addConverter(Array<Short>::class, Iterator::class, LambdaTransformer<Array<Short>, Iterator<Short>> { it.iterator() })
        addConverter(Array<Byte>::class, Iterator::class, LambdaTransformer<Array<Byte>, Iterator<Byte>> { it.iterator() })
        addConverter(Array<Boolean>::class, Iterator::class, LambdaTransformer<Array<Boolean>, Iterator<Boolean>> { it.iterator() })
        addConverter(Array<Float>::class, Iterator::class, LambdaTransformer<Array<Float>, Iterator<Float>> { it.iterator() })
        addConverter(Array<Long>::class, Iterator::class, LambdaTransformer<Array<Long>, Iterator<Long>> { it.iterator() })
        addConverter(File::class, String::class, LambdaTransformer<File, String> { it.absolutePath })
        addConverter(String::class, Long::class, LambdaTransformer<String, Long> { it.toLong() })
        addConverter(String::class, Any::class, LambdaTransformer<String, Any> { it })
        addConverter(String::class, Int::class, LambdaTransformer<String, Int> { it.toInt() })
        addConverter(String::class, Byte::class, LambdaTransformer<String, Byte> { it.toByte() })
        addConverter(String::class, Short::class, LambdaTransformer<String, Short> { it.toShort() })
        addConverter(String::class, Boolean::class, LambdaTransformer<String, Boolean> { Transformations.toBoolean(it) })
        addConverter(String::class, Float::class, LambdaTransformer<String, Float> { it.toFloat() })
        addConverter(String::class, Double::class, LambdaTransformer<String, Double> { it.toDouble() })
        addConverter(String::class, Char::class, LambdaTransformer<String, Char> { it.toCharArray()[0] })
        addConverter(String::class, BigDecimal::class, LambdaTransformer<String, BigDecimal> { BigDecimal(it) })
        addConverter(Number::class, Long::class, LambdaTransformer<Number, Long> { it.toLong() })
        addConverter(Number::class, Int::class, LambdaTransformer<Number, Int> { it.toInt() })
        addConverter(Number::class, Byte::class, LambdaTransformer<Number, Byte> { it.toByte() })
        addConverter(Number::class, Short::class, LambdaTransformer<Number, Short> { it.toShort() })
        addConverter(Number::class, Boolean::class, LambdaTransformer<Number, Boolean> { Transformations.toBoolean(it) })
        addConverter(Number::class, Float::class, LambdaTransformer<Number, Float> { it.toFloat() })
        addConverter(Number::class, Double::class, LambdaTransformer<Number, Double> { it.toDouble() })
        addConverter(Number::class, BigDecimal::class, LambdaTransformer<Number, BigDecimal> { BigDecimal(it.toString()) })
        addConverter(Any::class, String::class, LambdaTransformer<Any, String> { it.toString() })
        addConverter(Char::class, Boolean::class, LambdaTransformer<Char, Boolean> { Transformations.toBoolean(it) })
        addConverter(String::class, Array<String>::class, LambdaTransformer<String, Array<String>> {
            it.split(",").map { it.trim() }.toTypedArray()
        })
        addConverter(String::class, Collection::class, LambdaTransformer<String, Collection<String>> {
            it.split(",").map { it.trim() }
        })
        addConverter(String::class, Array<Any>::class, LambdaTransformer<String, Array<Any>> {
            it.split(",").map { it.trim() }.toTypedArray()
        })
        addConverter(Array<String>::class, Collection::class, LambdaTransformer<Array<String>, Collection<String>> {
            it.toList()
        })
        addConverter(String::class, Array<Boolean>::class, LambdaTransformer<String, Array<Boolean>> {
            it.split(",").map { Transformations.toBoolean(it.trim()) }.toTypedArray()
        })
        addConverter(String::class, Array<Int>::class, LambdaTransformer<String, Array<Int>> {
            it.split(",").map { it.trim().toInt() }.toTypedArray()
        })
        addConverter(String::class, Array<Long>::class, LambdaTransformer<String, Array<Long>> {
            it.split(",").map { it.trim().toLong() }.toTypedArray()
        })
        addConverter(String::class, Array<Short>::class, LambdaTransformer<String, Array<Short>> {
            it.split(",").map { it.trim().toShort() }.toTypedArray()
        })
        addConverter(String::class, Array<Byte>::class, LambdaTransformer<String, Array<Byte>> {
            it.split(",").map { it.trim().toByte() }.toTypedArray()
        })
        addConverter(String::class, Array<Float>::class, LambdaTransformer<String, Array<Float>> {
            it.split(",").map { it.trim().toFloat() }.toTypedArray()
        })
        addConverter(String::class, Array<Double>::class, LambdaTransformer<String, Array<Double>> {
            it.split(",").map { it.trim().toDouble() }.toTypedArray()
        })
        addConverter(String::class, Array<BigDecimal>::class, LambdaTransformer<String, Array<BigDecimal>> {
            it.split(",").map { BigDecimal(it.trim()) }.toTypedArray()
        })
        addConverter(String::class, Array<File>::class, LambdaTransformer<String, Array<File>> {
            it.split(",").map { File(it.trim()) }.toTypedArray()
        })
        addConverter(Collection::class, Array<String>::class, LambdaTransformer<Collection<*>, Array<String>> {
            it.toTypedArray().map { it?.toString() ?: "" }.toTypedArray()
        })
        addConverter(Collection::class, Array<String>::class, LambdaTransformer<Collection<*>, Array<String>> {
            it.toTypedArray().map { it?.toString() ?: "" }.toTypedArray()
        })

        addConverter(Map::class, Map::class, LambdaTransformer<Map<String, Any?>, Map<String, Any?>> {
            it
        })
    }


    internal val conversionRegistry: MutableMap<Pair<KClass<*>, KClass<*>>, TypeTransformer<*, *>>
        get() = CONVERSION_REGISTRY

    fun addConverter(source: KClass<*>, target: KClass<*>, transformer: TypeTransformer<*, *>) {
        val key = source to target
        conversionRegistry.put(key, transformer)
    }

    fun findWideningTransformer(source: KClass<*>, target: KClass<*>): TypeTransformer<*, *>? {


        val typeTransformer = conversionRegistry[source to target]
        if (typeTransformer != null) {
            return typeTransformer
        }
        //do a widening search
        val transformerKey = conversionRegistry
                .keys
                .filter { (first, second) ->
                    val sourceAssignable = first.isSuperclassOf(source)
                    val targetAssignable = second.isSuperclassOf(target) || second.equals(target)
                    val fits = sourceAssignable && targetAssignable
                    fits
                }
                .firstOrNull()
        return conversionRegistry[transformerKey]
    }

    inline fun <reified K> convertWithNull(instance: Any?, typeHint: KClass<*>? = null): K? {
        return instance?.let {
            convert(it, typeHint)
        }
    }


    fun isCollectionType(a: KClass<*>): Boolean {
        val collection = isCollection(a)
        val iterable = isIterableType(a)
        return collection || iterable
    }


    private fun isIterableType(a: KClass<*>) = a.equals(Iterable::class) || a.isSubclassOf(Iterable::class)

    private fun isCollection(a: KClass<*>) = a.equals(Collection::class) || a.isSubclassOf(Collection::class)

    fun areBothCollectionTypes(a: KClass<*>, b: KClass<*>): Boolean {
        return isCollectionType(a) && isCollectionType(b)
    }

    @SuppressWarnings("UNCHECKED_CAST")
    inline fun <reified K> convert(instance: Any, typeHint: KClass<*>? = null): K {
        val type = typeHint?.let { it } ?: K::class
        //special case with enum as we always need the absolute concrete type
        if (type.equals(Any::class)) {
            return instance as K
        }
        if (instance is String && type.java.isEnum) {
            @Suppress("UPPER_BOUND_VIOLATED", "UNCHECKED_CAST")
            return (java.lang.Enum.valueOf<Any>(type.java as Class<Any>, instance) as K)
        }


        if (type.isInstance(instance)) {
            return instance as K
        }
        @SuppressWarnings("UNCHECKED_CAST")
        val transfomer = findWideningTransformer(instance::class, type) as TypeTransformer<Any, K>?
        return transfomer?.transform(instance)
                ?: throw SystemException("unable to convert from ${instance::class.qualifiedName} to ${type.qualifiedName}")
    }


}