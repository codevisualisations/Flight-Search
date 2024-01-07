package com.fustun.flightsearch.data.repositories

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserPreferencesRepository(
    private val dataStore : DataStore<Preferences>
) {
    val favoriteFlights: Flow<Set<String>> = dataStore.data
        .map { preferences ->
            preferences[PreferencesKeys.FAVORITE_FLIGHTS] ?: emptySet()
        }

    suspend fun toggleFavoriteFlight(flightId: String) {
        dataStore.edit { preferences ->
            val currentFavorites = preferences[PreferencesKeys.FAVORITE_FLIGHTS] ?: emptySet()
            val newFavorites = if (flightId in currentFavorites) {
                currentFavorites - flightId
            } else {
                currentFavorites + flightId
            }
            preferences[PreferencesKeys.FAVORITE_FLIGHTS] = newFavorites
        }
    }

    suspend fun clearData(){
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    private object PreferencesKeys {
        val FAVORITE_FLIGHTS = stringSetPreferencesKey("favorite_flights")
    }
}