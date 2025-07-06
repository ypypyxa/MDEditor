package com.pivnoydevelopment.mdeditor.common.domain.impl

import android.content.Context
import android.os.Handler
import android.os.Looper
import com.pivnoydevelopment.mdeditor.R
import com.pivnoydevelopment.mdeditor.common.domain.api.MarkdownLoaderInteractor
import com.pivnoydevelopment.mdeditor.common.domain.api.MarkdownLoaderRepository
import com.pivnoydevelopment.mdeditor.common.domain.model.LoadResult
import com.pivnoydevelopment.mdeditor.common.utils.NetworkUtils

class MarkdownLoaderLoaderInteractorImpl(
    private val repository: MarkdownLoaderRepository,
    private val appContext: Context
) : MarkdownLoaderInteractor {

    private val mainHandler = Handler(Looper.getMainLooper())

    override fun loadMarkdown(url: String, onResult: (LoadResult) -> Unit) {

        val internetError = appContext.getString(R.string.internet_error)
        val loadingError = appContext.getString(R.string.loading_error)

        mainHandler.post {
            onResult(LoadResult.Loading)
        }

        Thread {
            if (!NetworkUtils.isInternetAvailable(appContext)) {
                mainHandler.post {
                    onResult(LoadResult.Error("$loadingError $internetError"))
                }
                return@Thread
            }

            try {
                val content = repository.getMarkdown(url)
                mainHandler.post {
                    onResult(LoadResult.Success(content))
                }
            } catch (e: Exception) {
                mainHandler.post {
                    onResult(LoadResult.Error("$loadingError ${e.localizedMessage}"))
                }
            }
        }.start()
    }
}