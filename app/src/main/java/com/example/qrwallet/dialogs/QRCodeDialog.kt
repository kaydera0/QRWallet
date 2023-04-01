package com.example.qrwallet.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.DialogFragment
import com.example.qrwallet.databinding.QrCodeDialogLayoutBinding

class QRCodeDialog(val qrcode:Bitmap):DialogFragment() {

    private var _binding:QrCodeDialogLayoutBinding? = null
    private val binding get() = _binding!!

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        _binding = QrCodeDialogLayoutBinding.inflate(layoutInflater)
        val qrcode = addWhiteBorder(qrcode,10)
        binding.imageView.setImageBitmap(qrcode)
        binding.shareButton.setOnClickListener {

            val bitmapPath = MediaStore.Images.Media.insertImage(requireContext().contentResolver, qrcode, "palette", "share palette");
            val bitmapUri = Uri.parse(bitmapPath)
            val intent = Intent(Intent.ACTION_SEND)
            intent.putExtra(Intent.EXTRA_STREAM, bitmapUri)
            intent.setType("image/png")
            startActivity(intent)
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
    // Barcode scanner do not see QR code in edge to edge bitmap format
    private fun addWhiteBorder(bmp: Bitmap, borderSize: Int): Bitmap? {
        val bmpWithBorder =
            Bitmap.createBitmap(bmp.width + borderSize * 2, bmp.height + borderSize * 2, bmp.config)
        val canvas = Canvas(bmpWithBorder)
        canvas.drawColor(Color.WHITE)
        canvas.drawBitmap(bmp, borderSize.toFloat(), borderSize.toFloat(), null)
        return bmpWithBorder
    }
}