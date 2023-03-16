package com.example.example1

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import com.google.android.material.snackbar.Snackbar
import com.example.example1.databinding.ActivityMainBinding


//create an array of answers

private const val TAG = "MainActivity";
private var number_of_clicked = 0
private var grade = 0

private lateinit var binding:ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private val quizViewModel: QuizViewModel by viewModels()
    private val cheatLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){
        result ->
        if (result.resultCode == Activity.RESULT_OK) {
            quizViewModel.isCheater = result.data?.getBooleanExtra(EXTRA_ANSWER_IS_SHOWN, false) ?: false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate(Bundle?) called")
        //setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Log.d(TAG, "Got a view: $quizViewModel")
        // hook up the button to its id

        // what happened after click the buttons
        binding.trueButton.setOnClickListener{
            view: View -> checkAnswer(view,true)
        }

        binding.falseButton.setOnClickListener {
            view: View -> checkAnswer(view,false)
        }

        binding.questionTextView.setOnClickListener {
            quizViewModel.moveToNext()
            updateQuestion()
        }

        binding.cheatButton.setOnClickListener {
            //val intent = Intent(this, CheatActivity::class.java)
            val answerIsTrue = quizViewModel.currentQuestionAnswer
            val intent = CheatActivity.newIntent(this@MainActivity, answerIsTrue)
            //startActivity(intent)
            cheatLauncher.launch(intent)
        }

        // onset listener for the next button
        // ie what happen if you press the next button
        binding.next.setOnClickListener {
            quizViewModel.moveToNext()
            updateQuestion()
        }

        // onset listener for the previous button
        binding.previous.setOnClickListener {
            //if (currentIndex == 0) currentIndex = questionBank.size - 1
            //else quizeViewModel.moveToPrevious()
            quizViewModel.moveToPrevious()
            updateQuestion()
        }

        updateQuestion()
    }

    override fun onStart(){
        super.onStart()
        Log.d(TAG, "onStart() is called")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume is called")
    }

    override fun onPause(){
        super.onPause()
        Log.d(TAG, "onPause is called")
    }

    override fun onStop(){
        super.onStop()
        Log.d(TAG, "onStart() is called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestory() is called")
    }

    @SuppressLint("StringFormatInvalid")
    private fun checkAnswer(view: View, userAnswer:Boolean){
        number_of_clicked++
        //questionBank[currentIndex].clicked = true
        quizViewModel.buttonClicked()
        val correctAnswer = quizViewModel.currentQuestionAnswer
        var messengerResId = 0
        if (userAnswer == correctAnswer) {
            messengerResId = R.string.correct_string
            grade++
        } else {
            messengerResId = R.string.incorrect_string
        }
        if (quizViewModel.isCheater) {
            messengerResId = R.string.judgment_toast
            quizViewModel.isCheater = false
        }
        isAnswered()
        Snackbar.make(view, messengerResId, Snackbar.LENGTH_SHORT).show()
        if (number_of_clicked == quizViewModel.questionBankSize) {
            grade = (grade * 100) / quizViewModel.questionBankSize
            Toast.makeText(this, grade.toString().plus(" %"), Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateQuestion(){
        // this will get u the id for the current question in the question bank
        val questionTextResId = quizViewModel.currentQuestionText
        binding.questionTextView.setText(questionTextResId)
        isAnswered()
    }

    private fun isAnswered(){
        if (quizViewModel.isAnswerClicked == false) {
            binding.trueButton.isEnabled = true
            binding.falseButton.isEnabled = true
        }
        else {
            binding.trueButton.isEnabled = false
            binding.falseButton.isEnabled = false
        }
    }
}