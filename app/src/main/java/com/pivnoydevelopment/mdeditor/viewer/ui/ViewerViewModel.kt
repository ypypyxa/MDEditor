package com.pivnoydevelopment.mdeditor.viewer.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ViewerViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is Viewer Fragment"
    }
    val text: LiveData<String> = _text
}