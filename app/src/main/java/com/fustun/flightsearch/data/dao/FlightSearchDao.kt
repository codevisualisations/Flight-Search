package com.fustun.flightsearch.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.fustun.flightsearch.models.DestinationAirport
import com.fustun.flightsearch.models.OriginAirport
import kotlinx.coroutines.flow.Flow

@Dao
interface FlightSearchDao{
    @Query("SELECT * FROM airport")
    fun daoGetAllFlights() : Flow<List<OriginAirport>>

    @Insert
    suspend fun insertDestinations(destinationAirports : List<DestinationAirport>)

    @Query("SELECT destinationAirportId FROM destinations WHERE originAirportId = :originAirportId")
    suspend fun daoGetDestinationIds(originAirportId : Int) : List<Int>

    @Query("SELECT * FROM airport WHERE id IN (:airportIds)")
    suspend fun daoGetCorrespondingOriginsForDestinationIds(airportIds: List<Int>): List<OriginAirport>
}