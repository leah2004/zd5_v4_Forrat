package com.example.bibloteka

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import kotlinx.coroutines.launch

class TeacherActivity : AppCompatActivity() {

    private lateinit var db: AppDatabase
    private lateinit var booksRecyclerView: RecyclerView
    private lateinit var adapter: BookAdapter
    private lateinit var sectionSpinner: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_teacher)

        db = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "book-db").build()
        booksRecyclerView = findViewById(R.id.booksRecyclerView)
        sectionSpinner = findViewById(R.id.sectionSpinner)

        booksRecyclerView.layoutManager = LinearLayoutManager(this)
        adapter = BookAdapter()
        booksRecyclerView.adapter = adapter

        loadBooks()

        // Настройка Spinner
        setupSectionSpinner()

        findViewById<Button>(R.id.recommendButton).setOnClickListener {
            val selectedBook = adapter.getSelectedBook()
            if (selectedBook != null) {
                val dialogView = layoutInflater.inflate(R.layout.dialog_recommendation, null)
                val userEditText = dialogView.findViewById<EditText>(R.id.userEditText)

                AlertDialog.Builder(this)
                    .setTitle("Рекомендовать книгу")
                    .setView(dialogView)
                    .setPositiveButton("Отправить") { _, _ ->
                        val username = userEditText.text.toString()
                        if (username.isNotEmpty()) {
                            lifecycleScope.launch {
                                selectedBook.recommendedTo = username
                                db.bookDao().update(selectedBook)
                                Toast.makeText(this@TeacherActivity, "Книга рекомендована $username", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            Toast.makeText(this@TeacherActivity, "Введите имя ученика", Toast.LENGTH_SHORT).show()
                        }
                    }
                    .setNegativeButton("Отмена", null)
                    .show()
            } else {
                Toast.makeText(this, "Выберите книгу для рекомендации", Toast.LENGTH_SHORT).show()
            }
        }


        findViewById<Button>(R.id.viewTakenBooksButton).setOnClickListener {
            navigateToTakenBooks()
        }
    }

    private fun loadBooks() {
        lifecycleScope.launch {
            val books = db.bookDao().getAllBooks()
            adapter.submitList(books)
        }
    }
    private fun navigateToTakenBooks() {
        val intent = Intent(this, RecommendationsActivity::class.java)
        startActivity(intent)
    }

    private fun setupSectionSpinner() {
        // Здесь можно добавить логику для заполнения Spinner разделами книг
    }

    @SuppressLint("MissingInflatedId")
    private fun showRecommendationDialog(book: Book) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_recommendation, null)
        val userEditText = dialogView.findViewById<EditText>(R.id.userEditText)

        AlertDialog.Builder(this)
            .setTitle("Рекомендовать книгу")
            .setView(dialogView)
            .setPositiveButton("Отправить") { _, _ ->
                val username = userEditText.text.toString()
                recommendBookToUser(book, username)
            }
            .setNegativeButton("Отмена", null)
            .show()
    }

    private fun recommendBookToUser(book: Book, username: String) {
        // Логика для отправки рекомендации пользователю
        Toast.makeText(this, "Книга '${book.title}' рекомендована пользователю $username", Toast.LENGTH_SHORT).show()
    }
}