package dev.meller.routeledger.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "expenses",
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
data class ExpenseEntity(
    @PrimaryKey val id: String,
    val shiftId: String,
    val amountRub: Int,
    val category: String,
    val note: String,
    val createdAt: String,
)
