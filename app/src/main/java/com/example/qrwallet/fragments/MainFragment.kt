package com.example.qrwallet.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
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

        return binding.root
    }
    private fun setAdapter() {
        binding.viewPagerMainFragment.adapter = PageAdapter(childFragmentManager, fragments, lifecycle)
        binding.viewPagerMainFragment.setCurrentItem(1,false)


    }

    override fun onDestroy() {
        super.onDestroy()
        _binding=null
    }
}