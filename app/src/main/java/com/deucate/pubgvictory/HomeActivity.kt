package com.deucate.pubgvictory

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.deucate.pubgvictory.account.AccountFragment
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
    }

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->

        var fragment: Fragment = HomeFragment()

        when (item.itemId) {
            R.id.navigation_home -> {
                fragment = HomeFragment()
            }
            R.id.navigation_search -> {
            }
            R.id.navigation_add -> {
            }
            R.id.navigation_notifications -> {
            }
            R.id.navigation_account -> {
                fragment = AccountFragment()
            }
        }

        supportFragmentManager.beginTransaction().replace(R.id.container, fragment, "").commit()

        return@OnNavigationItemSelectedListener true
    }

}
