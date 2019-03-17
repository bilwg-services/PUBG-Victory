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
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.deucate.pubgvictory.MainViewModel
import com.deucate.pubgvictory.R
import com.deucate.pubgvictory.auth.LoginActivity
import com.deucate.pubgvictory.model.User
import com.deucate.pubgvictory.utils.Constatns
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_account.view.*
import net.glxn.qrgen.android.QRCode


class AccountFragment : Fragment() {

    lateinit var viewModel: MainViewModel

    private lateinit var user: User

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_account, container, false)

        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        this.user = viewModel.user.value!!
        viewModel.user.observe(this, Observer {
            this.user = it
            updateUI(rootView)
        })

        val qrCode = QRCode.from(user.ID).bitmap()
        rootView.qrCode.setImageBitmap(qrCode)
        rootView.authUID.text = user.ID

        rootView.accountAdditionalSettings.setOnClickListener {
            Toast.makeText(activity!!, "Start additional activity", Toast.LENGTH_SHORT).show()
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
        rootView.accountEmail.text = user.Email
        rootView.accountPhone.text = user.Phone

        Glide.with(this).load(FirebaseAuth.getInstance().currentUser!!.photoUrl)
            .into(rootView.accountProfilePicture)
    }


}
