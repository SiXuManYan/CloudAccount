package com.fatcloud.account.feature.forms.personal.bank

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.blankj.utilcode.util.ToastUtils
import com.fatcloud.account.R
import com.fatcloud.account.base.common.BasePresenter
import com.fatcloud.account.base.net.BaseHttpSubscriber
import com.fatcloud.account.common.Constants
import com.fatcloud.account.data.CloudDataBase
import com.fatcloud.account.entity.defray.prepare.PreparePay
import com.fatcloud.account.entity.local.form.BankPersonalDraft
import com.fatcloud.account.entity.order.IdentityImg
import com.fatcloud.account.entity.order.persional.BankPersonal
import com.fatcloud.account.entity.order.persional.NamePhoneBean
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import javax.inject.Inject

/**
 * Created by Wangsw on 2020/7/16 0016 11:02.
 * </br>
 *
 */
class FormPersonalBankPresenter @Inject constructor(private var view: FormPersonalBankView) : BasePresenter(view) {

    lateinit var database: CloudDataBase @Inject set

    private val gson = Gson()

    /**
     * 添加个体营业执照注销
     */
    fun addLicenseChangePersonal(lifecycle: LifecycleOwner, model: BankPersonal) {

        val bodyJsonStr = gson.toJson(model)

        val jsonObject: JsonObject = gson.fromJson(bodyJsonStr, JsonObject::class.java)

        requestApi(lifecycle, Lifecycle.Event.ON_DESTROY,
            apiService.addSelfemployedBank(jsonObject),
            object : BaseHttpSubscriber<PreparePay>(view) {
                override fun onSuccess(data: PreparePay?) {

                    data?.let {
                        view.commitSuccess(it)
                    }

                }

            }
        )
    }


    /**
     * 添加个体营业执照注销
     */
    fun addLicenseChangePersonalP9(lifecycle: LifecycleOwner, model: BankPersonal) {

        val bodyJsonStr = gson.toJson(model)

        val jsonObject: JsonObject = gson.fromJson(bodyJsonStr, JsonObject::class.java)

        requestApi(lifecycle, Lifecycle.Event.ON_DESTROY,
            apiService.addPersonalBankP9(jsonObject),
            object : BaseHttpSubscriber<JsonElement>(view) {
                override fun onSuccess(data: JsonElement?) {

                    view.commitSuccessP9P10()

                }

            }
        )
    }



    /**
     * 添加个体营业执照注销
     */
    fun addLicenseChangePersonalP10(lifecycle: LifecycleOwner, model: BankPersonal) {

        val bodyJsonStr = gson.toJson(model)

        val jsonObject: JsonObject = gson.fromJson(bodyJsonStr, JsonObject::class.java)

        requestApi(lifecycle, Lifecycle.Event.ON_DESTROY,
            apiService.addPersonalPackageBankP10(jsonObject),
            object : BaseHttpSubscriber<JsonElement>(view) {
                override fun onSuccess(data: JsonElement?) {

                    view.commitSuccessP9P10()

                }

            }
        )
    }



    fun saveDraft(
        productId: String?,
        orderWorkId: String?,
        accountNatureType: String,
        idImage: ArrayList<IdentityImg>,
        licenseImage: ArrayList<IdentityImg>,
        depositImage: ArrayList<IdentityImg>,
        personLegal: NamePhoneBean,
        personFinance: NamePhoneBean,
        personVerification1: NamePhoneBean,
        personVerification2: NamePhoneBean,
        personReconciliation: NamePhoneBean
    ) {


        database.bankPersonalDraftDao().apply {

            productId?.let {
                updateLegalPersonIdImageByProductId(image = idImage, productId = it)
            }
            orderWorkId?.let {
                updateLegalPersonIdImageByOrderWordId(image = idImage, orderWordId = it)
            }


            productId?.let {
                updateLicenseImageByProductId(image = licenseImage, productId = it)
            }
            orderWorkId?.let {
                updateLicenseImageByOrderWordId(image = licenseImage, orderWordId = it)
            }

            if (accountNatureType == Constants.AN1) {

                // 基本户才有存款账户信息

                productId?.let {
                    updateDepositImageByProductId(image = depositImage, productId = it)
                }

                orderWorkId?.let {
                    updateDepositImageByOrderWordId(image = depositImage, orderWordId = it)
                }
            }

            productId?.let {
                updatePersonLegalByProductId(model = personLegal, productId = it)
            }
            orderWorkId?.let {
                updatePersonLegalByOrderWordId(model = personLegal, orderWordId = it)
            }


            productId?.let {
                updatePersonFinanceByProductId(model = personFinance, productId = it)
            }


            orderWorkId?.let {
                updatePersonFinanceByOrderWordId(model = personFinance, orderWordId = it)
            }


            productId?.let {
                updatePersonVerification1ByProductId(model = personVerification1, productId = it)
            }

            orderWorkId?.let {
                updatePersonVerification1ByOrderWordId(model = personVerification1, orderWordId = it)
            }

            productId?.let {
                updatePersonVerification2ByProductId(model = personVerification2, productId = it)
            }

            orderWorkId?.let {
                updatePersonVerification2ByOrderWordId(model = personVerification2, orderWordId = it)
            }

            productId?.let {
                updatePersonReconciliationByProductId(model = personReconciliation, productId = it)
            }
            orderWorkId?.let {
                updatePersonReconciliationByOrderWordId(model = personReconciliation, orderWordId = it)
            }


        }
        BankPersonalDraft.update()
        ToastUtils.showShort(R.string.save_success)

    }




}