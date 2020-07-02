package com.fatcloud.account.data

import androidx.room.*
import com.fatcloud.account.entity.local.form.PersonalLicenseDraft

@Dao
interface PersonalLicenseDraftDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun add(user: PersonalLicenseDraft)

    @Query("SELECT * FROM tb_personal_license_draft")
    fun find(): PersonalLicenseDraft?

    @Query("DELETE FROM tb_personal_license_draft")
    fun clear()


    @Update
    fun update(user: PersonalLicenseDraft)
}