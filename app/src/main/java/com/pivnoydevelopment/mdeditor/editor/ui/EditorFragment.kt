package com.pivnoydevelopment.mdeditor.editor.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.TextView
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.pivnoydevelopment.mdeditor.R
import com.pivnoydevelopment.mdeditor.databinding.FragmentEditorBinding

class EditorFragment : Fragment() {

    private var _binding: FragmentEditorBinding? = null
    private var editorViewModel: EditorViewModel? = null

    private var markdown = ""
    private var isEdited = false

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
            markdown = it
            binding.markdownEditor.setText(markdown, TextView.BufferType.EDITABLE)
        }

        binding.markdownEditor.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val text = s.toString()
                isEdited = text != markdown
            }
            override fun afterTextChanged(s: Editable?) {
            }
        })
    }

    private fun setupListeners() {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            if (isEdited) {
                showDialog()
            } else {
                findNavController().popBackStack()
            }
        }
        binding.buttonBack.setOnClickListener {
            if (isEdited) {
                showDialog()
            } else {
                findNavController().popBackStack()
            }
        }
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
            showHeaderMenu(it)
        }
        binding.buttonBold.setOnClickListener {
            wrapSelection("**", "**")
        }
        binding.buttonItalic.setOnClickListener {
            wrapSelection("*", "*")
        }
        binding.buttonStrikeout.setOnClickListener {
            wrapSelection("~~", "~~")
        }
        binding.buttonImage.setOnClickListener {
            insertAtCursor("![Описание](https://)")
        }
        binding.buttonTable.setOnClickListener {
            showTableMenu(it)
        }
    }

    private fun showHeaderMenu(anchor: View) {
        val popup = PopupMenu(requireContext(), anchor)
        popup.menu.apply {
            add("H1")
            add("H2")
            add("H3")
            add("H4")
            add("H5")
            add("H6")
        }
        popup.setOnMenuItemClickListener { item ->
            val level = item.title?.substring(1)?.toInt()
            insertAtCursor("#".repeat(level!!) + " ")
            true
        }
        popup.show()
    }

    private fun showTableMenu(anchor: View) {
        val popup = PopupMenu(requireContext(), anchor)
        popup.menu.apply {
            add("2 x 2")
            add("3 x 3")
            add("4 x 4")
        }
        popup.setOnMenuItemClickListener { item ->
            val dimensions = item.title?.split(" x ")
            val rows = dimensions?.get(0)?.toInt()
            val cols = dimensions?.get(1)?.toInt()
            insertAtCursor(generateMarkdownTable(rows!!, cols!!))
            true
        }
        popup.show()
    }

    private fun generateMarkdownTable(rows: Int, cols: Int): String {
        val header = (1..cols).joinToString("|") { "Header$it" }
        val separator = (1..cols).joinToString("|") { "---" }
        val row = (1..cols).joinToString("|") { "Cell" }

        val builder = StringBuilder()
        builder.append("\n|").append(header).append("|\n")
        builder.append("|").append(separator).append("|\n")
        repeat(rows) {
            builder.append("|").append(row).append("|\n")
        }
        return builder.toString()
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

    private fun showDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(requireContext().getString(R.string.cancel_edit))
            .setMessage(requireContext().getString(R.string.unsaved_changes_will_be_lost))
            .setNegativeButton(requireContext().getString(R.string.cancel)) { _, _ -> }
            .setPositiveButton(requireContext().getString(R.string.yes)) { _, _ -> findNavController().popBackStack() }
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}