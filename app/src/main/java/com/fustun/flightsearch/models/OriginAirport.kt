package com.fustun.flightsearch.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "airport")
data class OriginAirport(
    @PrimaryKey
    val id: Int,

    @ColumnInfo("iata_code")
    val iataCode: String,

    @ColumnInfo(name = "name")
    val airportName: String,

    @ColumnInfo(name = "passengers")
    val passengers: Int
)
