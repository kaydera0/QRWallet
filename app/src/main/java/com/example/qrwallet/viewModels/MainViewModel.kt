package com.example.qrwallet.viewModels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.qrwallet.dataBase.room.RoomDB
import com.example.qrwallet.dataClasses.UserCardData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(val roomDB: RoomDB):ViewModel() {
    val userData = MutableLiveData<UserCardData>()

    init {
        Log.d("MY_TAG", "vm init")
        viewModelScope.launch {
            userData.postValue(roomDB.roomDao()?.getUserData()?.roomClassToDataClass())
        }
//        Log.d("MY_TAG", userData.value?.name!!)
    }
    fun updateUserdata(){
        viewModelScope.launch {
            userData.postValue(roomDB.roomDao()?.getUserData()?.roomClassToDataClass())
        }
    }


}