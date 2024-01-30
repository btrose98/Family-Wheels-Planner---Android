package com.example.familywheelsplanner_android.presentation

import android.net.http.NetworkException
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.familywheelsplanner_android.domain.Reservation
import com.example.familywheelsplanner_android.domain.ReservationError
import com.example.familywheelsplanner_android.domain.ReservationRepository
import com.example.familywheelsplanner_android.domain.ReservationViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class ReservationViewModel @Inject constructor(
    private val reservationRepo: ReservationRepository,
): ViewModel() {

    private val defaultScope: CoroutineScope = viewModelScope

    private var _reservationViewState: MutableStateFlow<ReservationViewState> =
        MutableStateFlow(ReservationViewState.Loading)
    val reservationViewState: StateFlow<ReservationViewState> = _reservationViewState

    init {
        fetchReservations()
    }

    fun fetchReservations() {
        _reservationViewState.value = ReservationViewState.Loading
        defaultScope.launch {
            try {
                val reservations = reservationRepo.fetchReservations()
                _reservationViewState.value = ReservationViewState.Success(reservations)
            } catch (e: Exception) {
                val error = when(e) {
                    is NetworkException -> ReservationError.NetworkError(e.message ?: "Unknown network error")
                    else -> ReservationError.UnkownError
                }
                _reservationViewState.value = ReservationViewState.Error(error)
            }
        }
    }

    fun makeReservation(reservation: Reservation) {
        defaultScope.launch {
            if(!isReservationDateValid(reservation)) {
                _reservationViewState.value = ReservationViewState.Error(ReservationError.InvalidDateTime)
                return@launch
            }
            reservationRepo.makeReservation(reservation)
            fetchReservations()
        }
    }

    fun deleteReservation(reservationId: Int) {
        defaultScope.launch {
            if(!isReservationIdValid(reservationId)) {
                _reservationViewState.value = ReservationViewState.Error(ReservationError.InvalidId)
                return@launch
            }
            reservationRepo.deleteReservation(reservationId)
            fetchReservations()
        }
    }

    private fun isReservationDateValid(reservation: Reservation): Boolean {
        val currentTime = LocalDateTime.now()

        //is reservation a valid day
        if(reservation.reservationDate.toLocalDate() < currentTime.toLocalDate()) {
            return false
        }

        //if reservationDate is today, is reservation a valid time
        if(reservation.reservationDate.toLocalDate() == currentTime.toLocalDate() &&
            reservation.reservationDate.toLocalTime() < currentTime.toLocalTime()) {
            return false
        }

        return true
    }

    private fun isReservationIdValid(id: Int): Boolean {
        return id >= 0
    }
}