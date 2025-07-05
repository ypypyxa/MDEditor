package com.pivnoydevelopment.mdeditor.viewer.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.pivnoydevelopment.mdeditor.R
import com.pivnoydevelopment.mdeditor.databinding.FragmentViewerBinding
import com.pivnoydevelopment.mdeditor.viewer.utils.MarkdownParser

class ViewerFragment : Fragment() {

    private var _binding: FragmentViewerBinding? = null
    private var viewerViewModel: ViewerViewModel? = null

    private val binding get() = _binding!!

    private lateinit var parser: MarkdownParser

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewerViewModel = ViewModelProvider(this)[ViewerViewModel::class.java]

        _binding = FragmentViewerBinding.inflate(inflater, container, false)
        val root: View = binding.root

        initComponents()
        setupListeners()

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewerViewModel?.markdown?.observe(viewLifecycleOwner) {
            val markdown = it
            val views = parser.parse(markdown)
            views.forEach { binding.markdownContainer.addView(it) }
        }
    }

    private fun initComponents() {
        parser = MarkdownParser(requireContext())
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