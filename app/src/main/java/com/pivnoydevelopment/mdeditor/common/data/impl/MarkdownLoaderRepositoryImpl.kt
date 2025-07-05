package com.pivnoydevelopment.mdeditor.common.data.impl

import com.pivnoydevelopment.mdeditor.common.data.network.MarkdownLoaderApi
import com.pivnoydevelopment.mdeditor.common.domain.api.MarkdownLoaderRepository

class MarkdownLoaderRepositoryImpl(private val api: MarkdownLoaderApi) : MarkdownLoaderRepository {
    override fun getMarkdown(url: String): String {
        return api.downloadMarkdown(url)
    }
}