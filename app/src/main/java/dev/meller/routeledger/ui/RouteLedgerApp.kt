package dev.meller.routeledger.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Analytics
import androidx.compose.material.icons.outlined.Explore
import androidx.compose.material.icons.outlined.Payments
import androidx.compose.material.icons.outlined.Route
import androidx.compose.material.icons.outlined.Tune
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.meller.routeledger.ui.screens.AreasScreen
import dev.meller.routeledger.ui.screens.DashboardScreen
import dev.meller.routeledger.ui.screens.IncomeScreen
import dev.meller.routeledger.ui.screens.SettingsScreen
import dev.meller.routeledger.ui.screens.ShiftScreen
import dev.meller.routeledger.ui.theme.LedgerGold

private enum class RouteLedgerTab(val title: String, val icon: ImageVector) {
    Pulse("Пульс", Icons.Outlined.Analytics),
    Shift("Смена", Icons.Outlined.Route),
    Income("Доход", Icons.Outlined.Payments),
    Areas("Районы", Icons.Outlined.Explore),
    Settings("Ещё", Icons.Outlined.Tune),
}

@Composable
fun RouteLedgerApp(viewModel: AppViewModel) {
    var currentTab by remember { mutableStateOf(RouteLedgerTab.Pulse) }
    val snapshot by viewModel.snapshot.collectAsState()
    val darkTheme by viewModel.darkTheme.collectAsState()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            NavigationBar(
                modifier = Modifier.navigationBarsPadding(),
                containerColor = MaterialTheme.colorScheme.surface,
                tonalElevation = 0.dp,
            ) {
                RouteLedgerTab.entries.forEach { tab ->
                    NavigationBarItem(
                        selected = currentTab == tab,
                        onClick = { currentTab = tab },
                        icon = { Icon(tab.icon, contentDescription = tab.title) },
                        label = { Text(tab.title) },
                    )
                }
            }
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background),
        ) {
            AppHeader()
            val currentSnapshot = snapshot
            if (currentSnapshot == null) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    Text("Готовлю журнал...")
                }
            } else {
                AnimatedContent(targetState = currentTab, label = "tab-content") { tab ->
                    when (tab) {
                        RouteLedgerTab.Pulse -> DashboardScreen(currentSnapshot)
                        RouteLedgerTab.Shift -> ShiftScreen(
                            snapshot = currentSnapshot,
                            onStartShift = viewModel::startShift,
                            onAddOrder = viewModel::addQuickOrder,
                        )
                        RouteLedgerTab.Income -> IncomeScreen(currentSnapshot)
                        RouteLedgerTab.Areas -> AreasScreen(currentSnapshot)
                        RouteLedgerTab.Settings -> SettingsScreen(
                            darkTheme = darkTheme,
                            onDarkThemeChanged = viewModel::setDarkTheme,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun AppHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(horizontal = 22.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary),
            contentAlignment = Alignment.Center,
        ) {
            Text("RL", color = LedgerGold, fontWeight = FontWeight.Bold)
        }
        Spacer(Modifier.width(12.dp))
        Column {
            Text("Route Ledger", style = MaterialTheme.typography.titleLarge)
            Text("Курьерская аналитика без шума", style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
fun LedgerScreenSurface(content: @Composable () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background,
        content = content,
    )
}

fun Modifier.screenPadding(): Modifier = padding(horizontal = 22.dp)

@Composable
fun SectionSpacer() = Spacer(Modifier.height(18.dp))
