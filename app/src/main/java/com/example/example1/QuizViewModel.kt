package com.example.example1

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.createSavedStateHandle

private const val TAG = "QuizViewModel"
private val IS_CHEATER_KEY = "IS_CHEATER_KEY"

class QuizViewModel (private val savedStateHandle: SavedStateHandle): ViewModel() {
    private val questionBank = listOf(
        Question(R.string.question1,true, false),
        Question(R.string.question2, false, false),
        Question(R.string.question3, false, false),
        Question(R.string.question4, false, false),
        Question(R.string.question5, true, false)
    )

    var isCheater: Boolean
    get() = savedStateHandle.get(IS_CHEATER_KEY) ?: false
    set(value) = savedStateHandle.set(IS_CHEATER_KEY, value)

    private var currentIndex = 0

    val currentQuestionAnswer: Boolean
    get() = questionBank[currentIndex].answer

    val currentQuestionText: Int
    get() = questionBank[currentIndex].textResId

    val questionBankSize: Int
    get() = questionBank.size

    val isAnswerClicked: Boolean
    get() = questionBank[currentIndex].clicked

    fun moveToNext(){
        currentIndex= (currentIndex + 1) % questionBank.size
    }
    fun moveToPrevious(){
        if (currentIndex == 0) currentIndex = questionBank.size - 1
        else currentIndex= (currentIndex - 1) % questionBank.size
    }

    fun buttonClicked(){
        questionBank[currentIndex].clicked = true
    }

    fun skip() {
        currentIndex= (currentIndex + 2) % questionBank.size
    }
}