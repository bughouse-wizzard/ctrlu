package com.example.ctrlu.utils

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.example.ctrlu.data.DailyUsageDao
import com.example.ctrlu.model.AppContribution
import com.example.ctrlu.model.DailyUsageEntry
import com.example.ctrlu.receiver.DailyStatsReceiver
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import kotlin.random.Random

object UsageStatsCollector {
    private fun getTodayDate(): String = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

    suspend fun collectAndSave(context: Context, dao: DailyUsageDao) {
        val today = getTodayDate()

        val topApps = UsageLiveTracker.getTopUsedApps(context, limit = 5)
        val screenTimeToday = UsageLiveTracker.getTodayScreenTime(context)

        val contributions = topApps.map {
            AppContribution(
                appName = it.appName,
                usageMillis = it.usageTimeMillis,
                colorHex = getRandomColor()
            )
        }

        val json = Json.encodeToString(contributions)
        val entry = DailyUsageEntry(
            date = today,
            totalUsageMillis = screenTimeToday,
            appsJson = json
        )

        dao.insert(entry) // onConflict = REPLACE => обновится существующая запись
    }

    fun scheduleDailyStatsCollection(context: Context) {
        val intent = Intent(context, DailyStatsReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 23)
            set(Calendar.MINUTE, 59)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )
    }

    private fun getRandomColor(): String {
        val rnd = Random.Default
        return "#%06x".format(rnd.nextInt(0xFFFFFF))
    }
}
