package com.example.qrwallet.viewModels

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.qrwallet.dataBase.room.RoomDB
import com.example.qrwallet.dataBase.room.toDataClass
import com.example.qrwallet.dataClasses.PhoneContactDataClass
import com.example.qrwallet.dataClasses.UserCardData
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(val roomDB: RoomDB,
@ApplicationContext context: Context):ViewModel() {
    val userData = MutableLiveData<UserCardData>()
    val favUsersArr = MutableLiveData<ArrayList<UserCardData>>()
    val addedContactsUserArr = MutableLiveData<ArrayList<PhoneContactDataClass>>()
    val networkStatus = MutableLiveData(false)
    private val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    init {
        viewModelScope.launch {
            userData.postValue(roomDB.userDao()?.getUserData()?.roomClassToDataClass())
            favUsersArr.postValue(roomDB.contactsDao()?.getContactsList()?.toDataClass())
        }
        val networkStatusCallback = object :ConnectivityManager.NetworkCallback(){
            override fun onAvailable(network: Network) = networkStatus.postValue(true)
            override fun onUnavailable() = networkStatus.postValue(false)
            override fun onLost(network: Network) = networkStatus.postValue(false)
        }
        val request = NetworkRequest.Builder().addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET).build()
        connectivityManager.registerNetworkCallback(request,networkStatusCallback)
    }
    fun updateUserdata(){
        viewModelScope.launch {
            userData.postValue(roomDB.userDao()?.getUserData()?.roomClassToDataClass())
        }
    }
    fun addUserCardToFav(userCardData: UserCardData){
        var arrValue = ArrayList<UserCardData>()
        if (favUsersArr.value!=null){
        arrValue = favUsersArr.value!!}
        arrValue.add(userCardData)
        favUsersArr.value = arrValue
        viewModelScope.launch {
            roomDB.contactsDao()?.insertContact(userCardData.toRoomContact())
        }
    }
    fun removeCardUser(userCardData: UserCardData){
        var arrValue = ArrayList<UserCardData>()
        if (favUsersArr.value!=null){
            arrValue = favUsersArr.value!!}
        arrValue.remove(userCardData)
        favUsersArr.value = arrValue
        viewModelScope.launch {
            roomDB.contactsDao()?.deleteByUserNameAndPhone(userCardData.name,userCardData.phone)
        }
    }
}