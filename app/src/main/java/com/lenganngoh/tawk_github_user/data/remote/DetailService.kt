package com.lenganngoh.tawk_github_user.data.remote

import com.lenganngoh.tawk_github_user.data.model.User
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.HeaderMap
import retrofit2.http.Url

interface DetailService {
    @GET
    fun fetchRemoteUserDetail(
        @Url url: String,
        @HeaderMap headerMap: Map<String, String>
    ): Single<User>
}