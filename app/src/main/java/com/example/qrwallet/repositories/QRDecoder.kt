package com.example.qrwallet.repositories

import com.example.qrwallet.dataClasses.UserCardData

class QRDecoder {
    fun codeToDataClass(string:String):UserCardData{
        val fieldsArr = string.split("\\\\")
        return UserCardData(fieldsArr[0],fieldsArr[1],fieldsArr[2],fieldsArr[3],fieldsArr[4],fieldsArr[5],fieldsArr[6].replace("!",""))
    }
}