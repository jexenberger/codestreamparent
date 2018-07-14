package io.codestream.util

import java.time.LocalDate
import java.time.ZoneId
import java.util.*

fun LocalDate.toDate(zoneId: ZoneId = ZoneId.systemDefault()): Date {
    return Date.from(this.atStartOfDay().atZone(zoneId).toInstant())
}