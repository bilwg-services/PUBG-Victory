package com.deucate.pubgvictory.matches


import android.annotation.SuppressLint
import android.content.ClipboardManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.deucate.pubgvictory.MainViewModel
import com.deucate.pubgvictory.model.Event
import com.deucate.pubgvictory.model.Room
import com.deucate.pubgvictory.utils.Util
import kotlinx.android.synthetic.main.alert_room_detail.view.*
import android.R.attr.label
import android.content.ClipData
import android.content.Context
import androidx.core.content.ContextCompat.getSystemService
import com.deucate.pubgvictory.R
import kotlinx.android.synthetic.main.fragment_matches.view.*


class MatchesFragment : Fragment() {

    private lateinit var viewModel: MainViewModel
    private val utils: Util = Util()

    private lateinit var adapter: MatchAdapter
    private val events = ArrayList<Event>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_matches, container, false)
        rootView.matchRecyclerView.layoutManager = LinearLayoutManager(context)

        adapter = MatchAdapter(events) { _, event ->
            getMatchFromEvent(event) { room, error ->
                if (error == null) {
                    if (room != null) {
                        showRoomDetails(room)
                    } else {
                        showErrorMessage()
                    }
                } else {
                    showErrorMessage()
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

    @SuppressLint("InflateParams")
    private fun showRoomDetails(room: Room) {
        val alertLayout = LayoutInflater.from(context!!).inflate(R.layout.alert_room_detail, null, false)
        val clipboard = context!!.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager?

        alertLayout.alertRoomIDCopyBtn.setOnClickListener {
            val clip = ClipData.newPlainText("RoomID", room.RoomID)
            clipboard!!.primaryClip = clip
        }

        alertLayout.alertRoomPasswordCopyBtn.setOnClickListener {
            val clip = ClipData.newPlainText("RoomID", room.Password)
            clipboard!!.primaryClip = clip
        }

        if (room.RoomID != null) {
            alertLayout.alertRoomID.text = room.RoomID
            alertLayout.alertRoomPassword.text = room.Password ?: "Password not set."
        } else {
            Toast.makeText(context!!, "Room not created yet.", Toast.LENGTH_LONG).show()
            return
        }
        AlertDialog.Builder(context!!).setTitle("Room detail").setView(alertLayout)
                .setPositiveButton("Ok") { dialog, _ ->
                    dialog.dismiss()
                }.show()
    }

    private fun showErrorMessage() {
        Toast.makeText(
                context,
                "Data not found. Contact developer of the app.",
                Toast.LENGTH_SHORT
        ).show()
    }

    private fun getMatchFromEvent(event: Event, callBack: (room: Room?, error: String?) -> Unit) {
        event.RoomRef.get().addOnCompleteListener {
            if (it.isSuccessful) {
                val result = it.result
                if (result != null && result.exists()) {
                    callBack(utils.getRoomFromDocument(result), null)
                }
            } else {
                callBack.invoke(null, it.exception!!.localizedMessage)
            }
        }
    }
}
