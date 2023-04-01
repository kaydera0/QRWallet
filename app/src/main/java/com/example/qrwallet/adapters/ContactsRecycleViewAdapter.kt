package com.example.qrwallet.adapters

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
import com.example.qrwallet.dataClasses.PhoneContactDataClass

import com.example.qrwallet.databinding.PhoneContactLayoutBinding
import com.example.qrwallet.dialogs.QRCodeDialog
import com.example.qrwallet.temporaryUtils.QRCodeUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ContactsRecycleViewAdapter(var contactsList: ArrayList<PhoneContactDataClass>,val qrCodeUtils: QRCodeUtils,val fm : FragmentManager) : RecyclerView.Adapter<ContactsRecycleViewAdapter.ViewHolder>() {

    private lateinit var binding: PhoneContactLayoutBinding

    class ViewHolder(
        val binding: PhoneContactLayoutBinding,
        val qrCodeUtils: QRCodeUtils,
        val fm : FragmentManager
    ) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("ResourceAsColor", "SuspiciousIndentation")
        fun bind(contact: PhoneContactDataClass) {

            binding.name.text = contact.name
            binding.number.text = contact.number
            binding.qrButton.setOnClickListener {
                CoroutineScope(Dispatchers.Main).launch {
                    val QRCode = qrCodeUtils.getQRBitmap(contact)
                    QRCodeDialog(QRCode).show(fm, TRANSACTION_TAG)
                }
            }
//            binding.arrowButton.setOnClickListener {
//                if (binding.hiddenView.visibility == View.VISIBLE) {
//                    TransitionManager.beginDelayedTransition(binding.baseView, AutoTransition())
//                    binding.hiddenView.setVisibility(View.GONE)
//                    binding.arrowButton.setImageResource(android.R.drawable.arrow_down_float)
//
//                } else {
//                    TransitionManager.beginDelayedTransition(
//                        binding.baseView,
//                        AutoTransition()
//                    )
//                    binding.hiddenView.setVisibility(View.VISIBLE)
//                    binding.arrowButton.setImageResource(android.R.drawable.arrow_up_float)
//                    CoroutineScope(Dispatchers.Main).launch {
//                        val QRCode = qrCodeUtils.getQRBitmap(contact)
//                        binding.contactQRImage.setImageBitmap(QRCode)
//                        binding.contactQRImage.setOnClickListener {
//                            QRCodeDialog(QRCode).show(fm, TRANSACTION_TAG)
//                        }
//                    }
//                }
//            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        binding =
            PhoneContactLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding,qrCodeUtils,fm)
    }

    override fun getItemCount(): Int {
        return contactsList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(contactsList[position])
    }
    fun filterList(filterlist: ArrayList<PhoneContactDataClass>) {
        contactsList = filterlist
        notifyDataSetChanged()
    }
    companion object{
        val TRANSACTION_TAG = "FRAGMENT_TRANSACTION_TAG"
    }
}