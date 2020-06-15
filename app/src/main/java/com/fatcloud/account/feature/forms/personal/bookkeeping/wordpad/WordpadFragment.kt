package com.fatcloud.account.feature.forms.personal.bookkeeping.wordpad

import android.graphics.Bitmap
import android.view.View
import butterknife.OnClick
import com.blankj.utilcode.util.ConvertUtils
import com.fatcloud.account.R
import com.fatcloud.account.base.ui.BaseBottomSheetDialogFragment
import com.fatcloud.account.common.CommonUtils
import com.github.gcacace.signaturepad.views.SignaturePad
import kotlinx.android.synthetic.main.fragment_word_pad.*

/**
 * Created by Wangsw on 2020/6/13 0013 18:27.
 * </br>
 *
 */
class WordpadFragment : BaseBottomSheetDialogFragment<WordpadPresenter>(), WordpadView {


    companion object {
        fun newInstance(): WordpadFragment {
            return WordpadFragment()
        }
    }


    override fun getLayoutId() = R.layout.fragment_word_pad

    override fun loadOnVisible() = Unit

    override fun initViews(parent: View) {


        signature_pad.setOnSignedListener(object : SignaturePad.OnSignedListener {
            override fun onStartSigning() {

            }

            override fun onClear() {

            }

            override fun onSigned() {

            }

        })

    }


    @OnClick(
        R.id.resign_tv,
        R.id.commit_tv
    )
    fun onClick(view: View) {
        if (CommonUtils.isDoubleClick(view)) {
            return
        }
        when (view.id) {
            R.id.resign_tv -> {
                signature_pad.clear()
            }
            R.id.commit_tv -> {
                commitWordpad()
                dismissAllowingStateLoss()
            }
            else -> {
            }
        }
    }

    private fun commitWordpad() {
        commitCallBack?.let {
            val signatureBitmap = signature_pad.signatureBitmap
            // 回传Bitmap
            it.showAutographForImageView(signatureBitmap)

            val byteArray = ConvertUtils.bitmap2Bytes(signatureBitmap, Bitmap.CompressFormat.PNG)
            // 上传图片至oss
           presenter.getOssSecurityToken(this,byteArray)

        }
    }

    var commitCallBack: CommitCallBack? = null

    interface CommitCallBack {
        /**
         * 签字完成后，立即显示在目标 调用者的imageView 上
         */
        fun showAutographForImageView(signatureBitmap: Bitmap?)

        /**
         * 获取签名上传oss 后的最终地址
         */
        fun uploadAutographSuccess(finalUrl: String)

    }

    override fun uploadAutographSuccess(finalUrl: String?) {

        commitCallBack?.let {
            if (!finalUrl.isNullOrEmpty()) {
                it.uploadAutographSuccess(finalUrl)
                return
            }


        }
    }


}