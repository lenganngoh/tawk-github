package com.lenganngoh.tawk_github_user.ui.main

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lenganngoh.tawk_github_user.data.model.User
import com.lenganngoh.tawk_github_user.data.repository.MainRepository
import com.lenganngoh.tawk_github_user.test.Test
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.launch

class MainViewModel @ViewModelInject constructor(private val repo: MainRepository) : ViewModel() {

    var isLoading: MutableLiveData<Boolean> = MutableLiveData(false)
    var searchList: MutableLiveData<List<User>> = MutableLiveData(ArrayList())
    var lastId: MutableLiveData<Long> = MutableLiveData(0)

    fun getUsers(): LiveData<List<User>> {
        return repo.getUsers()
    }

    fun getLastId() {
        repo.getLastId().subscribeOn(Schedulers.io()).subscribe {
            viewModelScope.launch {
                if (it > lastId.value!!) {
                    lastId.value = it
                }
            }
        }
    }

    fun fetchRemoteUsers(id: Long) {
        isLoading.value = true
        repo.fetchRemoteUsers(getSearchUrl(id), Test.token)
            .subscribeOn(Schedulers.io())
            .subscribe { response, _ ->
                if (!response.isNullOrEmpty()) {
                    if (id <= 0) {
                        repo.clear()
                    }
                    response.forEach {
                        val id = repo.insert(it)
                        if (id < 0) {
                            repo.update(it)
                        }
                    }
                }
            }
    }

    fun search(term: String) {
        repo.getUsers(term).subscribeOn(Schedulers.io())
            .subscribe {
                viewModelScope.launch {
                    searchList.value = it
                }
            }
    }

    private fun getSearchUrl(id: Long): String {
        return "/users?since=$id"
    }
}