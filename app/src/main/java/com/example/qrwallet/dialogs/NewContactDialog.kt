package com.example.qrwallet.dialogs

import android.R
import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
import com.example.qrwallet.dataClasses.UserCardData
import com.example.qrwallet.databinding.NewCaontactDialogLayoutBinding
import com.example.qrwallet.viewModels.MainViewModel

class NewContactDialog(val userCardData: UserCardData):DialogFragment() {

    private var _binding: NewCaontactDialogLayoutBinding? = null
    private val binding get() = _binding!!
    private val userInfo = ArrayList<String>()
    val vm:MainViewModel by activityViewModels()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        _binding = NewCaontactDialogLayoutBinding.inflate(layoutInflater)

        setExpandedCardSwettings()

        binding.nameInCardView.text = userCardData.name
        binding.phoneInCardView.text = userCardData.phone
        addFieldToCard()
        binding.listViewUser.adapter =
            ArrayAdapter(requireContext(), R.layout.simple_list_item_1, userInfo)

        binding.decline.setOnClickListener {
            dismiss()
        }
        binding.save.setOnClickListener {
            dismiss()
        }
        binding.saveToFav.setOnClickListener {
            vm.addUserCard(userCardData)
            dismiss()
        }


        val builder = AlertDialog.Builder(requireActivity())
        builder.setView(binding.root)
        val dialog = builder.create()
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return dialog
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
    private fun addFieldToCard(){
        if (userCardData.email.isNotEmpty())
            userInfo.add(userCardData.email)
        if (userCardData.address.isNotEmpty())
            userInfo.add(userCardData.address)
        if (userCardData.postCode.isNotEmpty())
            userInfo.add(userCardData.postCode)
        if (userCardData.facebook.isNotEmpty())
            userInfo.add(userCardData.facebook)
        if (userCardData.linkedIn.isNotEmpty())
            userInfo.add(userCardData.linkedIn)
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
}