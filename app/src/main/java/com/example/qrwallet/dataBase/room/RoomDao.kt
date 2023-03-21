package com.example.qrwallet.dataBase.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface RoomDao {

    @Insert
    suspend fun insertUser(roomUser: RoomUser)

    @Update
    suspend fun updateUser(roomUser: RoomUser)

    @Query("SELECT * FROM userData WHERE id = 0")
    suspend fun getUserData():RoomUser

    @Query("DELETE FROM userData")
    suspend fun clearTable()
}