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

package io.codestream.util

fun <T, K> ok(value: T): Either<T, K> = Either.left(value)
fun <K> ok(): Either<Unit, K> = Either.left(Unit)
fun <T, K> ok(value: () -> T): Either<T, K> = Either.left(value())
fun <T, K> fail(value: K): Either<T, K> = Either.right(value)
fun <T, K> fail(value: () -> K): Either<T, K> = Either.right(value())
fun <T, K> failWhen(l: T, r: () -> K?): Either<T, K> {
    return r()?.let { fail<T, K>(it) } ?: ok(l)
}

@Suppress("UNCHECKED_CAST")
sealed class Either<L, R> {


    val left: L?
        get() = when (this) {
            is Either.Left -> this.value
            is Either.Right -> null
        }


    val right: R?
        get() = when (this) {
            is Either.Left -> null
            is Either.Right -> this.value
        }


    class Left<L, R>(val value: L) : Either<L, R>() {
        override fun toString(): String = "Left: $value"
    }

    class Right<L, R>(val value: R) : Either<L, R>() {
        override fun toString(): String = "Right: $value"
    }







    fun <U, Z> apply(l: (L) -> Either<U, Z>, r: (R) -> Either<U, Z>): Either<U, Z> {
        return when (this) {
            is Either.Left -> l(this.value)
            is Either.Right -> r(this.value)
        }
    }



    fun <Z> map(l: (value: L) -> Z, r: (error: R) -> Z): Z {
        return when (this) {
            is Either.Left -> l(this.value)
            is Either.Right -> r(this.value)
        }
    }

    fun <Z, U> flatMap(l: (value: L) -> Either<Z, U>, r: (error: R) -> Either<Z, U>): Either<Z, U> {
        return when (this) {
            is Either.Left -> l(this.value)
            is Either.Right -> r(this.value)
        }
    }

    fun <Z> mapL(l: (value: L) -> Z): Either<Z, R> {
        return when (this) {
            is Left -> left(l(this.value))
            is Right -> {
                this as Either<Z, R>
            }
        }
    }

    fun whenL(l: (value: L) -> Unit): Either<L, R> {
        when (this) {
            is Either.Left -> l(this.value)
        }
        return this
    }

    fun whenR(r: (value: R) -> Unit): Either<L, R> {
        when (this) {
            is Either.Right -> r(this.value)
        }
        return this
    }

    fun <Z> flatMapL(l: (value: L) -> Either<Z, R>): Either<Z, R> {
        return when (this) {
            is Either.Left -> l(this.value)
            is Either.Right -> right(this.value)
        }
    }

    fun <Z> flatMapR(r: (value: R) -> Either<L, Z>): Either<L, Z> {
        return when (this) {
            is Either.Left -> left(this.value)
            is Either.Right -> r(this.value)
        }
    }

    fun <Z> mapR(r: (value: R) -> Z): Either<L, Z> {
        return when (this) {
            is Either.Left -> this as Either<L, Z>
            is Either.Right -> right(r(this.value))
        }
    }

    operator fun component1() : L? {
        return left
    }

    operator fun component2() : R? {
        return right
    }



    companion object {
        fun <Z, K> left(left: Z): Either<Z, K> {
            return Either.Left(left)
        }

        fun <Z, K> right(right: K): Either<Z, K> {
            return Either.Right(right)
        }

        fun <Z, Y : Exception> onException(handler: () -> Z): Either<Z, Y> {
            return Either.onException<Z, Y, Y>(handler, { it })
        }

        fun <Z, Y, U : Exception> onException(handler: () -> Z, onError: (U) -> Y): Either<Z, Y> {
            return try {
                ok(handler())
            } catch (e: Exception) {
                fail(onError(e as U))
            }
        }
    }

}



inline infix fun <Z,L,R> Either<L,R>.bind(f: (L) -> Either<Z, R>): Either<Z, R> {
    return when (this) {
        is Either.Left -> f(this.value)
        is Either.Right -> Either.Right(this.value)
    }
}



inline infix fun <Z,L,R> Either<L,R>.bindR(f: (R) -> Either<L, Z>): Either<L, Z> {
    return when (this) {
        is Either.Left -> Either.Left(this.value)
        is Either.Right -> f(this.value)
    }
}