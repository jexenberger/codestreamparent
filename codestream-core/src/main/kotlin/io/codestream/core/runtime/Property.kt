package io.codestream.core.runtime

import io.codestream.core.metamodel.PropertyDef

interface Property {

    val name:String

    fun bind(defn: PropertyDef, ctx:Context)

}