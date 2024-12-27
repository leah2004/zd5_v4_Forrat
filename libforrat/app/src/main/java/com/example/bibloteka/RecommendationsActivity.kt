package com.example.bibloteka

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import kotlinx.coroutines.launch

class RecommendationsActivity : AppCompatActivity() {

    private lateinit var db: AppDatabase
    private lateinit var recommendationsRecyclerView: RecyclerView
    private lateinit var adapter: BookAdapter

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recommendations)

        db = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "book-db").build()
        recommendationsRecyclerView = findViewById(R.id.recommendationsRecyclerView)

        recommendationsRecyclerView.layoutManager = LinearLayoutManager(this)
        adapter = BookAdapter()
        recommendationsRecyclerView.adapter = adapter

        loadRecommendations()
    }

    private fun loadRecommendations() {
        lifecycleScope.launch {
            // Здесь нужно получить рекомендованные книги из базы данных
            val recommendedBooks = db.bookDao().getAllBooks() // Замените на метод получения рекомендованных книг
            adapter.submitList(recommendedBooks)
        }
    }
}