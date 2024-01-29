package com.example.familywheelsplanner_android.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.familywheelsplanner_android.domain.Reservation
import com.example.familywheelsplanner_android.domain.ReservationRepository
import com.example.familywheelsplanner_android.domain.ReservationViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
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
                _reservationViewState.value = ReservationViewState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun makeReservation(reservation: Reservation) {
        defaultScope.launch {
            reservationRepo.makeReservation(reservation)
            fetchReservations()
        }
    }

    fun deleteReservation(reservationId: Int) {
        defaultScope.launch {
            reservationRepo.deleteReservation(reservationId)
            fetchReservations()
        }
    }
}