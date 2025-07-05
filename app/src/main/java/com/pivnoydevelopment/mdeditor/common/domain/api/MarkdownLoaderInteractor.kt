package com.pivnoydevelopment.mdeditor.common.domain.api

import com.pivnoydevelopment.mdeditor.common.domain.model.LoadResult

interface MarkdownLoaderInteractor {
    fun loadMarkdown(url: String, onResult: (LoadResult) -> Unit)
}