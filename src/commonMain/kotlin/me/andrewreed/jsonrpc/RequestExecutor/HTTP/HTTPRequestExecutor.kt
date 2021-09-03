package me.andrewreed.jsonrpc.RequestExecutor.HTTP

import io.ktor.client.*
import me.andrewreed.jsonrpc.RequestExecutor.RequestExecutor
import me.andrewreed.jsonrpc.RequestExecutor.RequestExecutorResult


class HTTPRequestExecutor(private val config: HTTPRequestExecutorConfig, private val httpClient: HttpClient) :
    RequestExecutor {

    override suspend fun <Request> execute(request: Request, completionHandler: (RequestExecutorResult) -> Unit) {
        completionHandler(RequestExecutorResult.error)
    }
}
