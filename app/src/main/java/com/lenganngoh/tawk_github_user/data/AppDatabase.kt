package com.lenganngoh.tawk_github_user.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.lenganngoh.tawk_github_user.data.local.DetailDao
import com.lenganngoh.tawk_github_user.data.local.MainDao
import com.lenganngoh.tawk_github_user.data.model.User

@Database(entities = [User::class], version = 1, exportSchema = false)
abstract class AppDatabase: RoomDatabase() {

    abstract fun mainDao(): MainDao
    abstract fun detailDao(): DetailDao

    companion object {
        @Volatile private var instance: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase =
            instance ?: synchronized(this) { instance ?: buildDatabase(context).also { instance = it } }

        private fun buildDatabase(appContext: Context) =
            Room.databaseBuilder(appContext, AppDatabase::class.java, "tawk-github.db")
                .fallbackToDestructiveMigration()
                .build()
    }
}