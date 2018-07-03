package io.codestream.core.api.events

import java.time.LocalDateTime
import java.util.*

open class CodestreamEvent(val id: String = UUID.randomUUID().toString(), val timeStamp: LocalDateTime = LocalDateTime.now())