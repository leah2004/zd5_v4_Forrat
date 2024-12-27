package com.example.bibloteka
import androidx.annotation.DrawableRes
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "books")
data class Book(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val author: String,
    @DrawableRes val coverResId: Int,
    val section: String,
    val days: Int,
    val isElectronic: Boolean,
    var isSelected: Boolean = false,
    var isTaken: Boolean = false,
    var recommendedTo: String? = null
)