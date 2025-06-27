package com.rifqi.trackfunds.core.data.di

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.rifqi.trackfunds.core.data.remote.api.ReceiptApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideJson(): Json = Json {
        ignoreUnknownKeys = true
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        return OkHttpClient.Builder()
            // FIX: Tambahkan konfigurasi timeout di sini
            .connectTimeout(30, TimeUnit.SECONDS) // Waktu tunggu untuk terhubung
            .readTimeout(30, TimeUnit.SECONDS)    // Waktu tunggu untuk membaca data
            .writeTimeout(30, TimeUnit.SECONDS)   // Waktu tunggu untuk mengirim data
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(json: Json, okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://rifqiaditya.app.n8n.cloud/webhook/")
            .client(okHttpClient)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
    }

    @Provides
    @Singleton
    fun provideReceiptApiService(retrofit: Retrofit): ReceiptApiService {
        return retrofit.create(ReceiptApiService::class.java)
    }
}