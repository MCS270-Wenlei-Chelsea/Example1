package com.example.example1

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.example1.databinding.ActivityCheatBinding

private const val EXTRA_ANSWER_IS_TRUE = "com.example.example1.answer_is_true"
const val EXTRA_ANSWER_IS_SHOWN = "com.example.example1.answer_shown"


class CheatActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCheatBinding
    private val cheatViewModel: CheatViewModel by viewModels()
    private var answerIsTrue = false// initial to false to start

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCheatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        answerIsTrue = intent.getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false)

        //setAnswerShownResult(false)
        var answerText = when{
            answerIsTrue  -> R.string.true_string
            else -> R.string.false_string
        }

        binding.showAnswerButton.setOnClickListener {
            // once they type the show answer button, enable cheating
            //cheatViewModel.answerText()
            binding.answerText.setText(answerText)
            setAnswerShownResult (true)
            cheatViewModel.answerChicked = true
        }

        if (cheatViewModel.answerChicked) {
            binding.answerText.setText(answerText)
            setAnswerShownResult (true)
        }

        binding.backButton.setOnClickListener {
            finish()
        }
    }

    private fun setAnswerShownResult(isAnswerShown: Boolean) {
        val data = Intent().apply{
            putExtra(EXTRA_ANSWER_IS_SHOWN, isAnswerShown)
        }
        setResult(Activity.RESULT_OK, data)
    }

    companion object{
        fun newIntent(packageContext: Context, answerIsTrue:Boolean): Intent {
            return Intent(packageContext, CheatActivity::class.java).apply {
                putExtra(EXTRA_ANSWER_IS_TRUE, answerIsTrue)
            }
        }
    }
}