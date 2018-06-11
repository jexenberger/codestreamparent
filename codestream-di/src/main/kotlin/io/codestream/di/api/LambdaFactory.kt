package io.codestream.di.api

class LambdaFactory<T>(val supplier: (id:ComponentId, ctx: Context) -> T, val postBinding: Boolean = true) : Factory<T> {

    override fun postBinding(): Boolean {
        return postBinding
    }

    override fun get(id:ComponentId, ctx: Context): T {
        return supplier(id, ctx)
    }
}