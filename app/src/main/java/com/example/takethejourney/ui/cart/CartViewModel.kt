package com.example.takethejourney.ui.cart

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.takethejourney.data.model.Book

class CartViewModel(application: Application) : AndroidViewModel(application) {
    private val cartManager = CartManager(application.applicationContext)
    private val _cartItems = MutableLiveData<List<Book>>(emptyList())
    val cartItems: LiveData<List<Book>> get() = _cartItems
    private val _totalPrice = MutableLiveData<Double>()
    val totalPrice: LiveData<Double> = _totalPrice

    init { loadCartItems()
    _cartItems.observeForever{ items -> calculateTotalPrice(items)}
    }
    private fun calculateTotalPrice(items:List<Book>) {
        val total = items.sumOf {it.price}
        _totalPrice.value = total
    }
    fun loadCartItems() {
        _cartItems.value = cartManager.getCart() // Загружаем заново
    }

    // Добавление книги в корзину
    fun addToCart(book: Book) {
        if (!_cartItems.value!!.contains(book)) {
            _cartItems.value = _cartItems.value!! + book
        }
    }

    fun clearCart() {
        cartManager.clearCart()  // Очистка локального хранилища
        loadCartItems() // Обновляем LiveData
    }
    // Удаление книги из корзины
    fun removeFromCart(book: Book) {
        _cartItems.value = _cartItems.value!!.filter { it.id != book.id }
    }

    // Проверка, есть ли книга в корзине
    fun isInCart(bookId: Int): Boolean {
        return _cartItems.value!!.any { it.id == bookId }
    }
}
