package com.fatcloud.account.data

import androidx.room.*
import com.fatcloud.account.entity.local.form.BankPublicDraft
import com.fatcloud.account.entity.order.enterprise.Shareholder

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


    /**
     * 财务负责人
     */
    @Query("UPDATE tb_bank_public_draft SET share_holders = :shareholder WHERE order_work_id = :orderWorkId")
    fun updateShareHolder(shareholder: List<Shareholder>, orderWorkId: String)

    /**
     * 营业执照
     */
    @Query("UPDATE tb_bank_public_draft SET business_license_url = :url , business_license_path = :path WHERE order_work_id = :orderWorkId")
    fun updateLicenseUrlAndPath(url: String, path: String, orderWorkId: String)


    /**
     * 电子图章
     */
    @Query("UPDATE tb_bank_public_draft SET electronic_seal_url = :url,electronic_seal_path = :path WHERE order_work_id = :orderWorkId")
    fun updateElectronicSealUrlAndPath(url: String, path: String, orderWorkId: String)


    /**
     * 法人签字授权书
     */
    @Query("UPDATE tb_bank_public_draft SET electronic_seal_path = :url,legal_person_warrant_image_path = :path WHERE order_work_id = :orderWorkId")
    fun updateWarrantUrlAndPath(url: String, path: String, orderWorkId: String)


    @Update
    fun update(user: BankPublicDraft)


}