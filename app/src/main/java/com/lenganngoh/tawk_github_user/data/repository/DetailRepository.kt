package com.lenganngoh.tawk_github_user.data.repository

import androidx.lifecycle.LiveData
import com.lenganngoh.tawk_github_user.data.local.DetailDao
import com.lenganngoh.tawk_github_user.data.model.User
import com.lenganngoh.tawk_github_user.data.remote.DetailService
import io.reactivex.Observable
import io.reactivex.Single
import javax.inject.Inject

class DetailRepository @Inject constructor(
    private val remote: DetailService,
    private val local: DetailDao
) {
    fun getUser(id: Long): LiveData<User> = local.getUser(id)

    fun insert(user: User): Long {
        return local.insert(user)
    }

    fun update(user: User) {
        local.update(user)
    }

    fun fetchRemoteUserDetail(url: String, token: String): Single<User> {
        return remote.fetchRemoteUserDetail(url, getHeaderMap(token))
    }

    private fun getHeaderMap(token: String): Map<String, String> {
        val headerMap = mutableMapOf<String, String>()
        headerMap["Content-Type"] = "application/json"
        headerMap["Authorization"] = String.format("token %s", token)
        return headerMap
    }
}