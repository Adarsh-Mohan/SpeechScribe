package com.example.speechscribe.utils

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


object NoteUtils {
    /**
     * @param time that will be convert  and formatted to string
     * @return string
     */
    fun dateFromLong(time: Long): String {
        val format: DateFormat =
            SimpleDateFormat("EEE, dd MMM yyyy 'at' hh:mm aaa", Locale.US)
        return format.format(Date(time))
    }
}
