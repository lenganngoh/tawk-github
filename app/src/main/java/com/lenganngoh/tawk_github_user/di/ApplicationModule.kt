package com.lenganngoh.tawk_github_user.di

import android.content.Context
import com.lenganngoh.tawk_github_user.BuildConfig
import com.lenganngoh.tawk_github_user.data.AppDatabase
import com.lenganngoh.tawk_github_user.data.local.DetailDao
import com.lenganngoh.tawk_github_user.data.local.MainDao
import com.lenganngoh.tawk_github_user.data.remote.DetailService
import com.lenganngoh.tawk_github_user.data.remote.MainService
import com.lenganngoh.tawk_github_user.data.repository.DetailRepository
import com.lenganngoh.tawk_github_user.data.repository.MainRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit

@Module
@InstallIn(ApplicationComponent::class)
object ApplicationModule {
    @Provides
    internal fun provideHttpClient(): OkHttpClient =
        OkHttpClient.Builder()
            .connectTimeout(1, TimeUnit.MINUTES)
            .build()

    @Provides
    internal fun provideRetrofit(httpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BuildConfig.API_BASE_URL)
            .client(httpClient)
            .build()

    @Provides
    fun provideDatabase(@ApplicationContext appContext: Context) = AppDatabase.getDatabase(appContext)

    @Provides
    fun provideMainService(retrofit: Retrofit): MainService = retrofit.create(
        MainService::class.java
    )

    @Provides
    fun provideMainDao(db: AppDatabase) = db.mainDao()

    @Provides
    fun provideMainRepository(remote: MainService, local: MainDao) = MainRepository(remote, local)

    @Provides
    fun provideDetailService(retrofit: Retrofit): DetailService = retrofit.create(
        DetailService::class.java
    )

    @Provides
    fun provideDetailDao(db: AppDatabase) = db.detailDao()

    @Provides
    fun provideDetailRepository(remote: DetailService, local: DetailDao) = DetailRepository(remote, local)
}