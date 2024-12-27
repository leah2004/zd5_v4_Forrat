package com.example.bibloteka

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface BookDao {
    @Insert
    suspend fun insert(book: Book)

    @Query("SELECT * FROM books")
    suspend fun getAllBooks(): List<Book>
    @Delete
    suspend fun delete(book: Book)
    @Update
    suspend fun update(book: Book)
    @Query("SELECT * FROM books WHERE recommendedTo IS NOT NULL")
    suspend fun getRecommendedBooks(): List<Book>
}