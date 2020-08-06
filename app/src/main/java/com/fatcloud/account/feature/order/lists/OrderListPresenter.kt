package com.fatcloud.account.feature.order.lists

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.fatcloud.account.base.common.BasePresenter
import com.fatcloud.account.base.net.BaseJsonArrayHttpSubscriber
import com.fatcloud.account.common.Constants
import com.fatcloud.account.data.CloudDataBase
import com.fatcloud.account.entity.form.p9p10.NativeFormPersonalPackageP9P10Draft
import com.fatcloud.account.entity.local.form.*
import com.fatcloud.account.entity.order.persional.Order
import com.google.gson.JsonArray
import java.math.BigDecimal
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

/**
 * Created by Wangsw on 2020/6/3 0003 17:44.
 * </br>
 *
 */
class OrderListPresenter @Inject constructor(private var orderListView: OrderListView) : BasePresenter(orderListView) {

    lateinit var database: CloudDataBase @Inject set

    override fun loadList(lifecycle: LifecycleOwner, page: Int, pageSize: Int, lastItemId: String?) {
        requestApi(lifecycle, Lifecycle.Event.ON_DESTROY,
            apiService.getOrderList(pageSize, lastItemId),

            object : BaseJsonArrayHttpSubscriber<Order>(orderListView, false) {

                override fun onSuccess(jsonArray: JsonArray?, list: ArrayList<Order>, lastItemId: String?) {
                    orderListView.bindList(list, lastItemId)
                }
            }
        )

    }

    fun getDraftModule() {

        var list: ArrayList<Order> = ArrayList()

        // P1
        val p1 = PersonalLicenseDraft.get()

        if (!p1.productId.isNullOrBlank()) {
                Order().apply {
                    money = p1.finalMoney
                    productId = p1.productId
                    productPriceId = p1.productPriceId
                    productName =  "个体户营业执照"
                    productPriceName = productName
                    mold = Constants.P1
                    state = Constants.OS_UN_SUBMITTED
                    stateText = "未提交"
                }
            }



        // P2
        EnterprisePackageDraft.get()

        //P3
        PersonalBookkeepingDraft.get()

        // P4
        PersonalTaxDraft.get()

        // P8
        BankPersonalDraft.get()

        // P9 P10
        NativeFormPersonalPackageP9P10Draft.get()


    }

}