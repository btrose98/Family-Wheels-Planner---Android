package com.example.familywheelsplanner_android.di

import com.example.familywheelsplanner_android.data.ReservationRepositoryImpl
import com.example.familywheelsplanner_android.domain.ReservationRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import javax.inject.Singleton

@Module
@InstallIn(ActivityComponent::class)
abstract class RepositoryModules {

    @Binds
    @Singleton
    abstract fun bindReservationRepository(
        reservationRepositoryImpl: ReservationRepositoryImpl
    ): ReservationRepository

}