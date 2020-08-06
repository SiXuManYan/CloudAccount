package com.fatcloud.account.feature.defray.prepare

import com.fatcloud.account.base.common.BasePresenter
import com.fatcloud.account.common.Constants
import com.fatcloud.account.data.CloudDataBase
import javax.inject.Inject

/**
 * Created by Wangsw on 2020/6/17 0017 15:27.
 * </br>
 *
 */
class PayPreparePresenter @Inject constructor(private var view: PayPrepareView) : BasePresenter(view) {

    lateinit var database: CloudDataBase @Inject set

    fun deleteDraft(mMold: String) {
        when (mMold) {
            Constants.P1 -> {
                database.personalLicenseDraftDao().clear()
            }
            Constants.P2 -> {
                database.enterprisePackageDraftDao().clear()
            }
            Constants.P3 -> {
                database.personalBookkeepingDraftDao().clear()
            }
            Constants.P4 -> {
                database.personalTaxDraftDao().clear()
            }
            Constants.P5 -> {
                // 不需要保存功能
            }
            Constants.P6 -> {
                // 不需要保存功能
            }
            Constants.P7 -> {
                // 不需要保存功能
            }
            Constants.P8 -> {
                database.bankPersonalDraftDao().clear()
            }
            Constants.P9, Constants.P10 -> {
                database.p9p10PersonalPackageDao().clear()
            }
            Constants.P11 -> {
                // 无表单
            }
            else -> {
            }
        }
    }

}