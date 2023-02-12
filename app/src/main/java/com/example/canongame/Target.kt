package com.example.canongame

import android.graphics.*

class Target (var targetDistance: Float, var targetDebut: Float,
              var targetEnd: Float, var targetInitialSpeed: Float,
              var width: Float, var view: CanonView ) {
    val Target_PIECES = 7
    val target = RectF(targetDistance, targetDebut, targetDistance + width, targetEnd)
    var targetTouch = BooleanArray(Target_PIECES)
    val targetPaint = Paint()
    var lengthPiece = 0f
    var targetSpeed = targetInitialSpeed
    var numTouchTarget = 0

    fun draw(canvas: Canvas) {
        val currentPoint = PointF()
        currentPoint.x = target.left
        currentPoint.y = target.top
        for (i in 0 until Target_PIECES) {
            if (!targetTouch[i]) {
                if (i % 2 != 0)
                    targetPaint.color = Color.BLUE
                else
                    targetPaint.color = Color.YELLOW
                canvas.drawRect(
                    currentPoint.x, currentPoint.y, target.right,
                    currentPoint.y + lengthPiece, targetPaint
                )
            }
            currentPoint.y += lengthPiece
        }
    }

    fun setRect() {
        target.set(
            targetDistance, targetDebut,
            targetDistance + width, targetEnd)
        targetSpeed = targetInitialSpeed
        lengthPiece = (targetEnd - targetDebut) / Target_PIECES
    }

    fun update(interval: Double) {
        var up = (interval * targetSpeed).toFloat()
        target.offset(0f, up)
        if (target.top < 0 || target.bottom > view.screenHeight) {
            targetSpeed *= -1f
            up = (interval * 3 * targetSpeed).toFloat()
            target.offset(0f, up)
        }

    }
    fun detectChoc(ball: CanonBall) {
        val section = ((ball.canonball.y-target.top) / lengthPiece).toInt()

        if(section>=0 && section<Target_PIECES &&
            !targetTouch[section]) {
            targetTouch[section] = true
            ball.resetCanonBall()
            view.increaseTimeLeft()
            if (++numTouchTarget == Target_PIECES) view.gameOver()
        }



    }

    fun resetTarget() {
        for(i in 0 until Target_PIECES)
            targetTouch[i] = false
        numTouchTarget = 0
        targetSpeed = targetInitialSpeed
        target.set(targetDistance, targetDebut, targetDistance+width, targetEnd)
    }
}
