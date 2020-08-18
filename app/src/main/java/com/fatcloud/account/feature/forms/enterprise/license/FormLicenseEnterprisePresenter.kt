package com.fatcloud.account.feature.forms.enterprise.license

import android.content.Context
import android.view.View
import android.widget.LinearLayout
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.blankj.utilcode.util.ToastUtils
import com.blankj.utilcode.util.VibrateUtils
import com.fatcloud.account.R
import com.fatcloud.account.base.common.BasePresenter
import com.fatcloud.account.base.net.BaseHttpSubscriber
import com.fatcloud.account.common.BigDecimalUtil
import com.fatcloud.account.common.Constants
import com.fatcloud.account.common.ProductUtils
import com.fatcloud.account.entity.defray.prepare.PreparePay
import com.fatcloud.account.entity.order.enterprise.EnterpriseInfo
import com.fatcloud.account.entity.order.enterprise.Shareholder
import com.fatcloud.account.view.CompanyMemberEditView
import com.google.gson.Gson
import com.google.gson.JsonObject
import java.lang.Exception
import java.math.BigDecimal
import javax.inject.Inject

/**
 * Created by Wangsw on 2020/6/10 0010 18:30.
 * </br>
 *
 */
class FormLicenseEnterprisePresenter @Inject constructor(private var view: FormLicenseEnterpriseView) :
    BasePresenter(view) {

    private val gson = Gson()

    fun getShareholderView(index: Int, context: Context, shareholderMoreContainer: LinearLayout): CompanyMemberEditView {

        return CompanyMemberEditView(context).apply {
            id = index + 1 // 保证id 不从0开始
            currentMold = Constants.SH3
            tag = index  // 坐标
            mIsExtra = true
            initHighlightTitle(context.getString(R.string.shareholder_info2))
            initNameTitle(context.getString(R.string.shareholder_name))

            initIdAddressHint("请输入股东身份证地址")
            initPhoneHint("请输入股东联系电话")
            initShareRatioHint(context.getString(R.string.share_ratio_hint))

            //  添加股东
            showAddActionView().setOnClickListener {
                VibrateUtils.vibrate(10)
                it.visibility = View.GONE
                shareholderMoreContainer.addView(getShareholderView(index + 1, context, shareholderMoreContainer), index + 1)
            }

            // 移除当前股东
            showHighlightDeleteView().setOnClickListener {
                shareholderMoreContainer.removeView(shareholderMoreContainer.findViewById(id))
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
        financialManagerView: CompanyMemberEditView,

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
        if (!ProductUtils.hasIdCardUrl(legalPersonView.frontImageUrl, true, "法人")) {
            return
        }
        if (!ProductUtils.hasIdCardUrl(legalPersonView.backImageUrl, false, "法人")) {
            return
        }

        if (legalPersonView.getNameValue().isBlank()) {
            ToastUtils.showShort("请输入法人姓名")
            return
        }
        val idNumberValue = legalPersonView.getIdNumberValue()
        if (idNumberValue.isBlank()) {
            ToastUtils.showShort("请输入法人身份证号")
            return
        }
        if (!ProductUtils.isIdCardNumber(idNumberValue, "法人")) {
            return
        }
        val expiryDateValue = legalPersonView.getExpiryDateValue()
        if (expiryDateValue.isBlank()) {
            ToastUtils.showShort("请输入法人身份证有效期")
            return
        }


        if (legalPersonView.getIdAddressValue().isBlank()) {
            ToastUtils.showShort("请输入法人身份证地址")
            return
        }

        val phoneValue = legalPersonView.getPhoneValue()
        if (phoneValue.isBlank()) {
            ToastUtils.showShort("请输入法人联系电话")
            return
        }
        if (!ProductUtils.isPhoneNumber(phoneValue, "法人")) {
            return
        }


        val legalRatio = legalPersonView.getShareRatioValue()
        if (legalRatio.isBlank()) {
            ToastUtils.showShort("请输入法人股份占比")
            return
        }

        // 监事
        if (!ProductUtils.hasIdCardUrl(supervisorView.frontImageUrl, true, "监事")) {
            return
        }
        if (!ProductUtils.hasIdCardUrl(supervisorView.backImageUrl, false, "监事")) {
            return
        }

        if (supervisorView.getNameValue().isBlank()) {
            ToastUtils.showShort("请输入监事姓名")
            return
        }
        val idNumberValue1 = supervisorView.getIdNumberValue()
        if (idNumberValue1.isBlank()) {
            ToastUtils.showShort("请输入监事身份证号")
            return
        }

        if (!ProductUtils.isIdCardNumber(idNumberValue1, "监事")) {
            return
        }

        if (supervisorView.getIdAddressValue().isBlank()) {
            ToastUtils.showShort("请输入监事身份证地址")
            return
        }
        val phoneValue1 = supervisorView.getPhoneValue()
        if (phoneValue1.isBlank()) {
            ToastUtils.showShort("请输入监事联系电话")
            return
        }

        if (!ProductUtils.isPhoneNumber(phoneValue1, "监事")) {
            return
        }
        // 财务负责人


        if (!ProductUtils.hasIdCardUrl(financialManagerView.frontImageUrl, true, "财务负责人")) {
            return
        }
        if (!ProductUtils.hasIdCardUrl(financialManagerView.backImageUrl, false, "财务负责人")) {
            return
        }

        if (financialManagerView.getNameValue().isBlank()) {
            ToastUtils.showShort("请输入财务负责人姓名")
            return
        }
        val idStr = financialManagerView.getIdNumberValue()
        if (idStr.isBlank()) {
            ToastUtils.showShort("请输入财务负责人身份证号")
            return
        }

        if (!ProductUtils.isIdCardNumber(idStr, "财务负责人")) {
            return
        }

        if (financialManagerView.getIdAddressValue().isBlank()) {
            ToastUtils.showShort("请输入财务负责人身份证地址")
            return
        }
        val phoneStr = financialManagerView.getPhoneValue()
        if (phoneStr.isBlank()) {
            ToastUtils.showShort("请输入财务负责人联系电话")
            return
        }

        if (!ProductUtils.isPhoneNumber(phoneStr, "财务负责人")) {
            return
        }


        // 股东（变为选填，不校验）
        var holderRatio = "0"


        val defaultHolderImageUrlFront = shareholderView.frontImageUrl
        val defaultHolderImageUrlBack = shareholderView.backImageUrl
        val defaultHolderNameValue = shareholderView.getNameValue()
        val defaultHolderIdNumber = shareholderView.getIdNumberValue()
        val defaultIdAddressValue = shareholderView.getIdAddressValue()
        val defaultHolderPhone = shareholderView.getPhoneValue()
        val defaultHolderRatioValue = shareholderView.getShareRatioValue()


        val defaultHolderValueAccess = defaultHolderValueAccess(
            defaultHolderImageUrlFront,
            defaultHolderImageUrlBack,
            defaultHolderNameValue,
            defaultHolderIdNumber,
            defaultIdAddressValue,
            defaultHolderPhone,
            defaultHolderRatioValue
        )
        if (!defaultHolderValueAccess) {
            return
        }

        val defaultHolderNotBlank = (defaultHolderImageUrlFront.isNotBlank() && defaultHolderImageUrlBack.isNotBlank() && defaultHolderNameValue.isNotBlank()
                && defaultHolderIdNumber.isNotBlank() && defaultIdAddressValue.isNotBlank() && defaultIdAddressValue.isNotBlank()
                && defaultHolderPhone.isNotBlank() && defaultHolderRatioValue.isNotBlank())


        if (shareholderMoreContainer.childCount > 0) {
            val max = shareholderMoreContainer.childCount
            for (i in 0 until max) {
                val moreHolder = shareholderMoreContainer.getChildAt(i) as CompanyMemberEditView

                if (!defaultHolderValueAccess(
                        moreHolder.frontImageUrl,
                        moreHolder.backImageUrl,
                        moreHolder.getNameValue(),
                        moreHolder.getIdNumberValue(),
                        moreHolder.getIdAddressValue(),
                        moreHolder.getPhoneValue(),
                        moreHolder.getShareRatioValue()
                    )
                ) {
                    return
                }


            }

        }


        // 计算股份占比
        holderRatio = defaultHolderRatioValue

        var legal_ratio = BigDecimal.ZERO
        var supervisor_ratio = BigDecimal.ZERO
        var holder_ratio = BigDecimal.ZERO
        try {
            legal_ratio = BigDecimal(legalRatio)
            supervisor_ratio = if (!supervisorView.getShareRatioValue().isNullOrBlank()) {
                BigDecimal(supervisorView.getShareRatioValue())
            } else {
                BigDecimal.ZERO
            }
            holder_ratio = BigDecimal(holderRatio)

        } catch (e: Exception) {

        }

        val ratioSum =
            BigDecimalUtil.add(
                BigDecimalUtil.add(legal_ratio, supervisor_ratio),
                BigDecimalUtil.add(holder_ratio, getExtraHolderRatio(shareholderMoreContainer))
            )

        if (ratioSum.compareTo(BigDecimal(100)) == 1) {
            ToastUtils.showShort("股份占比总和不能大于100%")
            return
        }



        if (ProductUtils.hasDuplicateName(
                getExtraNames(shareholderMoreContainer),
                legalPersonView.getNameValue(),
                supervisorView.getNameValue(),
                defaultHolderNameValue,
                financialManagerView.getNameValue()
            )
        ) {
            return
        }


        val enterpriseInfo = EnterpriseInfo().apply {
            addr = detailAddress
            area = areaName
            bankNo = bankNumber
            this.bankPhone = bankPhone
            businessScope?.addAll(selectPid)
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
                legalPersonViewHolder = legalPersonView.getShareHolder(),
                supervisorHolder = supervisorView.getShareHolder(),
                financialManagerHolder = financialManagerView.getShareHolder(),
                defaultHolder = if (defaultHolderNotBlank) {
                    shareholderView.getShareHolder()
                } else {
                    null
                },
                shareholderMoreContainer = shareholderMoreContainer
            )
        }
        addEnterprise(lifecycle, enterpriseInfo)

    }

    private fun defaultHolderValueAccess(
        defaultHolderImageUrlFront: String,
        defaultHolderImageUrlBack: String,
        defaultHolderNameValue: String,
        defaultHolderIdNumber: String,
        defaultIdAddressValue: String,
        defaultHolderPhone: String,
        defaultHolderRatioValue: String
    ): Boolean {

        // 有值才校验
        if (defaultHolderImageUrlFront.isNotBlank() || defaultHolderImageUrlBack.isNotBlank() || defaultHolderNameValue.isNotBlank()
            || defaultHolderIdNumber.isNotBlank() || defaultIdAddressValue.isNotBlank() || defaultIdAddressValue.isNotBlank()
            || defaultHolderPhone.isNotBlank() || defaultHolderRatioValue.isNotBlank()
        ) {

            if (!ProductUtils.hasIdCardUrl(defaultHolderImageUrlFront, true, "股东")) {
                return false
            }

            if (!ProductUtils.hasIdCardUrl(defaultHolderImageUrlBack, false, "股东")) {
                return false
            }

            if (defaultHolderNameValue.isBlank()) {
                ToastUtils.showShort("请输入股东姓名")
                return false
            }

            if (defaultHolderIdNumber.isBlank()) {
                ToastUtils.showShort("请输入股东身份证号")
                return false
            }
            if (!ProductUtils.isIdCardNumber(defaultHolderIdNumber, "股东")) {
                return false
            }


            if (defaultIdAddressValue.isBlank()) {
                ToastUtils.showShort("请输入股东身份证地址")
                return false
            }

            if (defaultHolderPhone.isBlank()) {
                ToastUtils.showShort("请输入股东联系电话")
                return false
            }

            if (!ProductUtils.isPhoneNumber(defaultHolderPhone, "股东")) {
                return false
            }


            if (defaultHolderRatioValue.isBlank()) {
                ToastUtils.showShort("请输入股东股份占比")
                return false
            }

        }
        return true
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
        legalPersonViewHolder: Shareholder, supervisorHolder: Shareholder, financialManagerHolder: Shareholder,
        defaultHolder: Shareholder?, shareholderMoreContainer: LinearLayout
    ): List<Shareholder> {

        val holders: ArrayList<Shareholder> = ArrayList()
        holders.add(legalPersonViewHolder)
        holders.add(supervisorHolder)
        holders.add(financialManagerHolder)

        defaultHolder?.let {
            holders.add(it)
        }

        if (shareholderMoreContainer.childCount > 0) {
            val max = shareholderMoreContainer.childCount
            for (i in 0 until max) {
                val companyMemberEditView =
                    shareholderMoreContainer.getChildAt(i) as CompanyMemberEditView
                holders.add(companyMemberEditView.getShareHolder())
            }
        }


        return holders

    }

    fun getExtraHolderRatio(shareholderMoreContainer: LinearLayout): BigDecimal {

        var ratioSum = BigDecimal.ZERO

        if (shareholderMoreContainer.childCount > 0) {
            try {
                val max = shareholderMoreContainer.childCount
                for (i in 0 until max) {
                    val companyMemberEditView =
                        shareholderMoreContainer.getChildAt(i) as CompanyMemberEditView

                    val shareRatioValue = companyMemberEditView.getShareRatioValue()
                    if (!shareRatioValue.isNullOrBlank()) {
                        ratioSum = BigDecimalUtil.add(BigDecimal(shareRatioValue), ratioSum)
                    }
                }
            } catch (e: Exception) {

            }
        }
        return ratioSum
    }


    private fun getExtraNames(shareholderMoreContainer: LinearLayout): List<String> {

        val names: ArrayList<String> = ArrayList()
        if (shareholderMoreContainer.childCount > 0) {
            val max = shareholderMoreContainer.childCount
            for (i in 0 until max) {
                val companyMemberEditView = shareholderMoreContainer.getChildAt(i) as CompanyMemberEditView
                names.add(companyMemberEditView.getNameValue())
            }

        }
        return names
    }


}