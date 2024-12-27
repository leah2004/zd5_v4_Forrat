package com.example.bibloteka

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class BookAdapter : RecyclerView.Adapter<BookAdapter.BookViewHolder>() {

    private val books = mutableListOf<Book>()

    // Метод для обновления списка книг
    fun submitList(newBooks: List<Book>) {
        books.clear()
        books.addAll(newBooks)
        notifyDataSetChanged()
    }
    fun getSelectedBook(): Book? {
        return books.find { it.isSelected } // Предполагается, что у книги есть поле isSelected
    }

    // Создание нового ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_book, parent, false)
        return BookViewHolder(view)
    }

    // Привязка данных к ViewHolder
    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val book = books[position]
        holder.bind(book)
        holder.itemView.setOnClickListener {
            book.isSelected = !book.isSelected // Переключаем состояние выбора
            notifyItemChanged(position) // Обновляем элемент
        }
    }
    fun removeBook(book: Book) {
        val index = books.indexOf(book)
        if (index != -1) {
            books.removeAt(index)
            notifyItemRemoved(index)
        }
    }
    // Возвращает количество элементов в списке
    override fun getItemCount(): Int = books.size

    // ViewHolder для книги
    class BookViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val coverImageView: ImageView = itemView.findViewById(R.id.bookCoverImageView)
        private val titleTextView: TextView = itemView.findViewById(R.id.bookTitleTextView)
        private val sectionTextView: TextView = itemView.findViewById(R.id.bookSectionTextView)
        private val daysTextView: TextView = itemView.findViewById(R.id.bookDaysTextView)
        private val authorTextView: TextView = itemView.findViewById(R.id.bookAuthorTextView)

        // Метод для привязки данных книги к элементам интерфейса
        fun bind(book: Book) {
            titleTextView.text = book.title
            sectionTextView.text = "Раздел: ${book.section}"
            authorTextView.text = "Автор: ${book.author}" // Отображение автора
            daysTextView.text = "Дней: ${book.days}"

            // Загрузка обложки книги с помощью Glide
            coverImageView.setImageResource(book.coverResId)
        }
    }
}