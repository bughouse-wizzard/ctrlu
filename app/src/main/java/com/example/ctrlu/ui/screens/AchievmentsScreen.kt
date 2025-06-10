package com.example.ctrlu.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.ctrlu.ui.components.AchievementCard
import androidx.compose.material3.MaterialTheme

@Composable
fun AchievementsScreen(
    unlockCount: Int
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = "Тебе самому не надоело?",
            style = androidx.compose.material3.MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 16.dp),
            color = MaterialTheme.colorScheme.primary

        )

        AchievementCard(unlockCount)

        // Здесь можно добавить другие карточки достижений
    }
}