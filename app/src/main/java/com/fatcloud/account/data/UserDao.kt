package com.fatcloud.account.data

import androidx.room.*
import com.fatcloud.account.entity.users.User

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addUser(user: User)

    @Query("SELECT * FROM tb_user")
    fun findUser(): User?

    @Query("DELETE FROM tb_user")
    fun clear()


    @Update
    fun updateUser(user: User)
}