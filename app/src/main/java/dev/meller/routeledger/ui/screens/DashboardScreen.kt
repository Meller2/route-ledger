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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.meller.routeledger.core.model.LedgerSnapshot
import dev.meller.routeledger.ui.LedgerScreenSurface
import dev.meller.routeledger.ui.SectionSpacer
import dev.meller.routeledger.ui.components.HeroProfitCard
import dev.meller.routeledger.ui.components.InsightPill
import dev.meller.routeledger.ui.components.MetricCard
import dev.meller.routeledger.ui.components.PremiumCard
import dev.meller.routeledger.ui.components.SectionTitle
import dev.meller.routeledger.ui.components.SoftLineChart
import dev.meller.routeledger.ui.components.km
import dev.meller.routeledger.ui.components.rub
import dev.meller.routeledger.ui.screenPadding
import dev.meller.routeledger.ui.theme.LedgerBlue
import dev.meller.routeledger.ui.theme.LedgerClay
import dev.meller.routeledger.ui.theme.LedgerGold
import dev.meller.routeledger.ui.theme.LedgerSage

@Composable
fun DashboardScreen(snapshot: LedgerSnapshot) {
    val summary = snapshot.summary
    LedgerScreenSurface {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .screenPadding()
                .padding(bottom = 24.dp),
        ) {
            SectionTitle(
                title = "Пульс смен",
                subtitle = "Деньги, время и районы — в одном спокойном дашборде.",
            )
            SectionSpacer()
            HeroProfitCard(
                title = "Чистая прибыль",
                amount = summary.netProfitRub,
                subtitle = "${summary.ordersCount} заказов · ${summary.distanceKm.km()} · расходы ${summary.expensesRub.rub()}",
                profitPerHour = summary.profitPerHour,
            )
            SectionSpacer()
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
                Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    MetricCard("₽ за километр", summary.profitPerKm.rub(), "после расходов", LedgerGold)
                    MetricCard("Средний заказ", summary.averageOrderRub.rub(), "с чаевыми", LedgerBlue)
                }
                Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    MetricCard("Чаевые", summary.tipsRub.rub(), "приятный бонус", LedgerSage)
                    MetricCard("Дистанция", summary.distanceKm.km(), "по заказам", LedgerClay)
                }
            }
            SectionSpacer()
            PremiumCard(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(20.dp)) {
                    Text("Неделя в деньгах", style = MaterialTheme.typography.titleLarge)
                    Spacer(Modifier.height(6.dp))
                    Text("Мягкий график без биржевого стресса.", color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Spacer(Modifier.height(18.dp))
                    SoftLineChart(snapshot.dailyPoints.map { it.netRub })
                    Spacer(Modifier.height(8.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        InsightPill("лучше: пятница", LedgerGold)
                        InsightPill("риск: долгие ожидания", LedgerClay)
                    }
                }
            }
            SectionSpacer()
            SectionTitle("Инсайты", "То, что помогает решить — выходить или нет.")
            Spacer(Modifier.height(12.dp))
            InsightCard("Центр сейчас самый стабильный", "Средний темп выше других районов, а ожидание ниже среднего.")
            Spacer(Modifier.height(10.dp))
            InsightCard("Азино вечером проседает", "Много километров и ниже чистая прибыль за час.")
        }
    }
}

@Composable
private fun InsightCard(title: String, body: String) {
    PremiumCard(Modifier.fillMaxWidth()) {
        Column(Modifier.padding(18.dp)) {
            Text(title, style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(4.dp))
            Text(body, color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.bodyMedium)
        }
    }
}
