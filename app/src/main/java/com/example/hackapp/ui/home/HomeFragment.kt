package com.example.hackapp.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hackapp.databinding.FragmentHomeBinding
import com.example.hackapp.model.Event
import com.example.hackapp.viewmodel.EventViewModel

//updates UI
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null //binding var allows access to UI from xml
    private val binding get() = _binding!!

    private lateinit var viewModel: EventViewModel //instance of EventViewModel, manages UI-related data
    private lateinit var adapter: EventAdapter //populates RecyclerView with event data

    override fun onCreateView( //initializes binding and inflates fragment's layout
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) { //ViewModel is initialized
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(EventViewModel::class.java)

        // Set up the adapter
        adapter = EventAdapter()//instance of EventAdapter (provides data to recyclerview), adapter converts
        // data into view iterms of events to display
        binding.recyclerView.layoutManager = LinearLayoutManager(context) //arranges in list
        binding.recyclerView.adapter = adapter //adapter assigned to recyclerview

        viewModel.events.observe(viewLifecycleOwner) { events ->
            adapter.submitList(events) //updates data when changed
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE //loading screen
        }

        viewModel.error.observe(viewLifecycleOwner) { error ->
            error?.let {
                Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            }
        }

        // Load events initially
        viewModel.loadEvents()
    }

    private fun bookmarkEvent(event: Event) {
        // Logic to bookmark the event (e.g., save to local storage or Firebase)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
