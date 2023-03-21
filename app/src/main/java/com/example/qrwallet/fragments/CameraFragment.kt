package com.example.qrwallet.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.CodeScannerView
import com.budiyev.android.codescanner.DecodeCallback
import com.example.qrwallet.R
import com.example.qrwallet.databinding.FragmentCameraBinding
import com.example.qrwallet.dialogs.NewContactDialog
import com.example.qrwallet.dialogs.QRCodeDialog
import com.example.qrwallet.repositories.QRdecoder
import com.example.qrwallet.viewModels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CameraFragment : Fragment() {

    private var _binding: FragmentCameraBinding? = null
    private val binding get() = _binding!!
    private lateinit var codeScanner: CodeScanner
    private val vm: MainViewModel by activityViewModels()
    private val newContactTag = "NewContactDialog_TAG"
    val qRdecoder = QRdecoder()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCameraBinding.inflate(layoutInflater)


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
                    findNavController().navigate(R.id.action_cameraFragment_to_userFragment)
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
}