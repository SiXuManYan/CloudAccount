package com.fatcloud.account.feature.order.lists

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.fatcloud.account.base.common.BasePresenter
import com.fatcloud.account.base.net.BaseJsonArrayHttpSubscriber
import com.fatcloud.account.common.Constants
import com.fatcloud.account.common.TimeUtil
import com.fatcloud.account.data.CloudDataBase
import com.fatcloud.account.entity.form.p9p10.NativeFormPersonalPackageP9P10Draft
import com.fatcloud.account.entity.local.form.*
import com.fatcloud.account.entity.order.persional.Order
import com.fatcloud.account.entity.users.User
import com.google.gson.JsonArray
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

    fun getDraftModule(): ArrayList<Order> {

        val list: ArrayList<Order> = ArrayList()

        // P1
        val p1 = PersonalLicenseDraft.get()

        val userPhone = User.get().username

        if (!p1.productId.isNullOrBlank() && p1.loginPhone == userPhone) {
            list.add(Order().apply {
                money = p1.finalMoney
                productId = p1.productId
                productPriceId = p1.productPriceId
                productPriceName = productName
                productName = "个体户营业执照"
                mold = Constants.P1
                state = Constants.OS_UN_SUBMITTED
                stateText = "未提交"
                nativeExtraDraftObject = p1
                createDt = getCreateDate(p1.createTime)
            })
        }

        // P2
        val p2 = EnterprisePackageDraft.get()
        if (!p2.productId.isNullOrBlank() && p2.loginPhone == userPhone) {
            list.add(
                Order().apply {
                    money = p2.finalMoney
                    productId = p2.productId
                    productPriceId = p2.productPriceId
                    productName = "企业套餐办理"
                    productPriceName = productName
                    mold = Constants.P2
                    state = Constants.OS_UN_SUBMITTED
                    stateText = "未提交"
                    nativeExtraDraftObject = p2
                    createDt = getCreateDate(p2.createTime)
                }
            )
        }


        //P3
        val p3 = PersonalBookkeepingDraft.get()
        if (!p3.productId.isNullOrBlank() && p3.loginPhone == userPhone) {
            list.add(
                Order().apply {
                    money = p3.finalMoney
                    productId = p3.productId
                    productPriceId = p3.productPriceId
                    productName = "个体户代理记账"
                    productPriceName = productName
                    mold = Constants.P3
                    state = Constants.OS_UN_SUBMITTED
                    stateText = "未提交"
                    nativeExtraDraftObject = p3
                    createDt = getCreateDate(p3.createTime)
                }
            )
        }


        // P4
        val p4 = PersonalTaxDraft.get()
        if (!p4.productId.isNullOrBlank() && p4.loginPhone == userPhone) {
            list.add(
                Order().apply {
                    money = p4.finalMoney
                    productId = p4.productId
                    productPriceId = p4.productPriceId
                    productName = "个体户代理记账"
                    productPriceName = productName
                    mold = Constants.P4
                    state = Constants.OS_UN_SUBMITTED
                    stateText = "未提交"
                    nativeExtraDraftObject = p4
                    createDt = getCreateDate(p4.createTime)
                }
            )
        }


        // P8
        val p8 = BankPersonalDraft.get()
        if (!p8.productId.isNullOrBlank() && p8.loginPhone == userPhone && p8.mold == Constants.P8) {
            list.add(
                Order().apply {
                    money = p8.finalMoney
                    productId = p8.productId
                    productPriceId = p8.productPriceId
                    productName = "个体户银行对公账户"
                    productPriceName = productName
                    mold = Constants.P8
                    state = Constants.OS_UN_SUBMITTED
                    stateText = "未提交"
                    nativeExtraDraftObject = p8
                    createDt = getCreateDate(p8.createTime)
                }
            )
        }


        // P9
        val p9 = NativeFormPersonalPackageP9P10Draft.get()
        if (!p9.productId.isNullOrBlank() && p9.loginPhone == userPhone && p9.mold == Constants.P9) {
            list.add(
                Order().apply {
                    money = p9.finalMoney
                    productId = p9.productId
                    productPriceId = p9.productPriceId
                    productName = "个体户套餐"
                    productPriceName = productName
                    mold = Constants.P9
                    state = Constants.OS_UN_SUBMITTED
                    stateText = "未提交"
                    nativeExtraDraftObject = p9
                    createDt = getCreateDate(p9.createTime)
                }
            )
        }

        // P10
        val p10 = NativeFormPersonalPackageP9P10Draft.get()
        if (!p10.productId.isNullOrBlank() && p10.loginPhone == userPhone && p10.mold == Constants.P10) {
            list.add(
                Order().apply {
                    money = p10.finalMoney
                    productId = p10.productId
                    productPriceId = p10.productPriceId
                    productName = "个人独资企业套餐"
                    productPriceName = productName
                    mold = Constants.P10
                    state = Constants.OS_UN_SUBMITTED
                    stateText = "未提交"
                    nativeExtraDraftObject = p10
                    createDt = getCreateDate(p10.createTime)
                }
            )
        }


        return list


    }

    private fun getCreateDate(createTime: String): String {

        val currentTimeString = TimeUtil.getDateTimeFromMillisecond(System.currentTimeMillis())
        if (createTime.isBlank()) {
            return currentTimeString
        }

        return try {
            TimeUtil.getDateTimeFromMillisecond(createTime.toLong())
        } catch (e: Exception) {
            currentTimeString
        }

    }

}