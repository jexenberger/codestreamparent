package io.codestream.util.transformation

import kotlin.reflect.KClass

typealias Init = (Int) -> Any
typealias Set = (Int, Any, Any) -> Unit
typealias Get<K> = (Int, Any) -> K
typealias AsIterator<T> = (Int) -> Iterator<T>

data class TypeHandler<K>(val init: Init, val set: Set, val get: Get<K>, val iterator: AsIterator<K>)

class ArrayDecorator<T>(val instance: Any) : Iterable<T> {


    private val type: KClass<*>

    init {
        type = arrayTypes[instance::class]?.let { instance::class } ?: throw IllegalStateException("${instance::class} is not a supported type")
    }

    operator fun get(idx:Int) : T {
        return arrayTypes[type]!!.get(idx, instance) as T
    }

    operator fun set(idx:Int, value:T) {
        arrayTypes[type]!!.set(idx,instance, value as Any)
    }

    override fun iterator(): Iterator<T> {
        return arrayTypes[type]!!.iterator as Iterator<T>
    }


    companion object {
        val arrayTypes = mapOf<KClass<*>, TypeHandler<*>>(
                IntArray::class to TypeHandler(
                        { IntArray(it) },
                        { idx, arr, value: Any -> (arr as IntArray).set(idx, value as Int) },
                        { idx, arr -> (arr as IntArray).get(idx) }, { (it as IntArray).iterator() }
                ),
                BooleanArray::class to TypeHandler(
                        { BooleanArray(it) },
                        { idx, arr, value: Any -> (arr as BooleanArray).set(idx, value as Boolean) },
                        { idx, arr -> (arr as BooleanArray).get(idx) }, { (it as BooleanArray).iterator() }
                ),
                ByteArray::class to TypeHandler(
                        { ByteArray(it) },
                        { idx, arr, value: Any -> (arr as ByteArray).set(idx, value as Byte) },
                        { idx, arr -> (arr as ByteArray).get(idx) }, { (it as ByteArray).iterator() }
                ),
                ShortArray::class to TypeHandler(
                        { ShortArray(it) },
                        { idx, arr, value: Any -> (arr as ShortArray).set(idx, value as Short) },
                        { idx, arr -> (arr as ShortArray).get(idx) }, { (it as ShortArray).iterator() }
                ),
                LongArray::class to TypeHandler(
                        { LongArray(it) },
                        { idx, arr, value: Any -> (arr as LongArray).set(idx, value as Long) },
                        { idx, arr -> (arr as LongArray).get(idx) }, { (it as LongArray).iterator() }
                ),
                CharArray::class to TypeHandler(
                        { CharArray(it) },
                        { idx, arr, value: Any -> (arr as CharArray).set(idx, value as Char) },
                        { idx, arr -> (arr as CharArray).get(idx) }, { (it as CharArray).iterator() }
                ),
                FloatArray::class to TypeHandler(
                        { FloatArray(it) },
                        { idx, arr, value: Any -> (arr as FloatArray).set(idx, value as Float) },
                        { idx, arr -> (arr as FloatArray).get(idx) }, { (it as FloatArray).iterator() }
                ),
                DoubleArray::class to TypeHandler(
                        { DoubleArray(it) },
                        { idx, arr, value: Any -> (arr as DoubleArray).set(idx, value as Double) },
                        { idx, arr -> (arr as DoubleArray).get(idx) }, { (it as DoubleArray).iterator() }
                ),
                Array<Int>::class to TypeHandler(
                        { arrayOf<Int>() },
                        { idx, arr, value: Any -> (arr as Array<Int>).set(idx, value as Int) },
                        { idx, arr -> (arr as Array<Int>).get(idx) }, { (it as Array<Int>).iterator() }
                ),
                Array<Boolean>::class to TypeHandler(
                        { arrayOf<Boolean>() },
                        { idx, arr, value: Any -> (arr as Array<Boolean>).set(idx, value as Boolean) },
                        { idx, arr -> (arr as Array<Boolean>).get(idx) }, { (it as Array<Boolean>).iterator() }
                ),
                Array<Byte>::class to TypeHandler(
                        { arrayOf<Byte>() },
                        { idx, arr, value: Any -> (arr as Array<Byte>).set(idx, value as Byte) },
                        { idx, arr -> (arr as Array<Byte>).get(idx) }, { (it as Array<Byte>).iterator() }
                ),
                Array<Short>::class to TypeHandler(
                        { arrayOf<Short>() },
                        { idx, arr, value: Any -> (arr as Array<Short>).set(idx, value as Short) },
                        { idx, arr -> (arr as Array<Short>).get(idx) }, { (it as Array<Short>).iterator() }
                ),
                Array<Long>::class to TypeHandler(
                        { arrayOf<Long>() },
                        { idx, arr, value: Any -> (arr as Array<Long>).set(idx, value as Long) },
                        { idx, arr -> (arr as Array<Long>).get(idx) }, { (it as Array<Long>).iterator() }
                ),
                Array<Char>::class to TypeHandler(
                        { arrayOf<Char>() },
                        { idx, arr, value: Any -> (arr as Array<Char>).set(idx, value as Char) },
                        { idx, arr -> (arr as Array<Char>).get(idx) }, { (it as Array<Char>).iterator() }
                ),
                Array<Float>::class to TypeHandler(
                        { arrayOf<Float>() },
                        { idx, arr, value: Any -> (arr as Array<Float>).set(idx, value as Float) },
                        { idx, arr -> (arr as Array<Float>).get(idx) }, { (it as Array<Float>).iterator() }
                ),
                Array<Double>::class to TypeHandler(
                        { arrayOf<Double>() },
                        { idx, arr, value: Any -> (arr as Array<Double>).set(idx, value as Double)  },
                        { idx, arr -> (arr as Array<Double>).get(idx) }, { (it as Array<Double>).iterator() }
                ),
                Array<String>::class to TypeHandler(
                        { arrayOf<String>() },
                        { idx, arr, value: Any -> (arr as Array<String>).set(idx, value as String) },
                        { idx, arr -> (arr as Array<String>).get(idx) }, { (it as Array<String>).iterator() }
                ),
                Array<Any>::class to TypeHandler(
                        { arrayOf<Any>() },
                        { idx, arr, value: Any -> (arr as Array<Any>).set(idx, value) },
                        { idx, arr -> (arr as Array<Any>).get(idx) }, { (it as Array<Any>).iterator() }
                )
        )

        fun isArrayType(candidate:KClass<*>) = arrayTypes.containsKey(candidate)

    }

}