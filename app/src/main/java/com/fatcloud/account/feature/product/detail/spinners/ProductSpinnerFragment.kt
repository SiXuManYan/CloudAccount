package com.fatcloud.account.feature.product.detail.spinners

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.AdapterView
import butterknife.OnClick
import com.blankj.utilcode.util.VibrateUtils
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.request.RequestOptions
import com.fatcloud.account.R
import com.fatcloud.account.app.Glide
import com.fatcloud.account.base.ui.BaseBottomSheetDialogFragment
import com.fatcloud.account.common.BigDecimalUtil
import com.fatcloud.account.common.CommonUtils
import com.fatcloud.account.common.Constants
import com.fatcloud.account.entity.product.Price
import com.fatcloud.account.entity.product.ProductDetail
import com.fatcloud.account.extend.RoundTransFormation
import com.fatcloud.account.feature.forms.enterprise.license.FormLicenseEnterpriseActivity
import com.fatcloud.account.feature.forms.personal.bookkeeping.FormAgentBookkeepingPersonalActivity
import kotlinx.android.synthetic.main.fragment_product_spinner.*
import java.math.BigDecimal


/**
 * Created by Admin on 2020/6/10 17:01.
 * </br>
 * 代理记账 类别选择表单
 */
class ProductSpinnerFragment : BaseBottomSheetDialogFragment<ProductSpinnerPresenter>(), ProductSpinnerView {

    private var productDetail: ProductDetail? = null

    /**
     * 个人代理记账，最终金额倍数 4
     */
    private val multiplePersonal = 4

    /**
     * 企业代理记账，最终金额倍数 12
     */
    private val multipleEnterprise = 12


    /**
     * 个人或企业代理记账，超过2000万收入时的最终金额倍数 0.1%
     * 注意，超过2000万时，后台返回的Money为 0.001，
     * 需要用前台的2000万来乘以倍数
     */
    private val multipleBigIncome = 20000000


    /**
     * 营业执照价格 298
     */
    private val priceBusinessLicense = 298

    /**
     * 税务登记价格
     */
    private val priceTaxRegistration = 0

    /**
     * 银行对公账户价格
     */
    private val priceBankPublicAccount = 298


    /**
     * 刻章价格
     */
    private val priceSeal = 420

    /**
     * 当前选中的第三项
     */
    private var thirdProductPriceId = ""

    private var isFirstSelect = false
    private var isSecondSelect = false
    private var isThirdSelect = false

    private var incomeMoney: BigDecimal = BigDecimal.ZERO

    /**
     *
     */
    private var mFinalMoney: BigDecimal = BigDecimal.ZERO



    companion object {
        fun newInstance(productDetail: ProductDetail): ProductSpinnerFragment {
            val fragment = ProductSpinnerFragment()
            val args = Bundle()
            args.putSerializable(Constants.PARAM_DATA, productDetail)
            fragment.arguments = args
            return fragment
        }
    }


    override fun getLayoutId(): Int = R.layout.fragment_product_spinner

    override fun loadOnVisible() = Unit


    override fun initViews(parent: View) {
        initDefault()

        productDetail?.let {

            initFirstSpinner(it)
        }


    }


    private fun initDefault() {
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
    }


