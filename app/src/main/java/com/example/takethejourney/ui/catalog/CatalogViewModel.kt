package com.example.takethejourney.ui.catalog

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.takethejourney.data.database.BookDatabase
import com.example.takethejourney.data.model.Book
import com.example.takethejourney.data.repository.BookRepository

class CatalogViewModel(private val repository: BookRepository) : ViewModel() {

    val allBooks: LiveData<List<Book>> = repository.allBooks



}