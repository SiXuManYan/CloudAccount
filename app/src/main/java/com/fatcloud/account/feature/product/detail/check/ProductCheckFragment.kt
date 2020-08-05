package com.fatcloud.account.feature.product.detail.check

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.CompoundButton
import butterknife.OnCheckedChanged
import butterknife.OnClick
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.request.RequestOptions
import com.fatcloud.account.R
import com.fatcloud.account.app.Glide
import com.fatcloud.account.base.ui.BaseBottomSheetDialogFragment
import com.fatcloud.account.common.Constants
import com.fatcloud.account.entity.product.Price
import com.fatcloud.account.entity.product.ProductDetail
import com.fatcloud.account.extend.RoundTransFormation
import com.fatcloud.account.feature.forms.personal.change.FormLicenseChangeActivity
import com.fatcloud.account.feature.forms.personal.change.FormLicenseChangeActivity.Companion.TYPE_CHANGE_NAME
import com.fatcloud.account.feature.forms.personal.change.FormLicenseChangeActivity.Companion.TYPE_CHANGE_NAME_AND_SCOPE
import com.fatcloud.account.feature.forms.personal.change.FormLicenseChangeActivity.Companion.TYPE_CHANGE_SCOPE
import kotlinx.android.synthetic.main.fragment_product_check.*
import java.lang.Exception
import java.math.BigDecimal

/**
 * Created by Wangsw on 2020/7/10 0010 13:25.
 * </br>
 * 名称变更选择
 */
class ProductCheckFragment : BaseBottomSheetDialogFragment<ProductCheckPresenter>(), ProductCheckView {


    private var productDetail: ProductDetail? = null
    private var finalMoney: BigDecimal = BigDecimal.ZERO
    private var productPriceId: String = ""
    private var mPrices: ArrayList<Price> = ArrayList()


    companion object {
        fun newInstance(productDetail: ProductDetail): ProductCheckFragment {
            val fragment = ProductCheckFragment()
            val args = Bundle()
            args.putSerializable(Constants.PARAM_DATA, productDetail)
            fragment.arguments = args
            return fragment
        }
    }


    override fun getLayoutId(): Int = R.layout.fragment_product_check


    override fun initViews(parent: View) {

        if (arguments != null) {
            productDetail = arguments!!.getSerializable(Constants.PARAM_DATA) as ProductDetail

            productDetail?.let {
                mPrices = it.prices
            }
        }

        productDetail?.let {

            amount_tv.text = getString(R.string.money_symbol_format, it.money.stripTrailingZeros()?.toPlainString())

            Glide.with(this)
                .load(it.logoImgUrl)
                .apply(RequestOptions().transform(MultiTransformation(CenterCrop(), RoundTransFormation(context, 4))))
                .error(R.drawable.ic_error_image_load)
                .into(image_iv)

            content_tv.text = it.name


            // 预先选中第一项
            val storeNameChangePrice = mPrices[0].childs[0]
            productPriceId = storeNameChangePrice.id
            finalMoney = storeNameChangePrice.money
            work_change_tax_rb.isChecked = true
            type_change_store_name_rb.isChecked = true


            try {



                // 营业执照变更
                work_change_tax_rb.text = mPrices[0].name

                // 营业执照+税务登记变更
                work_change_tax_and_license_rb.text = mPrices[1].name


                // 店铺名称变更
                type_change_store_name_rb.text = mPrices[0].childs[0].name

                // 经营范围变更
                type_change_business_scope_rb.text = mPrices[0].childs[1].name

                // 店铺名称 + 经营范围变更
                type_change_name_and_scope.text = mPrices[0].childs[2].name

            } catch (e: Exception) {

            }


        }


    }

    override fun loadOnVisible() = Unit

