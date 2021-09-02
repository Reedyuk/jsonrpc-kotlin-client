package me.andrewreed.jsonrpc

import co.touchlab.kermit.Kermit
import io.ktor.client.*
import io.ktor.client.features.json.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.jsonObject
import kotlin.native.concurrent.SharedImmutable

@SharedImmutable
internal expect val ApplicationDispatcher: CoroutineDispatcher

internal val kermit = Kermit()

class RPCClient(private val url: String) {

    private val requestIdGenerator = RequestIdGenerator()

    private val ktorClient: HttpClient = HttpClient() {
        install(JsonFeature)
    }

    // suspend fun <R> invoke(invocation: Invocation<R>): ResultDispatcher<R> {
    suspend fun <R> invoke(invocation: Invocation<R>): R {
        val request = makeRequest(invocation)
        //
        val response = execute(request)
        return invocation.parser.parse(response)

//        DispatchQueue.global().async { [weak self] in
//                self?.execute(request: request, withResultDispatcher: resultDispatcher)
//        }
//
//        return resultDispatcher.promise
        // throw NotImplementedError("TODO")

        // Init request
//        let request = makeRequest(invocation: invocation)
//
//        // Init result dispatcher
//        let resultDispatcher = ResultDispatcher(invocation: invocation)
//
//        // Perform request
//        DispatchQueue.global().async { [weak self] in
//                self?.execute(request: request, withResultDispatcher: resultDispatcher)
//        }
//
//        return resultDispatcher.promise
    }

    private fun <R> makeRequest(invocation: Invocation<R>): Request<R> {
        val identifier = requestIdGenerator.next()
        return Request(identifier, invocation)
    }

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

// dont think its needed.
// class ResultDispatcher<R>(val invocation: Invocation<R>) {
//
// }

// final class ResultDispatcher<Result>
// {
// // MARK: - Initialization
//
//    init(invocation: Invocation<Result>)
//    {
//        self.invocation = invocation
//        (self.promise, self.resolver) = Promise<Result>.pending()
//    }
//
// // MARK: - Properties
//
//    let invocation: Invocation<Result>
//
//    let promise: Promise<Result>
//
// // MARK: - Functions
//
//    func dispatch(result: Result)
//    {
//        self.resolver.fulfill(result)
//    }
//
//    func dispatch(error: Error)
//    {
//        self.resolver.reject(error)
//    }
//
// // MARK: - Variables
//
//    private let resolver: Resolver<Result>
//
// }

class JsonPrimitiveResultParser : AnyResultParser<JsonPrimitive>() {
    override fun parse(obj: Any): JsonPrimitive {
        try {
            return obj as JsonPrimitive
        } catch (error: Throwable) {
            throw InvalidFormatResultParserError(error)
        }
    }
}

abstract class RPCService(private val client: RPCClient) {

    suspend fun <Result> invoke(method: String, params: Params? = null, parser: AnyResultParser<Result>): Result {
        val invocation = makeInvocation(method, params, parser)
        return client.invoke(invocation)
    }

    suspend fun invoke(method: String, params: Params? = null): JsonPrimitive {
        return invoke(method, params, JsonPrimitiveResultParser())
    }

    fun <Result> makeInvocation(method: String, params: Params? = null, parser: AnyResultParser<Result>): Invocation<Result> {
        return Invocation<Result>(method, params, parser)
    }
}

class ExecuteException(jsonObject: JsonObject) : Throwable(jsonObject.toString())

abstract class ResultParserError(error: Throwable) : Throwable(error)
class InvalidFormatResultParserError(error: Throwable) : ResultParserError(error)
