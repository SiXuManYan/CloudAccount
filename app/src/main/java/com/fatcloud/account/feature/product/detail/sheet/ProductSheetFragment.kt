package com.fatcloud.account.feature.product.detail.sheet

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import butterknife.OnClick
import com.blankj.utilcode.util.VibrateUtils
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.request.RequestOptions
import com.fatcloud.account.R
import com.fatcloud.account.app.Glide
import com.fatcloud.account.base.ui.BaseBottomSheetDialogFragment
import com.fatcloud.account.common.CommonUtils
import com.fatcloud.account.common.Constants
import com.fatcloud.account.entity.product.Price
import com.fatcloud.account.entity.product.ProductDetail
import com.fatcloud.account.extend.RoundTransFormation
import com.fatcloud.account.feature.forms.personal.license.FormLicensePersonalActivity
import com.fatcloud.account.feature.forms.personal.logout.FormLicenseLogoutActivity
import com.fatcloud.account.feature.forms.personal.tax.FormTaxRegistrationPersonalActivity
import com.jude.easyrecyclerview.adapter.BaseViewHolder
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter
import kotlinx.android.synthetic.main.fragment_product_sheet.*
import java.math.BigDecimal


/**
 * Created by Wangsw on 2020/6/9 17:01.
 * </br>
 * 产品详情，sheet
 */
class ProductSheetFragment : BaseBottomSheetDialogFragment<ProductSheetPresenter>(), ProductSheetView {

    private var productDetail: ProductDetail? = null
    private var adapter: RecyclerArrayAdapter<Price>? = null
    private var finalMoney: BigDecimal = BigDecimal.ZERO

    companion object {
        fun newInstance(productDetail: ProductDetail): ProductSheetFragment {
            val fragment = ProductSheetFragment()
            val args = Bundle()
            args.putSerializable(Constants.PARAM_DATA, productDetail)
            fragment.arguments = args
            return fragment
        }
    }


    override fun getLayoutId(): Int = R.layout.fragment_product_sheet

    override fun loadOnVisible() = Unit


    override fun initViews(parent: View) {
        if (arguments != null) {
            productDetail = arguments!!.getSerializable(Constants.PARAM_DATA) as ProductDetail
        }


        productDetail?.let {

            amount_tv.text = getString(R.string.money_symbol_format, it.money.stripTrailingZeros()?.toPlainString())

            Glide.with(this)
                .load(it.logoImgUrl)
                .apply(RequestOptions().transform(MultiTransformation(CenterCrop(), RoundTransFormation(context, 4))))
                .error(R.drawable.ic_error_image_load)
                .into(image_iv)

            content_tv.text = it.name
            adapter = getRecyclerAdapter()
            adapter?.addAll(it.prices)
            content_rv.adapter = adapter


            if (adapter != null) {
                performClick(0, adapter!!)
            }


            hint_tv.visibility = View.VISIBLE
            amount_hint_tv.visibility = View.VISIBLE

            when (it.mold) {

                Constants.P1,
                Constants.P4 -> {
                    hint_tv.visibility = View.VISIBLE
                    amount_hint_tv.visibility = View.GONE
                }

                Constants.P6 -> {
                    hint_tv.visibility = View.VISIBLE
                    amount_hint_tv.visibility = View.VISIBLE
                }

                else -> {
                    hint_tv.visibility = View.GONE
                    amount_hint_tv.visibility = View.GONE
                }
            }


        }


    }

    private fun getRecyclerAdapter(): RecyclerArrayAdapter<Price>? {
        val adapter = object : RecyclerArrayAdapter<Price>(context) {

            override fun OnCreateViewHolder(parent: ViewGroup?, viewType: Int): BaseViewHolder<Price> {
                return ProductSheetHolder(parent)
            }

        }

        adapter.setOnItemClickListener {
            VibrateUtils.vibrate(10)

//            clearSelect()
//            val allData = adapter.allData
//            val model = allData[it]
//            finalMoney = model.money
//            amount_tv.text = getString(R.string.money_symbol_format, finalMoney.stripTrailingZeros().toPlainString())
//            model.nativeIsSelect = true
//            adapter.notifyDataSetChanged()

            performClick(it, adapter)

        }
        return adapter
    }


    private fun performClick(position: Int, adapter: RecyclerArrayAdapter<Price>) {

        clearSelect()
        val allData = adapter.allData
        val model = allData[position]
        finalMoney = model.money
        amount_tv.text = getString(R.string.money_symbol_format, finalMoney.stripTrailingZeros().toPlainString())
        model.nativeIsSelect = true
        adapter.notifyDataSetChanged()
    }


    private fun clearSelect() {
        adapter?.allData?.forEachIndexed { index, price ->
            if (price.nativeIsSelect) {
                price.nativeIsSelect = false
            }
        }

    }


    override fun onDismiss(dialog: DialogInterface) {
        clearSelect()
        adapter?.notifyDataSetChanged()
        super.onDismiss(dialog)
    }


    @OnClick(R.id.next_tv)
    fun click() {
        VibrateUtils.vibrate(10)
        val allData = adapter?.allData
        allData?.forEachIndexed { index, price ->

            if (price.nativeIsSelect) {

                handleNext(price, index == 1, index)
                return@forEachIndexed
            } else {
                // 遍历到最后一条发现，用户一条都没选
                if (index == allData.size - 1) {
                    content_rv.startAnimation(CommonUtils.getShakeAnimation(2))
                }
            }
        }
    }

    private fun handleNext(price: Price?, extraAddSeal: Boolean, index: Int) {
        val finalMoneyStr = finalMoney.stripTrailingZeros().toPlainString()
        when (productDetail?.mold) {
            Constants.P1 -> {
                // 营业执照

                startActivity(
                    Intent(activity, FormLicensePersonalActivity::class.java)
                        .putExtra(Constants.PARAM_PRODUCT_ID, productDetail?.id)
                        .putExtra(Constants.PARAM_FINAL_MONEY, finalMoneyStr)
                        .putExtra(Constants.PARAM_PRODUCT_PRICE_ID, price?.id)
                )

            }
            Constants.P4 -> {
                // 税务登记
                startActivity(
                    Intent(activity, FormTaxRegistrationPersonalActivity::class.java)
                        .putExtra(Constants.PARAM_PRODUCT_ID, productDetail?.id)
                        .putExtra(Constants.PARAM_FINAL_MONEY, finalMoneyStr)
                        .putExtra(Constants.PARAM_PRODUCT_PRICE_ID, price?.id)
                )
            }
            Constants.P6 -> {
                // 个体户营业执照注销
                startActivity(
                    Intent(activity, FormLicenseLogoutActivity::class.java)
                        .putExtra(Constants.PARAM_PRODUCT_ID, productDetail?.id)
                        .putExtra(Constants.PARAM_FINAL_MONEY, finalMoneyStr)
                        .putExtra(Constants.PARAM_PRODUCT_PRICE_ID, price?.id)
                        .putExtra(Constants.PARAM_ADD_SEAL, extraAddSeal)
                        .putExtra(Constants.PARAM_INDEX, index)
                )
            }

            else -> {
            }
        }
        dismissAllowingStateLoss()
    }

}