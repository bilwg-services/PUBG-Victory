package com.deucate.pubgvictory.search

import android.content.Intent
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.deucate.pubgvictory.R
import com.deucate.pubgvictory.home.RoomActivity
import com.deucate.pubgvictory.home.RoomAdapter
import com.deucate.pubgvictory.model.Room
import com.deucate.pubgvictory.utils.Constatns
import com.deucate.pubgvictory.utils.Util
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_search.view.*

class SearchFragment : Fragment() {

    private val db = FirebaseFirestore.getInstance()
    private lateinit var util: Util

    private val rooms = MutableLiveData<ArrayList<Room>>()

    private lateinit var roomAdapter: RoomAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_search, container, false)

        util = Util(activity!!)

        rooms.value = ArrayList()
        roomAdapter = RoomAdapter(rooms.value!!)
        val recyclerView = rootView.searchRecyclerView
        recyclerView.adapter = roomAdapter
        recyclerView.layoutManager = LinearLayoutManager(activity)

        roomAdapter.listener = object : RoomAdapter.RoomCardClickListener {
            override fun onClickCard(room: Room) {
                val intent = Intent(activity, RoomActivity::class.java)
                intent.putExtra(Constatns.rooms, room)
                startActivity(intent)
            }
        }

        rootView.searchEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == KeyEvent.KEYCODE_CALL) {
                searchRoom(rootView.searchEditText.text.toString())
                rootView.searchEditText.text = SpannableStringBuilder("")
            }
            true
        }

        return rootView
    }

    private fun searchRoom(name: String) {
        db.collection("Rooms").orderBy("Title").startAt(name).endAt(name + '\uf8ff').get()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    for (doc in it.result!!.documents) {
                        rooms.value!!.add(doc.toObject(Room::class.java)!!)
                    }
                    roomAdapter.notifyDataSetChanged()
                } else {
                    util.showAlertDialog("Error", it.exception!!.localizedMessage)
                }
            }
    }


}
