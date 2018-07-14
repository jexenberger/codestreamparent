package io.codestream.runtime.yaml

data class Value(val lineNo:Int, val value:Any) {

    override fun toString(): String {
        return value.toString()
    }
}