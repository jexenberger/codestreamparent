package io.codestream.core.runtime

import io.codestream.core.api.ParameterCallback
import io.codestream.core.api.descriptor.ParameterDescriptor
import java.util.*

class DefaultParameterCallback(val value:Any? = null) : ParameterCallback{
    override fun capture(descriptor: ParameterDescriptor): Any? {
        return value
    }
}