package com.example.coolflashcardapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.EditText
import androidx.activity.result.contract.ActivityResultContracts

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val flashcardQuestion = findViewById<TextView>(R.id.flashcard_question)
        val flashcardAnswer = findViewById<TextView>(R.id.flashcard_answer)
        flashcardQuestion.setOnClickListener {
            flashcardQuestion.visibility = View.INVISIBLE;
            flashcardAnswer.visibility = View.VISIBLE;
        }

        findViewById<View>(R.id.add).setOnClickListener {
            val intent = Intent(this, AddCardActivity::class.java);
            startActivity(intent);
        }

        val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val data: Intent? = result.data
            if (data != null) {
                val string1 = data.getStringExtra("question")
                val string2 = data.getStringExtra("answer")

                Log.i("MainActivity", "Question: $string1")
                Log.i("MainActivity", "Answer: $string2")
            } else {
                Log.i("MainActivity", "Returned null data from AddCardActivity")
            }
        }
    }
}