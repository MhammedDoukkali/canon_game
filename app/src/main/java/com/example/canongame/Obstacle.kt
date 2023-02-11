package com.example.canongame

import android.graphics.Paint
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.RectF


class Obstacle (var obstacleDistance: Float,
                var obstacleDebut: Float, var obstacleEnd: Float,
                var initialObstacleSpeed: Float, var width: Float,
                var view: CanonView)
{
    val obstacle = RectF(obstacleDistance, obstacleDebut, obstacleDistance + width,
                        obstacleEnd)
    val obstaclePaint = Paint()
    var obstacleSpeed = initialObstacleSpeed

    fun setRect() {
        obstacle.set(obstacleDistance, obstacleDebut,
            obstacleDistance + width, obstacleEnd)
        obstacleSpeed = initialObstacleSpeed
    }

    fun draw(canvas:Canvas) {
        obstaclePaint.color = Color.BLUE
        canvas.drawRect(obstacle, obstaclePaint)
    }

    fun update(interval:Double) {
        var up = (interval * obstacleSpeed).toFloat()
        obstacle.offset(0f, up)
        if (obstacle.top < 0 || obstacle.bottom > view.screenHeight) {
            obstacleSpeed *= -1
            up = (interval * 3 * obstacleSpeed).toFloat()
            obstacle.offset(0f, up)
        }
    }
}