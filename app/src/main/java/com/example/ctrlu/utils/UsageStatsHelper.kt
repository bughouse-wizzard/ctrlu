package com.example.ctrlu.utils

import com.example.ctrlu.data.DailyUsageDao
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

object UsageStatsHelper {

    suspend fun getTimelineFromDb(dao: DailyUsageDao): Map<String, Long> {
        val entries = dao.getAll()
        val formatter = SimpleDateFormat("EEEE", Locale("ru"))

        return entries.associate {
            val date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(it.date)
            val day = formatter.format(date ?: Date())
            day to it.totalUsageMillis
        }
    }


    fun formatTime(millis: Long): String {
        val hours = TimeUnit.MILLISECONDS.toHours(millis)
        val minutes = TimeUnit.MILLISECONDS.toMinutes(millis) % 60
        return "${hours}ч ${minutes}м"
    }
}
