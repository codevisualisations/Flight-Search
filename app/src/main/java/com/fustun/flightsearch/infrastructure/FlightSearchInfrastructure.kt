package com.fustun.flightsearch.infrastructure

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.sharp.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.fustun.flightsearch.R
import com.fustun.flightsearch.models.Favorite
import com.fustun.flightsearch.models.OriginAirport
import com.fustun.flightsearch.ui.screens.FlightSearchScreens
import com.fustun.flightsearch.ui.view_models.FlightSearchViewModel

class FlightSearchInfrastructure(
    private val viewModel : FlightSearchViewModel,
    private val navController: NavController
){
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun FlightSearchTopBar(){
       TopAppBar(
            title = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val navBackStackEntry = navController.currentBackStackEntryAsState().value
                    val currentScreen = navBackStackEntry?.destination?.route

                    if (currentScreen == FlightSearchScreens.SEARCHES.name){
                        IconButton(
                            onClick = {
                                navController.popBackStack()
                                viewModel.toggleSearch() },
                        ) {
                            Icon(
                                imageVector = Icons.Sharp.ArrowBack,
                                contentDescription = "Back",
                                tint = Color.Black
                            )
                        }
                    }
                    Spacer(modifier = Modifier.padding(dimensionResource(id = R.dimen.small_padding)))
                    Text(
                        text = stringResource(id = R.string.app_name),
                        textAlign = TextAlign.Center,
                        color = Color.Black,
                    )
                    Spacer(modifier = Modifier.padding(dimensionResource(id = R.dimen.xx_large_padding)))
                    Image(
                        painter = painterResource(id = R.drawable.plane),
                        modifier = Modifier.size(dimensionResource(id = R.dimen.x_large_padding)),
                        contentDescription = null)
                }
            },
           colors = TopAppBarDefaults.topAppBarColors(
               containerColor = MaterialTheme.colorScheme.primary,
           )
        )
    }

    @Composable
    fun FlightRow(currentFlight : OriginAirport, modifier: Modifier = Modifier){
        Row {
            Text(
                text = currentFlight.iataCode,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable {
                    viewModel.performSearch(currentFlight)
                    navController.navigate(FlightSearchScreens.SEARCHES.name)
                }
            )
            Spacer(modifier = modifier.padding(dimensionResource(id = R.dimen.small_padding)))
            Text(
                text=currentFlight.airportName,
                modifier = Modifier.clickable {
                    viewModel.performSearch(currentFlight)
                    navController.navigate(FlightSearchScreens.SEARCHES.name)
                }
            )
        }
    }

    @Composable
    fun FlightCard(searchValue: Favorite, isFavorite: Boolean, onToggleFavorite: () -> Unit) {
        Card(
            modifier = Modifier
                .fillMaxSize()
                .padding(dimensionResource(id = R.dimen.small_padding))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(dimensionResource(id = R.dimen.small_padding)),
            ) {
                Column(
                    modifier = Modifier
                        .weight(0.8f)
                        .fillMaxSize()
                ) {
                    Text(text = "Depart", fontWeight = FontWeight.Bold)
                    Text(text = searchValue.departureCode)
                    Spacer(modifier = Modifier.padding(dimensionResource(id = R.dimen.small_padding)))
                    Text(text = "Arrive", fontWeight = FontWeight.Bold)
                    Text(text = searchValue.destinationCode)
                }

                Column(
                    modifier = Modifier
                        .weight(0.2f)
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    IconButton(
                        onClick = {
                            onToggleFavorite()
                        },
                        modifier = Modifier
                            .padding(top = dimensionResource(id = R.dimen.medium_padding))
                    ) {
                        Icon(
                            imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                            contentDescription = null
                        )
                    }
                }
            }
        }
    }
}
