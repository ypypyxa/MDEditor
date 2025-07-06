package com.pivnoydevelopment.mdeditor.loader.ui

import androidx.lifecycle.ViewModel
import com.pivnoydevelopment.mdeditor.common.domain.model.LoadResult
import com.pivnoydevelopment.mdeditor.common.utils.MarkdownLoaderManager
import com.pivnoydevelopment.mdeditor.common.utils.TempStorageManager

class LoaderViewModel : ViewModel() {

    private val tempStorage = TempStorageManager.getInteractor()
    private val downloader = MarkdownLoaderManager.getInteractor()

    fun saveTempMarkdown(markdown: String) {
        tempStorage.saveMarkdown(markdown)
    }

    fun downloadMarkdown(urlString: String, onResult: (LoadResult) -> Unit) {
        downloader.loadMarkdown(urlString, onResult)
    }
}