package com.pivnoydevelopment.mdeditor.root

import android.app.Application
import com.pivnoydevelopment.mdeditor.common.data.impl.TempStorageRepositoryImpl
import com.pivnoydevelopment.mdeditor.common.data.sharedpreferences.TempStorageClient
import com.pivnoydevelopment.mdeditor.common.data.sharedpreferences.TempStorageClient.Companion.TEMP_STORAGE_KEY
import com.pivnoydevelopment.mdeditor.common.domain.impl.TempStorageInteractorImpl
import com.pivnoydevelopment.mdeditor.common.utils.TempStorageManager


class App : Application() {

    override fun onCreate() {
        super.onCreate()

        val prefs = applicationContext.getSharedPreferences(
            TEMP_STORAGE_KEY,
            MODE_PRIVATE
        )
        val client = TempStorageClient(prefs)
        val repository = TempStorageRepositoryImpl(client)
        val interactor = TempStorageInteractorImpl(repository)

        TempStorageManager.init(interactor)
    }
}