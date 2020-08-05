package com.cgWeasel.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    internal lateinit var hitMeButton: Button
    internal lateinit var textScore: TextView
    internal lateinit var textTime: TextView

    internal var score = 0

    internal var gameStarted =  false

    internal lateinit var countDownTimer: CountDownTimer
    internal val initialCountDown: Long = 5000
    internal val countDownInterval: Long = 1000

    companion object{
        private val TAG = MainActivity::class.java.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d(TAG, "onCreate is called. Score is: $score")

        hitMeButton = findViewById(R.id.hitMeButton)
        textScore = findViewById(R.id.textScoreView)
        textTime = findViewById(R.id.textTimeView)

        resetGame()

        hitMeButton.setOnClickListener {view ->
            incrementScore()
        }
    }

    private fun resetGame() {
        score = 0

        textScoreView.text = getString(R.string.yourScore, score)

        val initialTimeLeft = initialCountDown / 1000
        textTimeView.text = getString(R.string.timeLeft, initialTimeLeft)

        countDownTimer = object : CountDownTimer(initialCountDown, countDownInterval){
            override fun onTick(milisUntilFinished: Long) {
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
}