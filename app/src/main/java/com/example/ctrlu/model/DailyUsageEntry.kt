package com.example.ctrlu.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Entity
data class DailyUsageEntry(
    @PrimaryKey val date: String,
    val totalUsageMillis: Long,
    val appsJson: String // JSON сериализованный список AppContribution
)

@Serializable
data class AppContribution(
    val appName: String,
    val usageMillis: Long,
    val colorHex: String
)
