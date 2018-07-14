package io.codestream.api

import io.codestream.di.annotation.Inject
import io.codestream.di.annotation.Qualified
import io.codestream.di.annotation.Value
import java.io.File
import io.codestream.util.OS
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

data class CodestreamSettings(
        @Value("yaml.module.path")
        val yamlModulePath: File = File("${OS.os().homeDir}/.cs/_modules"),
        @Inject
        val executor: ExecutorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()),
        @Value("enable.debug")
        val debug: Boolean = false

)
