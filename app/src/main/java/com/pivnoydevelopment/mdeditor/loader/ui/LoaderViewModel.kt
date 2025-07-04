package com.pivnoydevelopment.mdeditor.loader.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LoaderViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is Loader Fragment"
    }
    val text: LiveData<String> = _text
}