package me.andrewreed.jsonrpc.RequestExecutor

import me.andrewreed.jsonrpc.RequestExecutor.HTTP.HTTPRequestExecutorConfig
import me.andrewreed.jsonrpc.Invocation.Invocation

class Request<R>(val id: String? = null, invocation: Invocation<R>) {

    private val method = invocation.method
    private val params = invocation.params

    // buildBody
    fun buildBody(): Map<String, Any> {
        val body: MutableMap<String, Any> = mutableMapOf(
            JsonKeys.jsonrpc.name to HTTPRequestExecutorConfig.version,
            JsonKeys.method.name to method
        )
        params?.let { params -> body[JsonKeys.params.name] = params.joinToString(prefix = "[", postfix = "]", separator = ",") }
        id?.let { body[JsonKeys.id.name] = it }
        return body
    }

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

    override fun toString(): String = "{ " +
        "id = " + id + ", " +
        "method = " + method + ", " +
        "params = " + params + " " +
        "}"
}