package com.rifqi.trackfunds.core.data.di

import com.google.ai.client.generativeai.GenerativeModel
import com.rifqi.trackfunds.core.data.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object GenerativeAiModule {

    @Provides
    @Singleton
    fun provideGenerativeModel(): GenerativeModel {
        return GenerativeModel(
            modelName = "gemini-2.5-flash",

            // Kunci API yang diambil secara aman dari BuildConfig.
            apiKey = BuildConfig.GEMINI_API_KEY
        )
    }
}