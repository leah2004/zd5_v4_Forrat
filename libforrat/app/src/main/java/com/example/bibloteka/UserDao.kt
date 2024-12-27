package com.example.bibloteka


import androidx.room.Dao
import androidx.room.Database
import androidx.room.Insert
import androidx.room.Query
import androidx.room.RoomDatabase

@Dao
interface UserDao {
    @Insert
    suspend fun insert(user: User)

    @Query("SELECT * FROM User WHERE email = :email AND login = :login AND password = :password AND role = :role LIMIT 1")
    suspend fun getUser(email: String, login: String, password: String, role: String): User?

    @Query("SELECT COUNT(*) FROM User")
    suspend fun getUserCount(): Int // Method to count users
    @Query("SELECT * FROM User WHERE email = :email OR login = :login LIMIT 1")
    suspend fun getUserByEmailOrLogin(email: String, login: String): User?
}
