package com.example.familywheelsplanner_android.presentation

import android.net.http.NetworkException
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.familywheelsplanner_android.domain.FamilyMember
import com.example.familywheelsplanner_android.domain.Reservation
import com.example.familywheelsplanner_android.domain.ReservationError
import com.example.familywheelsplanner_android.domain.ReservationRepository
import com.example.familywheelsplanner_android.domain.ReservationViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class ReservationViewModel @Inject constructor(
    private val reservationRepo: ReservationRepository,
): ViewModel() {

    private val defaultScope: CoroutineScope = viewModelScope

    private var _reservationViewState: MutableStateFlow<ReservationViewState> =
        MutableStateFlow(ReservationViewState.Loading)
    val reservationViewState: StateFlow<ReservationViewState> = _reservationViewState

    private var _singleReservationViewState: MutableStateFlow<ReservationViewState> =
        MutableStateFlow(ReservationViewState.Loading)
    val singleReservationViewState: StateFlow<ReservationViewState> = _singleReservationViewState

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

    fun fetchAllReservations() {
        _reservationViewState.value = ReservationViewState.Loading
        defaultScope.launch {
            try {
                val reservations = reservationRepo.fetchAllReservations()
                _reservationViewState.value = ReservationViewState.Success(reservations)
                Timber.d("resulting reservations from server: $reservations")
            } catch (e: Exception) {
                val error = when(e) {
                    is NetworkException -> ReservationError.NetworkError(e.message ?: "Unknown network error")
                    else -> ReservationError.UnkownError
                }
                _reservationViewState.value = ReservationViewState.Error(error)
            }
        }
    }

    fun fetchReservationById(reservationId: Int) {
        _singleReservationViewState.value = ReservationViewState.Loading
        defaultScope.launch {
            try {
                val reservation = reservationRepo.fetchReservationById(reservationId)
                _singleReservationViewState.value = ReservationViewState.SingleSuccess(reservation)
                Timber.d("resulting reservations from server: $reservation")
            } catch (e: Exception) {
                val error = when(e) {
                    is NetworkException -> ReservationError.NetworkError(e.message ?: "Unknown network error")
                    else -> ReservationError.UnkownError
                }
                _singleReservationViewState.value = ReservationViewState.Error(error)
            }
        }
    }

    fun makeReservation(
        reservationId: Int,
        reservationStartDate: LocalDateTime,
        reservationEndDate: LocalDateTime,
        reservationOwner: Int, //TODO
        car: Int //TODO
    ) {
//        val tzEnforcedStartDate = Reservation.convertToUSEastern(reservationStartDate)
//        val tzEnforcedEndDate = Reservation.convertToUSEastern(reservationEndDate)
        //TESTING
        val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME
        val isoStartDate = reservationStartDate.format(formatter)
        val isoEndDate = reservationEndDate.format(formatter)
        val reservation = Reservation(
            reservationId,
            isoStartDate,
            isoEndDate,
            reservationOwner,
            car
        )

        Timber.d("reservationStartdate: $reservationStartDate")
        Timber.d("reservationEndDate: $reservationEndDate")
//        Timber.d("tzStart: $tzEnforcedStartDate")
//        Timber.d("tzEnd: $tzEnforcedEndDate")
        Timber.d("reservation: \n$reservation")

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
        val startDateTime = LocalDateTime.parse(reservation.startdatetime)
        val endDateTime = LocalDateTime.parse(reservation.enddatetime)

        if(startDateTime > endDateTime) return false

        if(startDateTime.toLocalDate() < currentTime.toLocalDate()) return false

        //if reservationDate is today, is reservation time valid
        if(startDateTime.toLocalDate() == currentTime.toLocalDate() &&
            startDateTime.toLocalTime() < currentTime.toLocalTime()) {
            return false
        }

        return true
    }

    private fun isReservationIdValid(id: Int): Boolean {
        return id >= 0
    }
}