package com.example.familywheelsplanner_android.domain

import java.time.LocalDateTime

data class Reservation(
    val reservationId: Int,
    val reservationDate: LocalDateTime,
    val reservationOwner: FamilyMember
)
