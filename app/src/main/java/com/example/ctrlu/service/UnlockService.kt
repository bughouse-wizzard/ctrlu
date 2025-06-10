package com.example.ctrlu.service

import android.Manifest
import android.annotation.SuppressLint
import android.app.*
import android.content.*
import android.content.pm.PackageManager
import android.os.Build
import android.os.IBinder
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.ctrlu.R

class UnlockService : Service() {

    private lateinit var unlockReceiver: BroadcastReceiver

    @SuppressLint("ForegroundServiceType")
    override fun onCreate() {
        super.onCreate()
        createNotificationChannels()

        unlockReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent?) {
                if (intent?.action == Intent.ACTION_USER_PRESENT) {
                    handleUnlockEvent(context)
                }
            }
        }

        val filter = IntentFilter(Intent.ACTION_USER_PRESENT)
        registerReceiver(unlockReceiver, filter)

        startForeground(1, buildForegroundNotification())
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(unlockReceiver)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? = null

    private fun handleUnlockEvent(context: Context) {
        val prefs = context.getSharedPreferences("unlock_prefs", Context.MODE_PRIVATE)
        val count = prefs.getInt("unlock_count", 0) + 1
        prefs.edit().putInt("unlock_count", count).apply()

        val notification = NotificationCompat.Builder(context, UNLOCK_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Опять ты?")
            .setContentText("Сегодня уже $count раз!")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU || ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            NotificationManagerCompat.from(context).notify(1002, notification)
        }
    }

    private fun buildForegroundNotification(): Notification {
        return NotificationCompat.Builder(this, SERVICE_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground) // Обязательная иконка
            .setPriority(NotificationCompat.PRIORITY_MIN) // Минимальный приоритет
            .setNotificationSilent() // Без звука
            .setOngoing(true) // Постоянное уведомление
            .build()
    }

    private fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                SERVICE_CHANNEL_ID,
                "Фоновое наблюдение",
                NotificationManager.IMPORTANCE_MIN
            ).apply {
                setShowBadge(false)
                lockscreenVisibility = Notification.VISIBILITY_SECRET
            }

            val unlockChannel = NotificationChannel(
                UNLOCK_CHANNEL_ID,
                "Разблокировки",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Уведомления при разблокировке экрана"
            }

            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(serviceChannel)
            manager.createNotificationChannel(unlockChannel)
        }
    }

    companion object {
        private const val SERVICE_CHANNEL_ID = "ctrlu_service"
        private const val UNLOCK_CHANNEL_ID = "ctrlu_unlocks"
    }
}