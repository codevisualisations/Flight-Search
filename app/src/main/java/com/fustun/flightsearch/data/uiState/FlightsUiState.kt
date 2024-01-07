package com.fustun.flightsearch.data.uiState

import com.fustun.flightsearch.models.DestinationAirport
import com.fustun.flightsearch.models.Favorite
import com.fustun.flightsearch.models.OriginAirport

data class FlightsUiState(
    val flightsList : List<OriginAirport> = emptyList(),
    val destinationsLists : List<DestinationAirport> = emptyList(),
    val flightSearches : List<Favorite> = emptyList(),
)
