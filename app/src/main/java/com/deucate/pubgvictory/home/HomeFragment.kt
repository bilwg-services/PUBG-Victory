package com.deucate.pubgvictory.home


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.deucate.pubgvictory.R
import com.deucate.pubgvictory.utils.Constatns
import com.deucate.pubgvictory.utils.Util
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_home.view.*

class HomeFragment : Fragment() {

    private val rooms = ArrayList<Room>()
    private val roomAdapter = RoomAdapter(rooms)

    private val roomDB = FirebaseFirestore.getInstance().collection(Constatns.rooms)
    private lateinit var util: Util

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_home, container, false)
        util = Util(activity!!)

        val recyclerView = rootView.roomRecyclerView
        recyclerView.adapter = roomAdapter
        recyclerView.layoutManager = LinearLayoutManager(activity)

        roomAdapter.listener = object : RoomAdapter.RoomCardClickListener {
            override fun onClickCard(room: Room) {
                val intent = Intent(activity,RoomActivity::class.java)
                intent.putExtra(Constatns.rooms,room)
                startActivity(intent)
            }
        }

        roomDB.get().addOnCompleteListener {
            if (it.isSuccessful) {
                for (doc in it.result!!.documents) {
                    rooms.add(doc.toObject(Room::class.java)!!)
                }
                roomAdapter.notifyDataSetChanged()
            } else {
                util.showAlertDialog("Error", it.exception!!.localizedMessage)
            }
        }

        return rootView
    }

}