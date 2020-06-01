package com.account.view

import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import android.util.AttributeSet
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView
import com.account.R
import java.lang.Exception

/**
 *  Created by Wangsw on 2020/6/1 09:41.
 *  圆形ImageView
 */
class CircleImageView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0) :
    AppCompatImageView(context, attrs, defStyle) {

    companion object {
        @JvmField
        internal val SCALE_TYPE = ScaleType.CENTER_CROP
        internal val BITMAP_CONFIG = Bitmap.Config.ARGB_8888
        internal const val COLOR_DRAWABLE_DIMENSION = 2
        internal const val DEFAULT_BORDER_WIDTH = 0
        internal const val DEFAULT_BORDER_COLOR = Color.TRANSPARENT
        internal const val DEFAULT_FILL_COLOR = Color.TRANSPARENT
    }

    private val drawableRect = RectF()
    private val borderRect = RectF()

    private val shaderMatrix = Matrix()
    private val bitmapPaint = Paint()
    private val borderPaint = Paint()
    private val fillPaint = Paint()

    private var fillColor = DEFAULT_FILL_COLOR

    private var bitmap: Bitmap? = null
    private var bitmapShader: BitmapShader? = null
    private var bitmapWidth = 0
    private var bitmapHeight = 0

    private var drawableRadius = 0f
    private var borderRadius = 0f

    private var isReady: Boolean
    private var setupPending = false

    var colorFilter = null
        set(value) {
            if (field != value) {
                field = value
                applyColorFilter()
                invalidate()
            }
        }

    var borderOverlay = false
        set(value) {
            if (field != value) {
                field = value
                setup()
            }
        }

    var disableCircularTransformation = false
        set(value) {
            if (field != value) {
                field = value
                initializeBitmap()
            }
        }

    var borderColor = DEFAULT_BORDER_COLOR
        set(@ColorInt value) {
            if (field != value) {
                field = value
                borderPaint.color = value
                invalidate()
            }
        }
    var borderWidth = DEFAULT_BORDER_WIDTH
        set(value) {
            if (field != value) {
                field = value
                setup()
            }
        }

    init {
        val a = context.obtainStyledAttributes(attrs, R.styleable.CircleImageView, defStyle, 0)
        borderWidth = a.getDimensionPixelOffset(R.styleable.CircleImageView_border_width, DEFAULT_BORDER_WIDTH)
        borderOverlay = a.getBoolean(R.styleable.CircleImageView_border_overlay, false)
        fillColor = a.getColor(R.styleable.CircleImageView_fill_color, DEFAULT_FILL_COLOR)
        a.recycle()

        super.setScaleType(SCALE_TYPE)
        isReady = true

        if (setupPending) {
            setup()
            setupPending = false
        }
    }

    override fun getScaleType() = SCALE_TYPE

    override fun setScaleType(scaleType: ScaleType) {
        if (scaleType != SCALE_TYPE) {
            throw IllegalArgumentException(String.format("ScaleType %s not supported.", scaleType))
        }
    }

    override fun setAdjustViewBounds(adjustViewBounds: Boolean) {
        if (adjustViewBounds) {
            throw IllegalArgumentException("adjustViewBounds not supported.")
        }
    }

    override fun onDraw(canvas: Canvas) {
        if (disableCircularTransformation) {
            super.onDraw(canvas)
            return
        }

        if (bitmap == null) {
            return
        }

        if (fillColor != Color.TRANSPARENT) {
            canvas.drawCircle(drawableRect.centerX(), drawableRect.centerY(), drawableRadius, fillPaint)
        }
        canvas.drawCircle(drawableRect.centerX(), drawableRect.centerY(), drawableRadius, bitmapPaint)
        if (borderWidth > 0) {
            canvas.drawCircle(borderRect.centerX(), borderRect.centerY(), borderRadius, borderPaint)
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        setup()
    }

    override fun setPadding(left: Int, top: Int, right: Int, bottom: Int) {
        super.setPadding(left, top, right, bottom)
        setup()
    }

    override fun setPaddingRelative(start: Int, top: Int, end: Int, bottom: Int) {
        super.setPaddingRelative(start, top, end, bottom)
        setup()
    }

    override fun setImageBitmap(bm: Bitmap?) {
        if (bm != null) {
            super.setImageBitmap(bm)
            initializeBitmap()
        }
    }

    override fun setImageDrawable(drawable: Drawable?) {
        if (drawable != null) {
            super.setImageDrawable(drawable)
            initializeBitmap()
        }
    }

    override fun setImageResource(@DrawableRes resId: Int) {
        super.setImageResource(resId)
        initializeBitmap()
    }


    override fun setImageURI(uri: Uri?) {
        super.setImageURI(uri)
        initializeBitmap()
    }

    private fun applyColorFilter() {
        bitmapPaint.colorFilter = colorFilter
    }

    private fun getBitmapFromDrawable(drawable: Drawable): Bitmap? {
        if (drawable is BitmapDrawable) {
            return drawable.bitmap
        }

        return try {
            val bitmap = if (drawable is ColorDrawable) {
                Bitmap.createBitmap(COLOR_DRAWABLE_DIMENSION, COLOR_DRAWABLE_DIMENSION, BITMAP_CONFIG)
            } else {
                Bitmap.createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight, BITMAP_CONFIG)
            }

            val canvas = Canvas(bitmap)
            drawable.setBounds(0, 0, canvas.width, canvas.height)
            drawable.draw(canvas)
            bitmap
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun initializeBitmap() {
        bitmap = if (disableCircularTransformation) {
            null
        } else {
            getBitmapFromDrawable(drawable)
        }
        setup()
    }

    private fun setup() {
        if (!isReady) {
            setupPending = true
            return
        }

        if (width == 0 && height == 0) {
            return
        }

        if (bitmap == null) {
            invalidate()
            return
        }

        bitmapShader = BitmapShader(bitmap!!, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)

        bitmapPaint.isAntiAlias = true
        bitmapPaint.shader = bitmapShader

        borderPaint.style = Paint.Style.STROKE
        borderPaint.isAntiAlias = true
        borderPaint.color = borderColor
        borderPaint.strokeWidth = borderWidth.toFloat()

        fillPaint.style = Paint.Style.FILL
        fillPaint.isAntiAlias = true
        fillPaint.color = fillColor

        bitmapHeight = bitmap!!.height
        bitmapWidth = bitmap!!.width

        borderRect.set(calculateBounds())
        borderRadius = Math.min((borderRect.height() - borderWidth) / 2.0f, (borderRect.width() - borderWidth) / 2.0f)

        drawableRect.set(borderRect)
        if (!borderOverlay && borderWidth > 0) {
            drawableRect.inset(borderWidth - 1.0f, borderWidth - 1.0f)
        }
        drawableRadius = Math.min(drawableRect.height() / 2.0f, drawableRect.width() / 2.0f)

        applyColorFilter()
        updateShaderMatrix()
        invalidate()
    }

    private fun calculateBounds(): RectF {
        val availableWidth = width - paddingLeft - paddingRight
        val availableHeight = height - paddingTop - paddingBottom

        val sideLength = Math.min(availableWidth, availableHeight)

        val left = paddingLeft + (availableWidth - sideLength) / 2f
        val top = paddingTop + (availableHeight - sideLength) / 2f

        return RectF(left, top, left + sideLength, top + sideLength)
    }

    private fun updateShaderMatrix() {
        val scale: Float
        var dx = 0f
        var dy = 0f

        shaderMatrix.set(null)

        if (bitmapWidth * drawableRect.height() > drawableRect.width() * bitmapHeight) {
            scale = drawableRect.height() / bitmapHeight
            dx = (drawableRect.width() - bitmapWidth * scale) * 0.5f
        } else {
            scale = drawableRect.width() / bitmapWidth
            dy = (drawableRect.height() - bitmapHeight * scale) * 0.5f
        }

        shaderMatrix.setScale(scale, scale)
        shaderMatrix.postTranslate(dx.plus(0.5f).plus(drawableRect.left), dy.plus(0.5f).plus(drawableRect.top))
        bitmapShader?.setLocalMatrix(shaderMatrix)
    }

/*    fun getBorderColor(): Int {
        return borderColor
    }

    fun setBorderColor(borderColor: Int) {
        if (borderColor == borderColor) {
            return
        }

        mBorderColor = borderColor
        mBorderPaint.setColor(mBorderColor)
        invalidate()
    }

    fun getBorderWidth(): Int {
        return mBorderWidth
    }

    fun setBorderWidth(borderWidth: Int) {
        if (borderWidth == mBorderWidth) {
            return
        }

        mBorderWidth = borderWidth
        setup()
    }*/

}