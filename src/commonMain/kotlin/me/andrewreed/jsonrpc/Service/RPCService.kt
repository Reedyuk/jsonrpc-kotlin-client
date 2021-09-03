package me.andrewreed.jsonrpc.Service

import kotlinx.serialization.json.JsonPrimitive
import me.andrewreed.jsonrpc.Invocation.Invocation
import me.andrewreed.jsonrpc.Client.RPCClient
import me.andrewreed.jsonrpc.Invocation.Params
import me.andrewreed.jsonrpc.ResultParsers.AnyResultParser
import me.andrewreed.jsonrpc.ResultParsers.JsonPrimitiveResultParser

abstract class RPCService(private val client: RPCClient) {

    suspend fun <Result> invoke(method: String, params: Params? = null, parser: AnyResultParser<Result>): Result {
        val invocation = makeInvocation(method, params, parser)
        return client.invoke(invocation)
    }

    suspend fun invoke(method: String, params: Params? = null): JsonPrimitive {
        return invoke(method, params, JsonPrimitiveResultParser())
    }

    private fun <Result> makeInvocation(method: String, params: Params? = null, parser: AnyResultParser<Result>): Invocation<Result> {
        return Invocation<Result>(method, params, parser)
    }
}