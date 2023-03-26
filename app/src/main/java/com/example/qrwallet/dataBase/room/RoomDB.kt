package com.example.qrwallet.dataBase.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities =[RoomUser::class,RoomContact::class],
    version =1
)
abstract class RoomDB:RoomDatabase() {
    abstract fun userDao():RoomUserDao?
    abstract fun contactsDao():RoomContactsDao?

    companion object{
        val DB_NAME ="database-user"
    }
}

