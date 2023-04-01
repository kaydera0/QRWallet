package com.example.qrwallet.fragments

import android.Manifest
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.qrwallet.R
import com.example.qrwallet.adapters.PageAdapter
import com.example.qrwallet.databinding.FragmentMainBinding
import com.example.qrwallet.viewModels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private val vm:MainViewModel by activityViewModels()
    private val fragments = mutableListOf(CameraFragment(),UserAndFavFragment(),ContactsFragment())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMainBinding.inflate(inflater, container, false)

        setAdapter()
        askPermission()

        vm.userData.observe(viewLifecycleOwner){
            Log.d("MY_TAG","IT -----.... $it")
            if (it==null){
                findNavController().navigate(R.id.action_mainFragment_to_profileFragment)
            }
        }

        return binding.root
    }
    private fun setAdapter() {
        binding.viewPagerMainFragment.adapter = PageAdapter(childFragmentManager, fragments, lifecycle)
        binding.viewPagerMainFragment.setCurrentItem(1,false)
    }
    private fun askPermission(){
        ActivityCompat.requestPermissions(requireActivity(),
            arrayOf(Manifest.permission.CAMERA, Manifest.permission.READ_CONTACTS, Manifest.permission.READ_EXTERNAL_STORAGE),
            1)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding=null
    }
}