    /**
     * 行业类别
     */
    private fun initFirstSpinner(it: ProductDetail) {

        val sourceList = it.prices
        val mList = ArrayList<Price>().apply {
            clear()
            add(0, Price().apply {
                name = "请选择行业类别"
            })
            addAll(sourceList)
        }

        val firstAdapter = ProductSpinnerAdapter(mList, context!!)
        industry_category_spinner.apply {
            adapter = firstAdapter
            onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

                override fun onNothingSelected(parent: AdapterView<*>?) = Unit

                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    //  填充报税类型
                    val price = firstAdapter.mList[position]
                    val childList = price.childs
                    childList.add(0, Price().apply {
                        name = "请选择报税类型"
                    })
                    initSecondSpinner(childList)
                    isFirstSelect = !TextUtils.isEmpty(price.mold)

                }

            }
        }

    }

    /**
     * 报税类型
     */
    private fun initSecondSpinner(secondChildList: ArrayList<Price>) {
        val secondAdapter = ProductSpinnerAdapter(secondChildList, context!!)

        tax_spinner.apply {
            adapter = secondAdapter
            onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) = Unit

                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    // 填充收入情况
                    val price = secondAdapter.mList[position]
                    val childList = price.childs
                    childList.add(0, Price().apply {
                        name = "请选择收入情况"
                    })
                    initThirdSpinner(childList)
                    isSecondSelect = !TextUtils.isEmpty(price.mold)
                }

            }


        }
    }


    /**
     * 收入情况
     */
    private fun initThirdSpinner(thirdChildList: ArrayList<Price>) {
        val thirdAdapter = ProductSpinnerAdapter(thirdChildList, context!!)
        income_spinner.apply {
            adapter = thirdAdapter
            onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) = Unit

                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

                    val price = thirdAdapter.mList[position]
                    isThirdSelect = !TextUtils.isEmpty(price.mold)
                    incomeMoney = price.money
                    thirdProductPriceId = price.id


                    when (productDetail!!.mold) {

                        Constants.P2 -> handleAmountEnterprise(price) // 企业代理记账
                        Constants.P3 -> handleAmountPersonal(price)   // 个人代理记账
                    }

                }
            }

        }
    }


    /**
     * 计算企业代理记账金额
     */
    private fun handleAmountEnterprise(price: Price) {

        // 根据用户选择的收入获取原始金额
        val originalMoney: BigDecimal
        when (price.mold) {
            Constants.PP2 -> {
                originalMoney = BigDecimalUtil.mul(price.money, BigDecimal(multipleBigIncome))
                actual_income_rl.visibility = View.VISIBLE
            }
            else -> {
                originalMoney = BigDecimalUtil.mul(price.money, BigDecimal(multipleEnterprise)) // PP1
                actual_income_rl.visibility = View.INVISIBLE
            }
        }

        // 企业额外默认金额（其他绑定业务 营业执照+对公账户+刻章+税务登记）
        val extraDefaultMoney = BigDecimalUtil
            .add(BigDecimal(priceBusinessLicense), BigDecimal(priceBankPublicAccount))
            .add(BigDecimal(priceSeal))
            .add(BigDecimal(priceTaxRegistration))

        // 最终金额
         mFinalMoney = BigDecimalUtil.add(originalMoney, extraDefaultMoney)


        if (isThirdSelect) {
            amount_tv.text = getString(R.string.money_symbol_format, mFinalMoney.stripTrailingZeros().toPlainString())
        }


    }


    /**
     * 计算个人代理记账金额
     */
    private fun handleAmountPersonal(price: Price) {
        // 根据用户选择的收入获取原始金额：个人
        val originalMoney: BigDecimal

        when (price.mold) {
            Constants.PP2 -> {
                originalMoney = BigDecimalUtil.mul(price.money, BigDecimal(multipleBigIncome))
                actual_income_rl.visibility = View.VISIBLE
            }
            else -> {
                originalMoney = BigDecimalUtil.mul(price.money, BigDecimal(multiplePersonal))// PP1
                actual_income_rl.visibility = View.INVISIBLE
            }
        }

        // 个人只计算代理记账最终金额即可
        if (isThirdSelect) {
            mFinalMoney = originalMoney
            amount_tv.text = getString(R.string.money_symbol_format, mFinalMoney.stripTrailingZeros().toPlainString())
        }
    }


    @OnClick(R.id.next_tv)
    fun click() {
        if (!isFirstSelect) {
            VibrateUtils.vibrate(10)
            first_rl.startAnimation(CommonUtils.getShakeAnimation(2))
            return
        }
        if (!isSecondSelect) {
            VibrateUtils.vibrate(10)
            second_rl.startAnimation(CommonUtils.getShakeAnimation(2))
            return
        }
        if (!isThirdSelect) {
            VibrateUtils.vibrate(10)
            third_rl.startAnimation(CommonUtils.getShakeAnimation(2))
            return
        }

        when (productDetail?.mold) {
            Constants.P2 -> {
                // 企业套餐
                startActivity(
                    Intent(activity, FormLicenseEnterpriseActivity::class.java)
                        .putExtra(Constants.PARAM_PRODUCT_ID, productDetail?.id)
                        .putExtra(Constants.PARAM_INCOME_MONEY, incomeMoney.stripTrailingZeros().toPlainString())
                        .putExtra(Constants.PARAM_FINAL_MONEY, mFinalMoney.stripTrailingZeros().toPlainString())
                        .putExtra(Constants.PARAM_PRODUCT_PRICE_ID, thirdProductPriceId)
                )
            }


            Constants.P3 -> {
                // 个体户代理记账
                startActivity(
                    Intent(activity, FormAgentBookkeepingPersonalActivity::class.java)
                        .putExtra(Constants.PARAM_PRODUCT_ID, productDetail?.id)
                        .putExtra(Constants.PARAM_FINAL_MONEY, mFinalMoney.stripTrailingZeros().toPlainString())
                        .putExtra(Constants.PARAM_PRODUCT_PRICE_ID, thirdProductPriceId)
                )

            }
            else -> {
            }
        }



        dismissAllowingStateLoss()

    }


}