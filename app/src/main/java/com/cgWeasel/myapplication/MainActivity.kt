package com.cgWeasel.myapplication

import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.marginBottom
import androidx.core.view.marginStart
import kotlinx.android.synthetic.main.activity_main.*

fun Int.toDp(): Int = (this / Resources.getSystem().displayMetrics.density).toInt()

class MainActivity : AppCompatActivity() {

    internal lateinit var hitMeButton: Button
    internal lateinit var textScore: TextView
    internal lateinit var textTime: TextView
    internal lateinit var bFrame: FrameLayout

    internal var score = 0

    internal var gameStarted =  false

    internal lateinit var countDownTimer: CountDownTimer
    internal val initialCountDown: Long = 60000
    internal val countDownInterval: Long = 1000
    internal var timeLeftOnTimer: Long = 60000

    companion object{
        private val TAG = MainActivity::class.java.simpleName
        private const val SCORE_KEY = "SCORE_KEY"
        private const val TIME_LEFT_KEY = "TIME_LEFT_KEY"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        hitMeButton = findViewById(R.id.hitMeButton)
        textScore = findViewById(R.id.textScoreView)
        textTime = findViewById(R.id.textTimeView)
        bFrame = findViewById(R.id.buttonFrame)

        if (savedInstanceState != null){
            score = savedInstanceState.getInt(SCORE_KEY)
            timeLeftOnTimer = savedInstanceState.getLong(TIME_LEFT_KEY)
            restoreGame()
        } else {
            resetGame()
        }

        hitMeButton.setOnClickListener {view ->
            getNewPosition()
            incrementScore()
        }
    }

    fun getNewPosition() {
        val frameWidth = bFrame.width
        val frameHeight = bFrame.height
        val bHeight = hitMeButton.height

        val frameMin = bHeight/2
        val frameMaxX = frameWidth-bHeight-32
        val frameMaxY = frameHeight-bHeight-32

        val randomPositionX : Int = (frameMin..frameMaxX).random()
        val randomPositionY : Int = (frameMin..frameMaxY).random()

        hitMeButton.translationX = randomPositionX.toFloat()
        hitMeButton.translationY = randomPositionY.toFloat()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putInt(SCORE_KEY, score)
        outState.putLong(TIME_LEFT_KEY, timeLeftOnTimer)
        countDownTimer.cancel()

        Log.d(TAG, "Saving Score: $score and Time Left: $timeLeftOnTimer")
    }

    override fun onDestroy() {
        super.onDestroy()

        Log.d(TAG, "onDestroy called!")
    }

    private fun restoreGame(){
        textScoreView.text = getString(R.string.yourScore, score)

        val restoredTime = timeLeftOnTimer / 1000
        textTimeView.text = getString(R.string.timeLeft, restoredTime)

        countDownTimer = object : CountDownTimer(timeLeftOnTimer, countDownInterval) {
            override fun onTick(milisUntilFinished: Long) {
                timeLeftOnTimer = milisUntilFinished
                val timeleft = milisUntilFinished / 1000
                textTimeView.text = getString(R.string.timeLeft, timeleft)
            }

            override fun onFinish() {
                gameOver()
            }
        }
        gameStarted = true
        countDownTimer.start()
    }

    private fun resetGame() {
        score = 0

        textScoreView.text = getString(R.string.yourScore, score)

        val initialTimeLeft = initialCountDown / 1000
        textTimeView.text = getString(R.string.timeLeft, initialTimeLeft)

        countDownTimer = object : CountDownTimer(initialCountDown, countDownInterval) {
            override fun onTick(milisUntilFinished: Long) {
                timeLeftOnTimer = milisUntilFinished
                val timeleft = milisUntilFinished / 1000
                textTimeView.text = getString(R.string.timeLeft, timeleft)
            }

            override fun onFinish() {
                gameOver()
            }
        }

        gameStarted = false
    }

    private fun gameOver() {
        Toast.makeText(this, getString(R.string.gameOverMessage, score), Toast.LENGTH_LONG).show()
        resetGame()
    }

    private fun incrementScore() {
        if (!gameStarted){
            countDownTimer.start()
            gameStarted = true
        }

        score += 1
        val newScore = getString(R.string.yourScore, score)
        textScoreView.text = newScore
    }

    private fun randomPosition() {

    }
}