package com.deucate.pubgvictory.home


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.deucate.pubgvictory.MainViewModel
import com.deucate.pubgvictory.R
import com.deucate.pubgvictory.model.Room
import com.deucate.pubgvictory.utils.Constatns
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_home.view.*

class HomeFragment : Fragment() {

    private lateinit var viewModel: MainViewModel
    private lateinit var adapter: RoomAdapter

    private var rooms = ArrayList<Room>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_home, container, false)

        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)

        viewModel.rooms.observe(this, Observer { rooms ->
            this.rooms = rooms
            adapter.notifyDataSetChanged()
        })

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
