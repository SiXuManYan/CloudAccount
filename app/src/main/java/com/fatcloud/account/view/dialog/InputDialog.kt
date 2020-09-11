package com.fatcloud.account.view.dialog

import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.widget.EditText
import android.widget.TextView
import butterknife.BindView
import com.fatcloud.account.R
import com.fatcloud.account.R2
import com.fatcloud.account.base.ui.BaseDialog

/**
 * 带输入的对话框
 *
 * @date 2019/1/3
 */
class InputDialog private constructor(context: Context): BaseDialog(context, R.style.Dialog) {

    override fun getContentId() = R.layout.dialog_input

    @BindView(R2.id.tv_title)
    lateinit var titleText: TextView
    @BindView(R2.id.et_content)
    lateinit var contentEdit: EditText
    @BindView(R2.id.tv_message)
    lateinit var messageText: TextView
    @BindView(R2.id.tv_positive)
    lateinit var positiveText: TextView
    @BindView(R2.id.tv_negative)
    lateinit var negativeText: TextView

    fun getInputContent() = contentEdit.text!!

    class Builder(context: Context?) {

        private var dialog: InputDialog? = null

        init {
            dialog = InputDialog(context!!)
            dialog!!.setCancelable(false)
            dialog!!.setCanceledOnTouchOutside(false)
            dialog!!.contentEdit.requestFocus()
        }

        fun setTitle(title: Int) : Builder {
            dialog!!.titleText.setText(title)
            return this
        }

        fun setTitle(title: String) : Builder {
            dialog!!.titleText.text = title
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

        fun setPositiveButton(text: Int, listener: DialogInterface.OnClickListener) : Builder {
            dialog!!.positiveText.setText(text)
            dialog!!.positiveText.setTextColor(Color.argb(0xFF, 0x1F, 0x76, 0xDD))
            dialog!!.positiveText.setOnClickListener { listener.onClick(dialog, 0) }
            return this
        }

        fun setPositiveButton(text: String, listener: DialogInterface.OnClickListener) : Builder {
            dialog!!.positiveText.text = text
            dialog!!.positiveText.setTextColor(Color.argb(0xFF, 0x1F, 0x76, 0xDD))
            dialog!!.positiveText.setOnClickListener { listener.onClick(dialog, 0) }
            return this
        }

        fun setNegativeButton(text: Int, listener: DialogInterface.OnClickListener) : Builder {
            dialog!!.negativeText.setText(text)
            dialog!!.negativeText.setTextColor(Color.argb(0xFF, 0xEF, 0, 0))
            dialog!!.negativeText.setOnClickListener { listener.onClick(dialog, 0) }
            return this
        }

        fun setNegativeButton(text: String, listener: DialogInterface.OnClickListener) : Builder {
            dialog!!.negativeText.text = text
            dialog!!.negativeText.setTextColor(Color.argb(0xFF, 0xEF, 0, 0))
            dialog!!.negativeText.setOnClickListener { listener.onClick(dialog, 0) }
            return this
        }

        /**
         * 通过Builder类设置完属性后构造对话框的方法
         */
        fun create() = dialog!!
    }
}