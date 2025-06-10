package com.example.ctrlu.utils

import android.content.Context
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.example.ctrlu.data.AppDatabase
import com.example.ctrlu.ui.viewmodel.UsageHistoryViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AppLifecycleObserver(
    private val context: Context,
    private val usageViewModel: UsageHistoryViewModel
) : DefaultLifecycleObserver {

    // Срабатывает при каждом открытии активности
    override fun onResume(owner: LifecycleOwner) {
        refreshData()
    }

    private fun refreshData() {
        usageViewModel.refreshStats()
        CoroutineScope(Dispatchers.IO).launch {
            val dao = AppDatabase.getDatabase(context).dailyUsageDao()
            UsageStatsCollector.collectAndSave(context, dao)
        }
    }
}