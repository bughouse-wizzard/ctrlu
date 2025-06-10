package com.example.ctrlu.ui.navigation

import com.example.ctrlu.R

enum class NavTab(val route: String, val title: String, val icon: Int) {
    Today("today", "Сегодня", R.drawable.ic_today),
    Week("week", "Неделя", R.drawable.ic_week),
    Unlocks("unlocks", "Достижения?", R.drawable.ic_unlock)
}