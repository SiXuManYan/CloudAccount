package com.account.view.dialog

import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.view.View
import android.widget.TextView
import butterknife.BindView
import com.account.R
import com.account.base.ui.BaseDialog

/**
 * 对话框
 *
 */
class AlertDialog private constructor(context: Context): BaseDialog(context, R.style.Dialog) {

    companion object {
        const val STANDARD = 0
        const val SPECIAL = 1
    }

    @BindView(R.id.tv_title)
    lateinit var titleText: TextView
    @BindView(R.id.tv_message)
    lateinit var messageText: TextView
    @BindView(R.id.tv_positive)
    lateinit var positiveText: TextView
    @BindView(R.id.tv_negative)
    lateinit var negativeText: TextView

    override fun getContentId() = R.layout.dialog_alert_colod

    class Builder(context: Context?) {

        private var dialog: AlertDialog? = null

        init {
            dialog = AlertDialog(context!!)
            dialog!!.setCancelable(false)
            dialog!!.setCanceledOnTouchOutside(false)
        }

        fun setTitle(title: Int) : Builder {
            dialog!!.titleText.setText(title)
            dialog!!.titleText.visibility = View.VISIBLE
            return this
        }

        fun setCancelable(flag: Boolean) : Builder {
            dialog!!.setCancelable(flag)
            dialog!!.setCanceledOnTouchOutside(flag)
            return this
        }

        fun setTitle(title: String) : Builder {
            dialog!!.titleText.text = title
            dialog!!.titleText.visibility = View.VISIBLE
            return this
        }

        fun setMessage(message: Int) : Builder {
            dialog!!.messageText.setText(message)
            return this
        }

        fun setMessage(message: String) : Builder {
            dialog!!.messageText.text = message
            return this
        }

        fun setMessage(message: CharSequence?): Builder {
            dialog!!.messageText.text = message
            return this
        }


        fun setPositiveButton(text: Int, textColor: Int, listener: DialogInterface.OnClickListener) : Builder {
            dialog!!.positiveText.setText(text)
            dialog!!.positiveText.setTextColor(if (textColor == STANDARD) {
                Color.argb(0xFF, 0x1F, 0x76, 0xDD)
            } else {
                Color.argb(0xFF, 0xEF, 0, 0)
            })
            dialog!!.positiveText.setOnClickListener { listener.onClick(dialog, 0) }
            return this
        }

        fun setPositiveButton(text: String, textColor: Int, listener: DialogInterface.OnClickListener) : Builder {
            dialog!!.positiveText.text = text
            dialog!!.positiveText.setTextColor(if (textColor == STANDARD) {
                Color.argb(0xFF, 0x1F, 0x76, 0xDD)
            } else {
                Color.argb(0xFF, 0xEF, 0, 0)
            })
            dialog!!.positiveText.setOnClickListener { listener.onClick(dialog, 0) }
            return this
        }

        fun setNegativeButton(text: Int, textColor: Int, listener: DialogInterface.OnClickListener) : Builder {
            dialog!!.negativeText.setText(text)
            dialog!!.negativeText.setTextColor(if (textColor == STANDARD) {
                Color.argb(0xFF, 0x1F, 0x76, 0xDD)
            } else {
                Color.argb(0xFF, 0xEF, 0, 0)
            })
            dialog!!.negativeText.setOnClickListener { listener.onClick(dialog, 0) }
            return this
        }

        fun setNegativeButton(text: String, textColor: Int, listener: DialogInterface.OnClickListener) : Builder {
            dialog!!.negativeText.text = text
            dialog!!.negativeText.setTextColor(if (textColor == STANDARD) {
                Color.argb(0xFF, 0x1F, 0x76, 0xDD)
            } else {
                Color.argb(0xFF, 0xEF, 0, 0)
            })
            dialog!!.negativeText.setOnClickListener { listener.onClick(dialog, 0) }
            return this
        }

        /**
         * 通过Builder类设置完属性后构造对话框的方法
         */
        fun create() = dialog!!

    }
}