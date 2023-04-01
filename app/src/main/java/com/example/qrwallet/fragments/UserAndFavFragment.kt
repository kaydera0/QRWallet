package com.example.qrwallet.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Configuration
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
import com.example.qrwallet.adapters.FavoritesRecycleViewAdapter
import com.example.qrwallet.dataBase.room.RoomDB
import com.example.qrwallet.dataClasses.UserCardData
import com.example.qrwallet.databinding.FragmentUserAndFavBinding
import com.example.qrwallet.dialogs.QRCodeDialog
import com.example.qrwallet.temporaryUtils.QRCodeUtils
import com.example.qrwallet.viewModels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import java.util.*
import javax.inject.Inject


@AndroidEntryPoint
class UserAndFavFragment : Fragment() {

    @Inject
    lateinit var roomDB: RoomDB

    @Inject
    lateinit var qrCodeUtils: QRCodeUtils
    private var _binding: FragmentUserAndFavBinding? = null
    private val binding get() = _binding!!
    private val vm: MainViewModel by activityViewModels()
    private var userQrCodeBitmap: Bitmap? = null
    private val dialogTag = "DIALOG_QR_CODE"
    private lateinit var adapter: FavoritesRecycleViewAdapter
    private var favContactsCardArr = ArrayList<UserCardData>()


    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUserAndFavBinding.inflate(inflater, container, false)
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


        binding.included.userQR.setOnClickListener {
            QRCodeDialog(userQrCodeBitmap!!).show(childFragmentManager, dialogTag)
        }
        binding.included.settingsConstraint.visibility = View.GONE

//        binding.button.setOnClickListener {
//            val localeListToSet = Locale("ru")
//            Locale.setDefault(localeListToSet)
//            val config = resources.configuration
//            config.setLocale(localeListToSet)
//            resources.updateConfiguration(config, resources.displayMetrics)
//            onConfigurationChanged(config)
//        }
        binding.settingsButton.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_settingsFragment)
        }


        return binding.root
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
//        binding.setText(R.string.done)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun setQrCode(userCardData: UserCardData) {
        CoroutineScope(Dispatchers.Main).launch {
            userQrCodeBitmap = qrCodeUtils.getQRBitmap(userCardData)
            binding.included.userQR.setImageBitmap(userQrCodeBitmap)
        }
    }

    private fun setExpandedCardSettings() {
        binding.included.arrowButton.setOnClickListener {
            if (binding.included.hiddenView.visibility == View.VISIBLE) {
                TransitionManager.beginDelayedTransition(
                    binding.included.baseCardview,
                    AutoTransition()
                )
                binding.included.hiddenView.setVisibility(View.GONE)
                binding.included.arrowButton.setImageResource(android.R.drawable.arrow_down_float)
            } else {
                TransitionManager.beginDelayedTransition(
                    binding.included.baseCardview,
                    AutoTransition()
                );
                binding.included.hiddenView.setVisibility(View.VISIBLE);
                binding.included.arrowButton.setImageResource(android.R.drawable.arrow_up_float);
            }
        }
    }

    @SuppressLint("SuspiciousIndentation", "IntentReset")
    private fun addFieldToCard() {
        binding.included.nameInCardView.text = vm.userData.value?.name
        binding.included.phoneInCardView.text = vm.userData.value?.phone
        if (vm.userData.value?.email?.isNotEmpty() == true) {
            binding.included.userEmailLinear.visibility = View.VISIBLE
            binding.included.userEmail.text = vm.userData.value?.email
            binding.included.userEmail.setOnClickListener {
                val mailIntent = Intent(Intent.ACTION_SEND)
                mailIntent.data = Uri.parse("mailto:")
                mailIntent.type = "text/plain"
                mailIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(vm.userData.value?.email))
                startActivity(Intent.createChooser(mailIntent, "choose email app"))
            }
        } else
            binding.included.userEmailLinear.visibility = View.GONE
        if (vm.userData.value?.address?.isNotEmpty() == true) {
            binding.included.userAddressLinear.visibility = View.VISIBLE
            binding.included.userAddress.text = vm.userData.value?.address
            binding.included.userAddress.setOnClickListener {
                val place = vm.userData.value?.address
                val googleMapUrl = "https://www.google.com/maps/place/$place"
                val mapIntent = Intent(Intent.ACTION_VIEW).setData(Uri.parse(googleMapUrl))
                startActivity(mapIntent)
            }
        } else
            binding.included.userAddressLinear.visibility = View.GONE
        if (vm.userData.value?.postCode?.isNotEmpty() == true) {
            binding.included.userPostCode.text = vm.userData.value?.postCode
            binding.included.userPostCodeLinear.visibility = View.VISIBLE
        } else
            binding.included.userPostCodeLinear.visibility = View.GONE
        if (vm.userData.value?.facebook?.isNotEmpty() == true) {
            binding.included.userFacebook.text = vm.userData.value?.facebook
            binding.included.userFacebookLinear.visibility = View.VISIBLE
            binding.included.userFacebookLinear.setOnClickListener {
                val facebookIntent =
                    Intent(Intent.ACTION_VIEW).setData(Uri.parse(vm.userData.value?.facebook))
                startActivity(facebookIntent)
            }
        } else
            binding.included.userFacebookLinear.visibility = View.GONE
        if (vm.userData.value?.linkedIn?.isNotEmpty() == true) {
            binding.included.userLinkedInLinear.visibility = View.VISIBLE
            binding.included.userLinkedIn.text = vm.userData.value?.linkedIn
            binding.included.userLinkedIn.setOnClickListener {
                val linkedInIntent =
                    Intent(Intent.ACTION_VIEW).setData(Uri.parse(vm.userData.value?.linkedIn))
                startActivity(linkedInIntent)
            }

        } else
            binding.included.userLinkedInLinear.visibility = View.GONE
    }

    private fun setRecycleViewAdapter() {
        vm.favUsersArr.observe(viewLifecycleOwner) {
            favContactsCardArr = it
            binding.recycleViewFav.adapter =
                FavoritesRecycleViewAdapter(
                    it,
                    requireContext(),
                    childFragmentManager,
                    qrCodeUtils,
                    vm
                )
        }
        adapter = FavoritesRecycleViewAdapter(
            favContactsCardArr,
            requireContext(),
            childFragmentManager,
            qrCodeUtils,
            vm
        )
        binding.recycleViewFav.adapter = adapter
        binding.recycleViewFav.layoutManager = LinearLayoutManager(requireContext())
    }
}