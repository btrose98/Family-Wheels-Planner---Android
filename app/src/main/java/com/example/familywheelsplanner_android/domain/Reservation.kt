package com.example.familywheelsplanner_android.domain

import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime
import java.time.ZoneId

data class Reservation(
    val id: Int,
    val startdatetime: LocalDateTime,
    val enddatetime: LocalDateTime,
    val owner: Int,
    val car: Int
) {
    companion object {
        fun convertToUSEastern(localDateTime: LocalDateTime): LocalDateTime {
            val zoneId = ZoneId.of("America/New_York") // US/Eastern timezone
            return localDateTime.atZone(zoneId).toLocalDateTime()
        }
    }
}