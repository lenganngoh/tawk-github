package com.lenganngoh.tawk_github_user.data.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.lenganngoh.tawk_github_user.data.model.User

@Dao
interface DetailDao {
    @Query("SELECT * FROM user WHERE id = :id")
    fun getUser(id: Long): LiveData<User>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(user: User) : Long

    @Update
    fun update(user: User)
}