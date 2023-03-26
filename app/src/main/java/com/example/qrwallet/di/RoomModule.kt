package com.example.qrwallet.di

import android.content.Context
import androidx.room.Room
import com.example.qrwallet.dataBase.room.RoomDB
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RoomModule {

    @Provides
    @Singleton
    fun provideRoomDB(@ApplicationContext context:Context) : RoomDB{
        return Room.databaseBuilder(context,RoomDB::class.java,RoomDB.DB_NAME).build()
    }
}