package com.deucate.pubgvictory.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import com.deucate.pubgvictory.HomeActivity
import com.deucate.pubgvictory.utils.Util
import com.deucate.pubgvictory.R
import com.deucate.pubgvictory.utils.Constatns
import com.deucate.pubgvictory.utils.User
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.alert_new_user.view.*
import java.lang.NullPointerException

class LoginActivity : AppCompatActivity() {

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    private lateinit var googleSignInClient: GoogleSignInClient
    private val signIn = 69

    private val util = Util(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (auth.currentUser != null) {
            startHomeActivity()
        }
        setContentView(R.layout.activity_login)

        googleSignInClient = GoogleSignIn.getClient(
                this, GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
        )

        findViewById<SignInButton>(R.id.signInButton).setOnClickListener {
            startActivityForResult(googleSignInClient.signInIntent, signIn)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            signIn -> {
                try {
                    val account = GoogleSignIn.getSignedInAccountFromIntent(data).getResult(ApiException::class.java)
                    signInToFirebase(account!!)
                } catch (e: ApiException) {
                    util.showAlertDialog("Error", e.localizedMessage)
                } catch (e: NullPointerException) {
                    util.showAlertDialog("Error", e.localizedMessage)
                }
            }
        }
    }

    private fun signInToFirebase(account: GoogleSignInAccount) {

        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        startHomeActivity()
                    } else {
                        util.showAlertDialog("Error", task.exception!!.localizedMessage)
                    }
                }
    }

    private fun checkExistingUser(uid: String) {
        db.collection(Constatns.users).document(uid).get().addOnCompleteListener {
            if (it.isSuccessful) {
                val result = it.result!!
                if (result.exists()) {
                    val user = result.toObject(User::class.java)
                } else {
                    newUser(uid)
                }
            } else {
                newUser(uid)
            }
        }
    }

    private fun newUser(uid: String) {

        val layout = LayoutInflater.from(this).inflate(R.layout.alert_new_user, null)

        val name = auth.currentUser!!.displayName!!
        layout.newUserName.text = SpannableStringBuilder(name)
        layout.newUserName.isEnabled = false

        val email = auth.currentUser!!.email
        if (email != null) {
            layout.newUserEmail.text = SpannableStringBuilder(email)
            layout.newUserEmail.isEnabled = false
        }

        val phoneNumber = auth.currentUser!!.phoneNumber
        if (phoneNumber != null) {
            layout.newUserPhone.text = SpannableStringBuilder(phoneNumber)
            layout.newUserPhone.isEnabled = false
        }

        AlertDialog.Builder(this).setView(layout).setPositiveButton("Next") { _, _ ->

            val userName = layout.newUserName.text.toString()
            val phone = layout.newUserPhone.text.toString()
            val emailAddress = layout.newUserEmail.text.toString()

            if (userName.isEmpty()){
                layout.newUserName.error = "Please enter name."
                return@setPositiveButton
            }
            if (phone.isEmpty()){
                layout.newUserPhone.error = "Please enter phone number."
                return@setPositiveButton
            }
            if (emailAddress.isEmpty()){
                layout.newUserEmail.error = "Please enter email address."
                return@setPositiveButton
            }

            val data = HashMap<String, Any>()
            data["Name"] = userName
            data["Email"] = emailAddress
            data["Phone"] = phone


            db.collection(Constatns.users).document(uid).set(data).addOnCompleteListener {
                if (it.isSuccessful){
                    startHomeActivity()
                }else{
                    util.showAlertDialog("Error",it.exception!!.localizedMessage)
                }
            }
        }.show()
    }

    private fun startHomeActivity() {
        startActivity(Intent(this, HomeActivity::class.java))
        finish()
    }
}
