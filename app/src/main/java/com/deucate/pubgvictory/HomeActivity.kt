package com.deucate.pubgvictory

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.deucate.pubgvictory.account.AccountFragment
import com.deucate.pubgvictory.home.HomeFragment
import com.deucate.pubgvictory.matches.MatchesFragment
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {

    private val currentFragment = MutableLiveData<Fragment>()
    private var currentFragmentID = 8080

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        currentFragment.observe(this, Observer {
            it.let { fragment ->
                if (currentFragmentID != fragment.id) {
                    supportFragmentManager.beginTransaction().replace(R.id.container, fragment, "").commit()
                    currentFragmentID = fragment.id
                }
            }
        })

        navigation.selectedItemId = R.id.navigation_home

    }

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->

        when (item.itemId) {
            R.id.navigation_home -> {
                currentFragment.value = HomeFragment()
            }
            R.id.navigation_search -> {
                currentFragment.value = SearchFragment()
            }
            R.id.navigation_room -> {
                currentFragment.value = MatchesFragment()
            }
            R.id.navigation_notifications -> {
                currentFragment.value = NotificationFragment()
            }
            R.id.navigation_account -> {
                currentFragment.value = AccountFragment()
            }
        }

        return@OnNavigationItemSelectedListener true
    }

}
