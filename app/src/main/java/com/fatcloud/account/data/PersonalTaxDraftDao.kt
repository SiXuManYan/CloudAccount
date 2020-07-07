package com.fatcloud.account.data

import androidx.room.*
import com.fatcloud.account.entity.local.form.PersonalTaxDraft

/**
 * 个体户税务登记草稿箱
 */
@Dao
interface PersonalTaxDraftDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun add(user: PersonalTaxDraft)

    @Query("SELECT * FROM tb_personal_tax_draft")
    fun find(): PersonalTaxDraft?

    @Query("DELETE FROM tb_personal_tax_draft")
    fun clear()


    @Update
    fun update(user: PersonalTaxDraft)
}