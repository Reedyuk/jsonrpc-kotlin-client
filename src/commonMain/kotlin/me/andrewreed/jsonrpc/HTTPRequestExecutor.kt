package me.andrewreed.jsonrpc

import io.ktor.client.*

interface ResultParser<Result> {
    fun parse(obj: Any): Result
}

open class AnyResultParser<T> : ResultParser<T> {
    override fun parse(obj: Any): T {
        // try to parse, maybe could use kotlin serialisation.
        return obj as T
    }
}

typealias Params = Map<String, Any?>

data class Invocation<Result>(
    val method: String,
    val params: Params?,
    val parser: AnyResultParser<Result>
)

class Request<R>(val id: String? = null, invocaction: Invocation<R>) {

    val method = invocaction.method
    val params = invocaction.params

    // buildBody
    fun buildBody(): Map<String, Any> {
        val body: MutableMap<String, Any> = mutableMapOf(
            JsonKeys.jsonrpc.name to HTTPRequestExecutorConfig.version,
            JsonKeys.method.name to method
            // JsonKeys.params.name to params
        )
        id?.let { body[JsonKeys.id.name] = it }

        return body
    }
    // prepare params

    private enum class JsonKeys {
        jsonrpc,
        method,
        params,
        result,
        error,
        code,
        message,
        data,
        id
    }
}

interface RequestExecutor {
    suspend fun <Request> execute(request: Request, completionHandler: (RequestExecutorResult) -> Unit)
}

enum class RequestExecutorResult {
    response,
    error,
    cancel
}

class HTTPRequestExecutor(private val config: HTTPRequestExecutorConfig, private val httpClient: HttpClient) : RequestExecutor {

    override suspend fun <Request> execute(request: Request, completionHandler: (RequestExecutorResult) -> Unit) {
        completionHandler(RequestExecutorResult.error)
    }

}
