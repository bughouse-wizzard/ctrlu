package com.example.ctrlu.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.ctrlu.data.AppDatabase
import com.example.ctrlu.utils.UsageStatsCollector
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DailyStatsReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        CoroutineScope(Dispatchers.IO).launch {
            val dao = AppDatabase.getDatabase(context).dailyUsageDao()
            UsageStatsCollector.collectAndSave(context, dao)
        }
    }
}