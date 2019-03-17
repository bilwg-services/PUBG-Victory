package com.deucate.pubgvictory

import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)

        viewModel.error.observe(this, Observer { error ->
            if (error != null) {
                AlertDialog.Builder(this).setTitle("Error").setMessage(error)
                    .setPositiveButton("Ok") { _, _ -> }.show()
            }
        })

        val navController = findNavController(R.id.container)
        val bottom = findViewById<BottomNavigationView>(R.id.bottom_nav_view)

        bottom?.setupWithNavController(navController)
    }

}
