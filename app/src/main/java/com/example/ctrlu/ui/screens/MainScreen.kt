package com.example.ctrlu.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import com.example.ctrlu.ui.components.AppNavigationRail
import com.example.ctrlu.ui.viewmodel.UnlockCounterViewModel
import com.example.ctrlu.ui.viewmodel.UsageHistoryViewModel
import com.example.ctrlu.ui.navigation.AppNavHost
@Composable
fun MainScreen(
    usageViewModel: UsageHistoryViewModel,
    unlockViewModel: UnlockCounterViewModel
) {
    val navController = rememberNavController()

    Row(modifier = Modifier.fillMaxSize()
        .background(MaterialTheme.colorScheme.onBackground)) {
        AppNavigationRail(navController = navController)

        Box(modifier = Modifier.weight(1f)) {
            AppNavHost(
                navController = navController,
                usageViewModel = usageViewModel,
                unlockViewModel = unlockViewModel
            )
        }
    }
}