package com.fatcloud.account.view.dialog

import android.content.Context
import android.view.Gravity
import android.view.KeyEvent
import butterknife.BindView
import com.fatcloud.account.R
import com.fatcloud.account.base.ui.BaseDialog
import com.fatcloud.account.view.loading.CommonProgressBar


class LoadingDialog private constructor(context: Context) : BaseDialog(context, R.style.Dialog) {

    @BindView(R.id.progress_cpb)
    lateinit var animView: CommonProgressBar

    init {
        window!!.attributes.gravity = Gravity.CENTER
        animView.startAnimator()
    }

    override fun getContentId() = R.layout.dialog_loading

    override fun dismiss() {
        animView.stopAnimator()
        super.dismiss()
    }

    class Builder(context: Context?) {

        private var dialog: LoadingDialog? = null

        init {
            dialog = LoadingDialog(context!!)
            dialog!!.setCancelable(false)
            dialog!!.setCanceledOnTouchOutside(false)
        }

        /**
         * 设置该对话框能否被Cancel掉，默认可以
         *
         * @param cancelable
         */
        fun setCancelable(cancelable: Boolean): Builder {
            dialog!!.setCancelable(cancelable)
            dialog!!.setCanceledOnTouchOutside(cancelable)
            dialog!!.setOnKeyListener { _, keyCode, event -> keyCode == KeyEvent.KEYCODE_BACK && event.repeatCount == 0 }
            return this
        }

        /**
         * 通过Builder类设置完属性后构造对话框的方法
         */
        fun create(): LoadingDialog = dialog!!
    }
}