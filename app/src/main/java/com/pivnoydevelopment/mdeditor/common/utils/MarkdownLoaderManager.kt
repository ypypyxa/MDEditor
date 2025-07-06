package com.pivnoydevelopment.mdeditor.common.utils

import android.content.Context
import com.pivnoydevelopment.mdeditor.common.data.impl.MarkdownLoaderRepositoryImpl
import com.pivnoydevelopment.mdeditor.common.data.network.MarkdownLoaderClient
import com.pivnoydevelopment.mdeditor.common.domain.api.MarkdownLoaderInteractor
import com.pivnoydevelopment.mdeditor.common.domain.impl.MarkdownLoaderLoaderInteractorImpl

object MarkdownLoaderManager {

    private lateinit var interactor: MarkdownLoaderInteractor

    fun init(context: Context) {
        val api = MarkdownLoaderClient()
        val repository = MarkdownLoaderRepositoryImpl(api)
        interactor = MarkdownLoaderLoaderInteractorImpl(repository, context.applicationContext)
    }

    fun getInteractor(): MarkdownLoaderInteractor {
        if (!::interactor.isInitialized) {
            throw IllegalStateException("MarkdownManager is not initialized. Call init() in Application.")
        }
        return interactor
    }
}