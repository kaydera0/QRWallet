package com.example.qrwallet.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.adapter.FragmentViewHolder
import com.example.qrwallet.fragments.CameraFragment
import com.example.qrwallet.fragments.ContactsFragment
import com.example.qrwallet.fragments.MainFragment
import javax.inject.Inject

class PageAdapter(
    fragmentActivity: FragmentManager,
    val fragments : MutableList<Fragment>,
    lifecycle: Lifecycle
) : FragmentStateAdapter(fragmentActivity, lifecycle) {

    override fun getItemCount(): Int {
        return fragments.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }

    override fun getItemId(position: Int): Long {
        return fragments[position].hashCode().toLong()
    }

    override fun containsItem(itemId: Long): Boolean {
        return fragments.find { it.hashCode().toLong() == itemId } != null
    }

    override fun onBindViewHolder(
        holder: FragmentViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        super.onBindViewHolder(holder, position, payloads)
    }
}