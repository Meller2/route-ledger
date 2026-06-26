package dev.meller.routeledger.data.demo

import dev.meller.routeledger.core.analytics.LedgerAnalytics
import dev.meller.routeledger.core.model.AreaInsight
import dev.meller.routeledger.core.model.DailyLedgerPoint
import dev.meller.routeledger.core.model.DeliveryOrder
import dev.meller.routeledger.core.model.Expense
import dev.meller.routeledger.core.model.ExpenseCategory
import dev.meller.routeledger.core.model.LedgerSnapshot
import dev.meller.routeledger.core.model.OrderRating
import dev.meller.routeledger.core.model.Shift
import dev.meller.routeledger.core.model.ShiftStatus
import dev.meller.routeledger.core.model.TransportType
import java.time.LocalDate
import java.time.LocalDateTime

object DemoLedger {
    private val now = LocalDateTime.now()

    val shifts = listOf(
        Shift(
            id = "shift-today",
            startedAt = now.withHour(13).withMinute(10),
            endedAt = now.withHour(18).withMinute(45),
            transportType = TransportType.Bike,
            startArea = "Центр",
            endArea = "Козья слобода",
            note = "Плотная, но чистая смена",
            status = ShiftStatus.Finished,
        ),
        Shift(
            id = "shift-yesterday",
            startedAt = now.minusDays(1).withHour(14).withMinute(0),
            endedAt = now.minusDays(1).withHour(20).withMinute(20),
            transportType = TransportType.Bike,
            startArea = "Суконная слобода",
            endArea = "Азино",
            note = "Много ожидания в ресторанах",
            status = ShiftStatus.Finished,
        ),
    )

    val orders = listOf(
        DeliveryOrder("o1", "shift-today", 410, 80, "Центр", "Кремлёвская", 2.1, 18, OrderRating.Excellent),
        DeliveryOrder("o2", "shift-today", 360, 0, "Кольцо", "Суконная", 3.4, 26, OrderRating.Good),
        DeliveryOrder("o3", "shift-today", 520, 120, "Парк Горького", "Калуга", 4.2, 31, OrderRating.Excellent),
        DeliveryOrder("o4", "shift-today", 290, 0, "Чистопольская", "Козья слобода", 2.8, 24, OrderRating.Slow),
        DeliveryOrder("o5", "shift-yesterday", 340, 0, "Азино", "Горки", 5.2, 40, OrderRating.Bad),
        DeliveryOrder("o6", "shift-yesterday", 460, 60, "Суконная", "Центр", 3.1, 27, OrderRating.Good),
    )

    val expenses = listOf(
        Expense("e1", "shift-today", 180, ExpenseCategory.Food, "Обед", now.withHour(16).withMinute(5)),
        Expense("e2", "shift-yesterday", 120, ExpenseCategory.Transport, "Метро до точки", now.minusDays(1).withHour(13).withMinute(35)),
    )

    val areas = listOf(
        AreaInsight("Центр", 612, 12, 18, "самый стабильный"),
        AreaInsight("Козья слобода", 548, 7, 24, "хороший финиш"),
        AreaInsight("Суконная", 489, 9, 31, "много ожидания"),
        AreaInsight("Азино", 372, 5, 48, "сомнительно вечером"),
    )

    val dailyPoints = listOf(
        DailyLedgerPoint(LocalDate.now().minusDays(5), 1760, 1580),
        DailyLedgerPoint(LocalDate.now().minusDays(4), 2320, 2140),
        DailyLedgerPoint(LocalDate.now().minusDays(3), 1480, 1320),
        DailyLedgerPoint(LocalDate.now().minusDays(2), 2680, 2410),
        DailyLedgerPoint(LocalDate.now().minusDays(1), 860, 740),
        DailyLedgerPoint(LocalDate.now(), 1780, 1600),
    )

    fun snapshot(): LedgerSnapshot = LedgerSnapshot(
        summary = LedgerAnalytics.buildSummary(shifts, orders, expenses),
        shifts = shifts,
        orders = orders,
        expenses = expenses,
        areas = areas,
        dailyPoints = dailyPoints,
    )
}
