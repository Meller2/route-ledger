package dev.meller.routeledger.core.analytics

import dev.meller.routeledger.core.model.DeliveryOrder
import dev.meller.routeledger.core.model.Expense
import dev.meller.routeledger.core.model.LedgerSummary
import dev.meller.routeledger.core.model.Shift
import java.time.Duration

object LedgerAnalytics {
    fun buildSummary(
        shifts: List<Shift>,
        orders: List<DeliveryOrder>,
        expenses: List<Expense>,
    ): LedgerSummary {
        val gross = orders.sumOf { it.amountRub + it.tipsRub }
        val tips = orders.sumOf { it.tipsRub }
        val expenseTotal = expenses.sumOf { it.amountRub }
        val distance = orders.sumOf { it.distanceKm }
        val minutes = shifts.sumOf { shift ->
            val end = shift.endedAt ?: return@sumOf 0
            Duration.between(shift.startedAt, end).toMinutes().toInt().coerceAtLeast(0)
        }

        return LedgerSummary(
            grossIncomeRub = gross,
            tipsRub = tips,
            expensesRub = expenseTotal,
            netProfitRub = gross - expenseTotal,
            ordersCount = orders.size,
            distanceKm = distance,
            minutesWorked = minutes,
        )
    }
}
