package com.pivnoydevelopment.mdeditor.common.data.sharedpreferences

interface TempStorageApi {
    fun saveMarkdown(markdown: String)
    fun getSavedMarkdown(): String?
    fun clearTemp()
}