package com.lenganngoh.tawk_github_user.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class User(
    @PrimaryKey
    val id: Long = 0,
    val login: String? = "",
    val node_id: String? = "",
    val avatar_url: String? = "",
    val gravatar_id: String? = "",
    val url: String? = "",
    val html_url: String? = "",
    val followers_url: String? = "",
    val following_url: String? = "",
    val gists_url: String? = "",
    val starred_url: String? = "",
    val subscriptions_url: String? = "",
    val organizations_url: String? = "",
    val repos_url: String? = "",
    val events_url: String? = "",
    val received_events_url: String? = "",
    val type: String? = "",
    val site_admin: Boolean,
    val name: String? = "",
    val company: String? = "",
    val blog: String? = "",
    val location: String? = "",
    val followers: Long = 0,
    val following: Long = 0,
    var note: String? = ""
)