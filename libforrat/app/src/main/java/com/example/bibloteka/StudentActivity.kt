package com.example.bibloteka

import android.annotation.SuppressLint
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

class StudentActivity : AppCompatActivity() {

    private lateinit var db: AppDatabase
    private lateinit var booksRecyclerView: RecyclerView
    private lateinit var adapter: BookAdapter
    private lateinit var sectionSpinner: Spinner

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student)

        db = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "book-db").build()
        booksRecyclerView = findViewById(R.id.booksRecyclerView)
        sectionSpinner = findViewById(R.id.sectionSpinner)

        booksRecyclerView.layoutManager = LinearLayoutManager(this)
        adapter = BookAdapter()
        booksRecyclerView.adapter = adapter

        loadBooks()

        // Настройка Spinner
        setupSectionSpinner()

        // Обработка нажатия кнопки "Взять книгу"
        findViewById<Button>(R.id.takeBookButton).setOnClickListener {
            val selectedBook = adapter.getSelectedBook() // Получаем выбранную книгу
            if (selectedBook != null) {
                takeBook(selectedBook)
            } else {
                Toast.makeText(this, "Выберите книгу для взятия", Toast.LENGTH_SHORT).show()
            }
        }
        findViewById<Button>(R.id.viewTakenBooksButton).setOnClickListener {
            navigateToTakenBooks()
        }
        findViewById<Button>(R.id.recommendationsButton).setOnClickListener {
            loadRecommendedBooks()
        }
        findViewById<Button>(R.id.takeBookButton).setOnClickListener {
            val selectedBook = adapter.getSelectedBook()
            if (selectedBook != null) {
                lifecycleScope.launch {
                    selectedBook.isTaken = true
                    db.bookDao().update(selectedBook)
                    Toast.makeText(this@StudentActivity, "Книга '${selectedBook.title}' добавлена в взятые", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this@StudentActivity, "Выберите книгу для взятия", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun loadRecommendedBooks() {
        lifecycleScope.launch {
            val recommendedBooks = db.bookDao().getRecommendedBooks()
            adapter.submitList(recommendedBooks)
        }
    }
    private fun loadBooks() {
        lifecycleScope.launch {
            val books = db.bookDao().getAllBooks()
            adapter.submitList(books)
        }
    }
    private fun navigateToTakenBooks() {
        val intent = Intent(this, TakenBooksActivity::class.java)
        startActivity(intent)
    }

    private fun setupSectionSpinner() {
        // Здесь можно добавить логику для заполнения Spinner разделами книг
    }

    private fun takeBook(book: Book) {
        // Логика для добавления книги в список взятых книг
        Toast.makeText(this, "Книга '${book.title}' взята", Toast.LENGTH_SHORT).show()
    }
}