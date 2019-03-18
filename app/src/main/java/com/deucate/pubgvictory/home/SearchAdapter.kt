package com.deucate.pubgvictory.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.deucate.pubgvictory.R
import com.deucate.pubgvictory.model.Room
import kotlinx.android.synthetic.main.card_search.view.*

class SearchAdapter(private val data: ArrayList<Room>) :
    RecyclerView.Adapter<SearchViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder =
        SearchViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.card_search,
                parent,
                false
            )
        )

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        holder.searchTextView.text = data[position].Title
    }

}

class SearchViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val searchTextView = view.cardSearchTextView!!
}