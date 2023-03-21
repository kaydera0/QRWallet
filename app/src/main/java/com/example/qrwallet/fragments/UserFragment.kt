package com.example.qrwallet.fragments

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.StrictMode
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
import com.example.qrwallet.dataClasses.UserCardData
import com.example.qrwallet.databinding.FragmentUserBinding
import com.example.qrwallet.dialogs.QRCodeDialog
import com.example.qrwallet.viewModels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.URL


@AndroidEntryPoint
class UserFragment : Fragment() {

    private var _binding: FragmentUserBinding? = null
    private val binding get() = _binding!!
    private val userInfo = ArrayList<String>()
    private val vm: MainViewModel by activityViewModels()
    private var userQrCodeBitmap: Bitmap? = null
    private val dialogTag = "DIALOG_QR_CODE"


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUserBinding.inflate(inflater, container, false)

        vm.userData.observe(viewLifecycleOwner) {
            if (it != null) {
                binding.nameInCardView.text = vm.userData.value?.name
                binding.phoneInCardView.text = vm.userData.value?.phone
                addFieldToCard()
                Log.d("MY_TAG", vm.userData.value?.name!! + " from observer")
                setQrCode(vm.userData.value!!)
            }
        }


        binding.listViewUser.adapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, userInfo)
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)


        setExpandedCardSwettings()


        binding.userQR.setOnClickListener {
            QRCodeDialog(userQrCodeBitmap!!).show(childFragmentManager, dialogTag)
        }

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun setQrCode(userCardData: UserCardData) {
        CoroutineScope(Dispatchers.Main).launch {
            val content =
                "${userCardData.name}\\\\${userCardData.phone}\\\\${userCardData.email}\\\\${userCardData.address}\\\\${userCardData.postCode}\\\\${userCardData.facebook}\\\\${userCardData.linkedIn}"
            val url = "https://api.qrserver.com/v1/create-qr-code/?data=$content!&size=200x200"
            val inputStream = URL(url).openStream()
            userQrCodeBitmap = BitmapFactory.decodeStream(inputStream)
            binding.userQR.setImageBitmap(userQrCodeBitmap)
        }
    }

    private fun setExpandedCardSwettings() {
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
    private fun addFieldToCard(){
        if (vm.userData.value?.email?.isNotEmpty() == true)
        userInfo.add(vm.userData.value?.email!!)
        if (vm.userData.value?.address?.isNotEmpty() == true)
        userInfo.add(vm.userData.value?.address!!)
        if (vm.userData.value?.postCode?.isNotEmpty() == true)
        userInfo.add(vm.userData.value?.postCode!!)
        if (vm.userData.value?.facebook?.isNotEmpty() == true)
        userInfo.add(vm.userData.value?.facebook!!)
        if (vm.userData.value?.linkedIn?.isNotEmpty() == true)
        userInfo.add(vm.userData.value?.linkedIn!!)
    }
}