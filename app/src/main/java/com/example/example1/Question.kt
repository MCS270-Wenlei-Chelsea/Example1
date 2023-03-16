package com.example.example1
import androidx.annotation.StringRes

data class Question(@StringRes val textResId: Int, val answer: Boolean, var clicked: Boolean)