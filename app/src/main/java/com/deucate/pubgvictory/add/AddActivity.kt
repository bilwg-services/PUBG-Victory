package com.deucate.pubgvictory.add

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.deucate.pubgvictory.R
import com.deucate.pubgvictory.ui.gallery.GalleryFragment

class AddActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, GalleryFragment.newInstance())
                    .commitNow()
        }
    }

}
