package com.pivnoydevelopment.mdeditor.loader.ui

import androidx.lifecycle.ViewModel
import com.pivnoydevelopment.mdeditor.common.utils.TempStorageManager

class LoaderViewModel : ViewModel() {

    private val tempStorage = TempStorageManager.getInteractor()

    fun saveTempMarkdown(markdown: String) {
        tempStorage.saveMarkdown(markdown)
    }
}