package com.rifqi.trackfunds.di

import com.rifqi.trackfunds.BuildConfig // Import dari modul :app
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Named("AppVersion")
    fun provideAppVersion(): String {
        return BuildConfig.VERSION_NAME
    }
}