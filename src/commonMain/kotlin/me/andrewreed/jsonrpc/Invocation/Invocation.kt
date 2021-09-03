package me.andrewreed.jsonrpc.Invocation

import me.andrewreed.jsonrpc.ResultParsers.AnyResultParser

typealias Params = Array<String>

data class Invocation<Result>(
    val method: String,
    val params: Params?,
    val parser: AnyResultParser<Result>
)