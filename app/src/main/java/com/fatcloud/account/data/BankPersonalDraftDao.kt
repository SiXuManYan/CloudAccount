package com.fatcloud.account.data

import androidx.room.*
import com.fatcloud.account.entity.local.form.BankPersonalDraft
import com.fatcloud.account.entity.order.IdentityImg
import com.fatcloud.account.entity.order.persional.NamePhoneBean

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




    /**
     * 更新法人身份证正反面
     */
    @Query("UPDATE tb_bank_personal_draft SET identity_img = :image WHERE product_id = :productId")
    fun updateLegalPersonIdImage(image: List<IdentityImg>, productId: String)


    /**
     * 更新营业执照正本图片信息
     * （七牛URL，本地路径）
     */
    @Query("UPDATE tb_bank_personal_draft SET license_img = :image WHERE product_id = :productId")
    fun updateLicenseImage(image: List<IdentityImg>, productId: String)


    /**
     * 更新存储账户图片
     */
    @Query("UPDATE tb_bank_personal_draft SET deposit_img = :image WHERE product_id = :productId")
    fun updateDepositImage(image: List<IdentityImg>, productId: String)


    /**
     * 更新法人姓名和联系方式
     */
    @Query("UPDATE tb_bank_personal_draft SET person_legal = :model WHERE product_id = :productId")
    fun updatePersonLegal(model: NamePhoneBean, productId: String)


    /**
     * 更新财务负责人姓名和联系方式
     */
    @Query("UPDATE tb_bank_personal_draft SET person_finance = :model WHERE product_id = :productId")
    fun updatePersonFinance(model: NamePhoneBean, productId: String)


    /**
     * 更新 大额业务查证联系人1信息
     */
    @Query("UPDATE tb_bank_personal_draft SET person_verification_first = :model WHERE product_id = :productId")
    fun updatePersonVerification1(model: NamePhoneBean, productId: String)


    /**
     * 更新 大额业务查证联系人2信息
     */
    @Query("UPDATE tb_bank_personal_draft SET person_verification_second = :model WHERE product_id = :productId")
    fun updatePersonVerification2(model: NamePhoneBean, productId: String)


    /**
     * 更新 对账联系人
     */
    @Query("UPDATE tb_bank_personal_draft SET person_reconciliation = :model WHERE product_id = :productId")
    fun updatePersonReconciliation(model: NamePhoneBean, productId: String)


}