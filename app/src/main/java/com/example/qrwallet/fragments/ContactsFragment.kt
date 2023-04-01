package com.example.qrwallet.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.qrwallet.adapters.ContactsRecycleViewAdapter
import com.example.qrwallet.dataClasses.PhoneContactDataClass
import com.example.qrwallet.databinding.FragmentContactsBinding
import com.example.qrwallet.temporaryUtils.QRCodeUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ContactsFragment : Fragment() {

    @Inject
    lateinit var qrCodeUtils: QRCodeUtils
    private var _binding: FragmentContactsBinding? = null
    private val binding get() = _binding!!
    private var adapter: ContactsRecycleViewAdapter? = null
    private var contactModelArrayList: ArrayList<PhoneContactDataClass>? = null

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
            val contactModel = PhoneContactDataClass(name,phoneNumber,null)
            contactModelArrayList!!.add(contactModel)
        }
        adapter = ContactsRecycleViewAdapter(contactModelArrayList!!,qrCodeUtils,childFragmentManager)
        binding.recycleViewContactsFragment.adapter = adapter
        binding.recycleViewContactsFragment.layoutManager =  LinearLayoutManager(requireContext())
        phones.close()

        binding.searchButton.setOnClickListener {
            filter(binding.searchContacts.text.toString())
        }

        return binding.root
    }
    private fun filter(text: String) {
        // creating a new array list to filter our data.
        val filteredlist: ArrayList<PhoneContactDataClass> = ArrayList()

        // running a for loop to compare elements.
        for (item in contactModelArrayList!!) {
            // checking if the entered string matched with any item of our recycler view.
            if (item.name?.toLowerCase()?.contains(text.toLowerCase()) == true) {
                // if the item is matched we are
                // adding it to our filtered list.
                filteredlist.add(item)
            }
        }
        if (filteredlist.isEmpty()) {
            Toast.makeText(requireContext(), "No Data Found..", Toast.LENGTH_SHORT).show()
        } else {
            // at last we are passing that filtered
            // list to our adapter class.
            adapter?.filterList(filteredlist)
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding=null
    }
}