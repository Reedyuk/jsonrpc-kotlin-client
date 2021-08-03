package me.andrewreed.jsonrpc

import io.ktor.http.*

data class HTTPRequestExecutorConfig(
    val baseURL: Url
) {
    companion object {
        val version = "2.0"
    }
}
