package com.fustun.flightsearch.app_config

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.fustun.flightsearch.data.FlightSearchDatabase
import com.fustun.flightsearch.data.repositories.UserPreferencesRepository
import com.fustun.flightsearch.ui.view_models.FlightSearchViewModel
import com.fustun.flightsearch.ui.view_models.FlightSearchViewModelProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch


private const val FLIGHT_FAVORITES_NAME = "flight_favorites"
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = FLIGHT_FAVORITES_NAME
)


class FlightSearchApplication : Application() {
    lateinit var database: FlightSearchDatabase
    lateinit var userPreferencesRepository: UserPreferencesRepository

    override fun onCreate() {
        super.onCreate()
        database = FlightSearchDatabase.getDatabase(this)
        userPreferencesRepository = UserPreferencesRepository(dataStore)
    }
}
