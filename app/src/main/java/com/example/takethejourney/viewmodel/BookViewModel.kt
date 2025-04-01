package com.example.takethejourney.viewmodel

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.takethejourney.data.database.BookDatabase
import com.example.takethejourney.data.model.Book
import com.example.takethejourney.data.repository.BookRepository
import kotlinx.coroutines.launch

class BookViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: BookRepository
    val allBooks: LiveData<List<Book>>
    private val sharedPreferences = application.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
    init {
        val bookDao = BookDatabase.getDatabase(application).bookDao()
        repository = BookRepository(bookDao)
        allBooks = repository.allBooks
    }
    fun searchBooks(query: String): LiveData<List<Book>> {
        return repository.searchBooks(query)
    }
    fun insertBooks(books: List<Book>) {
        viewModelScope.launch { repository.insertBooks(books) }
    }
}