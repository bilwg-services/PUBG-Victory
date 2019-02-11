package com.deucate.pubgvictory.account


import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.deucate.pubgvictory.R
import com.deucate.pubgvictory.auth.LoginActivity
import com.deucate.pubgvictory.utils.Constatns
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_account.view.*
import net.glxn.qrgen.android.QRCode


class AccountFragment : Fragment() {

    private val auth = FirebaseAuth.getInstance()

    private val email = MutableLiveData<String>()
    private val phone = MutableLiveData<String>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_account, container, false)

        val qrCode = QRCode.from(auth.uid!!).bitmap()
        rootView.qrCode.setImageBitmap(qrCode)
        rootView.authUID.text = auth.uid!!


        email.observe(this, Observer {
            rootView.accountEmail.text = it!!
        })

        phone.observe(this, Observer {
            rootView.accountPhone.text = it!!
        })

        val currentUser = auth.currentUser

        if (currentUser != null) {
            email.value = currentUser.email
            if (currentUser.phoneNumber == null || currentUser.phoneNumber == "") {
                phone.value = getString(R.string.phone_not_found)
            } else {
                phone.value = currentUser.phoneNumber
            }
            rootView.accountName.text = currentUser.displayName
            Glide.with(this).load(currentUser.photoUrl).into(rootView.accountProfilePicture)
        } else {
            startActivity(Intent(activity, LoginActivity::class.java))
            activity!!.finish()
        }

        rootView.accountAdditionalSettings.setOnClickListener {
            Toast.makeText(activity!!, "Start additional activity", Toast.LENGTH_SHORT).show()
        }

        rootView.logoutButton.setOnClickListener {
            auth.signOut()
            startActivity(Intent(activity, LoginActivity::class.java))
            activity!!.finish()
        }

        FirebaseFirestore.getInstance().collection(Constatns.users).document(currentUser!!.uid).get().addOnCompleteListener {
            if (it.isSuccessful) {
                val dbEmail = it.result!!.getString("Email")
                val dbMobileNumber = it.result!!.getString("Phone")

                if (!dbEmail.isNullOrEmpty()) {
                    email.value = dbMobileNumber
                }
                if (!dbMobileNumber.isNullOrEmpty()) {
                    email.value = dbEmail
                }
            } else {
                Toast.makeText(activity!!, it.exception!!.localizedMessage, Toast.LENGTH_LONG).show()
            }
        }

        return rootView
    }


}
