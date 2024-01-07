package com.fustun.flightsearch.ui.view_models

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.fustun.flightsearch.app_config.FlightSearchApplication
import com.fustun.flightsearch.data.repositories.FlightSearchRepository

object FlightSearchViewModelProvider {
    val factory = viewModelFactory{
        initializer {
            FlightSearchViewModel(
                FlightSearchRepository(flightSearchApplication().database.flightSearchDao()),
                flightSearchApplication().userPreferencesRepository
                )
        }
    }
}


fun CreationExtras.flightSearchApplication(): FlightSearchApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as FlightSearchApplication)