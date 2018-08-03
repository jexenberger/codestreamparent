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

package io.codestream.di.api

import io.codestream.di.runtime.ConstructorInjection
import io.codestream.di.runtime.DependencyResolver
import io.codestream.di.runtime.InstanceInjection
import kotlin.reflect.KClass

fun id(id: String) = StringId(id)
fun id(type: KClass<*>) = TypeId(type)

fun <T> bind() : ComponentDefinitionBuilder<T> {
    return ComponentDefinitionBuilder()
}

 fun <T> bind(factory: Factory<T>): ComponentDefinitionBuilder<T> {
    return bind<T>().toFactory(factory)
}

fun <T> bind(supplier: (id:ComponentId, ctx: Context) -> T): ComponentDefinitionBuilder<T> {
    return bind(instancesOf(supplier))
}

fun <T> theInstance(instance: T, bind: Boolean = false): Factory<T> {
    return InstanceInjection(instance, bind)
}

fun <T> addInstance(instance: T, bind: Boolean = false): ComponentDefinitionBuilder<T> {
    return bind(theInstance(instance, bind))
}

fun <T> anInstance(instance: T, bind: Boolean = false): ComponentDefinition<T> {
    return addInstance<T>(instance, bind).toDefn()
}

fun <T> addType(instance: KClass<*>, bind: Boolean = false): ComponentDefinitionBuilder<T> {
    return bind(theType(instance, bind))
}

fun <T> aType(instance: KClass<*>, bind: Boolean = false): ComponentDefinition<T> {
    return addType<T>(instance, bind).toDefn()
}

fun <T> theType(type: KClass<*>, bind: Boolean = true): Factory<T> {
    return ConstructorInjection(type, bind)
}

fun <T> instancesOf(bind: Boolean, factory: (id: ComponentId, ctx: Context) -> T): Factory<T> {
    return LambdaFactory(factory, bind)
}

fun <T> instancesOf(factory: (id: ComponentId, ctx: Context) -> T): Factory<T> {
    return LambdaFactory(factory, true)
}

fun context(): DefinableContext {
   return DefaultApplicationContext()
}

fun context(builder: DefinableContext.() -> Unit): DefinableContext {
    val ctx = context()
    ctx.builder()
    return ctx
}

fun addDependencyHandler(handler:Dependency) {
    DependencyResolver.add(handler)
}