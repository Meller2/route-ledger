package dev.meller.routeledger.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "delivery_orders",
    foreignKeys = [
        ForeignKey(
            entity = ShiftEntity::class,
            parentColumns = ["id"],
            childColumns = ["shiftId"],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
    indices = [Index("shiftId")],
)
data class DeliveryOrderEntity(
    @PrimaryKey val id: String,
    val shiftId: String,
    val amountRub: Int,
    val tipsRub: Int,
    val pickupArea: String,
    val dropoffArea: String,
    val distanceKm: Double,
    val durationMinutes: Int,
    val rating: String,
    val note: String,
)
