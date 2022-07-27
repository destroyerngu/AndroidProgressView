package com.example.progressview

import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.LinearInterpolator
import androidx.core.animation.addListener

class AnimatedButton: View {
    val mPaint = Paint().apply {
        color = Color.argb(255,46,157,74)
        style = Paint.Style.FILL
    }

    var mRadius = 0f
    var mRate = 0f
    var offsetX = 0f
    var maxOffsetX = 0f
    var offsetY = 0f
    var maxOffsetY = 0f

    val wavePaint = Paint().apply {
        color = Color.argb(150,173,86,255)
        style = Paint.Style.FILL
    }
    var cx = 0f
    var cy = 0f
    var waveRaius = 0f

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        if (width > height){
            mRadius = height/2f
            maxOffsetX = (width-2*mRadius)/2f
        }else{
            mRadius = width/2f
            maxOffsetY = (height-2*mRadius)/2f
        }
    }

    override fun onDraw(canvas: Canvas?) {
        canvas?.drawRoundRect(
            0f + offsetX,
            0f + offsetY,
            width.toFloat() - offsetX,
            height.toFloat() - offsetY,
            mRadius * mRate,
            mRadius * mRate,
            mPaint
        )

        //波纹扩散
        canvas?.drawCircle(cx,cy,waveRaius,wavePaint)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event?.action == MotionEvent.ACTION_DOWN){

            startAnim()

            cx = event.x
            cy = event.y
            ValueAnimator.ofFloat(0f,Math.min(width,height).toFloat()).apply {
                duration = 300
                interpolator = AccelerateInterpolator()
                addUpdateListener {
                    waveRaius = it.animatedValue as Float
                    invalidate()
                }
                addListener(onEnd = {
                    waveRaius = 0f
                    invalidate()
                })
                start()
            }
        }
        return true
    }

    fun startAnim(){
        val rateAnimator = ValueAnimator.ofFloat(0f,1f).apply {
            duration = 500
            interpolator = LinearInterpolator()
            addUpdateListener {
                mRate = it.animatedValue as Float
                invalidate()
            }
        }
        val sizeAnimator = ValueAnimator.ofFloat(
            0f,
            if (maxOffsetX > 0) maxOffsetX else maxOffsetY
        ).apply {
            duration = 500
            interpolator = LinearInterpolator()
            addUpdateListener {
                if (maxOffsetX > 0) {
                    offsetX = it.animatedValue as Float
                }else{
                    offsetY = it.animatedValue as Float
                }
                invalidate()
            }
        }
        AnimatorSet().apply {
            playSequentially(rateAnimator,sizeAnimator)
            start()
        }
    }
}











