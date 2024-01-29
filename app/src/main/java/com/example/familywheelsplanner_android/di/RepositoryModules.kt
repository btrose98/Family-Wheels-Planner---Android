package com.example.familywheelsplanner_android.di

import com.example.familywheelsplanner_android.data.ReservationApiService
import com.example.familywheelsplanner_android.data.ReservationApiServiceImpl
import com.example.familywheelsplanner_android.data.ReservationRepositoryImpl
import com.example.familywheelsplanner_android.domain.ReservationRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(ActivityComponent::class)
abstract class RepositoryModules {

    @Binds
    @Singleton
    abstract fun bindReservationApiService(
        reservationApiServiceImpl: ReservationApiServiceImpl
    ): ReservationApiService

    @Binds
    @Singleton
    abstract fun bindReservationRepository(
        reservationRepositoryImpl: ReservationRepositoryImpl
    ): ReservationRepository

}