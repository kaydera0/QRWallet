package com.example.qrwallet.dataBase.room

import com.example.qrwallet.dataClasses.UserCardData

fun List<RoomContact>.toDataClass() : ArrayList<UserCardData> {
    val arrayList =  ArrayList<UserCardData>()
    for (i in this){
        val userCardData = UserCardData(
            name = i.name,
            phone = i.phone,
            email = i.email,
            address = i.address,
            postCode = i.post,
            facebook = i.facebook,
            linkedIn = i.linkedIn
        )
        arrayList.add(userCardData)
    }
    return arrayList
}