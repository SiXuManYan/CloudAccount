package com.fatcloud.account.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import com.fatcloud.account.R

/**
 * Created by Wangsw on 2020/6/1 0001 10:32.
 *
 */
class PointView : LinearLayout {

    lateinit var point_tv: TextView

    constructor(context: Context?) : super(context) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(
        context,
        attrs,
        defStyleAttr,
        defStyleRes
    ) {
        init()
    }

    private fun init() {
        val view = LayoutInflater.from(context).inflate(R.layout.view_point, this, true)
        point_tv = view.findViewById<TextView>(R.id.point_tv)
    }

    public fun setPointNumber(valueText: Int) {
        if (this::point_tv.isInitialized) {
            point_tv.text = valueText.toString()
        }
    }

}