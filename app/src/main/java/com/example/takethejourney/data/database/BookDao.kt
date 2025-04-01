package com.example.takethejourney.data.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.takethejourney.data.model.Book

@Dao
interface BookDao {
    @Query("SELECT * FROM books ORDER BY id DESC")
    fun getAllBooks(): LiveData<List<Book>>

    @Query("SELECT * FROM books WHERE title LIKE :search OR author LIKE :search")
    fun searchBooks(search: String): LiveData<List<Book>>

    @Query("SELECT * FROM books WHERE id = :bookId")
    fun getBookById(bookId: Int): LiveData<Book>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBooks(books:List<Book>)

    @Query("DELETE FROM books")
    suspend fun deleteAllBooks()

    @Query("SELECT COUNT(*) FROM books")
    suspend fun getBookCount(): Int
}