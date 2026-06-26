package dev.meller.routeledger.core.maps

/**
 * Карты намеренно вынесены за интерфейс: MVP не зависит от Google Maps / Yandex Maps,
 * но feature-модуль районов уже готов принять настоящего провайдера карт позже.
 */
interface MapProvider {
    val providerName: String
    fun isConfigured(): Boolean
}

class NoOpMapProvider : MapProvider {
    override val providerName: String = "Maps placeholder"
    override fun isConfigured(): Boolean = false
}
