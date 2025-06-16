package com.example.vendora.utils.wrapper

import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

fun String.formatTimestamp(): String {
    val instant = Instant.parse(this)

    // Convert to local date time (device's time zone)
    val time = instant.toLocalDateTime(TimeZone.currentSystemDefault())

    return "${time.month} ${time.dayOfMonth}, ${time.year} | ${time.hour}:${time.minute}"
}