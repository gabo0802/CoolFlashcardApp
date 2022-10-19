package com.example.coolflashcardapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewAnimationUtils
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.TextView
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.material.snackbar.Snackbar
import kotlin.math.hypot

class MainActivity : AppCompatActivity() {

    lateinit var flashcardDatabase: FlashcardDatabase
    var allFlashcards = mutableListOf<Flashcard>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var currentCardDisplayedIndex = 0
        flashcardDatabase = FlashcardDatabase(this)
        flashcardDatabase.initFirstCard()
        val flashcardQuestion = findViewById<TextView>(R.id.flashcard_question)
        val flashcardAnswer = findViewById<TextView>(R.id.flashcard_answer)

        val leftOutAnim = AnimationUtils.loadAnimation(flashcardQuestion.getContext(), R.anim.left_out)
        val rightInAnim = AnimationUtils.loadAnimation(flashcardQuestion.getContext(), R.anim.right_in)

        leftOutAnim.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {
                // this method is called when the animation first starts
            }

            override fun onAnimationEnd(animation: Animation?) {
                // this method is called when the animation is finished playing
            }

            override fun onAnimationRepeat(animation: Animation?) {
                // we don't need to worry about this method
            }
        })

        rightInAnim.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {
                // this method is called when the animation first starts
            }

            override fun onAnimationEnd(animation: Animation?) {
                // this method is called when the animation is finished playing
            }

            override fun onAnimationRepeat(animation: Animation?) {
                // we don't need to worry about this method
            }
        })

        allFlashcards = flashcardDatabase.getAllCards().toMutableList()
        if (allFlashcards.size > 0) {
            flashcardQuestion.text = allFlashcards[0].question
            flashcardAnswer.text = allFlashcards[0].answer
        }

        flashcardQuestion.setOnClickListener {

            val cx = flashcardAnswer.width / 2
            val cy = flashcardAnswer.height / 2

            val finalRadius = hypot(cx.toDouble(), cy.toDouble()).toFloat()

            val anim = ViewAnimationUtils.createCircularReveal(flashcardAnswer, cx, cy, 0f, finalRadius)
            flashcardQuestion.visibility = View.INVISIBLE
            flashcardAnswer.visibility = View.VISIBLE

            anim.duration = 500
            anim.start()


        }

        flashcardAnswer.setOnClickListener {

            flashcardQuestion.visibility = View.VISIBLE
            flashcardAnswer.visibility = View.INVISIBLE
            Snackbar.make(flashcardAnswer, "Answer button was clicked",
                Snackbar.LENGTH_SHORT).show()

        }

        val resultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                val data: Intent? = result.data
                if (data != null) {
                    val question = data.getStringExtra("question")
                    val answer = data.getStringExtra("answer")

                    Log.i("MainActivity", "Question: $question")
                    Log.i("MainActivity", "Answer: $answer")

                    flashcardQuestion.text = question
                    flashcardAnswer.text = answer

                    if(question != null && answer != null) {
                        flashcardDatabase.insertCard(Flashcard(question.toString(), answer.toString()))
                        allFlashcards = flashcardDatabase.getAllCards().toMutableList()
                    } else {
                        Log.e("TAG","Missing question or answer to input into database. Question is $question and Answer is $answer")
                    }
                } else {
                    Log.i("MainActivity", "Returned null data from AddCardActivity")
                }
            }

        findViewById<View>(R.id.add).setOnClickListener {
            val intent = Intent(this, AddCardActivity::class.java)
            resultLauncher.launch(intent)
            overridePendingTransition(R.anim.right_in, R.anim.left_out)
        }

        findViewById<View>(R.id.next).setOnClickListener {
            if(allFlashcards.size == 0) {
                return@setOnClickListener
            }

            if(flashcardAnswer.visibility == View.VISIBLE) {
                flashcardQuestion.visibility = View.VISIBLE
                flashcardAnswer.visibility = View.INVISIBLE
            }

            currentCardDisplayedIndex++

            if(currentCardDisplayedIndex >= allFlashcards.size) {
                Snackbar.make(
                    findViewById<TextView>(R.id.flashcard_question),
                    "You've reached the end of the cards, going back to start.",
                    Snackbar.LENGTH_SHORT)
                    .show()
                currentCardDisplayedIndex = 0
            }

            allFlashcards = flashcardDatabase.getAllCards().toMutableList()
            val (question, answer) = allFlashcards[currentCardDisplayedIndex]

            findViewById<View>(R.id.flashcard_question).startAnimation(leftOutAnim)

            findViewById<TextView>(R.id.flashcard_answer).text = answer
            findViewById<TextView>(R.id.flashcard_question).text = question

            findViewById<View>(R.id.flashcard_question).startAnimation(rightInAnim)

        }
    }
}