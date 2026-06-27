package dev.meller.routeledger.data.repository

import dev.meller.routeledger.core.analytics.LedgerAnalytics
import dev.meller.routeledger.core.model.AreaInsight
import dev.meller.routeledger.core.model.DailyLedgerPoint
import dev.meller.routeledger.core.model.DeliveryOrder
import dev.meller.routeledger.core.model.Expense
import dev.meller.routeledger.core.model.LedgerSnapshot
import dev.meller.routeledger.core.model.LedgerSummary
import dev.meller.routeledger.core.model.Shift
import dev.meller.routeledger.data.demo.DemoLedger
import dev.meller.routeledger.data.local.dao.LedgerDao
import dev.meller.routeledger.data.local.mapper.toEntity
import dev.meller.routeledger.data.local.mapper.toModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import java.time.LocalDate
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

    val snapshot: Flow<LedgerSnapshot> = combine(shifts, orders, expenses) { shifts, orders, expenses ->
        LedgerSnapshot(
            summary = LedgerAnalytics.buildSummary(shifts, orders, expenses),
            shifts = shifts,
            orders = orders,
            expenses = expenses,
            areas = buildAreaInsights(orders),
            dailyPoints = buildDailyPoints(shifts, orders, expenses),
        )
    }

    suspend fun saveShift(shift: Shift) = dao.upsertShift(shift.toEntity())
    suspend fun saveOrder(order: DeliveryOrder) = dao.upsertOrder(order.toEntity())
    suspend fun saveExpense(expense: Expense) = dao.upsertExpense(expense.toEntity())

    suspend fun seedDemoDataIfEmpty() {
        if (dao.countShifts() > 0) return

        DemoLedger.shifts.forEach { saveShift(it) }
        DemoLedger.orders.forEach { saveOrder(it) }
        DemoLedger.expenses.forEach { saveExpense(it) }
    }

    private fun buildDailyPoints(
        shifts: List<Shift>,
        orders: List<DeliveryOrder>,
        expenses: List<Expense>,
    ): List<DailyLedgerPoint> {
        val shiftsById = shifts.associateBy { it.id }
        val dates = shifts.map { it.startedAt.toLocalDate() }.ifEmpty { listOf(LocalDate.now()) }

        return dates.distinct().sorted().takeLast(7).map { date ->
            val shiftIds = shifts.filter { it.startedAt.toLocalDate() == date }.map { it.id }.toSet()
            val income = orders.filter { it.shiftId in shiftIds }.sumOf { it.amountRub + it.tipsRub }
            val expenseTotal = expenses.filter { it.shiftId in shiftIds }.sumOf { it.amountRub }
            val fallbackIncome = orders
                .filter { shiftsById[it.shiftId]?.startedAt?.toLocalDate() == date }
                .sumOf { it.amountRub + it.tipsRub }

            DailyLedgerPoint(
                date = date,
                incomeRub = income.takeIf { it > 0 } ?: fallbackIncome,
                netRub = (income.takeIf { it > 0 } ?: fallbackIncome) - expenseTotal,
            )
        }
    }

    private fun buildAreaInsights(orders: List<DeliveryOrder>): List<AreaInsight> =
        orders
            .groupBy { it.pickupArea }
            .map { (area, areaOrders) ->
                val minutes = areaOrders.sumOf { it.durationMinutes }.coerceAtLeast(1)
                val income = areaOrders.sumOf { it.amountRub + it.tipsRub }
                val profitPerHour = (income / (minutes / 60.0)).toInt()
                val waitRisk = areaOrders.count { it.durationMinutes > 30 } * 18

                AreaInsight(
                    name = area,
                    averageProfitPerHour = profitPerHour,
                    ordersCount = areaOrders.size,
                    waitRisk = waitRisk.coerceIn(8, 72),
                    mood = if (profitPerHour >= 700) "выгодно" else "можно лучше",
                )
            }
            .sortedByDescending { it.averageProfitPerHour }
            .take(6)
}
