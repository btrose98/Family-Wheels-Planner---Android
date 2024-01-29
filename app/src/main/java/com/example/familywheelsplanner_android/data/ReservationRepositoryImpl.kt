package com.example.familywheelsplanner_android.data

import com.example.familywheelsplanner_android.domain.ReservationRepository
import javax.inject.Inject

class ReservationRepositoryImpl @Inject constructor(
    private val apiService: ReservationApiService
): ReservationRepository {

}