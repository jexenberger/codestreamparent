package io.codestream.core.api

import io.codestream.core.api.descriptor.ParameterDescriptor
import java.util.*

interface ParameterCallback {


    fun capture(descriptor: ParameterDescriptor) : Optional<Any>

}