package com.lenganngoh.tawk_github_user.data.repository

import androidx.lifecycle.LiveData
import com.lenganngoh.tawk_github_user.data.local.MainDao
import com.lenganngoh.tawk_github_user.data.model.User
import com.lenganngoh.tawk_github_user.data.remote.MainService
import com.lenganngoh.tawk_github_user.test.Test
import io.reactivex.Observable
import io.reactivex.Single
import javax.inject.Inject

class MainRepository @Inject constructor(
    private val remote: MainService,
    private val local: MainDao
) {
    fun getUsers(): LiveData<List<User>> = local.getUsers()

    fun getLastId(): Observable<Long> = local.getLastId()

    fun getUsers(term: String): Observable<List<User>> = local.getUsers(term)

    fun fetchRemoteUsers(url: String, token: String): Single<List<User>> {
        return remote.fetchRemoteUsers(url, getHeaderMap(token))
    }

    fun insert(user: User): Long {
        return local.insert(user)
    }

    fun update(user: User) {
        return local.update(user)
    }

    fun clear() {
        local.clear()
    }

    private fun getHeaderMap(token: String): Map<String, String> {
        val headerMap = mutableMapOf<String, String>()
        headerMap["Content-Type"] = "application/json"
        headerMap["Authorization"] = String.format("token %s", token)
        return headerMap
    }
}