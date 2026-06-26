package dev.meller.routeledger

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import dagger.hilt.android.AndroidEntryPoint
import dev.meller.routeledger.ui.RouteLedgerApp
import dev.meller.routeledger.ui.theme.RouteLedgerTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RouteLedgerTheme {
                RouteLedgerApp()
            }
        }
    }
}
