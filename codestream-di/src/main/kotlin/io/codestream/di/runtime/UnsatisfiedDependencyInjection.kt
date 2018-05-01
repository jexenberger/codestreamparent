package io.codestream.di.runtime

class UnsatisfiedDependencyInjection(name: String, msg: String) : RuntimeException("[$name] -> $msg") {
}