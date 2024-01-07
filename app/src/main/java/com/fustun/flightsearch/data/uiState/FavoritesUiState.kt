package com.fustun.flightsearch.data.uiState

import com.fustun.flightsearch.models.Favorite

data class FavoritesUiState(
    val flightFavorites : Set<String> = emptySet(),
)

