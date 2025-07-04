package com.pivnoydevelopment.mdeditor.viewer.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.pivnoydevelopment.mdeditor.R
import com.pivnoydevelopment.mdeditor.databinding.FragmentViewerBinding

class ViewerFragment : Fragment() {

    private var _binding: FragmentViewerBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val viewerViewModel =
            ViewModelProvider(this)[ViewerViewModel::class.java]

        _binding = FragmentViewerBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textViewerScreen
        viewerViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }

        setupListeners()

        return root
    }

    private fun setupListeners() {
        binding.buttonLoad.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_viewer_to_navigation_loader)
        }
        binding.buttonEdit.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_viewer_to_navigation_editor)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}