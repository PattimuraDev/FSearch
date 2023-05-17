package repo.pattimuradev.fsearch.misc

import java.util.*

object TimeHandler {
    private const val SECOND_MILLIS = 1000
    private const val MINUTE_MILLIS = 60 * SECOND_MILLIS
    private const val HOUR_MILLIS = 60 * MINUTE_MILLIS
    private const val DAY_MILLIS = 24 * HOUR_MILLIS
    fun currentDate(): Date{
        val calendar: Calendar = Calendar.getInstance()
        return calendar.time
    }

    fun getTimeAgo(inputTime: Long): String{
        var time = inputTime
        if (time < 1000000000000L) {
            // if timestamp given in seconds, convert to millis
            time *= 1000
        }

        val now = currentDate().time
        if (time > now || time <= 0) {
            return "in the future"
        }

        val selisih: Long = now - time
        return if (selisih < MINUTE_MILLIS) {
            "Moments ago"
        } else if (selisih < 2 * MINUTE_MILLIS) {
            "1m"
        } else if (selisih < 50 * MINUTE_MILLIS) {
            (selisih / MINUTE_MILLIS).toString() + "m"
        } else if (selisih < 90 * MINUTE_MILLIS) {
            "An hour ago"
        } else if (selisih < 24 * HOUR_MILLIS) {
            (selisih / HOUR_MILLIS).toString() + "h"
        } else if (selisih < 48 * HOUR_MILLIS) {
            "Yesterday"
        } else {
            (selisih / DAY_MILLIS).toString() + "d"
        }
    }
}