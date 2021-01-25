package com.lenganngoh.tawk_github_user.ui.detail

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import coil.load
import com.lenganngoh.tawk_github_user.R
import com.lenganngoh.tawk_github_user.data.model.User
import com.lenganngoh.tawk_github_user.databinding.ActivityDetailBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private val viewModel: DetailViewModel by viewModels()
    private var user: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupObservers()
        setupListeners()
        viewModel.fetchRemoteUserDetail(getExtraLogin(), getExtraNote())
    }

    private fun setupListeners() {
        binding.btnSave.setOnClickListener {
            if (user != null) {
                saveNote()
            }
        }

        binding.btnBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun saveNote() {
        user!!.note = binding.etNote.text.toString()
        Thread {
            viewModel.insert(user!!)
        }.start()
    }

    private fun getExtraUserId(): Long {
        return intent.extras?.getLong("EXTRA_USER_ID", -1)!!
    }

    private fun getExtraLogin(): String {
        return intent.extras?.getString("EXTRA_LOGIN", "")!!
    }

    private fun getExtraNote(): String {
        return intent.extras?.getString("EXTRA_NOTE", "")!!
    }

    private fun setupObservers() {
        viewModel.getUser(getExtraUserId()).observe(this, {
            this.user = it
            updateView(it)
        })
    }

    private fun updateView(user: User) {
        val name = if (user.name.isNullOrEmpty()) {
            user.login
        } else {
            user.name
        }
        binding.txtName.text = name

        binding.imgUser.load(user.avatar_url)

        binding.txtSmallName.text = String.format("%s: %s", getString(R.string.str_name), name)
        if (!user.company.isNullOrEmpty()) {
            binding.txtCompany.visibility = View.VISIBLE
            binding.txtCompany.text =
                String.format("%s: %s", getString(R.string.str_company), user.company)
        } else {
            binding.txtCompany.visibility = View.GONE
        }

        if (!user.blog.isNullOrEmpty()) {
            binding.txtBlog.visibility = View.VISIBLE
            binding.txtBlog.text = String.format("%s: %s", getString(R.string.str_blog), user.blog)
        } else {
            binding.txtBlog.visibility = View.GONE
        }

        if (binding.etNote.text.toString().isEmpty()) {
            binding.etNote.setText(user.note)
        }

        binding.txtFollowers.text =
            String.format("%s: %d", getString(R.string.str_followers), user.followers)
        binding.txtFollowing.text =
            String.format("%s: %d", getString(R.string.str_following), user.following)
    }
}