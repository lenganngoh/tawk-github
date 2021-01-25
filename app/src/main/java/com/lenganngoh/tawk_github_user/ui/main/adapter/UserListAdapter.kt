package com.lenganngoh.tawk_github_user.ui.main.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.lenganngoh.tawk_github_user.data.model.User
import com.lenganngoh.tawk_github_user.databinding.AdapterLoadingBinding
import com.lenganngoh.tawk_github_user.databinding.AdapterUserListBinding
import kotlin.collections.ArrayList


class UserListAdapter(private val onClickListener: OnUserClickListener) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    interface OnUserClickListener {
        fun onUserClick(user: User)
    }

    companion object {
        val LOADING = 0
        val DISPLAY = 1
    }

    private val users = ArrayList<User>()

    fun setUserList(users: List<User>) {
        this.users.clear()
        this.users.addAll(users)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var viewHolder: RecyclerView.ViewHolder? = null
        when (viewType) {
            DISPLAY -> {
                val binding: AdapterUserListBinding =
                    AdapterUserListBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                viewHolder = UserListViewHolder(binding, onClickListener)
            }
            LOADING -> {
                val binding: AdapterLoadingBinding = AdapterLoadingBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                viewHolder = LoadingViewHolder(binding)
            }
        }
        return viewHolder!!
    }

    override fun getItemCount(): Int = users.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            DISPLAY -> {
                val userListViewHolder = holder as UserListViewHolder
                userListViewHolder.bind(users[position])
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == users.size - 1) {
            LOADING
        } else {
            DISPLAY
        }
    }
}

class UserListViewHolder(
    private val adapterUserListBinding: AdapterUserListBinding,
    private val onClickListener: UserListAdapter.OnUserClickListener
) : RecyclerView.ViewHolder(adapterUserListBinding.root) {

    private lateinit var user: User

    fun bind(user: User) {
        this.user = user
        adapterUserListBinding.txtUsername.text = user.login
        adapterUserListBinding.txtLink.text = user.url
        adapterUserListBinding.imgUser.load(user.avatar_url)

        adapterUserListBinding.imgNotes.visibility = if (user.note.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }

        adapterUserListBinding.root.setOnClickListener {
            onClickListener.onUserClick(user)
        }
    }
}

class LoadingViewHolder(adapterLoadingBinding: AdapterLoadingBinding) :
    RecyclerView.ViewHolder(adapterLoadingBinding.root)