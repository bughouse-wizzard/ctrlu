// ui/components/WeekTimeline.kt
package com.example.ctrlu.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.util.concurrent.TimeUnit

@Composable
fun WeekTimeline(
    weeklyData: Map<String, Long>
) {
    val maxMillis = weeklyData.values.maxOrNull() ?: 1L

    Column(modifier = Modifier.fillMaxWidth()) {
        weeklyData.entries.forEach { (day, millis) ->
            DayProgressRow(
                day = day,
                millis = millis,
                maxMillis = maxMillis
            )
        }
    }
}

@Composable
private fun DayProgressRow(
    day: String,
    millis: Long,
    maxMillis: Long
) {
    val progress = (millis.toFloat() / maxMillis).coerceIn(0f, 1f)
    val hours = TimeUnit.MILLISECONDS.toHours(millis)
    val minutes = TimeUnit.MILLISECONDS.toMinutes(millis) % 60

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = day,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(1f)
        )

        LinearProgressIndicator(
            progress = progress,
            modifier = Modifier
                .weight(2f)
                .height(8.dp),
            color = MaterialTheme.colorScheme.primary,
            trackColor = MaterialTheme.colorScheme.surfaceVariant
        )

        Text(
            text = "${hours}ч ${minutes}м",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier
                .weight(1f)
                .padding(start = 8.dp)
        )
    }
}