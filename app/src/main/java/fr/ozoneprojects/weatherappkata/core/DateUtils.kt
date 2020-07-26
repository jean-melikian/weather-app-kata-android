package fr.ozoneprojects.weatherappkata.core

import java.text.SimpleDateFormat
import java.util.*

fun Date.unixToPrettyFormat(pattern: String, timezone: String): String =
    SimpleDateFormat(pattern, Locale.FRENCH).apply {
        timeZone = TimeZone.getTimeZone(timezone)
    }.format(this.time * 1000)
