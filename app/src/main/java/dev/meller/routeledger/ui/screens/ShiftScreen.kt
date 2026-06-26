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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
import dev.meller.routeledger.ui.theme.LedgerInk
import dev.meller.routeledger.ui.theme.LedgerMutedInk
import dev.meller.routeledger.ui.theme.LedgerPaper
import java.time.format.DateTimeFormatter

@Composable
fun ShiftScreen(snapshot: LedgerSnapshot) {
    val latestShift = snapshot.shifts.first()
    val shiftOrders = snapshot.orders.filter { it.shiftId == latestShift.id }
    val shiftGross = shiftOrders.sumOf { it.amountRub + it.tipsRub }

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
                    Text("${latestShift.startArea} → ${latestShift.endArea}", color = LedgerPaper, style = MaterialTheme.typography.headlineMedium)
                    Spacer(Modifier.height(6.dp))
                    Text(
                        "${latestShift.startedAt.format(DateTimeFormatter.ofPattern("HH:mm"))}–${latestShift.endedAt?.format(DateTimeFormatter.ofPattern("HH:mm"))} · ${latestShift.transportType.label}",
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
                onClick = { },
                modifier = Modifier.fillMaxWidth().height(58.dp),
                colors = ButtonDefaults.buttonColors(containerColor = LedgerInk, contentColor = LedgerPaper),
            ) {
                Icon(Icons.Outlined.Add, contentDescription = null)
                Text("  Начать новую смену")
            }
            Spacer(Modifier.height(10.dp))
            OutlinedButton(onClick = { }, modifier = Modifier.fillMaxWidth().height(56.dp)) {
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
                        Text("${(order.amountRub + order.tipsRub).rub()} · ${order.durationMinutes} мин · ${order.rating.label}", color = LedgerMutedInk)
                    }
                }
                Spacer(Modifier.height(10.dp))
            }
        }
    }
}
