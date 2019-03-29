package com.deucate.pubgvictory.participate

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.deucate.pubgvictory.R
import com.deucate.pubgvictory.model.User
import kotlinx.android.synthetic.main.card_participate_user.view.*

class UserAdapter(private val users: ArrayList<User>) : RecyclerView.Adapter<UserViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder =
        UserViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.card_participate_user,
                parent,
                false
            )
        )

    override fun getItemCount(): Int = users.size

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = users[position]
        holder.userNameTV.text = user.Name
    }

}

class UserViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val userNameTV = view.cardParticipateUserName!!
}