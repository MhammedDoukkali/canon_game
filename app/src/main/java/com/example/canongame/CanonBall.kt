package com.example.canongame

import android.graphics.Paint
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.PointF

class CanonBall (var view: CanonView){
    var canonball = PointF()
    var canonballSpeed = 0f
    var canonballSpeedX = 0f
    var canonballSpeedY = 0f
    var canonballOnScreen = true
    var canonballRadius = 0f
    var canonballPaint = Paint()

    init {
        canonballPaint.color = Color.GREEN
    }

    fun launch(angle : Double){
        canonball.x = canonballRadius
        canonball.y = view.screenHeight / 2f
        canonballSpeedX =(canonballSpeed *Math.sin(angle)).toFloat()
        canonballSpeedY =(-canonballSpeed*Math.cos(angle)).toFloat()
        canonballOnScreen = true
    }

    fun draw(canvas: Canvas) {
        canvas.drawCircle(canonball.x, canonball.y, canonballRadius, canonballPaint)
    }
}