package com.fatcloud.account.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.AppCompatEditText
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.request.RequestOptions
import com.fatcloud.account.R
import com.fatcloud.account.app.Glide
import com.fatcloud.account.common.CommonUtils
import com.fatcloud.account.common.Constants
import com.fatcloud.account.entity.order.enterprise.Shareholder
import com.fatcloud.account.extend.RoundTransFormation
import kotlinx.android.synthetic.main.view_shareholder.view.*

/**
 * Created by Wangsw on 2020/6/9 0001 10:29.
 * 股权人信息view
 * @see Shareholder
 */
class EditView : LinearLayout {


    private lateinit var title_tv: TextView
    private lateinit var content_et: TextView

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

    open fun setTitle(title: String) {
        title_tv.text = title
    }

    open fun getValue(value: String) {
        content_et.text.toString().trim()
    }


}