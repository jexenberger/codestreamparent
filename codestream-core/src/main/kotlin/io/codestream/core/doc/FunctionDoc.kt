package io.codestream.core.doc

data class FunctionDoc(val name:String, val description:String, val params:Collection<Pair<String, String>>) {
}