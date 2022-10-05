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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val flashcardQuestion = findViewById<TextView>(R.id.flashcard_question)
        val flashcardAnswer = findViewById<TextView>(R.id.flashcard_answer)
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

                    flashcardQuestion.text = question
                    flashcardAnswer.text = answer

                    Log.i("MainActivity", "Question: $question")
                    Log.i("MainActivity", "Answer: $answer")
                } else {
                    Log.i("MainActivity", "Returned null data from AddCardActivity")
                }
            }

        findViewById<View>(R.id.add).setOnClickListener {
            val intent = Intent(this, AddCardActivity::class.java)
            resultLauncher.launch(intent)
        }

    }
}