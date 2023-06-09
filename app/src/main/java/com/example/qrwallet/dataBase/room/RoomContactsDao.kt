package com.example.qrwallet.dataBase.room

import androidx.room.*

@Dao
interface RoomContactsDao {

    @Insert
    suspend fun insertContact(roomContact: RoomContact)

    @Update
    suspend fun updateContact(roomContact: RoomContact)

    @Query("SELECT * FROM contactData")
    suspend fun getContactsList():List<RoomContact>

    @Delete(RoomContact::class)
    suspend fun deleteContact(roomContact: RoomContact)

    @Query("DELETE FROM contactData WHERE name = :name AND phone = :phone")
    suspend fun deleteByUserNameAndPhone(name: String,phone:String)
}