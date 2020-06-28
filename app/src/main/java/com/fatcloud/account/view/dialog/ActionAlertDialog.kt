package com.fatcloud.account.view.dialog

import android.content.Context
import android.content.DialogInterface
import android.text.method.LinkMovementMethod
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.widget.NestedScrollView
import butterknife.BindView
import com.blankj.utilcode.util.ColorUtils
import com.blankj.utilcode.util.SizeUtils
import com.fatcloud.account.R
import com.fatcloud.account.base.ui.BaseDialog

/**
 * 应用内对话框
 */
class ActionAlertDialog private constructor(context: Context) : BaseDialog(context, R.style.Dialog) {

    companion object {
        const val STANDARD = 0
        const val SPECIAL = 1
    }

    @BindView(R.id.tv_title)
    lateinit var titleText: TextView
    @BindView(R.id.tv_positive)
    lateinit var positiveText: TextView
    @BindView(R.id.tv_negative)
    lateinit var negativeText: TextView
    @BindView(R.id.tv_message)
    lateinit var messageText: TextView

    @BindView(R.id.message_scroll_view)
    lateinit var message_scroll_view: NestedScrollView


    override fun getContentId() = R.layout.dialog_action_alert

    class Builder(context: Context?) {

        private var dialog: ActionAlertDialog? = null

        init {
            dialog = ActionAlertDialog(context!!)
            dialog!!.setCancelable(true)
            dialog!!.setCanceledOnTouchOutside(true)
        }

        fun setTitle(title: Int): ActionAlertDialog.Builder {
            dialog!!.titleText.setText(title)
            dialog!!.titleText.visibility = View.VISIBLE
            return this
        }


        fun setTitle(title: String): ActionAlertDialog.Builder {
            dialog!!.titleText.text = title
            dialog!!.titleText.visibility = View.VISIBLE
            return this
        }

        fun setCancelable(flag: Boolean): Builder {
            dialog!!.setCancelable(flag)
            dialog!!.setCanceledOnTouchOutside(flag)
            return this
        }


        fun setMessage(message: Int): Builder {
            dialog!!.messageText.setText(message)
            return this
        }

        fun setMessage(message: String): Builder {
            dialog!!.messageText.text = message
            return this
        }

        fun setMessage(message: CharSequence?): Builder {
            dialog!!.messageText.text = message
            return this
        }

        fun setMessageGravity(gravity: Int): Builder {
            dialog!!.messageText.gravity = gravity

            return this
        }


        fun setPositiveButton(text: Int, textColor: Int, listener: DialogInterface.OnClickListener): Builder {
            dialog!!.positiveText.setText(text)
            dialog!!.positiveText.setTextColor(if (textColor == AlertDialog.STANDARD) {
                ColorUtils.getColor(R.color.color_third_level)
            } else {
                ColorUtils.getColor(R.color.color_118EEA)
            })
            dialog!!.positiveText.setOnClickListener { listener.onClick(dialog, 0) }
            return this
        }

        fun setPositiveButton(text: String, textColor: Int, listener: DialogInterface.OnClickListener): Builder {
            dialog!!.positiveText.text = text
            dialog!!.positiveText.setTextColor(if (textColor == AlertDialog.STANDARD) {
                ColorUtils.getColor(R.color.color_third_level)
            } else {
                ColorUtils.getColor(R.color.color_118EEA)
            })
            dialog!!.positiveText.setOnClickListener { listener.onClick(dialog, 0) }
            return this
        }

        fun setNegativeButton(text: Int, textColor: Int, listener: DialogInterface.OnClickListener): Builder {
            dialog!!.negativeText.setText(text)
            dialog!!.negativeText.setTextColor(if (textColor == AlertDialog.STANDARD) {
                ColorUtils.getColor(R.color.color_third_level)
            } else {
                ColorUtils.getColor(R.color.color_118EEA)
            })
            dialog!!.negativeText.setOnClickListener { listener.onClick(dialog, 0) }
            return this
        }

        fun setNegativeButton(text: String, textColor: Int, listener: DialogInterface.OnClickListener): Builder {
            dialog!!.negativeText.text = text
            dialog!!.negativeText.setTextColor(if (textColor == AlertDialog.STANDARD) {
                ColorUtils.getColor(R.color.color_third_level)
            } else {
                ColorUtils.getColor(R.color.color_118EEA)
            })
            dialog!!.negativeText.setOnClickListener { listener.onClick(dialog, 0) }
            return this
        }

        fun setMessageMargins(left: Float, top: Float, right: Float, bottom: Float): Builder {
            val layoutParams = dialog!!.messageText.layoutParams as LinearLayout.LayoutParams
            layoutParams.apply {
                setMargins(SizeUtils.dp2px(left), SizeUtils.dp2px(top), SizeUtils.dp2px(right), SizeUtils.dp2px(bottom))
            }
            return this
        }

        fun setMessageScrollViewHeight(newHeight: Float): Builder {

            dialog!!.message_scroll_view.layoutParams.apply {
                height = SizeUtils.dp2px(newHeight)
            }
            return this
        }

        fun setMovementMethod(): Builder {
            dialog!!.messageText.movementMethod = LinkMovementMethod.getInstance()
            return this
        }

        /**
         * 通过Builder类设置完属性后构造对话框的方法
         */
        fun create() = dialog!!

    }

}