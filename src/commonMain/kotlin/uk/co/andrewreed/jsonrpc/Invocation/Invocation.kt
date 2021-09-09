package uk.co.andrewreed.jsonrpc.Invocation

import kotlinx.serialization.json.JsonArray
import uk.co.andrewreed.jsonrpc.ResultParsers.AnyResultParser

typealias Params = JsonArray

data class Invocation<Result>(
    val method: String,
    val params: Params?,
    val parser: AnyResultParser<Result>
)
