package com.example.canongame

import android.content.Context
import android.graphics.Paint
import android.graphics.Canvas
import android.graphics.Color
import android.util.AttributeSet
import android.view.SurfaceHolder
import android.view.SurfaceView

class CanonView @JvmOverloads constructor(context: Context,
attributes: AttributeSet? = null, defStyleAttr: Int = 0) :
SurfaceView(context, attributes, defStyleAttr),SurfaceHolder.Callback, Runnable{
    lateinit var canvas:Canvas
    val backgroundPaint = Paint()
    var screenWidth = 0f
    var screenHeight = 0f
    var drawing = false
    lateinit var thread: Thread
    val canon = Canon(70f, 20f, 0f, 0f, this)
    val ball = CanonBall(this)
    val obstacle = Obstacle(0f, 0f , 0f, 0f,
        0f, this)

    init {
        backgroundPaint.color = Color.LTGRAY
    }

    fun pause() {
        drawing = false
        thread.join()
    }
    fun resume(){
        drawing = true
        thread = Thread(this)
        thread.start()
    }

    override fun run() {
        while (drawing) {
            draw()
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        screenWidth = w.toFloat()
        screenHeight = h.toFloat()
        canon.canonBaseRadius = (h / 18f)
        canon.canonLength = (w / 8f)
        canon.width = (w / 24f)
        canon.setFinCanon(h / 2f)
        ball.canonballRadius =(w / 36f)
        ball.canonballSpeed =(w * 3 / 2f)
        ball.launch(0.0)
        obstacle.obstacleDistance = (w * 5 / 8f)
        obstacle.obstacleDebut = (h / 8f)
        obstacle.obstacleEnd = (h * 3 / 8f)
        obstacle.width = (w / 24f)
        obstacle.initialObstacleSpeed = (h / 2f)
        obstacle.setRect()
    }
    fun draw() {
        if(holder.surface.isValid) {
            canvas = holder.lockCanvas()
            canvas.drawRect(0f, 0f, canvas.width.toFloat(),
                            canvas.height.toFloat(), backgroundPaint)
            canon.draw(canvas)
            if(ball.canonballOnScreen)
                ball.draw(canvas)

            if(ball.canonballOnScreen)
                ball.draw(canvas)
            obstacle.draw(canvas)
            holder.unlockCanvasAndPost(canvas)
        }

    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int,
                                width:Int, height:Int) {}

    override fun surfaceCreated(holder: SurfaceHolder) {}

    override fun surfaceDestroyed(holder: SurfaceHolder) {}
}