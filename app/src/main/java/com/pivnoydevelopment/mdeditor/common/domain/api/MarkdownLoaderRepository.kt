package com.pivnoydevelopment.mdeditor.common.domain.api

interface MarkdownLoaderRepository {
    fun getMarkdown(url: String): String
}