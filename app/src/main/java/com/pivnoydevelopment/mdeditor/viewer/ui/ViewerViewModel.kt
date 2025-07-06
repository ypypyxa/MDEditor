package com.pivnoydevelopment.mdeditor.viewer.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.pivnoydevelopment.mdeditor.common.utils.TempStorageManager

class ViewerViewModel : ViewModel() {

    private val tempStorage = TempStorageManager.getInteractor()

    private val _markdown = MutableLiveData<String>().apply {
        value = tempStorage.getSavedMarkdown()
    }
    val markdown: LiveData<String> = _markdown
}