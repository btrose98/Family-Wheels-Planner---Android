package com.example.familywheelsplanner_android.di

import com.example.familywheelsplanner_android.data.ReservationApiService
import com.example.familywheelsplanner_android.data.ReservationApiServiceImpl
import com.example.familywheelsplanner_android.data.ReservationRepositoryImpl
import com.example.familywheelsplanner_android.domain.ReservationRepository
import com.example.familywheelsplanner_android.presentation.ReservationViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("http://your-api-base-url.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    fun provideReservationApiService(
        retrofit: Retrofit
    ): ReservationApiService {
        return ReservationApiServiceImpl(retrofit)
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