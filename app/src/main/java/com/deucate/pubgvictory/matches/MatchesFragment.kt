package com.deucate.pubgvictory.matches


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.deucate.pubgvictory.MainViewModel
import com.deucate.pubgvictory.R
import com.deucate.pubgvictory.model.Event

class MatchesFragment : Fragment() {

    private lateinit var viewModel: MainViewModel

    private var events = ArrayList<Event>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_matches, container, false)

        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)

        viewModel.myEvents.observe(this, Observer {
            events = it
        })

        return rootView
    }

}
