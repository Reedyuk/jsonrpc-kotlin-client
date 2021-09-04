package uk.co.andrewreed.jsonrpc.Client

import io.ktor.client.*
import io.ktor.client.features.json.*
import io.ktor.client.request.*
import io.ktor.http.*
import uk.co.andrewreed.jsonrpc.Invocation.Invocation
import uk.co.andrewreed.jsonrpc.RequestExecutor.Error
import uk.co.andrewreed.jsonrpc.RequestExecutor.Request
import uk.co.andrewreed.jsonrpc.RequestExecutor.Response
import uk.co.andrewreed.jsonrpc.kermit

class RPCClient(private val url: String) {

    private val requestIdGenerator = RequestIdGenerator()

    private val ktorClient: HttpClient = HttpClient {
        install(JsonFeature)
    }

    suspend fun <R> invoke(invocation: Invocation<R>) = invocation.parser.parse(execute(makeRequest(invocation)))

    private fun <R> makeRequest(invocation: Invocation<R>) = Request(requestIdGenerator.next(), invocation)

    private suspend fun <R> execute(request: Request<R>): Any {
        kermit.i("Request -> $request")

        //  convert to response object
        val response = ktorClient.post<Response>(url) {
            contentType(ContentType.Application.Json)
            body = request.buildBody()
        }

        kermit.i("Response -> $response")
        ktorClient.close()
        response.error?.let { throw ExecuteException(it) }
        return response.result!!
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

class ExecuteException(val error: Error) : Throwable(error.toString())

abstract class ResultParserError(error: Throwable) : Throwable(error)
class InvalidFormatResultParserError(error: Throwable) : ResultParserError(error)
