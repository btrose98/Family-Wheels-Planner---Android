package com.example.familywheelsplanner_android.data

import com.example.familywheelsplanner_android.domain.Reservation
import retrofit2.Retrofit
import javax.inject.Inject

class ReservationApiServiceImpl @Inject constructor(
    private val retrofit: Retrofit
): ReservationApiService {
    override suspend fun getReservations() {
        TODO("Not yet implemented")
    }

    override suspend fun makeReservation(reservation: Reservation) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteReservation(reservationId: String) {
        TODO("Not yet implemented")
    }
}