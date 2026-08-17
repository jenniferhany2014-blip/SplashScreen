package com.example.splashscreen.di

import com.example.splashscreen.ProductApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

/**
 * Provides network-related dependencies.
 *
 * Libraries:
 * - Dagger Hilt
 * - Retrofit
 * - Gson Converter
 *
 * Responsibility:
 * - Creates the Retrofit instance.
 * - Creates ProductApiService.
 * - Makes both dependencies available through Hilt.
 */
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val BASE_URL = "https://dummyjson.com/"

    /**
     * Creates the application's Retrofit instance.
     *
     * Retrofit is responsible for communicating with the
     * remote REST API.
     *
     * @return configured Retrofit instance.
     */
    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(
                GsonConverterFactory.create()
            )
            .build()
    }
    /**
     * Creates the ProductApiService implementation.
     *
     * Retrofit generates the implementation of the API interface.
     *
     * @param retrofit configured Retrofit instance.
     * @return ProductApiService implementation.
     */
    @Provides
    @Singleton
    fun provideProductApiService(
        retrofit: Retrofit
    ): ProductApiService {
        return retrofit.create(ProductApiService::class.java)
    }
}