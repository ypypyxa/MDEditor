package com.pivnoydevelopment.mdeditor.common.utils

import com.pivnoydevelopment.mdeditor.common.domain.api.TempStorageInteractor

object TempStorageManager {

    private lateinit var interactor: TempStorageInteractor

    fun init(interactor: TempStorageInteractor) {
        this.interactor = interactor
    }

    fun getInteractor(): TempStorageInteractor {
        if (!::interactor.isInitialized) {
            throw IllegalStateException("TempStorageManager is not initialized")
        }
        return interactor
    }
}