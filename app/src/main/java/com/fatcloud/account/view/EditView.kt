package com.fatcloud.account.view

import android.content.Context
import android.text.method.DigitsKeyListener
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.appcompat.widget.AppCompatEditText
import com.blankj.utilcode.util.StringUtils
import com.fatcloud.account.R
import com.fatcloud.account.entity.order.enterprise.Shareholder

/**
 * Created by Wangsw on 2020/6/9 0001 10:29.
 * 股权人信息view
 * @see Shareholder
 */
class EditView : LinearLayout {


    private lateinit var title_tv: TextView
    private lateinit var content_et: AppCompatEditText

    constructor(context: Context?) : super(context) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
        init()
    }

    private fun init() {
        val view = LayoutInflater.from(context).inflate(R.layout.view_edit, this, true)
        title_tv = view.findViewById<TextView>(R.id.title_iv)
        content_et = view.findViewById<AppCompatEditText>(R.id.content_et)
    }


    fun setTitle(title: CharSequence): EditView {
        title_tv.text = title
        return this
    }

    fun setTitle(@StringRes titleResId: Int): EditView {
        title_tv.text = StringUtils.getString(titleResId)
        return this
    }


    fun setValue(title: CharSequence): EditView {
        content_et.setText( title)
        return this
    }

    fun setValue(@StringRes valueResId: Int): EditView {
        content_et.setText(StringUtils.getString(valueResId))
        return this
    }

    fun setEditAble(editAble: Boolean): EditView {
        content_et.apply {
            isFocusable = editAble;
            isFocusableInTouchMode = editAble;
            isEnabled = editAble;
        }
        return this
    }


    fun setTitleAndHint(title: CharSequence, hint: CharSequence): EditView {
        title_tv.text = title
        content_et.hint = hint
        return this
    }


    fun setTitleAndHint(@StringRes titleResId: Int, @StringRes hintResId: Int): EditView {
        title_tv.text = StringUtils.getString(titleResId)
        content_et.hint = StringUtils.getString(hintResId)
        return this
    }





    fun value(): String {
        return content_et.text.toString().trim()
    }


    /**
     * @see android.text.InputType
     * @see android.text.InputType.TYPE_CLASS_TEXT
     * @see android.text.InputType.TYPE_CLASS_NUMBER
     * @see android.text.InputType.TYPE_CLASS_DATETIME
     * @see android.text.InputType.TYPE_CLASS_PHONE
     */
    fun setInputType(type: Int) :EditView{
        content_et.inputType = type
        return  this
    }


    /**
     * 设置输入限制
     */
    fun setInputDigits(digits:String):EditView{
        content_et.keyListener = DigitsKeyListener.getInstance(digits);
        return  this
    }





}