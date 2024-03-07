package com.example.familywheelsplanner_android.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface ReservationRepository {
    suspend fun fetchReservations(): List<Reservation>
    suspend fun fetchAllReservations(): List<Reservation>
    suspend fun fetchReservationById(reservationId: Int): Reservation
    suspend fun makeReservation(reservation: Reservation)
    suspend fun deleteReservation(reservationId: Int)
}