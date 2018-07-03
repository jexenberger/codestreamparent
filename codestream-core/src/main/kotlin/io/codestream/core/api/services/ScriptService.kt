package io.codestream.core.api.services

import io.codestream.di.api.Context
import java.io.InputStream

interface ScriptService {

    val language: String

    fun write(source:InputStream, target:InputStream, ctx:Context)

}