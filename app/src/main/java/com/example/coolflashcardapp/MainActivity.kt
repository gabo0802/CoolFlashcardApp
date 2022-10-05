package com.example.coolflashcardapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.material.snackbar.Snackbar

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

        allFlashcards = flashcardDatabase.getAllCards().toMutableList()
        if (allFlashcards.size > 0) {
            flashcardQuestion.text = allFlashcards[0].question
            flashcardAnswer.text = allFlashcards[0].answer
        }

        flashcardQuestion.setOnClickListener {
            flashcardQuestion.visibility = View.INVISIBLE
            flashcardAnswer.visibility = View.VISIBLE
            Toast.makeText(this, "Question button was clicked", Toast.LENGTH_SHORT).show()
            Log.i("MainActivity", "Question button was clicked")
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
        }

        findViewById<View>(R.id.next).setOnClickListener {
            if(allFlashcards.size == 0) {
                return@setOnClickListener
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

            findViewById<TextView>(R.id.flashcard_answer).text = answer
            findViewById<TextView>(R.id.flashcard_question).text = question
        }
    }
}