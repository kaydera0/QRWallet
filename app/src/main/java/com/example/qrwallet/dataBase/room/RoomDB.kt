package com.example.qrwallet.dataBase.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities =[RoomUser::class],
    version =1
)
abstract class RoomDB:RoomDatabase() {
    abstract fun roomDao():RoomDao?

    companion object{
        val DB_NAME ="database-user"
    }
}