package com.pivnoydevelopment.mdeditor.common.data.network

interface MarkdownLoaderApi {
    fun downloadMarkdown(urlString: String): String
}