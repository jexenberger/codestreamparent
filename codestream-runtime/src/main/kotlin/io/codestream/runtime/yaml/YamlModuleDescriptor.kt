package io.codestream.runtime.yaml

data class YamlModuleDescriptor(val name:String?, val description:String, val version:String?, val taskDir:String? = "") {
}