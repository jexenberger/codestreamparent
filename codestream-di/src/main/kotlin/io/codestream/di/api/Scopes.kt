package io.codestream.di.api

import io.codestream.di.runtime.InstanceScope
import io.codestream.di.runtime.PrototypeScope
import io.codestream.di.runtime.SingletonScope
import java.util.*

object Scopes {

    val scopeNames:Set<String> get() = Collections.unmodifiableSet(scopes.keys)

    private val scopes: MutableMap<String, Scope> = mutableMapOf(
            ScopeType.prototype.name to PrototypeScope,
            ScopeType.singleton.name to SingletonScope,
            ScopeType.instance.name to InstanceScope
    )

    operator fun plusAssign(scope: Scope) {
        scopes[scope.id] = scope
    }

    operator fun get(id: String) = scopes[id]


}