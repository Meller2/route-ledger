package dev.meller.routeledger.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import dev.meller.routeledger.core.model.LedgerSnapshot
import dev.meller.routeledger.ui.LedgerScreenSurface
import dev.meller.routeledger.ui.SectionSpacer
import dev.meller.routeledger.ui.components.InsightPill
import dev.meller.routeledger.ui.components.MetricCard
import dev.meller.routeledger.ui.components.PremiumCard
import dev.meller.routeledger.ui.components.SectionTitle
import dev.meller.routeledger.ui.components.rub
import dev.meller.routeledger.ui.screenPadding
import dev.meller.routeledger.ui.theme.LedgerGold
import dev.meller.routeledger.ui.theme.LedgerPaper
import java.time.format.DateTimeFormatter

@Composable
fun ShiftScreen(
    snapshot: LedgerSnapshot,
    onStartShift: (String) -> Unit,
    onAddOrder: (Int, Int, String, String) -> Unit,
) {
    var showShiftDialog by remember { mutableStateOf(false) }
    var showOrderDialog by remember { mutableStateOf(false) }
    val latestShift = snapshot.shifts.firstOrNull()

    if (showShiftDialog) {
        StartShiftDialog(
            onDismiss = { showShiftDialog = false },
            onConfirm = { area ->
                onStartShift(area)
                showShiftDialog = false
            },
        )
    }

    if (showOrderDialog) {
        AddOrderDialog(
            defaultPickup = latestShift?.endArea ?: latestShift?.startArea.orEmpty(),
            onDismiss = { showOrderDialog = false },
            onConfirm = { amount, tips, pickup, dropoff ->
                onAddOrder(amount, tips, pickup, dropoff)
                showOrderDialog = false
            },
        )
    }

    if (latestShift == null) {
        LedgerScreenSurface {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .screenPadding()
                    .padding(bottom = 24.dp),
            ) {
                SectionTitle("Смена", "Начни первую смену, и журнал оживёт.")
                SectionSpacer()
                Button(
                    onClick = { showShiftDialog = true },
                    modifier = Modifier.fillMaxWidth().height(58.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary,
                    ),
                ) {
                    Icon(Icons.Outlined.Add, contentDescription = null)
                    Text("  Начать смену")
                }
            }
        }
        return
    }

    val shiftOrders = snapshot.orders.filter { it.shiftId == latestShift.id }
    val shiftGross = shiftOrders.sumOf { it.amountRub + it.tipsRub }
    val endArea = latestShift.endArea ?: "в пути"
    val endedAt = latestShift.endedAt?.format(DateTimeFormatter.ofPattern("HH:mm")) ?: "сейчас"

    LedgerScreenSurface {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .screenPadding()
                .padding(bottom = 24.dp),
        ) {
            SectionTitle("Смена", "Быстро фиксируй заказы, расходы и простои.")
            SectionSpacer()
            PremiumCard(dark = true, modifier = Modifier.fillMaxWidth()) {
                Column(Modifier.padding(22.dp)) {
                    Text("Последняя смена", color = LedgerGold, style = MaterialTheme.typography.labelMedium)
                    Spacer(Modifier.height(8.dp))
                    Text("${latestShift.startArea} → $endArea", color = LedgerPaper, style = MaterialTheme.typography.headlineMedium)
                    Spacer(Modifier.height(6.dp))
                    Text(
                        "${latestShift.startedAt.format(DateTimeFormatter.ofPattern("HH:mm"))}–$endedAt · ${latestShift.transportType.label}",
                        color = LedgerPaper.copy(alpha = 0.72f),
                    )
                    Spacer(Modifier.height(18.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        InsightPill("${shiftOrders.size} заказа", LedgerGold)
                        InsightPill(shiftGross.rub())
                    }
                }
            }
            SectionSpacer()
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
                MetricCard("Доход смены", shiftGross.rub(), "до расходов", LedgerGold)
            }
            SectionSpacer()
            Button(
                onClick = { showShiftDialog = true },
                modifier = Modifier.fillMaxWidth().height(58.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                ),
            ) {
                Icon(Icons.Outlined.Add, contentDescription = null)
                Text("  Начать новую смену")
            }
            Spacer(Modifier.height(10.dp))
            OutlinedButton(onClick = { showOrderDialog = true }, modifier = Modifier.fillMaxWidth().height(56.dp)) {
                Text("Добавить заказ к текущей смене")
            }
            SectionSpacer()
            SectionTitle("Последние заказы")
            Spacer(Modifier.height(12.dp))
            shiftOrders.forEach { order ->
                PremiumCard(Modifier.fillMaxWidth()) {
                    Column(Modifier.padding(16.dp)) {
                        Text("${order.pickupArea} → ${order.dropoffArea}", style = MaterialTheme.typography.titleMedium)
                        Spacer(Modifier.height(4.dp))
                        Text(
                            "${(order.amountRub + order.tipsRub).rub()} · ${order.durationMinutes} мин · ${order.rating.label}",
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
                Spacer(Modifier.height(10.dp))
            }
        }
    }
}

@Composable
private fun StartShiftDialog(
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit,
) {
    var area by remember { mutableStateOf("Текущий район") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Новая смена") },
        text = {
            OutlinedTextField(
                value = area,
                onValueChange = { area = it },
                label = { Text("Район старта") },
                singleLine = true,
            )
        },
        confirmButton = {
            Button(onClick = { onConfirm(area) }) {
                Text("Начать")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Отмена")
            }
        },
    )
}

@Composable
private fun AddOrderDialog(
    defaultPickup: String,
    onDismiss: () -> Unit,
    onConfirm: (Int, Int, String, String) -> Unit,
) {
    var amount by remember { mutableStateOf("350") }
    var tips by remember { mutableStateOf("0") }
    var pickup by remember { mutableStateOf(defaultPickup.ifBlank { "Текущий район" }) }
    var dropoff by remember { mutableStateOf("Следующая точка") }
    val numberKeyboard = KeyboardOptions(keyboardType = KeyboardType.Number)

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Новый заказ") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedTextField(
                    value = amount,
                    onValueChange = { amount = it.filter(Char::isDigit) },
                    label = { Text("Сумма") },
                    keyboardOptions = numberKeyboard,
                    singleLine = true,
                )
                OutlinedTextField(
                    value = tips,
                    onValueChange = { tips = it.filter(Char::isDigit) },
                    label = { Text("Чаевые") },
                    keyboardOptions = numberKeyboard,
                    singleLine = true,
                )
                OutlinedTextField(
                    value = pickup,
                    onValueChange = { pickup = it },
                    label = { Text("Откуда") },
                    singleLine = true,
                )
                OutlinedTextField(
                    value = dropoff,
                    onValueChange = { dropoff = it },
                    label = { Text("Куда") },
                    singleLine = true,
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onConfirm(
                        amount.toIntOrNull() ?: 0,
                        tips.toIntOrNull() ?: 0,
                        pickup,
                        dropoff,
                    )
                },
            ) {
                Text("Добавить")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Отмена")
            }
        },
    )
}
