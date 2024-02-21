package com.example.familywheelsplanner_android.di

import com.example.familywheelsplanner_android.data.ReservationApiService
import com.example.familywheelsplanner_android.data.ReservationRepositoryImpl
import com.example.familywheelsplanner_android.domain.ReservationRepository
import com.example.familywheelsplanner_android.presentation.ReservationViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.example.familywheelsplanner_android.BuildConfig

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideReservationApiService(): ReservationApiService {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ReservationApiService::class.java)
    }

    @Provides
    fun provideReservationRepository(
        apiService: ReservationApiService
    ): ReservationRepository {
        return ReservationRepositoryImpl(apiService)
    }

    @Provides
    fun provideReservationViewModel(
        repository: ReservationRepository
    ): ReservationViewModel {
        return ReservationViewModel(repository)
    }
}