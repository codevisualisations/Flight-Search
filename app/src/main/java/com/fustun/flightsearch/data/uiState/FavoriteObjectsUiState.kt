package com.fustun.flightsearch.data.uiState

import com.fustun.flightsearch.models.Favorite


data class FavoriteObjectsUiState(
    val flightFavoriteObjects : Map<String, Favorite> = hashMapOf()
)