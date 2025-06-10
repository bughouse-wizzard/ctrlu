package com.example.ctrlu.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class MetaInfo(
    @PrimaryKey val key: String,
    val value: String
)