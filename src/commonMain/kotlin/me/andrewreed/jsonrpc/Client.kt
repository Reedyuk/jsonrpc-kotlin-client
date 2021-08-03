package me.andrewreed.jsonrpc

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlin.native.concurrent.SharedImmutable

@SharedImmutable
internal expect val ApplicationDispatcher: CoroutineDispatcher

class Client {

    private val ktorClient: HttpClient = HttpClient()

    suspend fun get(url: String) {
        val response: HttpResponse = ktorClient.get(url)
        println("Response -> $response")
        ktorClient.close()
    }
}
