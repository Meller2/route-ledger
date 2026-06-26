package dev.meller.routeledger.core.model

import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID

enum class TransportType(val label: String) {
    Walk("Пешком"), Bike("Велосипед"), Scooter("Самокат"), Car("Авто")
}

enum class ShiftStatus(val label: String) {
    Planned("Запланирована"), Active("Идёт"), Finished("Завершена")
}

enum class OrderRating(val label: String) {
    Excellent("Выгодно"), Good("Норм"), Slow("Долго"), Bad("Мусор")
}

enum class ExpenseCategory(val label: String) {
    Food("Еда"), Transport("Транспорт"), Mobile("Связь"), Maintenance("Обслуживание"), Other("Другое")
}

data class Shift(
    val id: String = UUID.randomUUID().toString(),
    val startedAt: LocalDateTime,
    val endedAt: LocalDateTime? = null,
    val transportType: TransportType,
    val startArea: String,
    val endArea: String? = null,
    val note: String = "",
    val status: ShiftStatus = if (endedAt == null) ShiftStatus.Active else ShiftStatus.Finished,
)

data class DeliveryOrder(
    val id: String = UUID.randomUUID().toString(),
    val shiftId: String,
    val amountRub: Int,
    val tipsRub: Int,
    val pickupArea: String,
    val dropoffArea: String,
    val distanceKm: Double,
    val durationMinutes: Int,
    val rating: OrderRating,
    val note: String = "",
)

data class Expense(
    val id: String = UUID.randomUUID().toString(),
    val shiftId: String,
    val amountRub: Int,
    val category: ExpenseCategory,
    val note: String,
    val createdAt: LocalDateTime,
)

data class AreaInsight(
    val name: String,
    val averageProfitPerHour: Int,
    val ordersCount: Int,
    val waitRisk: Int,
    val mood: String,
)

data class DailyLedgerPoint(
    val date: LocalDate,
    val incomeRub: Int,
    val netRub: Int,
)

data class LedgerSummary(
    val grossIncomeRub: Int,
    val tipsRub: Int,
    val expensesRub: Int,
    val netProfitRub: Int,
    val ordersCount: Int,
    val distanceKm: Double,
    val minutesWorked: Int,
) {
    val profitPerHour: Int
        get() = if (minutesWorked <= 0) 0 else (netProfitRub / (minutesWorked / 60.0)).toInt()

    val profitPerKm: Int
        get() = if (distanceKm <= 0.0) 0 else (netProfitRub / distanceKm).toInt()

    val averageOrderRub: Int
        get() = if (ordersCount == 0) 0 else grossIncomeRub / ordersCount
}

data class LedgerSnapshot(
    val summary: LedgerSummary,
    val shifts: List<Shift>,
    val orders: List<DeliveryOrder>,
    val expenses: List<Expense>,
    val areas: List<AreaInsight>,
    val dailyPoints: List<DailyLedgerPoint>,
)
