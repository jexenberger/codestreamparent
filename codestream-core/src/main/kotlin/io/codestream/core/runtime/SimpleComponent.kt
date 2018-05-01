package io.codestream.core.runtime

import io.codestream.core.metamodel.ComponentDef

interface SimpleComponent : Component {

    fun execute(defn:ComponentDef, ctx:Context)
}