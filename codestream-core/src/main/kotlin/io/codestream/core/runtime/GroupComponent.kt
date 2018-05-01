package io.codestream.core.runtime

import io.codestream.core.engine.NodeState
import io.codestream.core.metamodel.GroupComponentDef

interface GroupComponent : Component {

    fun before(defn: GroupComponentDef, ctx: Context): NodeState

    fun after(defn: GroupComponentDef, ctx: Context): NodeState

}