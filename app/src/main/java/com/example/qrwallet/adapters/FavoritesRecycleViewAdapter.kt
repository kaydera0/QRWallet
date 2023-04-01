package com.example.qrwallet.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
import com.example.qrwallet.dataClasses.UserCardData
import com.example.qrwallet.databinding.MainUserCardBinding
import com.example.qrwallet.dialogs.QRCodeDialog
import com.example.qrwallet.temporaryUtils.QRCodeUtils
import com.example.qrwallet.viewModels.MainViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class FavoritesRecycleViewAdapter(val userCardDataArray: List<UserCardData>, val context: Context, val fm : FragmentManager, val qrCodeUtils: QRCodeUtils, val vm : MainViewModel) :
    RecyclerView.Adapter<FavoritesRecycleViewAdapter.ViewHolder>() {

    private lateinit var binding: MainUserCardBinding

    class ViewHolder(val binding: MainUserCardBinding, val context: Context,val fm : FragmentManager,val qrCodeUtils: QRCodeUtils, val vm : MainViewModel) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("ResourceAsColor", "SuspiciousIndentation")
        fun bind(userCardData: UserCardData) {
            Log.d("MY_TAG",userCardData.toString() + " userCardData from bind ")
            addFieldToCard(userCardData)
            CoroutineScope(Dispatchers.Main).launch {
                val QRCode = qrCodeUtils.getQRBitmap(userCardData)
                binding.userQR.setImageBitmap(QRCode)
                binding.userQR.setOnClickListener {
                    QRCodeDialog(QRCode).show(fm, TRANSACTION_TAG)
                }
            }
            binding.arrowButton.setOnClickListener {
                if (binding.hiddenView.visibility == View.VISIBLE) {
                    TransitionManager.beginDelayedTransition(binding.baseCardview, AutoTransition())
                    binding.hiddenView.setVisibility(View.GONE)
                    binding.arrowButton.setImageResource(android.R.drawable.arrow_down_float)
                } else {
                    TransitionManager.beginDelayedTransition(
                        binding.baseCardview,
                        AutoTransition()
                    );
                    binding.hiddenView.setVisibility(View.VISIBLE);
                    binding.arrowButton.setImageResource(android.R.drawable.arrow_up_float);
                }
            }
            binding.deletecard.setOnClickListener {
                vm.removeCardUser(userCardData)
            }
        }
        private fun addFieldToCard(userCardData: UserCardData) {
            binding.nameInCardView.text = userCardData.name
            binding.phoneInCardView.text = userCardData.phone
            if (userCardData.email?.isNotEmpty() == true) {
                binding.userEmailLinear.visibility = View.VISIBLE
                binding.userEmail.text = userCardData.email
                binding.userEmail.setOnClickListener {
                    val mailIntent = Intent(Intent.ACTION_SEND)
                    mailIntent.data = Uri.parse("mailto:")
                    mailIntent.type = "text/plain"
                    mailIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(userCardData.email))
                    startActivity(context,Intent.createChooser(mailIntent, "choose email app"),null)
                }
            } else
                binding.userEmailLinear.visibility = View.GONE
            if (userCardData.address.isNotEmpty()) {
                binding.userAddressLinear.visibility = View.VISIBLE
                binding.userAddress.text = userCardData.address
                binding.userAddress.setOnClickListener {
                    val place = userCardData.address
                    val googleMapUrl = "https://www.google.com/maps/place/$place"
                    val mapIntent = Intent(Intent.ACTION_VIEW).setData(Uri.parse(googleMapUrl))
                    startActivity(context,mapIntent,null)
                }
            } else
                binding.userAddressLinear.visibility = View.GONE
            if (userCardData.postCode.isNotEmpty()) {
                binding.userPostCode.text = userCardData.postCode
                binding.userPostCodeLinear.visibility = View.VISIBLE
            } else
                binding.userPostCodeLinear.visibility = View.GONE
            if (userCardData.facebook.isNotEmpty()) {
                binding.userFacebook.text = userCardData.facebook
                binding.userFacebookLinear.visibility = View.VISIBLE
                binding.userFacebookLinear.setOnClickListener {
                val facebookIntent =
                    Intent(Intent.ACTION_VIEW).setData(Uri.parse(userCardData.facebook))
                startActivity(context,facebookIntent,null)
                                }
            } else
                binding.userFacebookLinear.visibility = View.GONE
            if (userCardData.linkedIn.isNotEmpty()) {
                binding.userLinkedInLinear.visibility = View.VISIBLE
                binding.userLinkedIn.text = userCardData.linkedIn
                binding.userLinkedIn.setOnClickListener {
                    val linkedInIntent =
                        Intent(Intent.ACTION_VIEW).setData(Uri.parse(userCardData.linkedIn))
                startActivity(context,linkedInIntent,null)
                }
            } else
                binding.userLinkedInLinear.visibility = View.GONE
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding =
            MainUserCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, context,fm,qrCodeUtils,vm)
    }

    override fun getItemCount(): Int {
        return userCardDataArray.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(userCardDataArray[position])
    }
    companion object{
        val TRANSACTION_TAG = "FRAGMENT_TRANSACTION_TAG"
    }
}


