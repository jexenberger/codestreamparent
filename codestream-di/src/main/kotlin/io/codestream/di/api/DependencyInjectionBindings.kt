package io.codestream.di.api

import io.codestream.util.OS
import javax.script.Bindings
import javax.script.SimpleBindings

open class DependencyInjectionBindings(val ctx: Context, protected val m: MutableMap<String, Any?> = mutableMapOf()) : AbstractMap<String, Any?>(), Bindings {

    override fun clear() = m.clear()

    override fun putAll(from: Map<out String, Any>) = m.putAll(from)

    override fun put(key: String, value: Any?) = m.put(key, value)

    override fun remove(key: String?) = m.remove(key)


    override val entries: MutableSet<MutableMap.MutableEntry<String, Any?>>
        get() = m.entries
    override val keys: MutableSet<String>
        get() = m.keys
    override val values: MutableCollection<Any?>
        get() = m.values

    init {
        put("_ctx", ctx)
        put("_os", OS.os())
    }

}