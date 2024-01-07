package com.fustun.flightsearch.data.repositories

import com.fustun.flightsearch.models.DestinationAirport
import com.fustun.flightsearch.models.OriginAirport
import kotlinx.coroutines.flow.Flow

interface FlightSearchRepositoryInterface {
    suspend fun repositoryGetAllFlights() : Flow<List<OriginAirport>>

    suspend fun repositoryInsertDestinations(destinationAirports : List<DestinationAirport>)

    suspend fun repositoryRetrieveDestinationIdsForAirport(originAirportId:Int) : List<Int>

    suspend fun repositoryGetCorrespondingOriginsForDestinationIds(ids : List<Int>) : List<OriginAirport>
}