    @OnCheckedChanged(
        R.id.work_change_tax_rb,
        R.id.work_change_tax_and_license_rb,

        R.id.type_change_store_name_rb,
        R.id.type_change_business_scope_rb,
        R.id.type_change_name_and_scope
    )
    fun checkedChange(view: CompoundButton, isChanged: Boolean) {
        if (!isChanged) {
            return
        }
        when (view.id) {

            R.id.work_change_tax_rb -> {

                try {
                    // 店铺名称变更
                    type_change_store_name_rb.text = mPrices[0].childs[0].name

                    // 经营范围变更
                    type_change_business_scope_rb.text = mPrices[0].childs[1].name

                    // 店铺名称 + 经营范围变更
                    type_change_name_and_scope.text = mPrices[0].childs[2].name

                    // 更新价格
                    work_change_tax_and_license_rb.isChecked = false
                    getChangeStoreNameMoney()
                    getBusinessScopeChangeMoney()
                    getStoreNameAndScopeChangeMoney()


                } catch (e: Exception) {

                }


            }
            R.id.work_change_tax_and_license_rb -> {

                try {
                    // 店铺名称变更
                    type_change_store_name_rb.text = mPrices[1].childs[0].name

                    // 经营范围变更
                    type_change_business_scope_rb.text = mPrices[1].childs[1].name

                    // 店铺名称 + 经营范围变更
                    type_change_name_and_scope.text = mPrices[1].childs[2].name



                    // 更新价格
                    work_change_tax_rb.isChecked = false
                    getChangeStoreNameMoney()
                    getBusinessScopeChangeMoney()
                    getStoreNameAndScopeChangeMoney()


                } catch (e: Exception) {

                }

            }


            R.id.type_change_store_name_rb -> {
                type_change_business_scope_rb.isChecked = false
                type_change_name_and_scope.isChecked = false
                getChangeStoreNameMoney()

            }
            R.id.type_change_business_scope_rb -> {
                type_change_store_name_rb.isChecked = false
                type_change_name_and_scope.isChecked = false

                getBusinessScopeChangeMoney()


            }
            R.id.type_change_name_and_scope -> {

                type_change_store_name_rb.isChecked = false
                type_change_business_scope_rb.isChecked = false
                getStoreNameAndScopeChangeMoney()

            }
            else -> {
            }
        }
    }

    /**
     * 店铺名称+经营范围变更价格
     */
    private fun getStoreNameAndScopeChangeMoney() {
        if (!type_change_name_and_scope.isChecked) {
            return
        }
        if (work_change_tax_rb.isChecked) {
            val price = mPrices[0].childs[2]
            productPriceId = price.id
            finalMoney = price.money
        } else {
            val price = mPrices[1].childs[2]
            productPriceId = price.id
            finalMoney = price.money
        }
        amount_tv.text = getString(R.string.money_symbol_format, finalMoney.stripTrailingZeros()?.toPlainString())
    }

    /**
     * 经营范围变更价格
     */
    private fun getBusinessScopeChangeMoney() {
        if (!type_change_business_scope_rb.isChecked) {
            return
        }
        if (work_change_tax_rb.isChecked) {
            val price = mPrices[0].childs[1]
            productPriceId = price.id
            finalMoney = price.money
        } else {
            val price = mPrices[1].childs[1]
            productPriceId = price.id
            finalMoney = price.money
        }
        amount_tv.text = getString(R.string.money_symbol_format, finalMoney.stripTrailingZeros()?.toPlainString())
    }

    /**
     * 店铺名称变更 价格
     */
    private fun getChangeStoreNameMoney() {
        if (!type_change_store_name_rb.isChecked) {
            return
        }

        if (work_change_tax_rb.isChecked) {
            val price = mPrices[0].childs[0]
            productPriceId = price.id
            finalMoney = price.money
        }else{
            val price = mPrices[1].childs[0]
            productPriceId = price.id
            finalMoney = price.money
        }
        amount_tv.text = getString(R.string.money_symbol_format, finalMoney.stripTrailingZeros()?.toPlainString())

    }


    @OnClick(R.id.next_tv)
    fun click(view: View) {
        when (view.id) {
            R.id.next_tv -> {
                startActivity(
                    Intent(activity, FormLicenseChangeActivity::class.java)
                        .putExtra(Constants.PARAM_PRODUCT_ID, productDetail?.id)
                        .putExtra(Constants.PARAM_FINAL_MONEY, finalMoney.stripTrailingZeros().toPlainString())
                        .putExtra(Constants.PARAM_PRODUCT_PRICE_ID, productPriceId)
                        .putExtra(
                            Constants.PARAM_CHANGE_TYPE,
                            when {
                                type_change_store_name_rb.isChecked -> {
                                    TYPE_CHANGE_NAME
                                }
                                type_change_business_scope_rb.isChecked -> {
                                    TYPE_CHANGE_SCOPE
                                }
                                else -> {
                                    TYPE_CHANGE_NAME_AND_SCOPE
                                }
                            }
                        )
                )
                dismissAllowingStateLoss()
            }
            else -> {
            }
        }


    }


}