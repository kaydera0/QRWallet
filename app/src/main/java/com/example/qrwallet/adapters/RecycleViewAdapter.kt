package com.example.qrwallet.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
import com.example.qrwallet.dataClasses.UserCardData
import com.example.qrwallet.databinding.ContactCardLayoutBinding
import com.example.qrwallet.network.RequestsForQRCodes
import com.example.qrwallet.viewModels.MainViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class RecycleViewAdapter(val userCardDataArray: List<UserCardData>, val context: Context,val vm:MainViewModel) :
    RecyclerView.Adapter<RecycleViewAdapter.ViewHolder>() {

    private lateinit var binding: ContactCardLayoutBinding

    class ViewHolder(val binding: ContactCardLayoutBinding, val context: Context,val vm:MainViewModel) :
        RecyclerView.ViewHolder(binding.root) {

        val list = ArrayList<String>()

        @SuppressLint("ResourceAsColor", "SuspiciousIndentation")
        fun bind(userCardData: UserCardData) {

            binding.nameInCardView.text = userCardData.name
            binding.phoneInCardView.text = userCardData.phone
            CoroutineScope(Dispatchers.Main).launch {
                binding.userQR.setImageBitmap(RequestsForQRCodes().requestForQRCode(userCardData))
            }
            addToList(userCardData)

            binding.arrowButton.setOnClickListener {
                if (binding.hiddenView.visibility == View.VISIBLE) {
                    TransitionManager.beginDelayedTransition(binding.baseCardview, AutoTransition())
                    binding.hiddenView.setVisibility(View.GONE)
                    binding.arrowButton.setImageResource(android.R.drawable.arrow_down_float)
                } else {
                    TransitionManager.beginDelayedTransition(
                        binding.baseCardview,
                        AutoTransition()
                    );
                    binding.hiddenView.setVisibility(View.VISIBLE);
                    binding.arrowButton.setImageResource(android.R.drawable.arrow_up_float);
                }
            }
        }

        private fun addToList(userCardData: UserCardData) {
            if (userCardData.email.isNotEmpty()) {
                list.add(userCardData.email)
            }
            if (userCardData.address.isNotEmpty()) {
                list.add(userCardData.address)
            }
            if (userCardData.postCode.isNotEmpty()) {
                list.add(userCardData.postCode)
            }
            if (userCardData.facebook.isNotEmpty()) {
                list.add(userCardData.facebook)
            }
            if (userCardData.linkedIn.isNotEmpty()) {
                list.add(userCardData.linkedIn)
            }
            list.add("Delete Card")
            binding.listViewUser.adapter =
                ArrayAdapter(context, android.R.layout.simple_list_item_1, list)
            binding.listViewUser.setOnItemClickListener { parent, view, position, id ->
                if (list.size>0){
                if (position == list.size-1){
                    vm.removeCardUser(userCardData)
                }}

            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding =
            ContactCardLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, context,vm)
    }

    override fun getItemCount(): Int {
        return userCardDataArray.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(userCardDataArray[position])
    }
}


