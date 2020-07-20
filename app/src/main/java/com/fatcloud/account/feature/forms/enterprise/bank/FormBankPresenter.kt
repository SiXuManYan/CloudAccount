package com.fatcloud.account.feature.forms.enterprise.bank

import android.content.Context
import android.widget.LinearLayout
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.fatcloud.account.R
import com.fatcloud.account.base.common.BasePresenter
import com.fatcloud.account.base.net.BaseHttpSubscriber
import com.fatcloud.account.common.Constants
import com.fatcloud.account.entity.order.enterprise.EnterpriseInfo
import com.fatcloud.account.entity.order.enterprise.Shareholder
import com.fatcloud.account.view.CompanyMemberEditView
import com.google.gson.JsonElement
import javax.inject.Inject

/**
 * Created by Wangsw on 2020/6/15 0015 15:08.
 * </br>
 *
 */
class FormBankPresenter @Inject constructor(private var view: FormBankView) : BasePresenter(view) {


    /**
     * 获取股东view
     */
    fun getShareholderView(context: Context, container: LinearLayout, index: Int, shareholder: Shareholder): CompanyMemberEditView {

        return CompanyMemberEditView(context).apply {

            id = index + 1    // 保证id 不从0开始
            tag = index
            currentMold = shareholder.mold

            var highlightTitle = ""
            var nameTitle = ""
            when (currentMold) {
                Constants.SH1 -> {
                    highlightTitle = "法人信息"
                    nameTitle = "法人姓名"
                    showIdExpirationDate()
                    setExpiryDateValue(shareholder.idnoDate,false)
                }
                Constants.SH2 -> {
                    highlightTitle = "监事信息"
                    nameTitle = "财务姓名"
                }
                else -> {
                    highlightTitle = "股东信息"
                    nameTitle = "股东姓名"
                }
            }

            initHighlightTitle(highlightTitle)
            initNameTitleValue(nameTitle, shareholder.name)
            setIdNumberValue( shareholder.idno)
            initIdAddressTitleValue(context.getString(R.string.id_address), shareholder.idnoAddr)
            initPhoneTitleValue(context.getString(R.string.contact_number), shareholder.phone)
            initShareRatioValue(
                shareholder.shareProportion
            )
            setServerImage(shareholder.imgs)
            hideUploadTagImage()
            disableImageViewClick()
            displayImageSwitcher()

        }
    }


    /**
     * 获取银行对公账户默认信息
     */
    fun getBankInfo(lifecycleOwner: LifecycleOwner, orderWorkId: String?) {

        requestApi(lifecycleOwner, Lifecycle.Event.ON_DESTROY, apiService.getEnterpriseOrderDetail2(orderWorkId),

            object : BaseHttpSubscriber<EnterpriseInfo>(view) {

                override fun onSuccess(data: EnterpriseInfo?) {
                    data?.let {
                        view.bindDetailInfo(data)
                    }
                }

                override fun onError(e: Throwable) {
                    // 接口未上线 404
                    onComplete()
                }
            }
        )
    }


    /**
     *
     * 添加具体流程的内容(如，开立银行对公账户)
     * @param orderWorkId               订单流程状态类型
     * @param businessLicenseImgUrl     公司营业执照
     * @param capital                   企业注册资金
     * @param electronicSealImgUrl      电子公章
     * @param enterpriseAddr            公司地址
     * @param enterpriseMold            企业性质
     * @param enterpriseName            公司名
     * @param financeIdno               财务负责人身份证号
     * @param financeIdnoImgUrlA        财务负责人身份证正面
     * @param financeIdnoImgUrlB        财务负责人身份证背面不能为空
     * @param financeName               财务负责人姓名
     * @param financePhone              财务负责人电话
     * @param financeShares             财务负责人股份比例
     * @param legalPersonWarrantImgUrl  法人签字授权书
     * @param reconciliatAddr           对账单收货地址详细
     * @param reconciliatArea           对账单收货地址区域
     * @param reconciliatContact        对账联系人
     * @param reconciliatPhone          对账联系人电话
     * @see Constants.OW1
     * @see Constants.OW2
     * @see Constants.OW3
     * @see Constants.OW4
     */
    fun addSpecificProcessContent(
        lifecycleOwner: LifecycleOwner,
        orderWorkId: String?,
        businessLicenseImgUrl: String?,
        capital: String?,
        electronicSealImgUrl: String?,
        enterpriseAddr: String?,
        enterpriseMold: String?,
        enterpriseName: String?,
        financeIdno: String?,
        financeIdnoImgUrlA: String?,
        financeIdnoImgUrlB: String?,
        financeName: String?,
        financePhone: String?,
        financeShares: String?,
        legalPersonWarrantImgUrl: String?,
        reconciliatAddr: String?,
        reconciliatArea: String?,
        reconciliatContact: String?,
        reconciliatPhone: String?,
        postcodeValue: String?
    ) {

        requestApi(lifecycleOwner, Lifecycle.Event.ON_DESTROY,
            apiService.addSpecificProcessContent(
                orderWorkId = orderWorkId,
                businessLicenseImgUrl = businessLicenseImgUrl,
                capital = capital,
                electronicSealImgUrl = electronicSealImgUrl,
                enterpriseAddr = enterpriseAddr,
                enterpriseMold = enterpriseMold,
                enterpriseName = enterpriseName,
                financeIdno = financeIdno,
                financeIdnoImgUrlA = financeIdnoImgUrlA,
                financeIdnoImgUrlB = financeIdnoImgUrlB,
                financeName = financeName,
                financePhone = financePhone,
                financeShares = financeShares,
                legalPersonWarrantImgUrl = legalPersonWarrantImgUrl,
                reconciliatAddr = reconciliatAddr,
                reconciliatArea = reconciliatArea,
                reconciliatContact = reconciliatContact,
                reconciliatPhone = reconciliatPhone,
                postcode = postcodeValue
            ),

            object : BaseHttpSubscriber<JsonElement>(view) {

                override fun onSuccess(data: JsonElement?) {
                    view.addSuccess()

                }
            }
        )
    }


}