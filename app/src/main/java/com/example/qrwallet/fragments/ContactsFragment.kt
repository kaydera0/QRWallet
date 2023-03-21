package com.example.qrwallet.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import com.example.qrwallet.R
import com.example.qrwallet.adapters.ContactsAdapter
import com.example.qrwallet.dataClasses.ContactDataClass
import com.example.qrwallet.databinding.FragmentContactsBinding
import com.example.qrwallet.databinding.FragmentProfileBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ContactsFragment : Fragment() {

    private var _binding: FragmentContactsBinding? = null
    private val binding get() = _binding!!
    private var listView: ListView? = null
    private var customAdapter: ContactsAdapter? = null
    private var contactModelArrayList: ArrayList<ContactDataClass>? = null

    @SuppressLint("Range")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentContactsBinding.inflate(layoutInflater)


        contactModelArrayList = ArrayList()

        val phones = context?.contentResolver?.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC")
        while (phones!!.moveToNext()) {
            val name = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
            val phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))

            val contactModel = ContactDataClass()
            contactModel.setNames(name)
            contactModel.setNumbers(phoneNumber)
            contactModelArrayList!!.add(contactModel)
            Log.d("name>>", name + "  " + phoneNumber)
        }
        phones.close()

        customAdapter = ContactsAdapter(requireContext(), contactModelArrayList!!)
        binding.contactsList.adapter = customAdapter

        return binding.root
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding=null
    }
}