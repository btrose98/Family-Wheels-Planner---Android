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
import com.example.familywheelsplanner_android.utils.LocalDateTimeDeserializer
import com.google.gson.GsonBuilder
import java.time.LocalDateTime

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideReservationApiService(): ReservationApiService {
        val baseUrl = System.getenv("BASE_URL") ?: BuildConfig.BASE_URL
        val gson = GsonBuilder()
            .registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeDeserializer())
            .create()

        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create(gson))
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