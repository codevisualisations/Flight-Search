package com.fustun.flightsearch.data.repositories

import com.fustun.flightsearch.data.dao.FlightSearchDao
import com.fustun.flightsearch.models.DestinationAirport
import com.fustun.flightsearch.models.OriginAirport
import kotlinx.coroutines.flow.Flow

class FlightSearchRepository(
    private val flightSearchDao : FlightSearchDao
):FlightSearchRepositoryInterface {
    override suspend fun repositoryGetAllFlights(): Flow<List<OriginAirport>> = flightSearchDao.daoGetAllFlights()

    override suspend fun repositoryInsertDestinations(destinationAirports : List<DestinationAirport>) = flightSearchDao.insertDestinations(destinationAirports)

    override suspend fun repositoryRetrieveDestinationIdsForAirport(originAirportId: Int): List<Int> = flightSearchDao.daoGetDestinationIds(originAirportId)

    override suspend fun repositoryGetCorrespondingOriginsForDestinationIds(ids: List<Int>): List<OriginAirport>  = flightSearchDao.daoGetCorrespondingOriginsForDestinationIds(ids)
}