package com.fatcloud.account.data

import androidx.room.*
import com.fatcloud.account.entity.local.form.PersonalBookkeepingDraft

/**
 * Created by Wangsw on 2020/7/3 0003 10:35.
 * </br>
 * 个体户代理记账 草稿箱
 */
@Dao
interface PersonalBookkeepingDraftDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun add(user: PersonalBookkeepingDraft)

    @Query("SELECT * FROM tb_personal_bookkeeping_draft")
    fun find(): PersonalBookkeepingDraft?

    @Query("DELETE FROM tb_personal_bookkeeping_draft")
    fun clear()


    @Update
    fun update(user: PersonalBookkeepingDraft)

}