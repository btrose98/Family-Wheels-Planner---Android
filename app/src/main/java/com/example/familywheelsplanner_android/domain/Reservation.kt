package com.example.familywheelsplanner_android.domain

import java.time.LocalDateTime
import java.time.ZoneId

data class Reservation(
    val reservationId: Int,
    val reservationDate: LocalDateTime,
    val reservationOwner: FamilyMember
) {
    companion object {
        fun convertToUSEastern(localDateTime: LocalDateTime): LocalDateTime {
            val zoneId = ZoneId.of("America/New_York") // US/Eastern timezone
            return localDateTime.atZone(zoneId).toLocalDateTime()
        }
    }
}