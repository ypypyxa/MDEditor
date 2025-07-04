package com.pivnoydevelopment.mdeditor.editor.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.pivnoydevelopment.mdeditor.R
import com.pivnoydevelopment.mdeditor.databinding.FragmentEditorBinding

class EditorFragment : Fragment() {

    private var _binding: FragmentEditorBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val editorViewModel =
            ViewModelProvider(this).get(EditorViewModel::class.java)

        _binding = FragmentEditorBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textEditorScreen
        editorViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }

        setupListeners()

        return root
    }

    private fun setupListeners() {
        binding.buttonView.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_editor_to_navigation_viewer)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}