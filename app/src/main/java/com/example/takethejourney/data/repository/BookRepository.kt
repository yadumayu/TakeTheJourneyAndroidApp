package com.example.takethejourney.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import com.example.takethejourney.data.database.BookDao
import com.example.takethejourney.data.model.Book

class BookRepository(private val bookDao: BookDao) {
    val allBooks: LiveData<List<Book>> = bookDao.getAllBooks()
    init{
        Log.d("BookRepository", "All books: ${allBooks.value}") // Логирование начального состояния
    }
    fun searchBooks(query:String): LiveData<List<Book>> {
        return bookDao.searchBooks("%$query%")
    }
    suspend fun insertBooks(books: List<Book>) {
        if(bookDao.getBookCount() == 0) {
            bookDao.insertBooks(books)
        }
    }

    suspend fun deleteAllBooks() {
        bookDao.deleteAllBooks()
    }


}