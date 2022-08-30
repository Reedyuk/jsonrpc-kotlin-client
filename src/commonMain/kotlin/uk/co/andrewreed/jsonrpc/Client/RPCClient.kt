package uk.co.andrewreed.jsonrpc.Client

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.cache.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import uk.co.andrewreed.jsonrpc.Invocation.Invocation
import uk.co.andrewreed.jsonrpc.RequestExecutor.Error
import uk.co.andrewreed.jsonrpc.RequestExecutor.Request
import uk.co.andrewreed.jsonrpc.RequestExecutor.Response
import uk.co.andrewreed.jsonrpc.kermit

class RPCClient(private val url: String) {

    private val requestIdGenerator = RequestIdGenerator()

    private val ktorClient: HttpClient = HttpClient {
        install(ContentNegotiation) { json() }
        expectSuccess = true
        developmentMode = true
        install(Logging) {
            logger = Logger.DEFAULT
            level = LogLevel.ALL
        }
    }

    suspend fun <R> invoke(invocation: Invocation<R>) = execute(makeRequest(invocation))

    private fun <R> makeRequest(invocation: Invocation<R>) = Request(requestIdGenerator.next(), invocation)

    private suspend fun <R> execute(request: Request<R>): Response {
        kermit.i("Request -> $request")

        //  convert to response object
        val response = ktorClient.post(url) {
            contentType(ContentType.Application.Json)
            setBody(request.buildBody())
        }
        kermit.i("Response -> ${response.bodyAsText()}")
        ktorClient.close()
        //response.error?.let { throw ExecuteException(it) }
        return response.body()
    }
}

typealias RequestId = Int

var lastIdx = 0

class RequestIdGenerator {
    fun next(): RequestId {
        lastIdx += 1
        return lastIdx
    }
}

class ExecuteException(val error: Error) : Throwable(error.toString())

abstract class ResultParserError(error: Throwable) : Throwable(error)
class InvalidFormatResultParserError(error: Throwable) : ResultParserError(error)
