package com.fatcloud.account.feature.product.detail.input

import android.content.Intent
import android.os.Bundle
import android.view.View
import butterknife.OnClick
import com.blankj.utilcode.util.ToastUtils
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.request.RequestOptions
import com.fatcloud.account.R
import com.fatcloud.account.app.Glide
import com.fatcloud.account.base.ui.BaseBottomSheetDialogFragment
import com.fatcloud.account.common.Constants
import com.fatcloud.account.entity.defray.prepare.PreparePay
import com.fatcloud.account.entity.product.ProductDetail
import com.fatcloud.account.extend.LimitInputTextWatcher
import com.fatcloud.account.extend.RoundTransFormation
import com.fatcloud.account.feature.defray.prepare.PayPrepareActivity
import kotlinx.android.synthetic.main.fragment_product_input.*
import java.math.BigDecimal

/**
 * Created by Wangsw on 2020/7/28 0028 9:36.
 * </br>
 *
 */
class ProductInputFragment : BaseBottomSheetDialogFragment<ProductInputPresenter>(), ProductInputView {

    private var finalMoney: BigDecimal = BigDecimal.ZERO
    private var productDetail: ProductDetail? = null

    override fun getLayoutId() = R.layout.fragment_product_input


    companion object {
        fun newInstance(productDetail: ProductDetail): ProductInputFragment {
            val fragment = ProductInputFragment()
            val args = Bundle()
            args.putSerializable(Constants.PARAM_DATA, productDetail)
            fragment.arguments = args
            return fragment
        }
    }


    override fun loadOnVisible() = Unit

    override fun initViews(parent: View) {
        if (arguments != null) {
            productDetail = arguments!!.getSerializable(Constants.PARAM_DATA) as ProductDetail
        }

        name_et.addTextChangedListener(LimitInputTextWatcher(name_et))

        productDetail?.let {

            amount_tv.text = getString(R.string.money_symbol_format, it.money.stripTrailingZeros()?.toPlainString())

            Glide.with(this)
                .load(it.logoImgUrl)
                .apply(RequestOptions().transform(MultiTransformation(CenterCrop(), RoundTransFormation(context, 4))))
                .error(R.drawable.ic_error_image_load)
                .into(image_iv)

            content_tv.text = it.name
            finalMoney = it.money

        }
    }


    @OnClick(R.id.next_tv)
    fun click(view: View) {
        when (view.id) {
            R.id.next_tv -> {
                val name = name_et.text.toString().trim()
                if (name.isBlank()) {
                    ToastUtils.showShort("请输入营业执照名称")
                    return
                }

                val id = productDetail?.id
                val priceId = productDetail?.prices

                priceId?.let {
                    if (it.isNotEmpty()) {
                        val money = finalMoney.stripTrailingZeros().toPlainString()
                        presenter.addEmployedTaxAssessmentP11(this,name, money, id, it[0].id)
                    }
                }
            }
            else -> {
            }
        }

    }

    override fun commitSuccess(preparePay: PreparePay) {
        ToastUtils.showShort("提交成功")
        startActivity(
            Intent(activity, PayPrepareActivity::class.java)
                .putExtra(Constants.PARAM_ORDER_ID, preparePay.orderId)
                .putExtra(Constants.PARAM_ORDER_NUMBER, preparePay.orderNo)
                .putExtra(Constants.PARAM_MONEY, preparePay.money.stripTrailingZeros().toPlainString())
                .putExtra(Constants.PARAM_IMAGE_URL, preparePay.productLogoImgUrl)
                .putExtra(Constants.PARAM_PRODUCT_NAME, preparePay.productName)
                .putExtra(Constants.PARAM_DATE, preparePay.createDt)
                .putExtra(Constants.PARAM_MOLD, Constants.P1)
        )
       dismissAllowingStateLoss()
    }


}