package me.andrewreed.jsonrpc

import io.ktor.client.*
import io.ktor.client.features.json.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlin.native.concurrent.SharedImmutable

@SharedImmutable
internal expect val ApplicationDispatcher: CoroutineDispatcher

class RPCClient(private val url: String) {

    private val requestIdGenerator = RequestIdGenerator()

    private val ktorClient: HttpClient = HttpClient() {
        install(JsonFeature)
    }

    //suspend fun <R> invoke(invocation: Invocation<R>): ResultDispatcher<R> {
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
        //throw NotImplementedError("TODO")

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
        //execute request and return result

        val response = ktorClient.post<JsonObject>(url) {
            contentType(ContentType.Application.Json)
            body = "{\n" +
                    "\t\"jsonrpc\":\"2.0\",\n" +
                    "\t\"method\":\"${request.method}\",\n" +
                    "\t\"params\":[],\n" +
                    "\t\"id\":73\n" +
                    "}"
        }
        println("Response -> $response")
        ktorClient.close()

        // need to convert to R

        return response
    }

//    suspend fun get(url: String) {
//        val response: HttpResponse = ktorClient.get(url)
//        println("Response -> $response")
//        ktorClient.close()
//    }
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
//class ResultDispatcher<R>(val invocation: Invocation<R>) {
//
//}

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
            // Need to find a way where we can parse this? as it will be trying to convert a json object.
            return obj as JsonPrimitive
        } catch (error: Throwable) {
            throw error
            // throw ResultParserError.invalidFormat(obj)
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
