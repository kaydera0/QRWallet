package com.example.qrwallet.dataBase.room

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.qrwallet.dataClasses.UserCardData

@Entity(tableName = "userData")
data class RoomUser(
    @PrimaryKey(autoGenerate = false) val id: Long,
    val name: String,
    val phone: String,
    val email: String,
    val address: String,
    val post: String,
    val facebook: String,
    val limkedIn: String
){

    fun roomClassToDataClass():UserCardData{
        return UserCardData(
            name = this.name,
            phone = this.phone,
            email = this.email,
            address = this.address,
            postCode = this.post,
            facebook = this.facebook,
            linkedIn = this.limkedIn)
    }

}
