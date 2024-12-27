package com.example.bibloteka

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.bumptech.glide.Glide
import kotlinx.coroutines.launch

class LibBook : AppCompatActivity() {

    private lateinit var db: AppDatabase
    private lateinit var booksRecyclerView: RecyclerView
    private lateinit var adapter: BookAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lib_book)

        db = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "book-db").build()
        booksRecyclerView = findViewById(R.id.booksRecyclerView)
        booksRecyclerView.layoutManager = LinearLayoutManager(this)

        adapter = BookAdapter()
        booksRecyclerView.adapter = adapter

        loadBooks()

        val button: Button = findViewById(R.id.addBookButton)
        button.setOnClickListener {
            // Создаем намерение для перехода на SecondActivity
            val intent = Intent(this, Lib::class.java)
            startActivity(intent) // Запускаем новую активность
        }

        val deleteButton: Button = findViewById(R.id.deleteBookButton)
        deleteButton.setOnClickListener {
            val selectedBook = adapter.getSelectedBook()
            if (selectedBook != null) {
                lifecycleScope.launch {
                    db.bookDao().delete(selectedBook)
                    adapter.removeBook(selectedBook)
                    Toast.makeText(this@LibBook, "Книга удалена", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this@LibBook, "Выберите книгу для удаления", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun loadBooks() {
        lifecycleScope.launch {
            val books = db.bookDao().getAllBooks()
            adapter.submitList(books)
        }
    }


}