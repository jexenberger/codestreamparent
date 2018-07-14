package io.codestream.runtime

import io.codestream.api.ParameterCallback
import io.codestream.api.descriptor.ParameterDescriptor

class DefaultParameterCallback(val value:Any? = null) : ParameterCallback{
    override fun capture(descriptor: ParameterDescriptor): Any? {
        return value
    }
}