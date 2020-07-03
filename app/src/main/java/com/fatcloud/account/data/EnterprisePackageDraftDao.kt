package com.fatcloud.account.data

import androidx.room.*
import com.fatcloud.account.entity.local.form.EnterprisePackageDraft

/**
 * Created by Wangsw on 2020/7/3 0003 15:23.
 * </br>
 * 企业套餐
 */
@Dao
interface EnterprisePackageDraftDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun add(user: EnterprisePackageDraft)

    @Query("SELECT * FROM tb_enterprise_package_draft")
    fun find(): EnterprisePackageDraft?

    @Query("DELETE FROM tb_enterprise_package_draft")
    fun clear()


    @Update
    fun update(user: EnterprisePackageDraft)

}