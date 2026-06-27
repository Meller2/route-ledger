package dev.meller.routeledger.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.meller.routeledger.core.model.DeliveryOrder
import dev.meller.routeledger.core.model.LedgerSnapshot
import dev.meller.routeledger.core.model.OrderRating
import dev.meller.routeledger.core.model.Shift
import dev.meller.routeledger.core.model.TransportType
import dev.meller.routeledger.data.repository.LedgerRepository
import dev.meller.routeledger.data.settings.SettingsRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class AppViewModel @Inject constructor(
    private val ledgerRepository: LedgerRepository,
    private val settingsRepository: SettingsRepository,
) : ViewModel() {
    val snapshot = ledgerRepository.snapshot
        .map<LedgerSnapshot, LedgerSnapshot?> { it }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

    val darkTheme = settingsRepository.darkTheme
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), false)

    init {
        viewModelScope.launch {
            ledgerRepository.seedDemoDataIfEmpty()
        }
    }

    fun setDarkTheme(enabled: Boolean) {
        viewModelScope.launch {
            settingsRepository.setDarkTheme(enabled)
        }
    }

    fun startShift(startArea: String = "Текущий район") {
        viewModelScope.launch {
            ledgerRepository.saveShift(
                Shift(
                    startedAt = LocalDateTime.now(),
                    transportType = TransportType.Bike,
                    startArea = startArea.ifBlank { "Текущий район" },
                    note = "Быстрый старт",
                ),
            )
        }
    }

    fun addQuickOrder(
        amountRub: Int = 350,
        tipsRub: Int = 0,
        pickupArea: String = "",
        dropoffArea: String = "Следующая точка",
    ) {
        viewModelScope.launch {
            val shift = snapshot.value?.shifts?.firstOrNull() ?: return@launch
            ledgerRepository.saveOrder(
                DeliveryOrder(
                    shiftId = shift.id,
                    amountRub = amountRub.coerceAtLeast(0),
                    tipsRub = tipsRub.coerceAtLeast(0),
                    pickupArea = pickupArea.ifBlank { shift.endArea ?: shift.startArea },
                    dropoffArea = dropoffArea.ifBlank { "Следующая точка" },
                    distanceKm = 2.4,
                    durationMinutes = 22,
                    rating = OrderRating.Good,
                    note = "Быстрый заказ",
                ),
            )
        }
    }
}
