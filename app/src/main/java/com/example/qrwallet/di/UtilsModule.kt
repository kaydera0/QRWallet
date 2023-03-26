package com.example.qrwallet.di

import android.content.Context
import androidx.room.Room
import com.example.qrwallet.dataBase.room.RoomDB
import com.example.qrwallet.temporaryUtils.QRCodeUtils
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class UtilsModule {

    @Provides
    @Singleton
    fun provideUtils(roomDB: RoomDB) : QRCodeUtils {
        return QRCodeUtils(roomDB)
    }
}