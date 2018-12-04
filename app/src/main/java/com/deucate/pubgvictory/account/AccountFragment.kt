package com.deucate.pubgvictory.account


import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bumptech.glide.Glide
import com.deucate.pubgvictory.R
import com.deucate.pubgvictory.auth.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_account.view.*

class AccountFragment : Fragment() {

    private val auth = FirebaseAuth.getInstance()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_account, container, false)

        val currentUser = auth.currentUser

        if (currentUser != null) {
            rootView.accountEmail.text = currentUser.email
            if (currentUser.phoneNumber == null || currentUser.phoneNumber == "") {
                rootView.accountPhone.text = getString(R.string.phone_not_found)
            } else {
                rootView.accountPhone.text = currentUser.phoneNumber
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

        return rootView
    }


}
