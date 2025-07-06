package com.pivnoydevelopment.mdeditor.editor.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import com.pivnoydevelopment.mdeditor.R
import com.pivnoydevelopment.mdeditor.databinding.FragmentEditorBinding

class EditorFragment : Fragment() {

    private var _binding: FragmentEditorBinding? = null
    private var editorViewModel: EditorViewModel? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        editorViewModel =
            ViewModelProvider(this).get(EditorViewModel::class.java)

        _binding = FragmentEditorBinding.inflate(inflater, container, false)
        val root: View = binding.root

        setupListeners()
        setupObservers()

        return root
    }

    private fun setupObservers() {
        editorViewModel?.markdown?.observe(viewLifecycleOwner) {
            val markdown = it
            binding.markdownEditor.setText(markdown, TextView.BufferType.EDITABLE)
        }
    }

    private fun setupListeners() {
        binding.buttonSave.setOnClickListener {
            val markdown = binding.markdownEditor.text.toString()
            editorViewModel?.saveMarkdown(markdown)
            findNavController().navigate(
                R.id.navigation_viewer,
                null,
                navOptions {
                    popUpTo(R.id.navigation_loader) {
                        inclusive = false
                    }
                }
            )
        }
        binding.buttonHeader.setOnClickListener {
            insertAtCursor("# ")
        }
        binding.buttonBold.setOnClickListener {
            wrapSelection("**", "**")
        }
        binding.buttonItalic.setOnClickListener {
            wrapSelection("_", "_")
        }
        binding.buttonStrikeout.setOnClickListener {
            wrapSelection("~~", "~~")
        }
        binding.buttonImage.setOnClickListener {
            insertAtCursor("![Описание](http://)")
        }
        binding.buttonTable.setOnClickListener {
            insertAtCursor("\n|Header1|Header2|\n|---|---|\n|Cell1|Cell2|\n")
        }
    }

    private fun wrapSelection(tagStart: String, tagEnd: String) {
        val start = binding.markdownEditor.selectionStart
        val end = binding.markdownEditor.selectionEnd
        val text = binding.markdownEditor.text
        val selected = text.substring(start, end)
        if (start == end) {
            text.insert(start, "$tagStart$tagEnd")
            binding.markdownEditor.setSelection(start + tagStart.length)
        } else {
            text.replace(start, end, "$tagStart$selected$tagEnd")
        }
    }

    private fun insertAtCursor(insertText: String) {
        val start = binding.markdownEditor.selectionStart
        binding.markdownEditor.text.insert(start, insertText)
        binding.markdownEditor.setSelection(start + insertText.length)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}