package com.lenganngoh.tawk_github_user.data.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.lenganngoh.tawk_github_user.data.model.User
import io.reactivex.Observable

@Dao
interface MainDao {
    @Query("SELECT * FROM user")
    fun getUsers(): LiveData<List<User>>

    @Query("SELECT id FROM user ORDER BY id DESC LIMIT 1")
    fun getLastId(): Observable<Long>

    @Query("SELECT * FROM user WHERE login LIKE '%' || :term  || '%' OR note LIKE '%' || :term  || '%'")
    fun getUsers(term: String): Observable<List<User>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(user: User): Long

    @Update
    fun update(user: User)

    @Query("DELETE from user")
    fun clear()
}