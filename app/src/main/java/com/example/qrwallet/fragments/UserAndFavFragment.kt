package com.example.qrwallet.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.StrictMode
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
import com.example.qrwallet.R
import com.example.qrwallet.adapters.RecycleViewAdapter
import com.example.qrwallet.dataBase.room.RoomDB
import com.example.qrwallet.dataClasses.UserCardData
import com.example.qrwallet.databinding.FragmentUserBinding
import com.example.qrwallet.dialogs.QRCodeDialog
import com.example.qrwallet.temporaryUtils.QRCodeUtils
import com.example.qrwallet.viewModels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import javax.inject.Inject


@AndroidEntryPoint
class UserAndFavFragment : Fragment() {

    @Inject
    lateinit var roomDB: RoomDB
    @Inject
    lateinit var qrCodeUtils: QRCodeUtils
    private var _binding: FragmentUserBinding? = null
    private val binding get() = _binding!!
    private val userInfo = ArrayList<String>()
    private val vm: MainViewModel by activityViewModels()
    private var userQrCodeBitmap: Bitmap? = null
    private val dialogTag = "DIALOG_QR_CODE"
    private lateinit var adapter: RecycleViewAdapter
    private var favContactsCardArr = ArrayList<UserCardData>()


    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUserBinding.inflate(inflater, container, false)
        binding.profileBtn.setOnClickListener {
            val bundle = bundleOf("return" to "return")
            findNavController().navigate(R.id.action_mainFragment_to_profileFragment, bundle)
        }
        vm.userData.observe(viewLifecycleOwner) {
            if (it != null) {
                addFieldToCard()
                Log.d("MY_TAG", vm.userData.value?.toString() + " from observer")
                setQrCode(it)
            }
        }

        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)


        setExpandedCardSettings()
        setRecycleViewAdapter()


        binding.userQR.setOnClickListener {
            QRCodeDialog(userQrCodeBitmap!!).show(childFragmentManager, dialogTag)
        }

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun setQrCode(userCardData: UserCardData) {
        CoroutineScope(Dispatchers.Main).launch {
            binding.userQR.setImageBitmap(qrCodeUtils.getQRBitmap(userCardData))
        }


//        CoroutineScope(Dispatchers.Main).launch {
//            val content =
//                "${userCardData.name}\\\\${userCardData.phone}\\\\${userCardData.email}\\\\${userCardData.address}\\\\${userCardData.postCode}\\\\${userCardData.facebook}\\\\${userCardData.linkedIn}"
//            val url = "https://api.qrserver.com/v1/create-qr-code/?data=$content!&size=200x200"
//            val inputStream = withContext(Dispatchers.IO) {
//                URL(url).openStream()
//            }
//            userQrCodeBitmap = BitmapFactory.decodeStream(inputStream)
//            binding.userQR.setImageBitmap(userQrCodeBitmap)
//            withContext(Dispatchers.IO) {
//                inputStream.close()
//            }
//            CoroutineScope(Dispatchers.Main).launch {
//                val inputStream = withContext(Dispatchers.IO) {
//                    URL(url).openStream()
//                }
//                val byteArr = inputStream.readBytes()
//                userCardData.qrCode = byteArr
//                roomDB.userDao()?.updateUser(userCardData.toRoomUser())
//                Log.d("MY_TAG","${byteArr.size} ---size byte array")
//                withContext(Dispatchers.IO) {
//                    inputStream.close()
//                }
//            }
//        }
//        if (userCardData.qrCode != null){
//            CoroutineScope(Dispatchers.Main).launch {
//            Log.d("MY_TAG","userCardData.qrCode != null")
//            val byteArr = vm.userData.value?.qrCode
//                Log.d("MY_TAG","${byteArr?.size} ---size byte array")
//            userQrCodeBitmap = BitmapFactory.decodeByteArray(byteArr,0, byteArr?.size!!)
//                delay(2000)
//            binding.userQR.setImageBitmap(userQrCodeBitmap)
//        }}
    }

    private fun setExpandedCardSettings() {
        binding.arrowButton.setOnClickListener {
            if (binding.hiddenView.visibility == View.VISIBLE) {
                TransitionManager.beginDelayedTransition(binding.baseCardview, AutoTransition())
                binding.hiddenView.setVisibility(View.GONE)
                binding.arrowButton.setImageResource(android.R.drawable.arrow_down_float)
            } else {
                TransitionManager.beginDelayedTransition(binding.baseCardview, AutoTransition());
                binding.hiddenView.setVisibility(View.VISIBLE);
                binding.arrowButton.setImageResource(android.R.drawable.arrow_up_float);
            }
        }
    }

    @SuppressLint("SuspiciousIndentation")
    private fun addFieldToCard() {
        binding.nameInCardView.text = vm.userData.value?.name
        binding.phoneInCardView.text = vm.userData.value?.phone
        if (vm.userData.value?.email?.isNotEmpty() == true) {
            binding.userEmailLinear.visibility = View.VISIBLE
            binding.userEmail.text = vm.userData.value?.email
        } else
            binding.userEmailLinear.visibility = View.GONE
        if (vm.userData.value?.address?.isNotEmpty() == true) {
            binding.userAddressLinear.visibility = View.VISIBLE
            binding.userAddress.text = vm.userData.value?.address
            binding.userAddress.setOnClickListener {
                val place = vm.userData.value?.address
                val googleMapUrl = "https://www.google.com/maps/place/$place"
                val mapIntent = Intent(Intent.ACTION_VIEW).setData(Uri.parse(googleMapUrl))
                startActivity(mapIntent)
            }
        } else
            binding.userAddressLinear.visibility = View.GONE
        if (vm.userData.value?.postCode?.isNotEmpty() == true) {
            binding.userPostCode.text = vm.userData.value?.postCode
            binding.userPostCodeLinear.visibility = View.VISIBLE
        } else
            binding.userPostCodeLinear.visibility = View.GONE
        if (vm.userData.value?.facebook?.isNotEmpty() == true) {
            binding.userFacebook.text = vm.userData.value?.facebook
            binding.userFacebookLinear.visibility = View.VISIBLE
        } else
            binding.userFacebookLinear.visibility = View.GONE
        if (vm.userData.value?.linkedIn?.isNotEmpty() == true){
            binding.userLinkedInLinear.visibility = View.VISIBLE
        binding.userLinkedIn.text = vm.userData.value?.linkedIn
    }
    else
    binding.userLinkedInLinear.visibility = View.GONE

}

private fun setRecycleViewAdapter() {
    vm.favUsersArr.observe(viewLifecycleOwner) {
        favContactsCardArr = it
        binding.recycleViewFav.adapter = RecycleViewAdapter(it, requireContext(), vm)
    }
    adapter = RecycleViewAdapter(favContactsCardArr, requireContext(), vm)
    binding.recycleViewFav.adapter = adapter
    binding.recycleViewFav.layoutManager = LinearLayoutManager(requireContext())
}
}