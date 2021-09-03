package me.andrewreed.jsonrpc.Client

import io.ktor.client.*
import io.ktor.client.features.json.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonObject
import me.andrewreed.jsonrpc.Invocation.Invocation
import me.andrewreed.jsonrpc.RequestExecutor.Request
import me.andrewreed.jsonrpc.kermit

class RPCClient(private val url: String) {

    private val requestIdGenerator = RequestIdGenerator()

    private val ktorClient: HttpClient = HttpClient {
        install(JsonFeature)
    }

    suspend fun <R> invoke(invocation: Invocation<R>) = invocation.parser.parse(execute(makeRequest(invocation)))

    private fun <R> makeRequest(invocation: Invocation<R>) = Request(requestIdGenerator.next(), invocation)

    private suspend fun <R> execute(request: Request<R>): Any {
        kermit.i("Request -> $request")

        val response = ktorClient.post<JsonObject>(url) {
            contentType(ContentType.Application.Json)
            body = request.buildBody()
        }

        kermit.i("Response -> $response")
        ktorClient.close()
        try {
            return response.getValue("result")
        } catch (error: NoSuchElementException) {
            throw ExecuteException(response.getValue("error").jsonObject)
        }
    }
}

typealias RequestId = String

var lastIdx = 0

class RequestIdGenerator {
    fun next(): RequestId {
        lastIdx += 1
        return lastIdx.toString()
    }
}

class ExecuteException(jsonObject: JsonObject) : Throwable(jsonObject.toString())

abstract class ResultParserError(error: Throwable) : Throwable(error)
class InvalidFormatResultParserError(error: Throwable) : ResultParserError(error)
