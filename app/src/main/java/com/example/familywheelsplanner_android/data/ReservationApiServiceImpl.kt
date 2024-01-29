package com.example.familywheelsplanner_android.data

import com.example.familywheelsplanner_android.domain.Reservation
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import javax.inject.Inject

class ReservationApiServiceImpl @Inject constructor(
    private val retrofit: Retrofit,
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.IO
): ReservationApiService {

    private val apiService: ReservationApiService by lazy {
        retrofit.create(ReservationApiService::class.java)
    }

    override suspend fun getReservations(): List<Reservation> {
        return withContext(defaultDispatcher) {
            apiService.getReservations()
        }
    }

    override suspend fun makeReservation(reservation: Reservation) {
        withContext(defaultDispatcher) {
            apiService.makeReservation(reservation)
        }
    }

    override suspend fun deleteReservation(reservationId: String) {
        withContext(defaultDispatcher) {
            apiService.deleteReservation(reservationId)
        }
    }
}