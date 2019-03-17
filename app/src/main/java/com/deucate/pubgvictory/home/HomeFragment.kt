package com.deucate.pubgvictory.home


import android.content.Intent
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.deucate.pubgvictory.MainViewModel
import com.deucate.pubgvictory.R
import com.deucate.pubgvictory.model.Room
import com.deucate.pubgvictory.utils.Constatns
import kotlinx.android.synthetic.main.fragment_home.view.*

class HomeFragment : Fragment() {

    private lateinit var viewModel: MainViewModel
    private lateinit var adapter: RoomAdapter


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_home, container, false)

        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)

        viewModel.rooms.observe(this, Observer {
            adapter.notifyDataSetChanged()
        })

        val searchEditText = rootView.searchEditText
        searchEditText.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                if (event.rawX >= (searchEditText.right - searchEditText.compoundDrawables[Constatns.DRAWABLE_RIGHT].bounds.width())) {
                    searchEditText.text = SpannableStringBuilder("")
                    return@setOnTouchListener true
                }
                if (event.rawX <= (searchEditText.right - searchEditText.compoundDrawables[Constatns.DRAWABLE_LEFT].bounds.width())) {
                    Toast.makeText(context, "Search", Toast.LENGTH_SHORT).show()
                    return@setOnTouchListener true
                }
            }
            return@setOnTouchListener false
        }

        adapter = RoomAdapter(viewModel.rooms.value)
        rootView.roomRecyclerView.adapter = adapter
        rootView.roomRecyclerView.layoutManager = LinearLayoutManager(activity)

        adapter.listener = object : RoomAdapter.RoomCardClickListener {
            override fun onClickCard(room: Room) {
                val intent = Intent(activity, RoomActivity::class.java)
                intent.putExtra(Constatns.rooms, room)
                startActivity(intent)
            }
        }


        return rootView
    }

}
