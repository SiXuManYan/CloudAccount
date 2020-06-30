package com.fatcloud.account.feature.forms.enterprise.license

import android.content.Context
import android.view.View
import android.widget.LinearLayout
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.blankj.utilcode.util.ScreenUtils
import com.blankj.utilcode.util.ToastUtils
import com.blankj.utilcode.util.VibrateUtils
import com.fatcloud.account.R
import com.fatcloud.account.base.common.BasePresenter
import com.fatcloud.account.base.net.BaseHttpSubscriber
import com.fatcloud.account.common.Constants
import com.fatcloud.account.common.ProductUtils
import com.fatcloud.account.entity.defray.prepare.PreparePay
import com.fatcloud.account.entity.order.enterprise.EnterpriseInfo
import com.fatcloud.account.entity.order.enterprise.Shareholder
import com.fatcloud.account.view.CompanyMemberEditView
import com.google.gson.Gson
import com.google.gson.JsonObject
import javax.inject.Inject

/**
 * Created by Wangsw on 2020/6/10 0010 18:30.
 * </br>
 *
 */
class FormLicenseEnterprisePresenter @Inject constructor(private var view: FormLicenseEnterpriseView) : BasePresenter(view) {

    private val gson = Gson()

     fun getShareholderView(index: Int, context: Context, shareholderMoreContainer: LinearLayout): CompanyMemberEditView {
        return CompanyMemberEditView(context).apply {
            id = index + 1 // 保证id 不从0开始
            currentMold = Constants.SH3
            // 坐标
            tag = index
            initHighlightTitle(context.getString(R.string.shareholder_info2))
            initNameTitle(context.getString(R.string.shareholder_name))

            initIdAddressHint("请输入股东身份证地址")
            initPhoneHint("请输入股东联系电话")
            initShareRatioHint(context.getString(R.string.share_ratio_hint))

            //  添加股东
            showAddActionView().setOnClickListener {
                VibrateUtils.vibrate(10)
                it.visibility = View.GONE
                shareholderMoreContainer.addView(getShareholderView(index + 1,context,shareholderMoreContainer), index + 1)

            }

            // 移除当前股东
            showHighlightDeleteView().setOnClickListener {
                shareholderMoreContainer.removeViewAt(index)
            }

        }
    }


    /**
     * 添加企业套餐
     */
    fun handlePost(
        lifecycle: LifecycleOwner,
        legalPersonView: CompanyMemberEditView,
        supervisorView: CompanyMemberEditView,
        shareholderView: CompanyMemberEditView,
        detailAddress: String,
        areaName: String,
        bankNumber: String,
        bankPhone: String,
        selectPid: ArrayList<String>,
        zeroName: String,
        firstName: String,
        secondName: String,
        incomeMoney: String,
        investMoney: String,

        investmentYear: String,
        finalMoney: String,
        productId: String,
        productPriceId: String,
        shareholderMoreContainer: LinearLayout
    ) {

        // 法人
        if (legalPersonView.getNameValue().isBlank()) {
            ToastUtils.showShort("请输入法人姓名")
            return
        }
        if (legalPersonView.getIdNumberValue().isBlank()) {
            ToastUtils.showShort("请输入法人身份证号")
            return
        }

        if (legalPersonView.getIdAddressValue().isBlank()) {
            ToastUtils.showShort("请输入法人身份证地址")
            return
        }

        if (legalPersonView.getPhoneValue().isBlank()) {
            ToastUtils.showShort("请输入法人联系电话")
            return
        }

        if (legalPersonView.getShareRatioValue().isBlank()) {
            ToastUtils.showShort("请输入法人股份占比")
            return
        }

        // 监事
        if (supervisorView.getNameValue().isBlank()) {
            ToastUtils.showShort("请输入监事姓名")
            return
        }
        if (supervisorView.getIdNumberValue().isBlank()) {
            ToastUtils.showShort("请输入监事身份证号")
            return
        }
        if (supervisorView.getIdAddressValue().isBlank()) {
            ToastUtils.showShort("请输入监事身份证地址")
            return
        }
        if (supervisorView.getPhoneValue().isBlank()) {
            ToastUtils.showShort("请输入监事联系方式")
            return
        }

        if (shareholderView.getNameValue().isBlank()) {
            ToastUtils.showShort("请输入股东姓名")
            return
        }
        if (shareholderView.getIdNumberValue().isBlank()) {
            ToastUtils.showShort("请输入股东身份证号")
            return
        }
        if (shareholderView.getIdAddressValue().isBlank()) {
            ToastUtils.showShort("请输入股东身份证地址")
            return
        }
        if (shareholderView.getPhoneValue().isBlank()) {
            ToastUtils.showShort("请输入股东联系方式")
            return
        }
        if (shareholderView.getShareRatioValue().isBlank()) {
            ToastUtils.showShort("请输入股东股份占比")
            return
        }


        val enterpriseInfo = EnterpriseInfo().apply {
            addr = detailAddress
            area = areaName
            bankNo = bankNumber
            this.bankPhone = bankPhone
            businessScope?.addAll(ProductUtils.stringList2IntList(selectPid))
            enterpriseName0 = zeroName
            enterpriseName1 = firstName
            enterpriseName2 = secondName
            income = ProductUtils.getEditValueToBigDecimal(incomeMoney)
            this.investMoney = ProductUtils.getEditValueToBigDecimal(investMoney)
            investYearNum = investmentYear
            money = ProductUtils.getEditValueToBigDecimal(finalMoney)
            this.productId = productId
            this.productPriceId = productPriceId.toInt()
            shareholders = getShareHolders(
                legalPersonView.getShareHolder(),
                supervisorView.getShareHolder(),
                shareholderView.getShareHolder(),
                shareholderMoreContainer
            )
        }
        addEnterprise(lifecycle, enterpriseInfo)

    }

    /**
     * 添加企业套餐
     */
    fun addEnterprise(lifecycle: LifecycleOwner, enterpriseInfo: EnterpriseInfo) {

        val bodyJsonStr = gson.toJson(enterpriseInfo)

        val jsonObject: JsonObject = gson.fromJson(bodyJsonStr, JsonObject::class.java)

        requestApi(lifecycle, Lifecycle.Event.ON_DESTROY,
            apiService.addEnterprise(jsonObject),
            object : BaseHttpSubscriber<PreparePay>(view) {
                override fun onSuccess(data: PreparePay?) {
                    data?.let {
                        view.addEnterpriseSuccess(it)
                    }
                }
            }
        )
    }

    /**
     * 获取企业股东类型 集合信息
     */
    fun getShareHolders(
        shareHolder: Shareholder,
        shareHolder1: Shareholder,
        shareHolder2: Shareholder,
        shareholderMoreContainer: LinearLayout
    ): List<Shareholder> {

        val holders: ArrayList<Shareholder> = ArrayList()
        holders.add(shareHolder)
        holders.add(shareHolder1)
        holders.add(shareHolder2)

        val max = shareholderMoreContainer.childCount - 1
        if (max > 0) {
            for (i in 0 until max) {
                val companyMemberEditView = shareholderMoreContainer.getChildAt(i) as CompanyMemberEditView
                holders.add(companyMemberEditView.getShareHolder())
            }
        }
        return holders

    }


}