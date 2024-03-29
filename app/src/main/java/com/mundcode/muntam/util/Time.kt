package com.mundcode.muntam.util

import kotlin.math.absoluteValue
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

// todo 테스트
fun Instant.asMTDateText(): String {
    val date = this.toLocalDateTime(TimeZone.UTC)
    return try {
        "%02d.%02d.%02d"
            .format("${date.year}".takeLast(2).toInt(), date.monthNumber, date.dayOfMonth)
    } catch (e: Exception) {
        "$e"
    }
}

fun Long.asTimeLimitText(): String {
    val totalSec = this
    val sec = totalSec % 60
    val totalMin = totalSec / 60
    val min = totalMin % 60
    val hour = totalMin / 60
    return "%d시간 %02d분 %02d초".format(hour, min, sec)
}

fun Long.asLapsedTimeText(): String {
    val totalSec = this
    val sec = totalSec % 60
    val totalMin = totalSec / 60
    val min = totalMin % 60
    val hour = totalMin / 60
    return "%d시간 %02d분 %02d초".format(hour, min, sec)
}

fun Long.asCurrentTimerText(): String {
    val sign = if (this < 0) "-" else ""

    val totalSec = this.absoluteValue
    val sec = totalSec % 60
    val totalMin = totalSec / 60
    val min = totalMin % 60
    val hour = totalMin / 60
    return "$sign%02d:%02d:%02d".format(hour, min, sec)
}
