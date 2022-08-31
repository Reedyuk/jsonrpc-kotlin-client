package uk.co.andrewreed.jsonrpc.Service

import kotlinx.serialization.json.JsonPrimitive
import uk.co.andrewreed.jsonrpc.Client.RPCClient
import uk.co.andrewreed.jsonrpc.Invocation.Invocation
import uk.co.andrewreed.jsonrpc.Invocation.Params
import uk.co.andrewreed.jsonrpc.ResultParsers.AnyResultParser
import uk.co.andrewreed.jsonrpc.ResultParsers.JsonPrimitiveResultParser

abstract class RPCService(private val client: RPCClient) {

    suspend fun <Result> invoke(method: String, params: Params? = null, parser: AnyResultParser<Result>): Result {
        val invocation = makeInvocation(method, params, parser)
        return client.invoke(invocation).result as Result
    }

    suspend fun invoke(method: String, params: Params? = null): JsonPrimitive {
        return invoke(method, params, JsonPrimitiveResultParser())
    }

    private fun <Result> makeInvocation(method: String, params: Params? = null, parser: AnyResultParser<Result>): Invocation<Result> {
        return Invocation<Result>(method, params, parser)
    }
}
