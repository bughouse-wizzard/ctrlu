package com.example.ctrlu.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.ctrlu.data.AppDatabase
import com.example.ctrlu.model.AppContribution
import com.example.ctrlu.model.AppUsageInfo
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.text.SimpleDateFormat
import java.util.*

class UsageHistoryViewModel(application: Application) : AndroidViewModel(application) {
    private val dao = AppDatabase.getDatabase(application).dailyUsageDao()

    private val _topApps = MutableStateFlow<List<AppUsageInfo>>(emptyList())
    val topApps: StateFlow<List<AppUsageInfo>> = _topApps

    private val _dailyUsage = MutableStateFlow(0L)
    val dailyUsage: StateFlow<Long> = _dailyUsage

    private val _weeklyUsage = MutableStateFlow<Map<String, Long>>(emptyMap())
    val weeklyUsage: StateFlow<Map<String, Long>> = _weeklyUsage

    init {
        viewModelScope.launch {
            loadTodayStats()
            loadWeeklyStats()
        }
    }

    private suspend fun loadTodayStats() {
        val today = getTodayDate()
        val entry = dao.getByDate(today)

        _dailyUsage.value = entry?.totalUsageMillis ?: 0L
        _topApps.value = parseAppsJson(entry?.appsJson)
    }

    private suspend fun loadWeeklyStats() {
        val entries = dao.getAll()
        val weekly = entries
            .takeLast(7)
            .associate { it.date to it.totalUsageMillis }
        _weeklyUsage.value = weekly
    }

    private fun parseAppsJson(json: String?): List<AppUsageInfo> {
        if (json.isNullOrEmpty()) return emptyList()
        return try {
            val contributions = Json.decodeFromString<List<AppContribution>>(json)
            contributions.map {
                AppUsageInfo(
                    packageName = it.appName,
                    appName = it.appName,
                    usageTimeMillis = it.usageMillis,
                    appIcon = null // если хочешь — можешь подгрузить Drawable по пакету
                )
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    private fun getTodayDate(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return sdf.format(Date())
    }

    fun refreshStats() {
        viewModelScope.launch {
            loadTodayStats()
            loadWeeklyStats()
        }
    }
}