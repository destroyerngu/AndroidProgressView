package com.example.progressview

import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator

class MouseEatingView: View {
    val mousePaint = Paint().apply {
        color = Color.MAGENTA
        style = Paint.Style.FILL
    }
    val hamburgPaint = Paint().apply {
        color = Color.MAGENTA
        style = Paint.Style.FILL
    }
    var mouseCx = 0f
    var mouseCy = 0f
    var hamburgCx = 0f
    var hamburgCy = 0f
    var mouseRadius = 0f
    var hamburgRadius = 0f
    var mouseAngle = 0f
    var space = 0f
    lateinit var animators:AnimatorSet

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        hamburgRadius = width/10f
        mouseRadius = 3*hamburgRadius

        mouseCx = 3*hamburgRadius
        mouseCy = height/2f

        hamburgCx = 9*hamburgRadius
        hamburgCy = height/2f

        if (6*hamburgRadius > height){
            hamburgRadius = height/6f
            mouseRadius = 3*hamburgRadius
            mouseCx = width/2f - 2*hamburgRadius // (width-10R)/2+3R
            hamburgCx = mouseCx + 6*hamburgRadius
        }
    }

    override fun onDraw(canvas: Canvas?) {
        //嘴
        canvas?.drawArc(
            mouseCx-3*hamburgRadius,
            mouseCy-3*hamburgRadius,
            mouseCx+3*hamburgRadius,
            mouseCy+3*hamburgRadius,
            mouseAngle,
            360-2*mouseAngle,
            true,
            mousePaint
        )
        //食物
        canvas?.drawCircle(
            hamburgCx-space,
            hamburgCy,
            hamburgRadius,
            hamburgPaint
        )
    }

    fun start(){
        val mouse = ValueAnimator.ofFloat(0f,45f).apply {
            duration = 700
            repeatMode = ValueAnimator.RESTART
            repeatCount = ValueAnimator.INFINITE
            interpolator = LinearInterpolator()
            addUpdateListener {
                mouseAngle = it.animatedValue as Float
                invalidate()
            }
        }
        val hamburg = ValueAnimator.ofFloat(0f,6*hamburgRadius).apply {
            duration = 700
            repeatMode = ValueAnimator.RESTART
            repeatCount = ValueAnimator.INFINITE
            interpolator = LinearInterpolator()
            addUpdateListener {
                space = it.animatedValue as Float
                invalidate()
            }
        }
        animators = AnimatorSet()
        animators.playTogether(mouse,hamburg)
        animators.start()
    }
    fun stop(){
        animators.cancel()
    }
}