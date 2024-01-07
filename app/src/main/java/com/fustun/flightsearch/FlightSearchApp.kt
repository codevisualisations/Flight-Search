package com.fustun.flightsearch

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.fustun.flightsearch.app_config.FlightSearchApplication
import com.fustun.flightsearch.infrastructure.FlightSearchInfrastructure
import com.fustun.flightsearch.ui.screens.FlightSearchDisplayScreens
import com.fustun.flightsearch.ui.screens.FlightSearchScreens
import com.fustun.flightsearch.ui.view_models.FlightSearchViewModel
import com.fustun.flightsearch.ui.view_models.FlightSearchViewModelProvider

class FlightSearchApp {

    @Composable
    fun RunFlightSearch(

    ) {

        val navController : NavHostController = rememberNavController()
        val viewModel : FlightSearchViewModel = viewModel(factory = FlightSearchViewModelProvider.factory)


        val flightSearchInfrastructure = FlightSearchInfrastructure(viewModel,navController)
        val flightSearchDisplayScreens = FlightSearchDisplayScreens(viewModel,flightSearchInfrastructure,navController)

        NavHost(
            navController = navController,
            startDestination = FlightSearchScreens.ROOT.name
        ){
            composable(route = FlightSearchScreens.ROOT.name){
                flightSearchDisplayScreens.DisplayHomeScreen()
            }

            composable(FlightSearchScreens.SEARCHES.name){
                flightSearchDisplayScreens.DisplayRecommendationsScreen()
            }

        }
    }

}





