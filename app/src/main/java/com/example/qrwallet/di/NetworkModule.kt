package com.example.qrwallet.di

import com.example.qrwallet.network.RequestsForQRCodes
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    @Singleton
    fun provideNetworkQRCodes(): RequestsForQRCodes {
        return RequestsForQRCodes()
    }

}