package com.pivnoydevelopment.mdeditor.loader.ui

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.pivnoydevelopment.mdeditor.R
import com.pivnoydevelopment.mdeditor.common.domain.model.LoadResult
import com.pivnoydevelopment.mdeditor.databinding.FragmentLoaderBinding
import java.io.BufferedReader
import java.io.InputStreamReader

class LoaderFragment : Fragment() {

    private var _binding: FragmentLoaderBinding? = null
    private var loaderViewModel: LoaderViewModel? = null

    private lateinit var documentPicker: ActivityResultLauncher<Array<String>>
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        loaderViewModel = ViewModelProvider(this)[LoaderViewModel::class.java]

        _binding = FragmentLoaderBinding.inflate(inflater, container, false)
        val root: View = binding.root

        initComponents()
        setupListeners()

        return root
    }

    private fun initComponents() {
        documentPicker =
            registerForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
                if (uri != null) {
                    loaderViewModel?.saveTempMarkdown(readTextFromUri(uri))
                    findNavController().navigate(R.id.action_navigation_loader_to_navigation_viewer)
                }
            }

        requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                documentPicker.launch(arrayOf("text/markdown", "text/plain"))
            } else {
                showPermissionDeniedDialog()
            }
        }
        binding.urlEditText.requestFocus()
    }

    private fun setupListeners() {
        binding.buttonOpenFile.setOnClickListener {
            requestPermission()
        }
        binding.urlEditText.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                binding.urlEditText.hint = requireContext().getString(R.string.default_url)
            } else {
                binding.urlEditText.hint = ""
            }
        }
        binding.buttonLoadFile.setOnClickListener {
            var urlString = binding.urlEditText.text.toString()
            if (urlString.isEmpty()) urlString = requireContext().getString(R.string.default_url)

            loaderViewModel?.downloadMarkdown(urlString) { result ->
                when (result) {
                    is LoadResult.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    is LoadResult.Success -> {
                        binding.progressBar.visibility = View.GONE
                        loaderViewModel?.saveTempMarkdown(result.content)
                        findNavController().navigate(R.id.action_navigation_loader_to_navigation_viewer)
                    }
                    is LoadResult.Error -> {
                        binding.progressBar.visibility = View.GONE
                        showToast(result.message)
                    }
                }
            }
        }
    }

    private fun requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            documentPicker.launch(arrayOf("text/markdown", "text/plain"))
        } else {
            requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
    }

    private fun showPermissionDeniedDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(requireContext().getString(R.string.permission_denied))
            .setMessage(requireContext().getString(R.string.permission_denied_message))
            .setPositiveButton(requireContext().getString(R.string.open_settings)) { _, _ ->
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri = Uri.fromParts("package", requireContext().packageName, null)
                intent.data = uri
                startActivity(intent)
            }
            .setNegativeButton(requireContext().getString(R.string.cancel), null)
            .show()
    }

    private fun readTextFromUri(uri: Uri): String {
        val stringBuilder = StringBuilder()
        requireContext().contentResolver.openInputStream(uri)?.use { inputStream ->
            BufferedReader(InputStreamReader(inputStream)).use { reader ->
                var line = reader.readLine()
                while (line != null) {
                    stringBuilder.appendLine(line)
                    line = reader.readLine()
                }
            }
        }
        return stringBuilder.toString()
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}