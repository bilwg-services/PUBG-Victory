package com.deucate.pubgvictory.matches


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.deucate.pubgvictory.MainViewModel
import com.deucate.pubgvictory.R
import kotlinx.android.synthetic.main.fragment_matches.view.*

class MatchesFragment : Fragment() {
    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_matches, container, false)
        rootView.matchRecyclerView.layoutManager = LinearLayoutManager(context)

        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)

        viewModel.myEvents.observe(this, Observer {
            rootView.matchRecyclerView.adapter = MatchAdapter(it)
            if (it.isEmpty()) {
                rootView.matchErrorTV.visibility = View.VISIBLE
            } else {
                rootView.matchErrorTV.visibility = View.GONE
            }
        })

        return rootView
    }


}
