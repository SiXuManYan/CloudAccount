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

            }
            Constants.P2 -> {
            }
            Constants.P3 -> {
            }
            Constants.P4 -> {
            }
            Constants.P5 -> {
            }
            Constants.P6 -> {
            }
            Constants.P7 -> {
            }
            Constants.P8 -> {
            }
            else -> {
            }
        }
    }
}