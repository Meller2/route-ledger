package dev.meller.routeledger.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import dev.meller.routeledger.data.local.entity.DeliveryOrderEntity
import dev.meller.routeledger.data.local.entity.ExpenseEntity
import dev.meller.routeledger.data.local.entity.ShiftEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface LedgerDao {
    @Query("SELECT * FROM shifts ORDER BY startedAt DESC")
    fun observeShifts(): Flow<List<ShiftEntity>>

    @Query("SELECT * FROM delivery_orders ORDER BY id DESC")
    fun observeOrders(): Flow<List<DeliveryOrderEntity>>

    @Query("SELECT * FROM expenses ORDER BY createdAt DESC")
    fun observeExpenses(): Flow<List<ExpenseEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertShift(shift: ShiftEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertOrder(order: DeliveryOrderEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertExpense(expense: ExpenseEntity)
}
