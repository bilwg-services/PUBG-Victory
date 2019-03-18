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

    private val searchData = ArrayList<Room>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_home, container, false)
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        rootView.roomRecyclerView.layoutManager = LinearLayoutManager(activity)
        rootView.homeSearchExtendedRecyclerView.layoutManager = LinearLayoutManager(activity)

        val roomAdapter = RoomAdapter(viewModel.rooms.value)
        roomAdapter.listener = object : RoomAdapter.RoomCardClickListener {
            override fun onClickCard(room: Room) {
                val intent = Intent(activity, RoomActivity::class.java)
                intent.putExtra(Constatns.rooms, room)
                startActivity(intent)
            }
        }
        val searchAdapter = SearchAdapter(searchData)

        rootView.roomRecyclerView.adapter = roomAdapter
        rootView.homeSearchExtendedRecyclerView.adapter = searchAdapter

        viewModel.rooms.observe(this, Observer {
            roomAdapter.notifyDataSetChanged()

        })

        val searchEditText = rootView.searchEditText
        searchEditText.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                if (event.rawX >= (searchEditText.right - searchEditText.compoundDrawables[Constatns.DRAWABLE_RIGHT].bounds.width())) {
                    searchEditText.text = SpannableStringBuilder("")
                    return@setOnTouchListener true
                }
            }
            return@setOnTouchListener false
        }

        searchEditText.onChange {
            if (it.length >= 3) {
                if (rootView.homeSearchExtendedLayout.visibility == View.GONE) {
                    rootView.homeSearchExtendedLayout.visibility = View.VISIBLE
                }
                viewModel.searchRoom(it) { rooms ->
                    if (rooms != null) {
                        rooms.forEach { room ->
                            if (!searchData.contains(room)) {
                                searchData.add(room)
                            }
                        }
                        searchAdapter.notifyDataSetChanged()
                    }
                }
            } else {
                searchData.clear()
                if (rootView.homeSearchExtendedLayout.visibility == View.VISIBLE) {
                    rootView.homeSearchExtendedLayout.visibility = View.GONE
                }
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
