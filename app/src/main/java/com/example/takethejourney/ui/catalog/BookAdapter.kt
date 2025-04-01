package com.example.takethejourney.ui.catalog

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.takethejourney.data.model.Book
import com.example.takethejourney.databinding.ItemBookBinding

class BookAdapter(private val onClick: (Book) -> Unit) :
    ListAdapter<Book, BookAdapter.BookViewHolder>(BookDiffCallback()) {

    class BookViewHolder(private val binding: ItemBookBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(book: Book, onClick: (Book) -> Unit) {
            binding.title.text = book.title
            binding.author.text = book.author
            binding.price.text = "${book.price} $"
            Glide.with(binding.root.context)
                .load(binding.root.context.resources.getIdentifier(book.image,
                    "drawable",
                    binding.root.context.packageName))
                .into(binding.imageView)
            binding.root.setOnClickListener {
                onClick(book)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val binding = ItemBookBinding.inflate(LayoutInflater.from(parent.context), parent,false)
        return BookViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        holder.bind(getItem(position),onClick)
    }
}
class BookDiffCallback : DiffUtil.ItemCallback<Book>() {
    override fun areItemsTheSame(oldItem: Book, newItem: Book) = oldItem.id == newItem.id
    override fun areContentsTheSame(oldItem: Book, newItem: Book) = oldItem == newItem
}