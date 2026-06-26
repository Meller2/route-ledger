package dev.meller.routeledger.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "shifts")
data class ShiftEntity(
    @PrimaryKey val id: String,
    val startedAt: String,
    val endedAt: String?,
    val transportType: String,
    val startArea: String,
    val endArea: String?,
    val note: String,
    val status: String,
)
