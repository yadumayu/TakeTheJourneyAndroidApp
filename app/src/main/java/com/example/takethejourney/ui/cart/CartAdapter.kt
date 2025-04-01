package com.example.takethejourney.ui.cart

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.takethejourney.data.model.Book
import com.example.takethejourney.databinding.ItemCartBinding
import com.example.takethejourney.ui.catalog.BookDiffCallback

class CartAdapter(private val onRemoveClick: (Book) -> Unit) :
    ListAdapter<Book, CartAdapter.CartViewHolder>(BookDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val binding = ItemCartBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CartViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        holder.bind(getItem(position), onRemoveClick)
    }

    class CartViewHolder(private val binding: ItemCartBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(book: Book, onRemoveClick: (Book) -> Unit) {
            binding.titleTextView.text = book.title
            binding.authorTextView.text = book.author
            binding.priceTextView.text = "${book.price}$"

            Glide.with(binding.root.context)
                .load(binding.root.context.resources.getIdentifier(book.image, "drawable", binding.root.context.packageName))
                .into(binding.bookCoverImageView)

            binding.removeButton.setOnClickListener { onRemoveClick(book) }
        }
    }
}
