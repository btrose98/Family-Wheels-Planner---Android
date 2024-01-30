package com.example.familywheelsplanner_android.domain

sealed class ReservationError(val errorMessage: String?) {
    object SlotUnavailable : ReservationError("Selected reservation slot is unavailable.")
    object PermissionDenied : ReservationError("You do not have permission to make a reservation.")
    object InvalidDateTime : ReservationError("Invalid date/time selected.")
    object PastDateTime : ReservationError("Selected date/time is in the past.")
    object InvalidId : ReservationError("Invalid reservation id.")
    data class NetworkError(val message: String) : ReservationError(message)
    object UnkownError : ReservationError("Unknown error.")
}