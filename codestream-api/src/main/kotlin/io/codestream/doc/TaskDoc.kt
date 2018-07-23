package io.codestream.doc

data class TaskDoc(val name:String, val description:String, val parameters:Collection<ParameterDoc>, val returnType:Pair<String, String>?)