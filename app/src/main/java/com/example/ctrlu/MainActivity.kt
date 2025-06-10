package com.example.ctrlu

import android.annotation.SuppressLint
import android.app.AppOpsManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.example.ctrlu.data.AppDatabase
import com.example.ctrlu.service.UnlockService
import com.example.ctrlu.ui.screens.MainScreen
import com.example.ctrlu.ui.theme.CtrluTheme
import com.example.ctrlu.ui.viewmodel.UnlockCounterViewModel
import com.example.ctrlu.ui.viewmodel.UsageHistoryViewModel
import com.example.ctrlu.utils.AppLifecycleObserver
import com.example.ctrlu.utils.UsageStatsCollector
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private lateinit var appDatabase: AppDatabase
    private lateinit var usageViewModel: UsageHistoryViewModel
    private lateinit var unlockViewModel: UnlockCounterViewModel

    @SuppressLint("StateFlowValueCalledInComposition")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Инициализация базы данных
        appDatabase = AppDatabase.getDatabase(applicationContext)

        // Инициализация ViewModel
        val viewModelFactory = ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        usageViewModel = ViewModelProvider(this, viewModelFactory)[UsageHistoryViewModel::class.java]
        unlockViewModel = ViewModelProvider(this, viewModelFactory)[UnlockCounterViewModel::class.java]

        // Ручной запуск сбора статистики при первом запуске
        lifecycleScope.launch {
            UsageStatsCollector.collectAndSave(applicationContext, appDatabase.dailyUsageDao())
        }

        // Периодическая сборка
        UsageStatsCollector.scheduleDailyStatsCollection(applicationContext)

        // Подключение к Lifecycle'у всего приложения
//        ProcessLifecycleOwner.get().lifecycle.addObserver(AppLifecycleObserver(applicationContext, usageViewModel))
        lifecycle.addObserver(AppLifecycleObserver(applicationContext, usageViewModel))
        // Проверка разрешений
        if (!hasUsageStatsPermission()) {
            startActivity(
                Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS).apply {
                    data = Uri.parse("package:$packageName")
                }
            )
        }

        requestIgnoreBatteryOptimizations()
        startUnlockService()

        // Установка UI
        setContent {
            CtrluTheme {
                MainScreen(
                    usageViewModel = usageViewModel,
                    unlockViewModel = unlockViewModel
                )
            }
        }
    }

    private fun hasUsageStatsPermission(): Boolean {
        val appOps = getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            appOps.unsafeCheckOpNoThrow(
                AppOpsManager.OPSTR_GET_USAGE_STATS,
                android.os.Process.myUid(),
                packageName
            ) == AppOpsManager.MODE_ALLOWED
        } else {
            appOps.checkOpNoThrow(
                AppOpsManager.OPSTR_GET_USAGE_STATS,
                android.os.Process.myUid(),
                packageName
            ) == AppOpsManager.MODE_ALLOWED
        }
    }

    private fun requestIgnoreBatteryOptimizations() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val pm = getSystemService(Context.POWER_SERVICE) as android.os.PowerManager
            if (!pm.isIgnoringBatteryOptimizations(packageName)) {
                startActivity(
                    Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS).apply {
                        data = Uri.parse("package:$packageName")
                    }
                )
            }
        }
    }

    private fun startUnlockService() {
        val intent = Intent(this, UnlockService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent)
        } else {
            startService(intent)
        }
    }
    override fun onResume() {
        super.onResume()
        usageViewModel.refreshStats()
    }

}