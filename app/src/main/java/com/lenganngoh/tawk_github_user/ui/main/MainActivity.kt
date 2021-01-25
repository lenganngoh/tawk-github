package com.lenganngoh.tawk_github_user.ui.main

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lenganngoh.tawk_github_user.data.model.User
import com.lenganngoh.tawk_github_user.databinding.ActivityMainBinding
import com.lenganngoh.tawk_github_user.ui.detail.DetailActivity
import com.lenganngoh.tawk_github_user.ui.main.adapter.UserListAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), UserListAdapter.OnUserClickListener {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()
    private lateinit var listAdapter: UserListAdapter
    private var isLoading = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupList()
        setupObservers()
        setupListeners()

        viewModel.fetchRemoteUsers(0)
    }

    private fun setupListeners() {
        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.search(s.toString())
            }
        })
    }

    private fun setupList() {
        binding.contList.setOnRefreshListener {
            binding.contList.isRefreshing = true
            viewModel.fetchRemoteUsers(0)
        }

        binding.rvList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE) {
                    isLoading = true
                    viewModel.fetchRemoteUsers(viewModel.lastId.value!!)
                }
            }
        })
        listAdapter = UserListAdapter(this)
        binding.rvList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvList.setHasFixedSize(true)
        binding.rvList.adapter = listAdapter
    }

    private fun setupObservers() {
        viewModel.isLoading.observe(this, {
            binding.contList.isRefreshing = false
        })

        viewModel.getUsers().observe(this, {
            viewModel.isLoading.value = false
            isLoading = false
            listAdapter.setUserList(it)
            viewModel.getLastId()
        })

        viewModel.searchList.observe(this, {
            listAdapter.setUserList(it)
        })
    }

    override fun onUserClick(user: User) {
        openDetailActivity(user)
    }

    private fun openDetailActivity(user: User) {
        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra("EXTRA_USER_ID", user.id)
        intent.putExtra("EXTRA_LOGIN", user.login)
        intent.putExtra("EXTRA_NOTE", user.note)
        startActivity(intent)
    }
}