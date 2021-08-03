package me.andrewreed.jsonrpc

import io.ktor.client.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlin.native.concurrent.SharedImmutable

@SharedImmutable
internal expect val ApplicationDispatcher: CoroutineDispatcher

class RPCClient(private val url: String) {

    private val ktorClient: HttpClient = HttpClient()

    suspend fun <R> invoke(invocation: Invocation<R>): R {
        //val request =
        //
        throw NotImplementedError("TODO")
    }

//    suspend fun get(url: String) {
//        val response: HttpResponse = ktorClient.get(url)
//        println("Response -> $response")
//        ktorClient.close()
//    }
}

abstract class RPCService(private val client: RPCClient) {

    suspend fun <Result> invoke(method: String, params: Params? = null, parser: AnyResultParser<Result>): Result {
        val invocation = makeInvocation(method, params, parser)

        return client.invoke(invocation)
    }

    fun <Result>makeInvocation(method: String, params: Params? = null, parser: AnyResultParser<Result>): Invocation<Result> {
        return Invocation<Result>(method, params, parser)
    }

}