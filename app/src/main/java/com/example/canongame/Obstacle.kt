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
}