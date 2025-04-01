package com.example.takethejourney.ui.cart

import android.content.Context
import com.example.takethejourney.data.model.Book
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class CartManager(context: Context) {

    private val prefs = context.getSharedPreferences("cart_prefs", Context.MODE_PRIVATE)
    private val gson = Gson()

    fun addBookToCart(book: Book) {
        val cart = getCart().toMutableList()
        cart.add(book)
        saveCart(cart)
    }

    fun removeBookFromCart(book: Book) {
        val cart = getCart().toMutableList()
        cart.removeAll { it.id == book.id } // Удаляем по ID
        saveCart(cart)
    }

    fun getCart(): List<Book> {
        val json = prefs.getString("cart", "[]") ?: "[]"
        val type = object : TypeToken<List<Book>>() {}.type
        return gson.fromJson(json, type)
    }

    private fun saveCart(cart: List<Book>) {
        val json = gson.toJson(cart)
        prefs.edit().putString("cart", json).apply()
    }

    fun clearCart() {
        prefs.edit().remove("cart").apply()
    }
}
