package io.codestream.di.api

class LambdaFactory<T>(val supplier: (ctx: Context) -> T, val postBinding: Boolean = true) : Factory<T> {

    override fun postBinding(): Boolean {
        return postBinding
    }

    override fun get(ctx: Context): T {
        return supplier(ctx)
    }
}