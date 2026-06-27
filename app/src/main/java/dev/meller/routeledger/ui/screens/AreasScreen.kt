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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.meller.routeledger.core.model.LedgerSnapshot
import dev.meller.routeledger.ui.LedgerScreenSurface
import dev.meller.routeledger.ui.SectionSpacer
import dev.meller.routeledger.ui.components.AreaQualityRing
import dev.meller.routeledger.ui.components.EmptyMapPreview
import dev.meller.routeledger.ui.components.PremiumCard
import dev.meller.routeledger.ui.components.SectionTitle
import dev.meller.routeledger.ui.components.rub
import dev.meller.routeledger.ui.screenPadding

@Composable
fun AreasScreen(snapshot: LedgerSnapshot) {
    LedgerScreenSurface {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .screenPadding()
                .padding(bottom = 24.dp),
        ) {
            SectionTitle("Районы", "Пока без карт, но уже с логикой под гео-слой.")
            SectionSpacer()
            EmptyMapPreview()
            SectionSpacer()
            SectionTitle("Личный рейтинг")
            Spacer(Modifier.height(12.dp))
            snapshot.areas.forEach { area ->
                PremiumCard(Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.padding(18.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                    ) {
                        AreaQualityRing(score = 100 - area.waitRisk)
                        Column(Modifier.weight(1f)) {
                            Text(area.name, style = MaterialTheme.typography.titleMedium)
                            Text(area.mood, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        Text("${area.averageProfitPerHour.rub()}/ч", style = MaterialTheme.typography.titleMedium)
                    }
                }
                Spacer(Modifier.height(10.dp))
            }
        }
    }
}
