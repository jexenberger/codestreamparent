package io.codestream.core.api

import java.io.File
import io.codestream.util.OS

data class CodestreamSettings(val yamlModulePath:File = File("${OS.os().homeDir}/.cs/_modules"),
                              val systemThreads:Int = Runtime.getRuntime().availableProcessors())