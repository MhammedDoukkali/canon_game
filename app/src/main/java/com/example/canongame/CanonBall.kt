package com.example.canongame

import android.graphics.Paint
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.PointF

class CanonBall (var view: CanonView, val obstacle: Obstacle, val target: Target){
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
    fun update(interval : Double) {
        if(canonballOnScreen) {
            canonball.x += (interval * canonballSpeedX).toFloat()
            canonball.y += (interval * canonballSpeedY).toFloat()

            if(canonball.x + canonballRadius > obstacle.obstacle.left
                && canonball.y +canonballRadius>obstacle.obstacle.top
                && canonball.y-canonballRadius<obstacle.obstacle.bottom) {
                canonballSpeedX *= -1
                canonball.offset((5*canonballSpeedX*interval).toFloat(), 0f)
                view.reduceTimeleft()
            }
            else if (canonball.x + canonballRadius > view.screenHeight
                || canonball.x - canonballRadius < 0) {
                canonballOnScreen = false
            }
            else if (canonball.y + canonballRadius > view.screenHeight
                || canonball.y - canonballRadius < 0) {
                canonballOnScreen = false
            }
            else if (canonball.x + canonballRadius > target.target.left
                && canonball.y +canonballRadius>target.target.top
                && canonball.y-canonballRadius<target.target.bottom) {
                target.detectChoc(this)
            }
        }

    }

    fun draw(canvas: Canvas) {
        canvas.drawCircle(canonball.x, canonball.y, canonballRadius, canonballPaint)
    }

    fun resetCanonBall() {
        canonballOnScreen = false
    }
}