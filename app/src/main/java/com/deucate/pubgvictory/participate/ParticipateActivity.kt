package com.deucate.pubgvictory.participate

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.deucate.pubgvictory.R
import com.deucate.pubgvictory.model.Room
import kotlinx.android.synthetic.main.activity_participate.*


class ParticipateActivity : AppCompatActivity() {

    private lateinit var viewModel: ParticipateViewModel

    private lateinit var adapter: UserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_participate)
        viewModel = ViewModelProviders.of(this).get(ParticipateViewModel::class.java)

        val recyclerView = participateRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        viewModel.error.observe(this, Observer {
            if (it != null) {
                Toast.makeText(this@ParticipateActivity, it, Toast.LENGTH_SHORT).show()
            }
        })

        val parcelRoom = intent.getSerializableExtra("room") as Room?
        if (parcelRoom != null) {
            initializeUI(parcelRoom)
        } else {
            viewModel.getRoomFromLink(intent) { error, room ->
                if (error == null) {
                    if (room != null) {
                        initializeUI(room)
                    } else {
                        Toast.makeText(this, "500 internal server error.", Toast.LENGTH_LONG).show()
                        this.finish()
                    }
                } else {
                    Toast.makeText(this, error, Toast.LENGTH_LONG).show()
                    this.finish()
                }
            }
        }
    }

    private fun initializeUI(room: Room) {
        participateTitle.text = room.Title
        participateButton.setOnClickListener {
            val uid = participateEditText!!.text.toString()
            if (room.Teams < /*users.size*/ 1) {
                Toast.makeText(this, "Data added", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            viewModel.getUserFromUID(uid) { error, user ->
                if (error == null) {
                    if (user != null) {
                        //Todo add user
                    } else {
                        AlertDialog.Builder(this).setTitle("Error")
                            .setMessage("Something went wrong.").show()
                    }
                } else {
                    AlertDialog.Builder(this).setTitle("Error").setMessage(error).show()
                }
            }
        }
    }

}
