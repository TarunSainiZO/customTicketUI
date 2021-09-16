package com.example.gradientborder.ui

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.annotation.AttrRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.withStyledAttributes
import com.example.gradientborder.R

class CustomGradientBorder @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null,
    @AttrRes defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val eraser = Paint(Paint.ANTI_ALIAS_FLAG)
    private val cropPath = Path()
    private val w
        get() = measuredWidth.toFloat()
    private val h
        get() = measuredHeight.toFloat()
    private var strokeColor = Color.BLACK

    init {
        setWillNotDraw(true)
        setLayerType(View.LAYER_TYPE_HARDWARE, null)
        eraser.xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
        context.withStyledAttributes(attrs, R.styleable.CustomGradientBorder) {
            strokeColor = getColor(R.styleable.CustomGradientBorder_strokeColor, Color.BLACK)
        }
    }

    private val gradientLine = Paint(Paint.ANTI_ALIAS_FLAG).also {
        it.style = Paint.Style.STROKE
        it.strokeWidth = 15.0f
        it.color = strokeColor
    }

    override fun dispatchDraw(canvas: Canvas?) {
        super.dispatchDraw(canvas)
        cropView(canvas!!)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        cropView(canvas!!)
        drawBorder(canvas)
    }

    private fun cropView(canvas: Canvas) {
        cropPath.moveTo(w, 0f)
        cropPath.lineTo(w, h)
        cropPath.lineTo(w - 50, h)
        cropPath.lineTo(w, 0f)
        canvas.drawPath(cropPath, eraser)
    }

    private fun drawBorder(canvas: Canvas) {
        canvas.drawLine(w, 0f, w - 50, h, gradientLine)
        gradientLine.shader =
            LinearGradient(0f, h, w, h, Color.TRANSPARENT, strokeColor, Shader.TileMode.MIRROR)
        canvas.drawLine(0f, h, w, h, gradientLine)
    }
}