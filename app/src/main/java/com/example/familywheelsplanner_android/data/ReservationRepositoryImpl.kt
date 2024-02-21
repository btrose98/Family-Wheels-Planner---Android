package com.example.familywheelsplanner_android.data

import com.example.familywheelsplanner_android.domain.Reservation
import com.example.familywheelsplanner_android.domain.ReservationRepository
import javax.inject.Inject

class ReservationRepositoryImpl @Inject constructor(
    private val apiService: ReservationApiService
): ReservationRepository {
    override suspend fun fetchReservations(): List<Reservation> {
        return apiService.getReservations()
    }

    override suspend fun fetchAllReservations(): List<Reservation> {
        return apiService.getAllReservations()
    }

    override suspend fun makeReservation(reservation: Reservation) {
        apiService.makeReservation(reservation)
    }

    override suspend fun deleteReservation(reservationId: Int) {
        apiService.deleteReservation(reservationId.toString())
    }

}