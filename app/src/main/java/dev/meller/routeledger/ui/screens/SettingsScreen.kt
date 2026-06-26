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
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.meller.routeledger.ui.LedgerScreenSurface
import dev.meller.routeledger.ui.SectionSpacer
import dev.meller.routeledger.ui.components.PremiumCard
import dev.meller.routeledger.ui.components.SectionTitle
import dev.meller.routeledger.ui.screenPadding
import dev.meller.routeledger.ui.theme.LedgerMutedInk

@Composable
fun SettingsScreen() {
    LedgerScreenSurface {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .screenPadding()
                .padding(bottom = 24.dp),
        ) {
            SectionTitle("Ещё", "Настройки, экспорт и будущие интеграции.")
            SectionSpacer()
            SettingsCard("Язык", "Русский интерфейс по умолчанию")
            Spacer(Modifier.height(10.dp))
            SettingsCard("Карты", "Провайдер пока не подключён, API key не нужен")
            Spacer(Modifier.height(10.dp))
            SettingsCard("Тема", "Quiet Premium: молочный фон, графит, золото, sage")
            Spacer(Modifier.height(10.dp))
            PremiumCard(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(18.dp)) {
                    Text("Демо-данные", style = MaterialTheme.typography.titleMedium)
                    Text("Показывать красивый стартовый дашборд", color = LedgerMutedInk)
                    Spacer(Modifier.height(8.dp))
                    Switch(checked = true, onCheckedChange = {})
                }
            }
        }
    }
}

@Composable
private fun SettingsCard(title: String, subtitle: String) {
    PremiumCard(Modifier.fillMaxWidth()) {
        Column(Modifier.padding(18.dp)) {
            Text(title, style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(4.dp))
            Text(subtitle, color = LedgerMutedInk)
        }
    }
}
