package dev.meller.routeledger.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.meller.routeledger.core.model.LedgerSnapshot
import dev.meller.routeledger.ui.LedgerScreenSurface
import dev.meller.routeledger.ui.SectionSpacer
import dev.meller.routeledger.ui.components.PremiumCard
import dev.meller.routeledger.ui.components.SectionTitle
import dev.meller.routeledger.ui.components.SoftLineChart
import dev.meller.routeledger.ui.components.rub
import dev.meller.routeledger.ui.screenPadding
import dev.meller.routeledger.ui.theme.LedgerMutedInk
import java.time.format.DateTimeFormatter

@Composable
fun IncomeScreen(snapshot: LedgerSnapshot) {
    LedgerScreenSurface {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .screenPadding()
                .padding(bottom = 24.dp),
        ) {
            SectionTitle("Доход", "История смен и расходов без бухгалтерского ада.")
            SectionSpacer()
            PremiumCard(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(20.dp)) {
                    Text("Чистыми за неделю", style = MaterialTheme.typography.titleLarge)
                    Spacer(Modifier.height(10.dp))
                    Text(snapshot.dailyPoints.sumOf { it.netRub }.rub(), style = MaterialTheme.typography.displaySmall)
                    Spacer(Modifier.height(18.dp))
                    SoftLineChart(snapshot.dailyPoints.map { it.incomeRub })
                }
            }
            SectionSpacer()
            SectionTitle("Смены")
            Spacer(Modifier.height(12.dp))
            snapshot.shifts.forEach { shift ->
                val orders = snapshot.orders.filter { it.shiftId == shift.id }
                val expenses = snapshot.expenses.filter { it.shiftId == shift.id }
                val gross = orders.sumOf { it.amountRub + it.tipsRub }
                val net = gross - expenses.sumOf { it.amountRub }
                PremiumCard(Modifier.fillMaxWidth()) {
                    Column(Modifier.padding(18.dp)) {
                        Text("${shift.startArea} → ${shift.endArea}", style = MaterialTheme.typography.titleMedium)
                        Spacer(Modifier.height(5.dp))
                        Text(
                            "${shift.startedAt.format(DateTimeFormatter.ofPattern("d MMM, HH:mm"))} · ${orders.size} заказов · чистыми ${net.rub()}",
                            color = LedgerMutedInk,
                        )
                    }
                }
                Spacer(Modifier.height(10.dp))
            }
        }
    }
}
