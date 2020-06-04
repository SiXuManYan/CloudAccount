package com.fatcloud.account.view.error

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import androidx.annotation.IntRange
import androidx.core.content.ContextCompat
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import com.blankj.utilcode.util.SizeUtils
import com.fatcloud.account.R
import com.fatcloud.account.view.loading.CommonProgressBar

/**
 * 异常提示
 *
 * @date 2019/2/
 */
class AccidentView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : FrameLayout(context, attrs, defStyleAttr) {

    private var hintText: TextView? = null
    private var retryText: TextView? = null
    private lateinit var tvLoading: View
    private lateinit var pbLoading: CommonProgressBar
    var onRetryClickListener: OnRetryClickListener? = null

    init {
        hide()
        setBackgroundColor(Color.WHITE)

        val linearLayout = LinearLayout(context)
        addView(linearLayout)
        val layoutParams = LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        layoutParams.gravity = Gravity.CENTER_VERTICAL
        linearLayout.layoutParams = layoutParams
        linearLayout.orientation = LinearLayout.VERTICAL

        hintText = TextView(context)
        linearLayout.addView(hintText)
        hintText?.setTextColor(ContextCompat.getColor(context, R.color.colorFontGray))
        hintText?.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f)
        hintText?.gravity = Gravity.CENTER_HORIZONTAL

        retryText = TextView(context)
        linearLayout.addView(retryText)
        val params = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        params.topMargin = SizeUtils.dp2px(20f)
        params.gravity = Gravity.CENTER_HORIZONTAL
        retryText?.layoutParams = params
        val gray = ContextCompat.getColor(context, R.color.colorLine)
        retryText?.setBackgroundResource(R.drawable.sel_clickable_round_red_gray_frame)
        retryText?.setPadding(SizeUtils.dp2px(20f), SizeUtils.dp2px(5f), SizeUtils.dp2px(20f), SizeUtils.dp2px(5f))
        retryText?.setTextColor(createColorStateList(gray, gray, ContextCompat.getColor(context, R.color.colorPrimary)))
        retryText?.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f)
        retryText?.setText(R.string.reload)
        retryText?.setOnClickListener {
            onRetryClickListener?.onRetryClick()
        }

        LayoutInflater.from(context).inflate(R.layout.view_loading, this)
        tvLoading = findViewById(R.id.tv_loading)
        pbLoading = findViewById(R.id.pb_loading)
    }

    /**
     * 无数据提示
     * @param msgRes 提示信息
     */
    fun showEmpty(msgRes: Int, imageRes: Int = R.mipmap.ic_launcher) {
        visibility = View.VISIBLE
        hintText?.setCompoundDrawablesWithIntrinsicBounds(0, imageRes, 0, 0)
        hintText?.compoundDrawablePadding = SizeUtils.dp2px(20f)
        hintText?.setText(msgRes)
        hintText?.visibility = View.VISIBLE
        retryText?.visibility = View.GONE

        pbLoading.visibility = View.GONE
        tvLoading.visibility = View.GONE
    }

    /**
     * 无数据提示
     * @param msgRes 提示信息
     */
    fun showEmpty(msgRes: String, imageRes: Int = R.mipmap.ic_launcher) {
        visibility = View.VISIBLE
        hintText?.setCompoundDrawablesWithIntrinsicBounds(0, imageRes, 0, 0)
        hintText?.compoundDrawablePadding = SizeUtils.dp2px(20f)
        hintText?.text = msgRes
        hintText?.visibility = View.VISIBLE
        retryText?.visibility = View.GONE

        pbLoading.visibility = View.GONE
        tvLoading.visibility = View.GONE
    }

    /**
     * 无数据提示
     * @param msgRes 提示信息
     */
    fun showEmpty(msgRes: Int) {
        visibility = View.VISIBLE
        hintText?.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.ic_launcher, 0, 0)
        hintText?.compoundDrawablePadding = SizeUtils.dp2px(20f)
        hintText?.setText(msgRes)
        hintText?.visibility = View.VISIBLE
        retryText?.visibility = View.GONE

        pbLoading.visibility = View.GONE
        tvLoading.visibility = View.GONE
    }


    fun showEmpty() {
        visibility = View.VISIBLE
        hintText?.visibility = View.VISIBLE
        retryText?.visibility = View.GONE
        pbLoading.visibility = View.GONE
        tvLoading.visibility = View.GONE
    }

    /**
     * 无数据提示
     * @param imageRes 提示信息
     * @param actionRes 按钮
     * @param onClickListener 点击
     */
    fun showEnable(imageRes: Int, actionRes: Int, onClickListener: OnClickListener) {
        visibility = View.VISIBLE
        hintText?.setCompoundDrawablesWithIntrinsicBounds(0, imageRes, 0, 0)
        hintText?.compoundDrawablePadding = SizeUtils.dp2px(20f)
        val params = retryText?.layoutParams as LinearLayout.LayoutParams
        params.topMargin = SizeUtils.dp2px(-20f)
        retryText?.layoutParams = params
        retryText?.visibility = View.VISIBLE
        retryText?.setText(actionRes)
        retryText?.setOnClickListener(onClickListener)

        hintText?.visibility = View.VISIBLE
        retryText?.visibility = View.VISIBLE

        pbLoading.visibility = View.GONE
        tvLoading.visibility = View.GONE
    }

    /**
     * 重试提示
     */
    fun showRetry() {
        visibility = View.VISIBLE
        hintText?.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.ic_launcher, 0, 0)
        hintText?.compoundDrawablePadding = SizeUtils.dp2px(20f)
        hintText?.setText(R.string.error_network_load_failed)
        hintText?.visibility = View.VISIBLE
        retryText?.visibility = View.VISIBLE

        pbLoading.visibility = View.GONE
        tvLoading.visibility = View.GONE
    }

    fun setEmpty(msgRes: Int, imageRes: Int = R.mipmap.ic_launcher) {
        hintText?.setCompoundDrawablesWithIntrinsicBounds(0, imageRes, 0, 0)
        hintText?.compoundDrawablePadding = SizeUtils.dp2px(20f)
        hintText?.setText(msgRes)

        hintText?.visibility = View.VISIBLE
        retryText?.visibility = View.GONE

        pbLoading.visibility = View.GONE
        tvLoading.visibility = View.GONE
    }

    fun showLoading() {
        visibility = View.VISIBLE
        pbLoading.visibility = View.VISIBLE
        pbLoading.startAnimator()
        tvLoading.visibility = View.VISIBLE

        retryText?.visibility = View.GONE
        hintText?.visibility = View.GONE
    }

    fun hide() {
        visibility = View.GONE
    }

    fun getLoadingViewVisibility(): Int {
        if (pbLoading == null) {
            return View.GONE
        }
        return pbLoading.visibility
    }

    private fun createColorStateList(@IntRange(from = 0, to = 255) selected: Int, @IntRange(from = 0, to = 255) pressed: Int, @IntRange(from = 0, to = 255) normal: Int): ColorStateList {
        val colors = intArrayOf(selected, pressed, normal)
        val states = Array(3) { intArrayOf(3) }
        states[0] = intArrayOf(android.R.attr.state_selected)
        states[1] = intArrayOf(android.R.attr.state_pressed)
        states[2] = intArrayOf()
        return ColorStateList(states, colors)
    }

    interface OnRetryClickListener {
        fun onRetryClick()
    }
}