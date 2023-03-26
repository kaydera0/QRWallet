package com.example.qrwallet.network

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.example.qrwallet.dataClasses.UserCardData
import java.net.URL

class RequestsForQRCodes {

    suspend fun requestForQRCode(userCardData: UserCardData): Bitmap {
        val content =
            "${userCardData.name}\\\\${userCardData.phone}\\\\${userCardData.email}\\\\${userCardData.address}\\\\${userCardData.postCode}\\\\${userCardData.facebook}\\\\${userCardData.linkedIn}"
        val url = "https://api.qrserver.com/v1/create-qr-code/?data=$content!&size=200x200"
        val inputStream = URL(url).openStream()
        val bitmapImg = BitmapFactory.decodeStream(inputStream)
        return bitmapImg

    }
}