package me.andrewreed.jsonrpc.ResultParsers

import kotlinx.serialization.json.JsonPrimitive
import me.andrewreed.jsonrpc.Client.InvalidFormatResultParserError

class JsonPrimitiveResultParser : AnyResultParser<JsonPrimitive>() {
    override fun parse(obj: Any): JsonPrimitive {
        try {
            return obj as JsonPrimitive
        } catch (error: Throwable) {
            throw InvalidFormatResultParserError(error)
        }
    }
}