package com.example.qrwallet.temporaryUtils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import com.example.qrwallet.dataBase.room.RoomDB
import com.example.qrwallet.dataClasses.PhoneContactDataClass
import com.example.qrwallet.dataClasses.UserCardData
import io.github.g0dkar.qrcode.QRCode
import kotlinx.coroutines.*
import java.io.ByteArrayOutputStream
import java.net.URL

class QRCodeUtils(val roomDB: RoomDB) {

    suspend fun getQRBitmap(userCardData: UserCardData): Bitmap {
        val byteArrayOutputStream = ByteArrayOutputStream()
        val content =
                "${userCardData.name}\\\\${userCardData.phone}\\\\${userCardData.email}\\\\${userCardData.address}\\\\${userCardData.postCode}\\\\${userCardData.facebook}\\\\${userCardData.linkedIn}"
        QRCode(content)
            .render(cellSize = 25)
            .writeImage(byteArrayOutputStream)
        val imageBytes = byteArrayOutputStream.toByteArray()
        val qrCode = BitmapFactory.decodeByteArray(imageBytes,0,imageBytes.size)
        return qrCode
    }
    suspend fun getQRBitmap(phoneContactDataClass: PhoneContactDataClass): Bitmap {
        val byteArrayOutputStream = ByteArrayOutputStream()
        val content =
            "${phoneContactDataClass.name}\\\\${phoneContactDataClass.number}"
        QRCode(content)
            .render(cellSize = 25)
            .writeImage(byteArrayOutputStream)
        val imageBytes = byteArrayOutputStream.toByteArray()
        val qrCode = BitmapFactory.decodeByteArray(imageBytes,0,imageBytes.size)
        return qrCode
    }
}