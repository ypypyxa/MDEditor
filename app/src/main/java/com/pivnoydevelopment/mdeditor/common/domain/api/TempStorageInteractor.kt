package com.pivnoydevelopment.mdeditor.common.domain.api

interface TempStorageInteractor {
    fun saveMarkdown(markdown: String)
    fun getSavedMarkdown(): String?
    fun clearTemp()
}