package com.example.takethejourney.ui.catalog

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.takethejourney.R
import com.example.takethejourney.databinding.FragmentCatalogBinding
import com.example.takethejourney.viewmodel.BookViewModel
import java.io.Console

class CatalogFragment : Fragment() {
    private lateinit var binding: FragmentCatalogBinding
    private lateinit var bookViewModel: BookViewModel
    private lateinit var bookAdapter : BookAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true) // Включаем меню
        binding = FragmentCatalogBinding.inflate(inflater, container, false)
        bookViewModel = ViewModelProvider(this)[BookViewModel::class.java]
        val navController = findNavController()
        navController.popBackStack(R.id.catalogFragment, false)
        bookAdapter = BookAdapter { book ->
            val action =
                CatalogFragmentDirections.actionCatalogFragmentToBookDetailFragment(book.id)
            findNavController().navigate(action)
        }
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = bookAdapter
        }
        bookViewModel.allBooks.observe(viewLifecycleOwner) { books ->
            bookAdapter.submitList(books)
            Log.d("books:", books.toString())
        }


        return binding.root
    }

    override fun onResume() {
        super.onResume()
        val navController = findNavController()
        if (navController.currentDestination?.id != R.id.catalogFragment) {
            navController.popBackStack(R.id.catalogFragment, false)
        }
    }
    @Deprecated("Deprecated in Java")
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu) // Загружаем меню

        // Находим SearchView в меню
        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView

        // Настраиваем обработчик поиска
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    bookViewModel.searchBooks(it).observe(viewLifecycleOwner) { books ->
                        bookAdapter.submitList(books)
                    }
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let {
                    bookViewModel.searchBooks(it).observe(viewLifecycleOwner) { books ->
                        bookAdapter.submitList(books)
                    }
                }
                return true
            }
        })

        super.onCreateOptionsMenu(menu, inflater)
    }
}