package com.fatcloud.account.feature.forms.personal.bookkeeping.signature

import android.graphics.Bitmap
import android.view.View
import butterknife.OnClick
import com.blankj.utilcode.util.ToastUtils
import com.fatcloud.account.R
import com.fatcloud.account.app.Glide
import com.fatcloud.account.base.ui.BaseMVPActivity
import com.fatcloud.account.common.CommonUtils
import com.fatcloud.account.common.Constants
import com.fatcloud.account.entity.product.NativeBookkeeping
import com.fatcloud.account.event.entity.WordpadEvent
import com.fatcloud.account.feature.forms.personal.bookkeeping.wordpad.WordpadFragment
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.activity_signature.*

/**
 * Created by Wangsw on 2020/6/13 0013 16:57.
 * </br>
 * 法人签字页
 */
class SignatureActivity : BaseMVPActivity<SignaturePresenter>(), SignatureView {


    private var nativeBookkeeping: NativeBookkeeping? = null

    /**
     * 签名svg路径
     */
    private var signatureSvg: String = ""

    override fun getLayoutId() = R.layout.activity_signature

    override fun showLoading() = showLoadingDialog()

    override fun hideLoading() = dismissLoadingDialog()


    override fun initViews() {
        initExtra()
        initEvent()
        initView()
    }

    private fun initExtra() {

        if (intent.extras == null || !intent.extras!!.containsKey(Constants.PARAM_DATA)) {
            finish()
            return
        }
        nativeBookkeeping = intent?.extras!!.getSerializable(Constants.PARAM_DATA) as NativeBookkeeping
    }

    private fun initEvent() {

        // 签字版
        presenter.subsribeEventEntity<WordpadEvent>(Consumer {
            val signatureSvg = it.signatureSvg
            Glide.with(this).load(signatureSvg).into(signature_iv)
        })
    }

    private fun initView() {
        nativeBookkeeping?.let {
            content_tv.text = getString(
                R.string.signature_format,
                "定位中",
                "定位中",
                it.storeName,
                it.legalPersonName,
                it.idNumber
            )

        }

    }


    @OnClick(
        R.id.bottom_left_tv,
        R.id.bottom_right_tv
    )
    fun onClick(view: View) {
        if (CommonUtils.isDoubleClick(view)) {
            return
        }
        when (view.id) {
            R.id.bottom_left_tv -> {
                // 签名
                WordpadFragment.newInstance().apply {
                    show(supportFragmentManager, this.tag)
                    commitCallBack = object : WordpadFragment.CommitCallBack {
                        override fun onCommit(signatureBitmap: Bitmap?) {
                            Glide.with(this@SignatureActivity).load(signatureBitmap).into(signature_iv)
                        }
                    }
                }

            }
            R.id.bottom_right_tv -> {

                handleCommit()
            }
            else -> {
            }
        }
    }

    private fun handleCommit() {

        // todo 上传签名至阿里云
        nativeBookkeeping?.let {
            presenter.addAgentBookkeeping(
                this, it.finalMoney,
                it.productId,
                it.productPriceId,
                it.legalPersonName,
                it.phone,
                it.idNumber,
                it.storeName,
                it.businessLicenseImgUrl,
                signatureSvg
            )

        }


    }

    override fun addAgentBookkeepingSuccess() {
        ToastUtils.showShort("代理记账提交成功")
    }


}