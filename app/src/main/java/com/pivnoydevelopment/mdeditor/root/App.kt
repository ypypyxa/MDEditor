package com.pivnoydevelopment.mdeditor.root

import android.app.Application
import com.pivnoydevelopment.mdeditor.common.utils.MarkdownLoaderManager
import com.pivnoydevelopment.mdeditor.common.utils.TempStorageManager


class App : Application() {

    override fun onCreate() {
        super.onCreate()

        MarkdownLoaderManager.init(this)
        TempStorageManager.init(this)
    }
}