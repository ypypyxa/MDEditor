package com.pivnoydevelopment.mdeditor.common.data.impl

import com.pivnoydevelopment.mdeditor.common.data.sharedpreferences.TempStorageApi
import com.pivnoydevelopment.mdeditor.common.domain.api.TempStorageRepository

class TempStorageRepositoryImpl(
    private val tempStorageApi: TempStorageApi
) : TempStorageRepository {

    override fun saveMarkdown(markdown: String) {
        tempStorageApi.saveMarkdown(markdown)
    }

    override fun getSavedMarkdown(): String? {
        return tempStorageApi.getSavedMarkdown()
    }

    override fun clearTemp() {
        tempStorageApi.clearTemp()
    }

}