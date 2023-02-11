package com.example.canongame

import android.graphics.Paint
import android.graphics.Canvas
import android.graphics.PointF

class Canon (var canonBaseRadius: Float,
var canonLength: Float, height: Float,
var width : Float, val view: CanonView){
    val canonPaint = Paint()
    var finCanon = PointF(canonLength, height)
    fun draw(canvas: Canvas) {
        canonPaint.strokeWidth = width * 1.5f
        canvas.drawLine(0f, view.screenHeight/2, finCanon.x,
                        finCanon.y, canonPaint)
        canvas.drawCircle(0f, view.screenHeight/2,
        canonBaseRadius, canonPaint)
    }
    fun setFinCanon(height: Float) {
        finCanon.set(canonLength, height)
    }
    fun align(angle: Double) {
        finCanon.x = (canonLength * Math.sin(angle)).toFloat()
        finCanon.y = (-canonLength * Math.cos(angle) + view.screenHeight / 2).toFloat()
    }
}