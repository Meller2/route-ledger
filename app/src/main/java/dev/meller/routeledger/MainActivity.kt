package dev.meller.routeledger

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import dagger.hilt.android.AndroidEntryPoint
import dev.meller.routeledger.ui.AppViewModel
import dev.meller.routeledger.ui.RouteLedgerApp
import dev.meller.routeledger.ui.theme.RouteLedgerTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: AppViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val darkTheme by viewModel.darkTheme.collectAsState()

            RouteLedgerTheme(darkTheme = darkTheme) {
                RouteLedgerApp(viewModel = viewModel)
            }
        }
    }
}
