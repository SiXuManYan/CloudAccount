package com.fatcloud.account.feature.product.detail.sheet

import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import butterknife.OnClick
import com.blankj.utilcode.util.ToastUtils
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
import com.jude.easyrecyclerview.adapter.BaseViewHolder
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter
import kotlinx.android.synthetic.main.fragment_product_sheet.*


/**
 * Created by Wangsw on 2020/6/9 17:01.
 * </br>
 * 产品详情，sheet
 */
class ProductSheetFragment : BaseBottomSheetDialogFragment<ProductSheetPresenter>(), ProductSheetView {

    private var productDetail: ProductDetail? = null
    private var adapter: RecyclerArrayAdapter<Price>? = null

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

        Glide.with(this)
            .load(productDetail?.logoImgUrl)
            .apply(
                RequestOptions().transform(
                    MultiTransformation(
                        CenterCrop(),
                        RoundTransFormation(context, 4)
                    )
                )
            )
            .error(R.drawable.ic_error_image_load)
            .into(image_iv)

        content_tv.text = productDetail?.name
        adapter = getRecyclerAdapter()
        adapter?.addAll(productDetail?.prices)
        content_rv.adapter = adapter
    }

    private fun getRecyclerAdapter(): RecyclerArrayAdapter<Price>? {
        val adapter = object : RecyclerArrayAdapter<Price>(context) {

            override fun OnCreateViewHolder(parent: ViewGroup?, viewType: Int): BaseViewHolder<Price> {
                return ProductSheetHolder(parent)
            }

        }

        adapter.setOnItemClickListener {
            VibrateUtils.vibrate(10)
            clearSelect()

            val allData = adapter.allData
            val model = allData[it]
            amount_tv.text = getString(R.string.money_symbol_format, model.money.stripTrailingZeros().toPlainString())
            model.nativeIsSelect = true
            adapter.notifyDataSetChanged()

        }
        return adapter
    }

    @OnClick(R.id.next_tv)
    fun click() {
        VibrateUtils.vibrate(10)
        val allData = adapter?.allData
        allData?.forEachIndexed { index, price ->

            if (price.nativeIsSelect) {
                ToastUtils.showShort("前往表单页面")
                return@forEachIndexed
            } else {
                if (index == allData.size - 1) {
                    ToastUtils.showShort("请选择套餐")
                    content_rv.startAnimation(CommonUtils.getShakeAnimation(2))

                }
            }
        }

    }

    private fun clearSelect(){
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

}