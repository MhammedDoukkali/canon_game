package com.example.canongame

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Paint
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Point
import android.os.Bundle
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import kotlin.math.atan

class CanonView @JvmOverloads constructor(context: Context,
attributes: AttributeSet? = null, defStyleAttr: Int = 0) :
SurfaceView(context, attributes, defStyleAttr),SurfaceHolder.Callback, Runnable{
    lateinit var canvas:Canvas
    val backgroundPaint = Paint()
    val textPaint = Paint()
    var screenWidth = 0f
    var screenHeight = 0f
    var drawing = false
    lateinit var thread: Thread
    val canon = Canon(0f, 0f, 0f, 0f, this)
    val obstacle = Obstacle(0f, 0f , 0f, 0f,
        0f, this)
    val target = Target(0f, 0f, 0f, 0f, 0f, this)
    val ball = CanonBall(this, obstacle, target)
    var shotsFired = 0
    var timeleft = 0.0
    val MISS_PENALTY = 2
    val HIT_REWARD = 3
    var gameOver = false
    val activity = context as FragmentActivity
    var totalElapsedTime = 0.0
    init {
        backgroundPaint.color = Color.LTGRAY
        textPaint.textSize= screenWidth/20
        textPaint.color = Color.BLACK
        timeleft = 10.0
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
        var previousFrameTime = System.currentTimeMillis()
        while (drawing) {
            val currentTime = System.currentTimeMillis()
            var elapsedTimeMS: Double = (currentTime-previousFrameTime).toDouble()
            totalElapsedTime += elapsedTimeMS / 1000.0
            updatePositions(elapsedTimeMS)
            draw()
            previousFrameTime = currentTime
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
//        ball.launch(0.0)
        obstacle.obstacleDistance = (w * 5 / 8f)
        obstacle.obstacleDebut = (h / 8f)
        obstacle.obstacleEnd = (h * 3 / 8f)
        obstacle.width = (w / 24f)
        obstacle.initialObstacleSpeed = (h / 2f)
        obstacle.setRect()
        target.width = (w / 24f)
        target.targetDistance = (w * 7 / 8f)
        target.targetDebut = (h / 8f)
        target.targetEnd = (h * 7 / 8f)
        target.targetInitialSpeed = (-h / 4f)
        target.setRect()
        textPaint.setTextSize(w / 20f)
        textPaint.isAntiAlias = true

    }
    fun draw() {
        if(holder.surface.isValid) {
            canvas = holder.lockCanvas()
            canvas.drawRect(0f, 0f, canvas.width.toFloat(),
                            canvas.height.toFloat(), backgroundPaint)
            val formatted = String.format("%.2f", timeleft)
            canvas.drawText("Il reste $formatted secondes. ", 30f, 50f, textPaint)
            canon.draw(canvas)
//            if(ball.canonballOnScreen)
//                ball.draw(canvas)

            if(ball.canonballOnScreen)
                ball.draw(canvas)
                obstacle.draw(canvas)
                target.draw(canvas)
            holder.unlockCanvasAndPost(canvas)

        }


    }

    fun showGameOverDialog(messageId: Int) {
        class GameResult: DialogFragment() {
            override fun onCreateDialog(bunble: Bundle?): Dialog {
                val builder = AlertDialog.Builder(getActivity())
                builder.setTitle(resources.getString(messageId))
                builder.setMessage(
                    resources.getString(
                        R.string.results_format, shotsFired,
                        totalElapsedTime
                    )
                )
                builder.setPositiveButton(R.string.reset_game,
                    DialogInterface.onClickListener{_, _->newGame()}
                )
                return builder.create()
            }
        }

        activity.runOnUiThread(
            Runnable {
                val ft = activity.supportFragmentManager.beginTransaction()
                val prev = activity.supportFragmentManger.findFragmentByTag("dialog")
                if(prev != null) {
                    ft.remove(prev)
                }
                ft.addToBackStack(null)
                val gameResult = GameResult()
                gameResult.setCancelable(false)
                gameResult.show(ft, "dialog")

            }
        )
    }

    fun updatePositions(elapsedTimeMS:Double) {
        val interval = elapsedTimeMS / 1000.0
        obstacle.update(interval)
        target.update(interval)
        ball.update(interval)
        timeleft -=interval
        if(timeleft <= 0.0) {
            timeleft = 0.0
            gameOver = true
            drawing = false
            showGameoverDialog(R.string.lose)
        }
    }

    fun reduceTimeleft() {
        timeleft -= MISS_PENALTY
    }

    fun increaseTimeLeft() {
        timeleft += HIT_REWARD
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val action = event.action
        if(action == MotionEvent.ACTION_DOWN || action == MotionEvent.ACTION_MOVE) {
            fireCanonball(event)
        }
        return true
    }

    fun fireCanonball(event: MotionEvent) {

        if(! ball.canonballOnScreen) {
            val angle =alignCanon(event)
            ball.launch(angle)
            ++shotsFired
        }
    }

    fun alignCanon (event: MotionEvent): Double {
        val touchPoint = Point(event.x.toInt(), event.y.toInt())
        val centerMinusY = screenHeight / 2 - touchPoint.y
        var angle = 0.0
        if(centerMinusY != 0.0f)
            angle = Math.atan((touchPoint.x).toDouble() /centerMinusY)
        if (touchPoint.y > screenHeight / 2)
            angle += Math.PI
        canon.align(angle)
        return angle
    }



    fun newGame() {
        target.resetTarget()
        obstacle.resetObstacle()
        timeleft = 10.0
        ball.resetCanonBall()
        shotsFired = 0
        totalElapsedTime = 0.0
        drawing = true
        if(gameOver) {
            gameOver = false
            thread = Thread(this)
            thread.start()
        }
    }

    fun gameOver() {
        drawing = false
        showGameOverDialog(R.string.win)
        gameOver = true
    }


    override fun surfaceChanged(holder: SurfaceHolder, format: Int,
                                width:Int, height:Int) {}

    override fun surfaceCreated(holder: SurfaceHolder) {}

    override fun surfaceDestroyed(holder: SurfaceHolder) {}
}
