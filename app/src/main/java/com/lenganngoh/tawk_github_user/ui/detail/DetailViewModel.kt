package com.lenganngoh.tawk_github_user.ui.detail

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.lenganngoh.tawk_github_user.data.model.User
import com.lenganngoh.tawk_github_user.data.repository.DetailRepository
import com.lenganngoh.tawk_github_user.test.Test
import io.reactivex.schedulers.Schedulers

class DetailViewModel @ViewModelInject constructor(private val repo: DetailRepository) :
    ViewModel() {

    fun getUser(id: Long): LiveData<User> {
        return repo.getUser(id)
    }

    fun insert(user: User) {
        repo.insert(user)
    }

    fun update(user: User) {
        repo.update(user)
    }

    fun fetchRemoteUserDetail(login: String, localNote: String?) {
        repo.fetchRemoteUserDetail(getFetchUrl(login), Test.token)
            .subscribeOn(Schedulers.io()).subscribe { response, _ ->
                if (response != null) {
                    response.note = localNote
                    repo.insert(response)
                }
            }
    }

    private fun getFetchUrl(login: String): String {
        return "/users/$login"
    }
}