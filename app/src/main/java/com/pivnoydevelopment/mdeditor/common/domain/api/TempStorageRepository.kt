package com.pivnoydevelopment.mdeditor.common.domain.api

interface TempStorageRepository {
    fun saveMarkdown(markdown: String)
    fun getSavedMarkdown(): String?
    fun clearTemp()
}