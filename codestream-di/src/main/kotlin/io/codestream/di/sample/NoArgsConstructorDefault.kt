package io.codestream.di.sample

class NoArgsConstructorDefault() {

    constructor(name:String) : this() {
        this.name = name
    }

    var name:String = "hello"
}