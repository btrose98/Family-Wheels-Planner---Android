package com.example.familywheelsplanner_android.data

import com.example.familywheelsplanner_android.domain.Reservation
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ReservationApiService {

    @GET("/reservations")
    suspend fun getReservations(): List<Reservation>

    @GET("/reservations/all")
    suspend fun getAllReservations(): List<Reservation>

    @POST("/reservations")
    suspend fun makeReservation(@Body reservation: Reservation)

    @DELETE("/reservations/{reservationId}")
    suspend fun deleteReservation(@Path ("reservationId") reservationId: String)
}