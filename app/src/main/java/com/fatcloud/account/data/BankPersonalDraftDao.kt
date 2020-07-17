package com.fatcloud.account.data

import androidx.room.*
import com.fatcloud.account.entity.local.form.BankPersonalDraft

/**
 * Created by Wangsw on 2020/7/17 0017 16:34.
 * </br>
 * 个体户银行对公账户草稿
 */
@Dao
interface BankPersonalDraftDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun add(user: BankPersonalDraft)

    @Query("SELECT * FROM tb_bank_personal_draft")
    fun find(): BankPersonalDraft?

    @Query("DELETE FROM tb_bank_personal_draft")
    fun clear()

    @Update
    fun update(user: BankPersonalDraft)



}