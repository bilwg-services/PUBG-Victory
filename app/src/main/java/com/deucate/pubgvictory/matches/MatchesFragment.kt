package com.deucate.pubgvictory.matches


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.deucate.pubgvictory.MainViewModel
import com.deucate.pubgvictory.R
import com.deucate.pubgvictory.model.Event
import com.deucate.pubgvictory.model.Room
import kotlinx.android.synthetic.main.fragment_matches.view.*

class MatchesFragment : Fragment() {

    private lateinit var viewModel: MainViewModel

    private lateinit var adapter: MatchAdapter
    private val events = ArrayList<Event>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_matches, container, false)
        rootView.matchRecyclerView.layoutManager = LinearLayoutManager(context)

        adapter = MatchAdapter(events) { _, event ->
            getMatchFromEvent(event) { room, error ->
                if (error == null) {
                    if (room != null) {
                        Toast.makeText(
                            context,
                            room.RoomID ?: "Room not created yet.",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                } else {
                    Toast.makeText(
                        context,
                        "Data not found. Contact developer of the app.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        rootView.matchRecyclerView.adapter = adapter

        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)

        viewModel.myEvents.observe(this, Observer {
            events.clear()
            events.addAll(it)
            adapter.notifyDataSetChanged()
            if (it.isEmpty()) {
                rootView.matchErrorTV.visibility = View.VISIBLE
            } else {
                rootView.matchErrorTV.visibility = View.GONE
            }
        })

        return rootView
    }

    private fun getMatchFromEvent(event: Event, callBack: (room: Room?, error: String?) -> Unit) {
        event.RoomRef.get().addOnCompleteListener {
            if (it.isSuccessful) {
                val result = it.result
                if (result != null && result.exists()) {
                    callBack(viewModel.getRoomFromDocument(result), null)
                }
            } else {
                callBack.invoke(null, it.exception!!.localizedMessage)
            }
        }
    }
}
