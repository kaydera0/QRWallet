package com.example.qrwallet.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.example.qrwallet.databinding.QrCodeDialogLayoutBinding

class QRCodeDialog(val qrcode:Bitmap):DialogFragment() {

    private var _binding:QrCodeDialogLayoutBinding? = null
    private val binding get() = _binding!!

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        _binding = QrCodeDialogLayoutBinding.inflate(layoutInflater)
        binding.imageView.setImageBitmap(qrcode)

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
}