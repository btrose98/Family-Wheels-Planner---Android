package com.example.familywheelsplanner_android.presentation

import androidx.lifecycle.ViewModel
import com.example.familywheelsplanner_android.domain.ReservationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ReservationViewModel @Inject constructor(
    private val reservationRepo: ReservationRepository
): ViewModel() {

}