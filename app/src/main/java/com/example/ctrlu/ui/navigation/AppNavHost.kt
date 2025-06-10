package com.example.ctrlu.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.ctrlu.ui.screens.TodayScreen
import com.example.ctrlu.ui.screens.WeekScreen
import com.example.ctrlu.ui.screens.AchievementsScreen
import com.example.ctrlu.ui.viewmodel.UnlockCounterViewModel
import com.example.ctrlu.ui.viewmodel.UsageHistoryViewModel

@Composable
fun AppNavHost(
    navController: NavHostController,
    usageViewModel: UsageHistoryViewModel,
    unlockViewModel: UnlockCounterViewModel,
    startDestination: String = NavTab.Today.route
) {
    NavHost(navController = navController, startDestination = startDestination) {
        composable(NavTab.Today.route) {
            val topApps by usageViewModel.topApps.collectAsState()
            val dailyUsage by usageViewModel.dailyUsage.collectAsState()

            TodayScreen(
                apps = topApps,
                totalMillis = dailyUsage
            )
        }

        composable(NavTab.Week.route) {
            val weeklyData by usageViewModel.weeklyUsage.collectAsState()
            WeekScreen(weeklyData = weeklyData)
        }

        composable(NavTab.Unlocks.route) {
            val unlockCount by unlockViewModel.unlockCount.collectAsState()
            AchievementsScreen(unlockCount = unlockCount)
        }
    }
}