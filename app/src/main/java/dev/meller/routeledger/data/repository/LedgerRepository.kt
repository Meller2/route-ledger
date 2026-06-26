package dev.meller.routeledger.data.repository

import dev.meller.routeledger.core.analytics.LedgerAnalytics
import dev.meller.routeledger.core.model.DeliveryOrder
import dev.meller.routeledger.core.model.Expense
import dev.meller.routeledger.core.model.LedgerSummary
import dev.meller.routeledger.core.model.Shift
import dev.meller.routeledger.data.local.dao.LedgerDao
import dev.meller.routeledger.data.local.mapper.toEntity
import dev.meller.routeledger.data.local.mapper.toModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LedgerRepository @Inject constructor(
    private val dao: LedgerDao,
) {
    val shifts: Flow<List<Shift>> = dao.observeShifts().map { list -> list.map { it.toModel() } }
    val orders: Flow<List<DeliveryOrder>> = dao.observeOrders().map { list -> list.map { it.toModel() } }
    val expenses: Flow<List<Expense>> = dao.observeExpenses().map { list -> list.map { it.toModel() } }

    val summary: Flow<LedgerSummary> = combine(shifts, orders, expenses) { shifts, orders, expenses ->
        LedgerAnalytics.buildSummary(shifts, orders, expenses)
    }

    suspend fun saveShift(shift: Shift) = dao.upsertShift(shift.toEntity())
    suspend fun saveOrder(order: DeliveryOrder) = dao.upsertOrder(order.toEntity())
    suspend fun saveExpense(expense: Expense) = dao.upsertExpense(expense.toEntity())
}
