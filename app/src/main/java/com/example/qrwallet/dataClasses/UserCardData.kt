package com.example.qrwallet.dataClasses

import com.example.qrwallet.dataBase.room.RoomContact
import com.example.qrwallet.dataBase.room.RoomUser

data class UserCardData
    (val name:String,
     val phone:String,
     val email:String,
     val address:String,
     val postCode:String,
     val facebook:String,
     val linkedIn:String,
     var qrCode:ByteArray? = null
){
     fun toRoomContact():RoomContact=RoomContact(
      id = 0,
      name = this.name,
      phone = this.phone,
      email = this.email,
      address = this.address,
      post = this.postCode,
      facebook = this.facebook,
      linkedIn = this.linkedIn,
     )
    fun toRoomUser(): RoomUser =RoomUser(
        id = 0,
        name = this.name,
        phone = this.phone,
        email = this.email,
        address = this.address,
        post = this.postCode,
        facebook = this.facebook,
        linkedIn = this.linkedIn,
        qrcode = this.qrCode

    )


    }