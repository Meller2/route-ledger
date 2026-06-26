package dev.meller.routeledger.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.meller.routeledger.core.maps.MapProvider
import dev.meller.routeledger.core.maps.NoOpMapProvider
import dev.meller.routeledger.data.local.RouteLedgerDatabase
import dev.meller.routeledger.data.local.dao.LedgerDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): RouteLedgerDatabase =
        Room.databaseBuilder(
            context,
            RouteLedgerDatabase::class.java,
            "route-ledger.db",
        ).build()

    @Provides
    fun provideLedgerDao(database: RouteLedgerDatabase): LedgerDao = database.ledgerDao()

    @Provides
    @Singleton
    fun provideMapProvider(): MapProvider = NoOpMapProvider()
}
