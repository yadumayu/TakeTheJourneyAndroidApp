package com.example.takethejourney.ui.order

import SendEmailTask
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.takethejourney.MainActivity
import com.example.takethejourney.R
import com.example.takethejourney.databinding.FragmentOrderBinding
import com.example.takethejourney.ui.cart.CartViewModel
import kotlinx.coroutines.launch

class OrderFragment : Fragment() {
    private lateinit var cartViewModel: CartViewModel
    private lateinit var binding: FragmentOrderBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOrderBinding.inflate(inflater, container, false)
        cartViewModel = ViewModelProvider(requireActivity())[CartViewModel::class.java]

        binding.submitOrderButton.setOnClickListener {
            submitOrder()
        }

        return binding.root
    }

    private fun submitOrder() {
        val name = binding.nameEditText.text.toString()
        val email = binding.emailEditText.text.toString()
        val address = binding.addressEditText.text.toString()

        if (name.isNotEmpty() && email.isNotEmpty() && address.isNotEmpty()) {
            val emailSender = SendEmailTask(requireContext())
            val subject = "New Order from $name"
            val message = buildOrderMessage(name, email, address)

            lifecycleScope.launch {
                val isSent = emailSender.sendEmail(
                    recipient = email,
                    subject = subject,
                    body = message
                )

                if (isSent) {
                    cartViewModel.clearCart()

                    // Закрываем OrderFragment и возвращаемся в корзину
                    findNavController().popBackStack(R.id.navigation_cart, false)

                    Toast.makeText(requireContext(), "Order submitted successfully", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), "Failed to send order", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            Toast.makeText(requireContext(), "Please fill all fields", Toast.LENGTH_SHORT).show()
        }
    }


    private fun buildOrderMessage(name: String, email: String, address: String): String {
        val items = cartViewModel.cartItems.value?.joinToString("\n") {
            "- ${it.title} (${it.price}$)"
        } ?: "No items"

        return """
            Name: $name
            Email: $email
            Address: $address
            
            Order Details:
            $items
            
            Total: ${cartViewModel.totalPrice.value}$ 
        """.trimIndent()
    }
}