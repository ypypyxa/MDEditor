package com.pivnoydevelopment.mdeditor.loader.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.pivnoydevelopment.mdeditor.R
import com.pivnoydevelopment.mdeditor.databinding.FragmentLoaderBinding

class LoaderFragment : Fragment() {

    private var _binding: FragmentLoaderBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val loaderViewModel =
            ViewModelProvider(this)[LoaderViewModel::class.java]

        _binding = FragmentLoaderBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textLoaderScreen
        loaderViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }

        setupListeners()


        return root
    }

    private fun setupListeners() {
        binding.buttonView.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_loader_to_navigation_viewer)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}