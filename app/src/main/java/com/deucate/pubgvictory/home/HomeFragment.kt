package com.deucate.pubgvictory.home


import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.SpannableStringBuilder
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.deucate.pubgvictory.MainViewModel
import com.deucate.pubgvictory.R
import com.deucate.pubgvictory.model.Room
import com.deucate.pubgvictory.utils.Constatns
import kotlinx.android.synthetic.main.fragment_home.view.*

class HomeFragment : Fragment() {

    private lateinit var viewModel: MainViewModel

    private val currentAdapter = MutableLiveData<RecyclerView.Adapter<*>>()
    private lateinit var roomAdapter: RoomAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_home, container, false)
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        rootView.roomRecyclerView.layoutManager = LinearLayoutManager(activity)

        roomAdapter = RoomAdapter(viewModel.rooms.value)
        roomAdapter.listener = object : RoomAdapter.RoomCardClickListener {
            override fun onClickCard(room: Room) {
                val intent = Intent(activity, RoomActivity::class.java)
                intent.putExtra(Constatns.rooms, room)
                startActivity(intent)
            }
        }

        viewModel.rooms.observe(this, Observer {
            if (currentAdapter.value != null) {
                currentAdapter.value!!.notifyDataSetChanged()
            }
        })

        currentAdapter.observe(this, Observer {
            rootView.roomRecyclerView.adapter = it
        })

        currentAdapter.value = roomAdapter

        val searchEditText = rootView.searchEditText
        searchEditText.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                if (event.rawX >= (searchEditText.right - searchEditText.compoundDrawables[Constatns.DRAWABLE_RIGHT].bounds.width())) {
                    searchEditText.text = SpannableStringBuilder("")
                    currentAdapter.value = roomAdapter
                    return@setOnTouchListener true
                }
            }
            return@setOnTouchListener false
        }

        searchEditText.onChange {
            if (it.length >= 3) {
                viewModel.searchRoom(it) { rooms ->
                    val searchAdapter = RoomAdapter(rooms)
                    currentAdapter.value = searchAdapter
                }
            } else if (it.isEmpty()) {
                currentAdapter.value = roomAdapter
            }
        }

        return rootView
    }

    private fun EditText.onChange(cb: (String) -> Unit) {
        this.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                cb(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

}
