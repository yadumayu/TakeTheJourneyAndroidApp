package com.example.takethejourney.ui.bookDetail

import android.os.Bundle
import android.telecom.Call.Details
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.takethejourney.R
import com.example.takethejourney.data.database.BookDatabase
import com.example.takethejourney.data.model.Book
import com.example.takethejourney.databinding.FragmentBookDetailBinding
import com.example.takethejourney.ui.cart.CartManager
import com.example.takethejourney.ui.cart.CartViewModel
class BookDetailFragment : Fragment() {
    private lateinit var cartManager: CartManager
    private lateinit var cartViewModel: CartViewModel
    private lateinit var binding: FragmentBookDetailBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentBookDetailBinding.inflate(inflater, container, false)
        cartViewModel = ViewModelProvider(requireActivity())[CartViewModel::class.java]
        val args: BookDetailFragmentArgs by navArgs()
        cartManager = CartManager(requireContext())

        val bookId = args.bookId
        val bookLiveData = BookDatabase.getDatabase(requireContext()).bookDao().getBookById(bookId)

        bookLiveData.observe(viewLifecycleOwner) { book ->
            binding.titleTextView.text = book.title
            binding.authorTextView.text = book.author
            binding.descriptionTextView.text = book.description
            binding.priceTextView.text = "${book.price}$"
            Glide.with(binding.root.context)
                .load(binding.root.context.resources.getIdentifier(book.image, "drawable", binding.root.context.packageName))
                .into(binding.bookCoverImageView)

            // Загружаем корзину заново при каждом входе в фрагмент
            cartViewModel.loadCartItems()

            // Проверяем, есть ли книга в корзине
            cartViewModel.cartItems.observe(viewLifecycleOwner) { cart ->
                val isBookInCart = cart.any { it.id == book.id }
                binding.addToCartButton.isEnabled = !isBookInCart
            }

            binding.addToCartButton.setOnClickListener {
                cartManager.addBookToCart(book)
                cartViewModel.loadCartItems() // Обновляем корзину сразу после добавления
                Toast.makeText(requireContext(), "Added to cart", Toast.LENGTH_SHORT).show()
                binding.addToCartButton.isEnabled = false // Блокируем кнопку сразу после добавления
            }
        }

        return binding.root
    }
}
