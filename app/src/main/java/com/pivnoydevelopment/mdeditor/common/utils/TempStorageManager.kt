package com.pivnoydevelopment.mdeditor.common.utils

import android.content.Context
import com.pivnoydevelopment.mdeditor.common.data.impl.TempStorageRepositoryImpl
import com.pivnoydevelopment.mdeditor.common.data.sharedpreferences.TempStorageClient
import com.pivnoydevelopment.mdeditor.common.data.sharedpreferences.TempStorageClient.Companion.TEMP_STORAGE_KEY
import com.pivnoydevelopment.mdeditor.common.domain.api.TempStorageInteractor
import com.pivnoydevelopment.mdeditor.common.domain.impl.TempStorageInteractorImpl

object TempStorageManager {

    private lateinit var interactor: TempStorageInteractor

    fun init(context: Context) {
        val prefs = context.applicationContext.getSharedPreferences(
            TEMP_STORAGE_KEY,
            Context.MODE_PRIVATE
        )
        val client = TempStorageClient(prefs)
        val repository = TempStorageRepositoryImpl(client)
        interactor = TempStorageInteractorImpl(repository)
    }

    fun getInteractor(): TempStorageInteractor {
        if (!::interactor.isInitialized) {
            throw IllegalStateException("TempStorageManager is not initialized. Call init() in Application.")
        }
        return interactor
    }
}