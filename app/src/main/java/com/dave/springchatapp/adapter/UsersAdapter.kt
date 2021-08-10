package com.dave.springchatapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dave.springchatapp.R
import com.dave.springchatapp.model.user.User

/**
 * Created by David Hakobyan on 6/25/21.
 */
class UsersAdapter : RecyclerView.Adapter<UserViewHolder> {
    private var list: ArrayList<User> = arrayListOf()

    constructor()

    constructor(list: List<User>) {
        this.list = ArrayList(list)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.text_item, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.setupUsingUser(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun setList(list: List<User>) {
        this.list = ArrayList(list)
        notifyDataSetChanged()
    }

    fun addUser(user: User) {
        list.add(user)
        notifyItemInserted(list.size - 1)
    }

}

class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private var nameTextView: TextView = itemView.findViewById(R.id.name)

    fun setupUsingUser(user: User) {
        val id = user.getId()
        val name = user.getName()
        val text = "$id $name"
        nameTextView.text = text
    }

}
