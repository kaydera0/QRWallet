package com.example.qrwallet.temporaryUtils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.example.qrwallet.dataBase.room.RoomDB
import com.example.qrwallet.dataClasses.UserCardData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.URL

class QRCodeUtils(val roomDB: RoomDB) {

     suspend fun getQRBitmap(userCardData:UserCardData):Bitmap{
         if (userCardData.qrCode != null)
         return BitmapFactory.decodeByteArray(userCardData.qrCode,0, userCardData.qrCode?.size!!)
         else{
             val content =
                "${userCardData.name}\\\\${userCardData.phone}\\\\${userCardData.email}\\\\${userCardData.address}\\\\${userCardData.postCode}\\\\${userCardData.facebook}\\\\${userCardData.linkedIn}"
            val url = "https://api.qrserver.com/v1/create-qr-code/?data=$content!&size=200x200"
            val inputStream = withContext(Dispatchers.IO) {
                URL(url).openStream()
            }
             val byteArr = inputStream.readBytes()
             userCardData.qrCode = byteArr
             roomDB.userDao()?.updateUser(userCardData.toRoomUser())
            withContext(Dispatchers.IO) {
                inputStream.close()
            }
             return BitmapFactory.decodeByteArray(userCardData.qrCode,0, userCardData.qrCode?.size!!)
         }

    }
}