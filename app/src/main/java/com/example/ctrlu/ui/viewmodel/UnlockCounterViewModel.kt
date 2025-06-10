package com.example.ctrlu.ui.viewmodel

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import androidx.compose.runtime.State

class UnlockCounterViewModel(application: Application) : AndroidViewModel(application) {
    private val prefs = application.getSharedPreferences("unlock_prefs", Context.MODE_PRIVATE)
    private val unlockKey = "unlock_count"
    private val dateKey = "last_reset_date"

    private val _unlockCount = MutableStateFlow(0)
    val unlockCount: MutableStateFlow<Int> = _unlockCount

    init {
        resetIfNeeded()
    }

    private fun resetIfNeeded() {
        val today = LocalDate.now().toString()
        val lastReset = prefs.getString(dateKey, null)

        if (lastReset != today) {
            prefs.edit()
                .putInt(unlockKey, 0)
                .putString(dateKey, today)
                .apply()
            _unlockCount.value = 0
        } else {
            _unlockCount.value = prefs.getInt(unlockKey, 0)
        }
    }

    fun incrementUnlockCount() {
        val current = prefs.getInt(unlockKey, 0) + 1
        prefs.edit().putInt(unlockKey, current).apply()
        _unlockCount.value = current
    }
}