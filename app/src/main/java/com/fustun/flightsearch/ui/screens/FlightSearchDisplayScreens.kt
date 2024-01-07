package com.fustun.flightsearch.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.Mic
import androidx.compose.material.icons.sharp.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavController
import com.fustun.flightsearch.R
import com.fustun.flightsearch.infrastructure.FlightSearchInfrastructure
import com.fustun.flightsearch.ui.view_models.FlightSearchViewModel

class FlightSearchDisplayScreens(
    private val viewModel: FlightSearchViewModel,
    private val flightInfrastructure: FlightSearchInfrastructure,
    private val navController: NavController,
    ){

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun DisplayHomeScreen(modifier : Modifier = Modifier){
        val airports by viewModel.currentFlightsList.collectAsState()
        val query by viewModel.searchTextState.collectAsState()
        val isSearching by viewModel.userIsSearching.collectAsState()
        val currentLazyColumnState = rememberLazyListState()
        val favoritesUiState = viewModel.favoritesUiState.collectAsState()
        val favoriteObjectsUiState = viewModel.favoriteObjectsUiState.collectAsState()


        Scaffold(
            topBar = {flightInfrastructure.FlightSearchTopBar()}
        ){ innerPadding ->
            Column(
                modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .padding(dimensionResource(id = R.dimen.small_padding)),
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                SearchBar(
                    query = query,
                    onQueryChange = {viewModel.onQueryChange(it)},
                    onSearch = {
                        viewModel.performSearch(airports[currentLazyColumnState.firstVisibleItemIndex])
                        navController.navigate(FlightSearchScreens.SEARCHES.name)
                    },
                    active = isSearching,
                    onActiveChange = { viewModel.toggleSearch() },
                    placeholder = { Text(text = "Enter Departure Airport") },
                    leadingIcon = {
                        Icon(
                        Icons.Sharp.Search,
                        contentDescription = null,
                            modifier = Modifier.clickable {
                                if (isSearching){
                                    viewModel.performSearch(airports[currentLazyColumnState.firstVisibleItemIndex])
                                    navController.navigate(FlightSearchScreens.SEARCHES.name)
                                }
                            }
                        )},
                    trailingIcon = {Icon(
                        Icons.Sharp.Mic,
                        contentDescription = null
                    )},
                    modifier = Modifier
                        .clip(RoundedCornerShape(dimensionResource(id = R.dimen.medium_padding)))
                        .weight(0.3f),
                ) {
                    LazyColumn(
                        modifier = Modifier
                            .padding(horizontal = dimensionResource(id = R.dimen.medium_padding)),
                        state = currentLazyColumnState
                    ) {
                        items(airports) { airport ->
                            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.medium_padding)))
                            flightInfrastructure.FlightRow(currentFlight = airport)
                        }
                    }
                }
                Column(
                    modifier = Modifier
                        .weight(0.7f)
                        .fillMaxSize()
                        .padding(dimensionResource(id = R.dimen.medium_padding))
                ) {
                    Text(
                        text =
                            "Favourite Routes:"
                        ,
                        textAlign = TextAlign.Left,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.padding(dimensionResource(id = R.dimen.small_padding)))

                    if (favoritesUiState.value.flightFavorites.isEmpty()) {
                        Text(
                            text = "Nothing to see here, try adding some favourite routes!",
                            textAlign = TextAlign.Left
                        )
                    } else {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(dimensionResource(id = R.dimen.small_padding))
                        ) {
                            items(favoriteObjectsUiState.value.flightFavoriteObjects.size) { index ->
                                val (key, favorite) = favoriteObjectsUiState.value.flightFavoriteObjects.entries.elementAt(index)
                                val isFavorite = favoritesUiState.value.flightFavorites.contains(favorite.id)
                                flightInfrastructure.FlightCard(
                                    favorite,
                                    isFavorite = isFavorite,
                                    onToggleFavorite = {
                                        viewModel.viewModelToggleFavoriteFlight(favorite.id)
                                        viewModel.addOrRemoveFavorite(favorite)
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun DisplayRecommendationsScreen(modifier: Modifier = Modifier) {
        val searches = viewModel.flightsUiState.collectAsState()
        val favoritesUiState = viewModel.favoritesUiState.collectAsState()

        Scaffold(
            topBar = { flightInfrastructure.FlightSearchTopBar() }
        ) { innerPadding ->
            Column(modifier.padding(innerPadding)) {
                LazyColumn {
                    items(searches.value.flightSearches) { searchValue ->
                        val isFavorite = favoritesUiState.value.flightFavorites.contains(searchValue.id)
                        flightInfrastructure.FlightCard(
                            searchValue = searchValue,
                            isFavorite = isFavorite,
                            onToggleFavorite = {
                                viewModel.viewModelToggleFavoriteFlight(searchValue.id)
                                viewModel.addOrRemoveFavorite(searchValue)
                            }
                        )
                    }
                }
            }
        }
    }
}