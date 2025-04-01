package com.example.takethejourney.ui.cart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.takethejourney.R
import com.example.takethejourney.databinding.FragmentCartBinding

class CartFragment : Fragment() {
    private lateinit var binding: FragmentCartBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var cartManager: CartManager
    private lateinit var cartAdapter: CartAdapter
    private lateinit var cartViewModel: CartViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCartBinding.inflate(inflater, container, false)
        cartViewModel = ViewModelProvider(requireActivity())[CartViewModel::class.java]
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)

        cartManager = CartManager(requireContext())

        cartAdapter = CartAdapter { book ->
            cartManager.removeBookFromCart(book)
            updateCart()
        }

        binding.checkoutButton.setOnClickListener {
            val booksInCart = cartManager.getCart()
            if (booksInCart.isEmpty()) {
                Toast.makeText(requireContext(), "Please select at least one book", Toast.LENGTH_SHORT).show()
            } else {
                findNavController().navigate(R.id.action_cartFragment_to_orderFragment)
            }
        }

        binding.cartRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = cartAdapter
        }

        updateCart()
        return binding.root
    }

    private fun updateCart() {
        val booksInCart = cartManager.getCart()
        cartAdapter.submitList(booksInCart)
        binding.totalPrice.text = cartViewModel.totalPrice.value.toString()
    }
}
