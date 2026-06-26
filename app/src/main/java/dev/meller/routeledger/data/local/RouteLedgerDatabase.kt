package dev.meller.routeledger.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import dev.meller.routeledger.data.local.dao.LedgerDao
import dev.meller.routeledger.data.local.entity.DeliveryOrderEntity
import dev.meller.routeledger.data.local.entity.ExpenseEntity
import dev.meller.routeledger.data.local.entity.ShiftEntity

@Database(
    entities = [
        ShiftEntity::class,
        DeliveryOrderEntity::class,
        ExpenseEntity::class,
    ],
    version = 1,
    exportSchema = false,
)
abstract class RouteLedgerDatabase : RoomDatabase() {
    abstract fun ledgerDao(): LedgerDao
}
