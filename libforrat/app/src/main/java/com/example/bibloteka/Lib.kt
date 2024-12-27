package com.example.bibloteka

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.bumptech.glide.Glide
import kotlinx.coroutines.launch
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException

class Lib : AppCompatActivity() {

    private lateinit var db: AppDatabase
    private lateinit var titleEditText: EditText
    private lateinit var searchButton: Button
    private lateinit var coverImageView: ImageView
    private lateinit var titleTextView: TextView
    private lateinit var sectionEditText: EditText
    private lateinit var daysEditText: EditText
    private lateinit var electronicCheckBox: CheckBox
    private lateinit var saveButton: Button
    private lateinit var nazad: Button
    var coverResId = R.drawable.icon
    private val bookCollection = listOf(
        Book(title = "Гарри Поттер", author = "Дж. К. Роулинг", coverResId = R.drawable.gg, section = "", days = 0, isElectronic = false),
        Book(title = "Война и мир", author = "Лев Толстой", coverResId = R.drawable.ww, section = "", days = 0, isElectronic = false),
        Book(title = "Преступление и наказание", author = "Фёдор Достоевский", coverResId = R.drawable.pp, section = "", days = 0, isElectronic = false),
        Book(title = "1984", author = "Джордж Оруэлл", coverResId = R.drawable.oo, section = "", days = 0, isElectronic = false)
    )


    private var bookCoverUrl: String? = null // URL обложки книги
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lib)

        // Инициализация базы данных
        db = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "book-db").build()

        // Инициализация элементов интерфейса
        titleEditText = findViewById(R.id.titleEditText)
        searchButton = findViewById(R.id.searchButton)
        coverImageView = findViewById(R.id.coverImageView)
        titleTextView = findViewById(R.id.titleTextView)
        sectionEditText = findViewById(R.id.sectionEditText)
        daysEditText = findViewById(R.id.daysEditText)
        electronicCheckBox = findViewById(R.id.electronicCheckBox)
        saveButton = findViewById(R.id.saveButton)
        nazad = findViewById(R.id.nazadButton)
        // Обработчик кнопки поиска
        searchButton.setOnClickListener {
            val title = titleEditText.text.toString().trim()
            if (title.isNotEmpty()) {
                searchBook(title)
            } else {
                Toast.makeText(this, "Введите название книги", Toast.LENGTH_SHORT).show()
            }
        }

        // Обработчик кнопки сохранения
        saveButton.setOnClickListener {
            saveBook()
        }

        nazad.setOnClickListener {
            // Создаем намерение для перехода на NewActivity
            val intent = Intent(this, LibBook::class.java)
            startActivity(intent) // Запускаем новую активность
        }
    }
    private fun searchBook(title: String) {
        val book = bookCollection.find { it.title.equals(title, ignoreCase = true) }
        if (book != null) {
            // Обновляем интерфейс
            titleTextView.text = book.title
            findViewById<TextView>(R.id.authorTextView).text = book.author
            coverImageView.setImageResource(book.coverResId) // Используем идентификатор ресурса
        } else {
            Toast.makeText(this, "Книга не найдена", Toast.LENGTH_SHORT).show()
        }
    }


    private fun saveBook() {
        val title = titleTextView.text.toString().trim()
        val author = findViewById<TextView>(R.id.authorTextView).text.toString().trim()
        val section = sectionEditText.text.toString().trim()
        val days = daysEditText.text.toString().trim()
        val isElectronic = electronicCheckBox.isChecked

        if (title.isEmpty() || author.isEmpty() || section.isEmpty() || days.isEmpty()) {
            Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show()
            return
        }

        lifecycleScope.launch {
            val book = Book(
                title = title,
                author = author,
                coverResId = coverResId,
                section = section,
                days = days.toInt(),
                isElectronic = isElectronic
            )
            db.bookDao().insert(book)
            Toast.makeText(this@Lib, "Книга сохранена", Toast.LENGTH_SHORT).show()
        }
    }
}