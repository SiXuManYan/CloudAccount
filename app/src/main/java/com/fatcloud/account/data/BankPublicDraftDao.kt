package com.fatcloud.account.data

import androidx.room.*
import com.fatcloud.account.entity.local.form.BankPublicDraft
import com.fatcloud.account.entity.local.form.EnterprisePackageDraft

/**
 * Created by Wangsw on 2020/7/3 0003 15:23.
 * </br>
 * 银行对公
 */
@Dao
interface BankPublicDraftDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun add(user: BankPublicDraft)

    @Query("SELECT * FROM tb_bank_public_draft")
    fun find(): BankPublicDraft?

    @Query("DELETE FROM tb_bank_public_draft")
    fun clear()


    @Update
    fun update(user: BankPublicDraft)

}