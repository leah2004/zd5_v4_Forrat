package com.example.bibloteka

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import kotlinx.coroutines.launch

class TakenBooksActivity : AppCompatActivity() {

    private lateinit var db: AppDatabase
    private lateinit var takenBooksRecyclerView: RecyclerView
    private lateinit var adapter: BookAdapter

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_taken_books)

        db = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "book-db").build()
        takenBooksRecyclerView = findViewById(R.id.takenBooksRecyclerView)

        takenBooksRecyclerView.layoutManager = LinearLayoutManager(this)
        adapter = BookAdapter()
        takenBooksRecyclerView.adapter = adapter

        loadTakenBooks()
    }

    private fun loadTakenBooks() {
        lifecycleScope.launch {
            // Здесь нужно получить взятые книги из базы данных
            val takenBooks = db.bookDao().getAllBooks() // Замените на метод получения взятых книг
            adapter.submitList(takenBooks)
        }
    }
}