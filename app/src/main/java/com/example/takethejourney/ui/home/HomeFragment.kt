package com.example.takethejourney.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.takethejourney.R
import com.example.takethejourney.databinding.FragmentHomeBinding
import com.example.takethejourney.ui.CarouselAdapter

class HomeFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: CarouselAdapter

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)
        val images = listOf(
            R.drawable.designsite,
            R.drawable.designsite2,
            R.drawable.designsite3
        )


        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        recyclerView = binding.recyclerView
        adapter = CarouselAdapter(images)

        recyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.adapter = adapter
        // Эффект центрирования
        recyclerView.setPadding(0, 0, 0, 0)
        recyclerView.clipToPadding = false
        val root: View = binding.root


        return root

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}