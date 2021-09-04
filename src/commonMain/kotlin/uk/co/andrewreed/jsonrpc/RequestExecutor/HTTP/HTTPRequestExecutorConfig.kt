package uk.co.andrewreed.jsonrpc.RequestExecutor.HTTP

import io.ktor.http.*

data class HTTPRequestExecutorConfig(
    val baseURL: Url
) {
    companion object {
        const val version = "2.0"
    }
}
