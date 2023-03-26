package com.example.qrwallet.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

        vm.userData.observe(viewLifecycleOwner) {
            if (it != null && bundle != "return") {
                findNavController().navigate(R.id.action_profileFragment_to_mainFragment)
            }
        }

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
}