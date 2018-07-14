package io.codestream.api

import io.codestream.api.descriptor.ParameterDescriptor
import java.util.*

interface ParameterCallback {


    fun capture(descriptor: ParameterDescriptor) : Any?

}