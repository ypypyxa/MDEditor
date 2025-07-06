package com.pivnoydevelopment.mdeditor.common.data.sharedpreferences

import android.content.SharedPreferences
import androidx.core.content.edit

class TempStorageClient(private val sharedPreferences: SharedPreferences) : TempStorageApi {

    companion object {
        const val TEMP_STORAGE_KEY = "temp_storage"
        private const val TEMP_MARKDOWN_KEY = "markdown"
    }

    override fun saveMarkdown(markdown: String) {
        sharedPreferences.edit { putString(TEMP_MARKDOWN_KEY, markdown) }
    }

    override fun getSavedMarkdown(): String? {
        return sharedPreferences.getString(TEMP_MARKDOWN_KEY, null)
    }

    override fun clearTemp() {
        sharedPreferences.edit { putString(TEMP_MARKDOWN_KEY, "") }
    }
}