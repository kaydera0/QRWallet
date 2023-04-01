package com.example.qrwallet.activities

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.qrwallet.databinding.ActivityMainBinding
import com.example.qrwallet.viewModels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val vm: MainViewModel by viewModels()
    private lateinit var sp: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sp = getSharedPreferences("LANG", Context.MODE_PRIVATE)
        val localeListToSet = Locale(sp.getString("lang","en")!!)
        Locale.setDefault(localeListToSet)
        val config = resources.configuration
        config.setLocale(localeListToSet)
        resources.updateConfiguration(config, resources.displayMetrics)
        onConfigurationChanged(config)
    }
}