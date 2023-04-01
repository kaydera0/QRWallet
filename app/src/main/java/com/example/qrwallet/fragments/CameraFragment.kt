package com.example.qrwallet.fragments

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.CodeScannerView
import com.budiyev.android.codescanner.DecodeCallback
import com.example.qrwallet.R
import com.example.qrwallet.dataBase.room.RoomDB
import com.example.qrwallet.databinding.FragmentCameraBinding
import com.example.qrwallet.dialogs.NewContactDialog
import com.example.qrwallet.repositories.QRDecoder
import com.example.qrwallet.viewModels.MainViewModel
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CameraFragment  : Fragment() {

    @Inject
    lateinit var roomDB: RoomDB
    private var _binding: FragmentCameraBinding? = null
    private val binding get() = _binding!!
    private lateinit var codeScanner: CodeScanner
    private val vm: MainViewModel by activityViewModels()
    private val newContactTag = "NewContactDialog_TAG"
    val qRdecoder = QRDecoder()
    private var imageUri: Uri? = null
    private var image = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCameraBinding.inflate(layoutInflater)

        binding.upload.setOnClickListener {
        pickImageGallery()
        }

        return _binding!!.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val scannerView = view.findViewById<CodeScannerView>(R.id.scanner_view)
        val activity = requireActivity()
        codeScanner = CodeScanner(activity, scannerView)
        codeScanner.decodeCallback = DecodeCallback {
            activity.runOnUiThread {
                try {
                    val result = qRdecoder.codeToDataClass(it.text)
                    NewContactDialog(result).show(childFragmentManager,newContactTag)
                } catch (e: java.lang.Exception) {
                    Toast.makeText(activity, R.string.scan_error, Toast.LENGTH_LONG).show()
                }
            }
        }
        scannerView.setOnClickListener {
            codeScanner.startPreview()
        }
    }

    override fun onResume() {
        super.onResume()
        codeScanner.startPreview()
    }

    override fun onPause() {
        codeScanner.releaseResources()
        super.onPause()
    }
    private fun pickImageGallery(){
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        galleryActivityResultLauncher.launch(intent)
    }

    private val galleryActivityResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){result->
        Log.d("MY_TAG"," in galleryActivityResultLauncher")
        if (result.resultCode == Activity.RESULT_OK){
            val data = result.data
            Log.d("MY_TAG"," in galleryActivityResultLauncher block if")
            imageUri = data?.data
            var image = InputImage.fromFilePath(requireContext(), imageUri!!)
            testScan(image)
        }
        else{
            // TODO:
        }

    }
    fun testScan(image:InputImage){
        val barcodeScannerOptions =BarcodeScannerOptions.Builder().setBarcodeFormats(Barcode.FORMAT_QR_CODE).build()
        val scanner = BarcodeScanning.getClient(barcodeScannerOptions)
        val result = scanner.process(image)
            .addOnSuccessListener { barcodes ->
                for (barcode in barcodes) {
                    val rawValue = barcode.rawValue
                    Log.d("RESULT_TAG", ">>>>>${rawValue.toString()} <<<<<<<<<<")
                    val result = qRdecoder.codeToDataClass(rawValue.toString())
                    NewContactDialog(result).show(childFragmentManager,newContactTag)
                }
            }
            .addOnFailureListener {
                // TODO:
            }
    }
}