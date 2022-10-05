package com.example.coolflashcardapp

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.coolflashcardapp.Flashcard
import com.example.coolflashcardapp.FlashcardDao

@Database(entities = [Flashcard::class], version = 1)

abstract class AppDatabase : RoomDatabase() {
    abstract fun flashcardDao(): FlashcardDao
}
