package com.deucate.pubgvictory.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.deucate.pubgvictory.model.Room
import java.util.*

class HomeViewPagerAdapter(private val rooms: ArrayList<Room>?, fragmentManager: FragmentManager) : FragmentStatePagerAdapter(fragmentManager) {

    override fun getItem(position: Int): Fragment {
        return RoomCardFragment().also {
            it.arguments = Bundle().also { bundle ->
                bundle.putSerializable("room", rooms!![position] )
            }
        }
    }

    override fun getCount(): Int = rooms?.size ?: 0

}