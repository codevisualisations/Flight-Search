package com.fustun.flightsearch.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "destinations")
data class DestinationAirport(
    @PrimaryKey
    val id: Int,
    val originAirportId: Int,
    val destinationAirportId: Int
)
