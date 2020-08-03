package com.fatcloud.account.data

import androidx.room.*
import com.fatcloud.account.entity.form.p9p10.NativeFormPersonalPackageP9P10Draft
import com.fatcloud.account.entity.local.form.PersonalTaxDraft

/**
 * 个体户套餐，个人独资
 */
@Dao
interface P9P10PersonalPackageDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun add(user: NativeFormPersonalPackageP9P10Draft)

    @Query("SELECT * FROM tb_p9_p10_personal_package_draft")
    fun find(): NativeFormPersonalPackageP9P10Draft?

    @Query("DELETE FROM tb_p9_p10_personal_package_draft")
    fun clear()


    @Update
    fun update(model: NativeFormPersonalPackageP9P10Draft)
}