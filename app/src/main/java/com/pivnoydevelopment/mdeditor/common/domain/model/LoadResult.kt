package com.pivnoydevelopment.mdeditor.common.domain.model

sealed class LoadResult {
    object Loading : LoadResult()
    data class Success(val content: String) : LoadResult()
    data class Error(val message: String) : LoadResult()
}