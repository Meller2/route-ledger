package dev.meller.routeledger.data.local.mapper

import dev.meller.routeledger.core.model.DeliveryOrder
import dev.meller.routeledger.core.model.Expense
import dev.meller.routeledger.core.model.ExpenseCategory
import dev.meller.routeledger.core.model.OrderRating
import dev.meller.routeledger.core.model.Shift
import dev.meller.routeledger.core.model.ShiftStatus
import dev.meller.routeledger.core.model.TransportType
import dev.meller.routeledger.data.local.entity.DeliveryOrderEntity
import dev.meller.routeledger.data.local.entity.ExpenseEntity
import dev.meller.routeledger.data.local.entity.ShiftEntity
import java.time.LocalDateTime

fun Shift.toEntity() = ShiftEntity(
    id = id,
    startedAt = startedAt.toString(),
    endedAt = endedAt?.toString(),
    transportType = transportType.name,
    startArea = startArea,
    endArea = endArea,
    note = note,
    status = status.name,
)

fun ShiftEntity.toModel() = Shift(
    id = id,
    startedAt = LocalDateTime.parse(startedAt),
    endedAt = endedAt?.let(LocalDateTime::parse),
    transportType = TransportType.valueOf(transportType),
    startArea = startArea,
    endArea = endArea,
    note = note,
    status = ShiftStatus.valueOf(status),
)

fun DeliveryOrder.toEntity() = DeliveryOrderEntity(
    id = id,
    shiftId = shiftId,
    amountRub = amountRub,
    tipsRub = tipsRub,
    pickupArea = pickupArea,
    dropoffArea = dropoffArea,
    distanceKm = distanceKm,
    durationMinutes = durationMinutes,
    rating = rating.name,
    note = note,
)

fun DeliveryOrderEntity.toModel() = DeliveryOrder(
    id = id,
    shiftId = shiftId,
    amountRub = amountRub,
    tipsRub = tipsRub,
    pickupArea = pickupArea,
    dropoffArea = dropoffArea,
    distanceKm = distanceKm,
    durationMinutes = durationMinutes,
    rating = OrderRating.valueOf(rating),
    note = note,
)

fun Expense.toEntity() = ExpenseEntity(
    id = id,
    shiftId = shiftId,
    amountRub = amountRub,
    category = category.name,
    note = note,
    createdAt = createdAt.toString(),
)

fun ExpenseEntity.toModel() = Expense(
    id = id,
    shiftId = shiftId,
    amountRub = amountRub,
    category = ExpenseCategory.valueOf(category),
    note = note,
    createdAt = LocalDateTime.parse(createdAt),
)
