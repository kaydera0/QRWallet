package com.example.qrwallet.fragments

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.qrwallet.R
import com.example.qrwallet.databinding.FragmentSettingsBinding
import java.util.*

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    private var languageList = ArrayList<String>()
    private lateinit var sp:SharedPreferences


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSettingsBinding.inflate(layoutInflater)
        sp = requireContext().getSharedPreferences("LANG",Context.MODE_PRIVATE)
        languageList.add(resources.getString(R.string.english))
        languageList.add(resources.getString(R.string.russian))
        languageList.add(resources.getString(R.string.ukrainian))

        val adapter = ArrayAdapter(
            requireContext(),
            R.layout.spinner_language_layout,
            languageList
        )
        binding.spinner.adapter = adapter

        binding.spinner.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View, position: Int, id: Long
            ) {
                val localeListToSet = when (position) {
                    0 -> Locale("en")
                    1 -> Locale("ru")
                    else -> Locale("uk")
                }
                Locale.setDefault(localeListToSet)
                val config = resources.configuration
                config.setLocale(localeListToSet)
                resources.updateConfiguration(config, resources.displayMetrics)
                onConfigurationChanged(config)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // write code to perform some action
            }
        }
        binding.backButton.setOnClickListener {
            findNavController().navigate(R.id.action_settingsFragment_to_mainFragment)

        }

        return binding.root
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        binding.languageText.setText(R.string.language)

        Log.d("SP_TAG",newConfig.locales.toLanguageTags())
        sp.edit().putString("lang",newConfig.locales.toLanguageTags()).apply()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}