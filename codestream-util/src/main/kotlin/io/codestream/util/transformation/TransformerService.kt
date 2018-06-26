package io.codestream.util.transformation

import io.codestream.util.SystemException
import java.io.File
import java.math.BigDecimal
import kotlin.reflect.KClass
import kotlin.reflect.full.isSubclassOf
import kotlin.reflect.full.isSuperclassOf


object TransformerService {

    internal val CONVERSION_REGISTRY: MutableMap<Pair<KClass<*>, KClass<*>>, TypeTransformer<*, *>> = mutableMapOf()



    init {
        addConverter(String::class, File::class, LambdaTransformer<String, File> { File(it) })
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


    fun isCollectionType(a: KClass<*>) : Boolean {
        val collection = isCollection(a)
        val iterable = isIterableType(a)
        val array = ArrayDecorator.isArrayType(a)
        return collection || iterable || array
    }



    private fun isIterableType(a: KClass<*>) = a.equals(Iterable::class) || a.isSubclassOf(Iterable::class)

    private fun isCollection(a: KClass<*>) = a.equals(Collection::class) || a.isSubclassOf(Collection::class)

    fun areBothCollectionTypes(a: KClass<*>, b:KClass<*>) : Boolean {
        return isCollectionType(a) && isCollectionType(b)
    }

    @SuppressWarnings("UNCHECKED_CAST")
    inline fun <reified K> convert(instance: Any, typeHint: KClass<*>? = null): K {
        val type = typeHint?.let { it } ?: K::class
        //special case with enum as we always need the absolute concrete type
        if (instance is String && type.java.isEnum) {
            @Suppress("UPPER_BOUND_VIOLATED", "UNCHECKED_CAST")
            return (java.lang.Enum.valueOf<Any>(type.java as Class<Any>, instance) as K)
        }


        if (type.isInstance(instance)) {
            return instance as K
        }
        @SuppressWarnings("UNCHECKED_CAST")
        val transfomer = findWideningTransformer(instance::class, type) as TypeTransformer<Any, K>?
        return transfomer?.transform(instance) ?: throw SystemException("unable to convert from ${instance::class.qualifiedName} to ${K::class.qualifiedName}")
    }


}