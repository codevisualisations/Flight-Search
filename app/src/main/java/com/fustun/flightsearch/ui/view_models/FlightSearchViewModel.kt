package com.fustun.flightsearch.ui.view_models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fustun.flightsearch.data.repositories.FlightSearchRepository
import com.fustun.flightsearch.data.repositories.UserPreferencesRepository
import com.fustun.flightsearch.data.uiState.FavoriteObjectsUiState
import com.fustun.flightsearch.data.uiState.FavoritesUiState
import com.fustun.flightsearch.data.uiState.FlightsUiState
import com.fustun.flightsearch.models.DestinationAirport
import com.fustun.flightsearch.models.Favorite
import com.fustun.flightsearch.models.OriginAirport
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.random.Random

class FlightSearchViewModel(
    private val flightSearchRepository: FlightSearchRepository,
    private val flightPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    private val _userIsSearching = MutableStateFlow(false)
    val userIsSearching: StateFlow<Boolean> get() = _userIsSearching.asStateFlow()

    private val _searchTextState = MutableStateFlow("")
    val searchTextState: StateFlow<String> get() = _searchTextState.asStateFlow()

    private val _flightsUiState = MutableStateFlow(FlightsUiState())
    val flightsUiState: StateFlow<FlightsUiState> get() = _flightsUiState.asStateFlow()

    private val _favoriteObjectsUiState = MutableStateFlow(FavoriteObjectsUiState())
    val favoriteObjectsUiState: StateFlow<FavoriteObjectsUiState> get() = _favoriteObjectsUiState.asStateFlow()

    val favoritesUiState: StateFlow<FavoritesUiState> = flightPreferencesRepository.favoriteFlights.map { value ->
        FavoritesUiState(flightFavorites = value)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = FavoritesUiState()
    )

    val currentFlightsList: StateFlow<List<OriginAirport>> = _searchTextState.combine(flightsUiState) { text, flights ->
        filterFlights(text, flights)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = flightsUiState.value.flightsList
    )

    init {
        viewModelScope.launch {
            val getAllFlights = async { viewModelGetAllFlights().first() }.await()
            _flightsUiState.value = _flightsUiState.value.copy(flightsList = getAllFlights)
            formDestinationsList()
            flightPreferencesRepository.clearData()
            flightSearchRepository.repositoryInsertDestinations(flightsUiState.value.destinationsLists)
        }
    }

    fun performSearch(airport: OriginAirport) {
        viewModelScope.launch {
            val newSearchResults = generateSearchResults(airport)
            _flightsUiState.value = _flightsUiState.value.copy(flightSearches = newSearchResults)
        }
    }

    fun toggleSearch() {
        _userIsSearching.value = !_userIsSearching.value
        if (!_userIsSearching.value) {
            onQueryChange("")
        }
    }

    fun onQueryChange(text: String) {
        _searchTextState.value = text
    }

    fun viewModelToggleFavoriteFlight(flight: String) {
        viewModelScope.launch {
            flightPreferencesRepository.toggleFavoriteFlight(flight)
        }
    }

    fun addOrRemoveFavorite(flight: Favorite) {
        _favoriteObjectsUiState.value = _favoriteObjectsUiState.value.copy(
            flightFavoriteObjects = _favoriteObjectsUiState.value.flightFavoriteObjects
                .toMutableMap()
                .apply { if (containsKey(flight.id)) remove(flight.id) else put(flight.id, flight) }
        )
    }


    private suspend fun viewModelRetrieveDestinationIdsForAirport(originAirportId: Int): List<Int> =
        flightSearchRepository.repositoryRetrieveDestinationIdsForAirport(originAirportId)

    private suspend fun viewModelGetCorrespondingOriginsForDestinationIds(neededIds: List<Int>): List<OriginAirport> =
        flightSearchRepository.repositoryGetCorrespondingOriginsForDestinationIds(neededIds)

    private suspend fun viewModelGetAllFlights(): Flow<List<OriginAirport>> =
        flightSearchRepository.repositoryGetAllFlights()

    private suspend fun generateSearchResults(airport: OriginAirport): List<Favorite> {
        val currentAirportIataCodeAndName = "${airport.iataCode}   ${airport.airportName}"
        val destinationIds = viewModelRetrieveDestinationIdsForAirport(airport.id)
        val airportDestinationsList = viewModelGetCorrespondingOriginsForDestinationIds(destinationIds)

        return airportDestinationsList.map { v ->
            Favorite(
                id = "$currentAirportIataCodeAndName,${v.iataCode}${v.airportName}",
                departureCode = currentAirportIataCodeAndName,
                destinationCode = "${v.iataCode}   ${v.airportName}"
            )
        }
    }

    private fun formDestinationsList() {
        val currentFlights = flightsUiState.value.flightsList
        val destinationList = mutableListOf<DestinationAirport>()

        for (i in currentFlights.indices) {
            val originAirportId = currentFlights[i].id
            val numDestinations = Random.nextInt(1, 5)
            val destinations = generateUniqueRandomDestinations(originAirportId, numDestinations)

            destinations.forEach { destinationId ->
                val destinationAirport = DestinationAirport(destinationList.size + 1, originAirportId, destinationId)
                destinationList.add(destinationAirport)
            }
        }
        _flightsUiState.value = _flightsUiState.value.copy(destinationsLists = destinationList)
    }

    private fun generateUniqueRandomDestinations(originAirportId: Int, numDestinations: Int): List<Int> {
        val random = Random.Default
        val destinations = mutableSetOf<Int>()

        while (destinations.size < numDestinations) {
            val randomId = random.nextInt(1, flightsUiState.value.flightsList.size + 1)
            if (randomId != originAirportId) {
                destinations.add(randomId)
            }
        }
        return destinations.toList()
    }

    private fun filterFlights(text: String, flights: FlightsUiState): List<OriginAirport> {
        return if (text.isBlank()) {
            flights.flightsList
        } else {
            flights.flightsList.filter { airport ->
                airport.iataCode.contains(text, ignoreCase = true) || airport.airportName.contains(text, ignoreCase = true)
            }
        }
    }
}
