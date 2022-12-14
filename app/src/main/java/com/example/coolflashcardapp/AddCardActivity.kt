package com.example.coolflashcardapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText

class AddCardActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_card)

        findViewById<View>(R.id.cancel).setOnClickListener {
            finish()
            overridePendingTransition(R.anim.right_in, R.anim.left_out)
        }

        findViewById<View>(R.id.save).setOnClickListener {
            val data = Intent(this, MainActivity::class.java)
            data.putExtra(
                "question",
                findViewById<EditText>(R.id.editTextField).text.toString()
            )
            data.putExtra(
                "answer",
                findViewById<EditText>(R.id.editTextField2).text.toString()
            )
            setResult(RESULT_OK, data)
            finish()
            overridePendingTransition(R.anim.right_in, R.anim.left_out)
        }

    }
}