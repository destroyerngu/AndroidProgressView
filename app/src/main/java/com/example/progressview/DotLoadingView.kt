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

class DotLoadingView: View {

    val mPaint = Paint().apply {
        color = Color.MAGENTA
        style = Paint.Style.FILL
    }
    var radius = 0f
    var cx = 0f
    var cy = 0f
    var rateArray = arrayOf(1f,1f,1f)
    var delayArray = arrayOf(0L,100L,200L)
    val animatorLists = arrayListOf<ValueAnimator>()

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        radius = height/2f
        cx = (width - 8*radius)/2f + radius
        cy = height/2f

        if (8*radius > width){
            radius = width / 8f
            cx = radius
        }
    }

    override fun onDraw(canvas: Canvas?) {
        for (i in 0..2){
            canvas?.drawCircle(
                cx + i * 3 * radius,
                cy,
                radius*rateArray[i],
                mPaint
            )
        }
    }

    fun createAnimators(){
        for (i in 0..2){
            ValueAnimator.ofFloat(0f,1f).apply {
                duration = 300
                interpolator = LinearInterpolator()
                repeatCount = ValueAnimator.INFINITE
                repeatMode = ValueAnimator.REVERSE
                startDelay = delayArray[i]
                addUpdateListener {
                    rateArray[i] = it.animatedValue as Float
                    invalidate()
                }
                animatorLists.add(this)
            }
        }
    }

    fun start(){
        createAnimators()
        animatorLists.forEach { it.start() }
    }
    fun stop(){
        animatorLists.forEach { it.cancel() }
    }
}







