package com.example.qrwallet.fragments

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.qrwallet.R
import com.example.qrwallet.dataBase.room.RoomDB
import com.example.qrwallet.dataBase.room.RoomUser
import com.example.qrwallet.databinding.FragmentProfileBinding
import com.example.qrwallet.viewModels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    @Inject
    lateinit var roomDB: RoomDB
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private val vm: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val bundle = arguments?.getString("return")
        askPermission()
        vm.userData.observe(viewLifecycleOwner) {
            if (it != null && bundle != "return") {
                findNavController().navigate(R.id.action_profileFragment_to_mainFragment)
            }
        }
        feelFields()
        binding.doneCardBtn.setOnClickListener {
            if (binding.cardName.text.isEmpty() && binding.phoneCard.text.isEmpty()) {
                binding.cardName.setHintTextColor(resources.getColor(R.color.red))
                binding.cardName.hint = resources.getText(R.string.necessary_field)
                binding.phoneCard.setHintTextColor(resources.getColor(R.color.red))
                binding.phoneCard.hint = resources.getText(R.string.necessary_field)
            } else {
                val currentData = RoomUser(
                    id = 0,
                    name = binding.cardName.text.toString(),
                    phone = binding.phoneCard.text.toString(),
                    email = binding.emailCard.text.toString(),
                    address = binding.addressCard.text.toString(),
                    post = binding.postCodeCard.text.toString(),
                    facebook = binding.facebookCard.text.toString(),
                    linkedIn = binding.linkedinCard.text.toString()
                )
                CoroutineScope(Dispatchers.IO).launch {
                    if (bundle != "return") {
                        roomDB.userDao()?.insertUser(
                            currentData
                        )
                    } else {
                        roomDB.userDao()?.updateUser(currentData)
                    }
                    vm.updateUserdata()
                }
                findNavController().navigate(R.id.action_profileFragment_to_mainFragment)
            }
        }
        return binding.root
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
    private fun askPermission(){
        ActivityCompat.requestPermissions(requireActivity(),
            arrayOf(Manifest.permission.CAMERA,Manifest.permission.READ_CONTACTS,Manifest.permission.READ_EXTERNAL_STORAGE),
            1)
    }
    private fun feelFields(){
        binding.cardName.setText(vm.userData.value?.name)
        binding.phoneCard.setText(vm.userData.value?.phone)
        binding.emailCard.setText(vm.userData.value?.email)
        binding.addressCard.setText(vm.userData.value?.address)
        binding.postCodeCard.setText(vm.userData.value?.postCode)
        binding.facebookCard.setText(vm.userData.value?.facebook)
        binding.linkedinCard.setText(vm.userData.value?.linkedIn)
    }
}