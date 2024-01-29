package com.example.familywheelsplanner_android.domain

sealed class ReservationViewState {
    object Loading : ReservationViewState()
    data class Success(val reservations: List<Reservation>) : ReservationViewState()
    data class Error(val message: String) : ReservationViewState()
}