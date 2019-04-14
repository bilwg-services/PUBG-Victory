package com.deucate.pubgvictory.account


import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.deucate.pubgvictory.MainViewModel
import com.deucate.pubgvictory.R
import com.deucate.pubgvictory.auth.LoginActivity
import com.deucate.pubgvictory.model.User
import com.deucate.pubgvictory.settings.SettingsActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_account.view.*
import net.glxn.qrgen.android.QRCode


class AccountFragment : Fragment() {

    private lateinit var viewModel: MainViewModel

    private lateinit var user: User

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_account, container, false)

        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        this.user = viewModel.user.value!!
        viewModel.user.observe(this, Observer {
            this.user = it
            updateUI(rootView)
        })

        val qrCode = QRCode.from(user.ID).withSize(250,250).bitmap()
        rootView.qrCode.setImageBitmap(qrCode)
        rootView.authUID.text = user.ID

        rootView.accountAdditionalSettings.setOnClickListener {
            activity?.startActivity(Intent(context,SettingsActivity::class.java))
        }

        rootView.logoutButton.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(activity, LoginActivity::class.java))
            activity!!.finish()
        }

        return rootView
    }

    private fun updateUI(rootView: View) {
        rootView.accountName.text = user.Name
        rootView.accountPhone.text = user.Phone

        Glide.with(this).load(FirebaseAuth.getInstance().currentUser!!.photoUrl)
                .into(rootView.accountProfilePicture)
    }
}
