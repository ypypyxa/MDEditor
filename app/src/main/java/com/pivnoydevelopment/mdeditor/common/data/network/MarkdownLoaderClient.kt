package com.pivnoydevelopment.mdeditor.common.data.network

import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class MarkdownLoaderClient : MarkdownLoaderApi {
    override fun downloadMarkdown(urlString: String): String {
        val url = URL(urlString)
        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "GET"
        connection.connectTimeout = 5000
        connection.readTimeout = 5000

        val responseCode = connection.responseCode
        if (responseCode != HttpURLConnection.HTTP_OK) {
            throw Exception("HTTP error code: $responseCode")
        }

        val reader = BufferedReader(InputStreamReader(connection.inputStream))
        val content = StringBuilder()
        var line: String?

        while (reader.readLine().also { line = it } != null) {
            content.append(line).append("\n")
        }

        reader.close()
        connection.disconnect()

        return content.toString()
    }
}