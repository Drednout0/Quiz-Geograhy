package com.bignerdranch.android.geomain


import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import android.widget.Button

import android.widget.TextView
import android.widget.Toast

import androidx.lifecycle.ViewModelProviders
import java.io.CharArrayWriter

private const val TAG = "MainActivity"
private const val  KEY_INDEX = "Index"
private const val  REQUEST_CODE_CHEAT = 0
class MainActivity : AppCompatActivity() {


    private lateinit var trueButton: Button

    private lateinit var falseButton: Button

    private lateinit var nextButton: Button

    private lateinit var questionTextView: TextView

    private lateinit var predButton: Button

    private lateinit var cheatButton: Button

    private var pravOtvet = 0;
    val mess = "Правильных ответов"

    private var nepravOtvet = 0;
    val messTwo = "Неправильных ответов"

    private val quizViewModel: QuizViewModel by lazy {
        ViewModelProviders.of(this).get(QuizViewModel::class.java)

    }

    private fun updateQuestion() {
        val questionTextResId = quizViewModel.currentQuestionText
        questionTextView.setText(questionTextResId)
    }

    private fun checkAnswer(userAnswer: Boolean) {
        val correctAnswer = quizViewModel.currentQuestionAnswer
        val messageResId = when {
            quizViewModel.isCheater ->
                R.string.judgment_toast
            userAnswer == correctAnswer ->
                R.string.correct_toast
            else -> R.string.incorrect_toast
        }
        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        trueButton = findViewById(R.id.true_button)
        falseButton = findViewById(R.id.false_button)
        nextButton = findViewById(R.id.next_button)
        predButton = findViewById(R.id.pred_button)
        cheatButton = findViewById((R.id.cheat_but))


        trueButton.setOnClickListener {
            checkAnswer(true)
            falseButton.isClickable = false
            trueButton.isClickable = false
        }
        falseButton.setOnClickListener {
            checkAnswer(false)
            questionTextView = findViewById(R.id.quest_text_view)
            falseButton.isClickable = false
            trueButton.isClickable = false

            nextButton.setOnClickListener {
                quizViewModel.moveToNext()
                updateQuestion()
                falseButton.isClickable = true

                trueButton.isClickable = true

                predButton.setOnClickListener {
                    quizViewModel.moveToNext()
                    updateQuestion()
                }

                cheatButton.setOnClickListener {
                    val answerIsTrue = quizViewModel.currentQuestionAnswer
                    val intent = CheatActivity.newIntent(this@MainActivity, answerIsTrue)

                    startActivityForResult(intent, REQUEST_CODE_CHEAT)
                }
                updateQuestion()

            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_OK) {
            return
        }
        if (requestCode == REQUEST_CODE_CHEAT) {
            quizViewModel.isCheater= data?.getBooleanExtra(EXTRA_ANSWER_SHOWN, false) ?:
            false
        }
    }
}