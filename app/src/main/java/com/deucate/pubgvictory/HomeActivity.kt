package com.deucate.pubgvictory

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.deucate.pubgvictory.account.AccountFragment
import com.deucate.pubgvictory.room.RoomFragment
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {

    private val title = MutableLiveData<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setSupportActionBar(toolbar)
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        title.observe(this, Observer {
            toolbar.title = it
        })
    }

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->

        var fragment: Fragment = HomeFragment()

        when (item.itemId) {
            R.id.navigation_home -> {
                fragment = HomeFragment()
                title.value = "Home"
            }
            R.id.navigation_search -> {
                title.value = "Search"
            }
            R.id.navigation_room -> {
                title.value = "Rooms"
                fragment = RoomFragment()
            }
            R.id.navigation_notifications -> {
                title.value = "Notifications"
            }
            R.id.navigation_account -> {
                title.value = "Profile"
                fragment = AccountFragment()
            }
        }

        supportFragmentManager.beginTransaction().replace(R.id.container, fragment, "").commit()

        return@OnNavigationItemSelectedListener true
    }

}
