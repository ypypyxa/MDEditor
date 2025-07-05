package com.pivnoydevelopment.mdeditor.common.domain.impl

import com.pivnoydevelopment.mdeditor.common.domain.api.TempStorageInteractor
import com.pivnoydevelopment.mdeditor.common.domain.api.TempStorageRepository

class TempStorageInteractorImpl(
    private val tempStorageRepository: TempStorageRepository
) : TempStorageInteractor {

    override fun saveMarkdown(markdown: String) {
        tempStorageRepository.saveMarkdown(markdown)
    }

    override fun getSavedMarkdown(): String? {
        return tempStorageRepository.getSavedMarkdown()
    }

    override fun clearTemp() {
        tempStorageRepository.clearTemp()
    }

}