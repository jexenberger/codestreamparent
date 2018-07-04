package io.codestream.core.api.events

import java.time.LocalDateTime
import java.util.*

open class CodestreamEvent(
        val desc:String,
        val id: String = UUID.randomUUID().toString(),
        val timeStamp: LocalDateTime = LocalDateTime.now()
) {
    override fun toString(): String {
        return "id=$id::timeStamp=$timeStamp[$desc]"
    }
}