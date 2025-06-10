package com.example.ctrlu.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ctrlu.ui.components.WeekTimeline
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun WeekScreen(
    weeklyData: Map<String, Long> // ключ — строка в формате "yyyy-MM-dd"
) {
    val localizedWeeklyData = weeklyData.mapKeys { (dateString) ->
        formatDateToWeekday(dateString)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = "Статистика за неделю",
            fontSize = 20.sp,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        if (weeklyData.isEmpty()) {
            Text(
                text = "Нет данных",
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        } else {
            WeekTimeline(localizedWeeklyData)
        }
    }
}

/**
 * Преобразует дату в формате "yyyy-MM-dd" в краткое название дня недели на русском.
 * Например, "2024-05-06" -> "Пн"
 */
fun formatDateToWeekday(dateString: String): String {
    return try {
        val date = LocalDate.parse(dateString)
        date.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale("ru"))
    } catch (e: Exception) {
        dateString // если не парсится, вернуть исходное значение
    }
}