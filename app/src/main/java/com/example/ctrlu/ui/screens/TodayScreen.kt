package com.example.ctrlu.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LockClock
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.ctrlu.model.AppUsageInfo
import com.example.ctrlu.ui.components.AppUsageCard
import com.example.ctrlu.ui.components.DailyUsageBar

@Composable
fun TodayScreen(
    apps: List<AppUsageInfo>,
    totalMillis: Long,
    unlocks: Int = 0
) {
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {

        // Заголовок
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "Отсутствие мозговой активности",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.primary
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Бар общего экранного времени
        DailyUsageBar(totalMillis = totalMillis)

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Самые активные приложения",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            itemsIndexed(apps) { index, app ->
                AppUsageCard(app = app, index = index)
            }
        }
    }
}