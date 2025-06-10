package com.example.ctrlu.utils

import android.app.usage.UsageEvents
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import com.example.ctrlu.model.AppUsageInfo
import java.util.*

object UsageLiveTracker {

    fun getTodayScreenTime(context: Context): Long {
        val usageStatsManager = context.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager

        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        val startTime = calendar.timeInMillis
        val endTime = System.currentTimeMillis()

        val events = usageStatsManager.queryEvents(startTime, endTime)

        var lastForegroundTime = 0L
        var totalScreenTime = 0L
        var currentApp: String? = null

        val event = UsageEvents.Event()

        while (events.hasNextEvent()) {
            events.getNextEvent(event)

            when (event.eventType) {
                UsageEvents.Event.MOVE_TO_FOREGROUND -> {
                    if (!isSystemApp(context.packageManager, event.packageName)) {
                        lastForegroundTime = event.timeStamp
                        currentApp = event.packageName
                    }
                }

                UsageEvents.Event.MOVE_TO_BACKGROUND -> {
                    if (event.packageName == currentApp && lastForegroundTime > 0) {
                        totalScreenTime += event.timeStamp - lastForegroundTime
                        lastForegroundTime = 0L
                        currentApp = null
                    }
                }
            }
        }

        return totalScreenTime
    }

    fun getTopUsedApps(context: Context, limit: Int = 5): List<AppUsageInfo> {
        val usageStatsManager = context.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager

        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        val startTime = calendar.timeInMillis
        val endTime = System.currentTimeMillis()

        val events = usageStatsManager.queryEvents(startTime, endTime)

        val usageMap = mutableMapOf<String, Long>()
        val lastOpenMap = mutableMapOf<String, Long>()

        val event = UsageEvents.Event()

        while (events.hasNextEvent()) {
            events.getNextEvent(event)

            when (event.eventType) {
                UsageEvents.Event.MOVE_TO_FOREGROUND -> {
                    if (!isSystemApp(context.packageManager, event.packageName)) {
                        lastOpenMap[event.packageName] = event.timeStamp
                    }
                }

                UsageEvents.Event.MOVE_TO_BACKGROUND -> {
                    val start = lastOpenMap[event.packageName] ?: continue
                    val duration = event.timeStamp - start
                    usageMap[event.packageName] = usageMap.getOrDefault(event.packageName, 0L) + duration
                    lastOpenMap.remove(event.packageName)
                }
            }
        }

        val pm = context.packageManager

        return usageMap.entries
            .sortedByDescending { it.value }
            .take(limit)
            .map {
                val appName = try {
                    pm.getApplicationLabel(pm.getApplicationInfo(it.key, 0)).toString()
                } catch (e: Exception) {
                    it.key
                }
                val icon = try {
                    pm.getApplicationIcon(it.key)
                } catch (e: Exception) {
                    null
                }

                AppUsageInfo(
                    packageName = it.key,
                    appName = appName,
                    usageTimeMillis = it.value,
                    appIcon = icon
                )
            }
    }

    private fun isSystemApp(pm: PackageManager, packageName: String): Boolean {
        return try {
            val appInfo = pm.getApplicationInfo(packageName, 0)
            appInfo.flags and (ApplicationInfo.FLAG_SYSTEM or ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0
        } catch (e: Exception) {
            false
        }
    }
}
