package com.fatcloud.account.feature.defray.prepare

import com.fatcloud.account.base.common.BasePresenter
import com.fatcloud.account.common.Constants
import com.fatcloud.account.common.ProductUtils
import com.fatcloud.account.data.CloudDataBase
import com.fatcloud.account.entity.form.p9p10.NativeFormPersonalPackageP9P10Draft
import com.fatcloud.account.entity.local.form.*
import com.fatcloud.account.event.Event
import com.fatcloud.account.event.RxBus
import javax.inject.Inject

/**
 * Created by Wangsw on 2020/6/17 0017 15:27.
 * </br>
 *
 */
class PayPreparePresenter @Inject constructor(private var view: PayPrepareView) : BasePresenter(view) {

    lateinit var database: CloudDataBase @Inject set

    fun deleteDraft(mMold: String) {

        ProductUtils.deleteDraft(mMold)
        RxBus.post(Event(Constants.EVENT_REFRESH_ORDER_LIST_FROM_DELETE_DRAFT))
    }

}