package com.example.qrwallet.repositories

import com.example.qrwallet.dataClasses.UserCardData

class QRdecoder {
    fun codeToDataClass(string:String):UserCardData{
        val filedsArr = string.split("\\\\")
        return UserCardData(filedsArr[0],filedsArr[1],filedsArr[2],filedsArr[3],filedsArr[4],filedsArr[5],filedsArr[6])
    }
}