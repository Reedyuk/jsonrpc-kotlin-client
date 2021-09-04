package uk.co.andrewreed.jsonrpc.ResultParsers

import kotlinx.serialization.json.JsonPrimitive
import uk.co.andrewreed.jsonrpc.Client.InvalidFormatResultParserError

class JsonPrimitiveResultParser : AnyResultParser<JsonPrimitive>() {
    override fun parse(obj: Any): JsonPrimitive {
        try {
            return obj as JsonPrimitive
        } catch (error: Throwable) {
            throw InvalidFormatResultParserError(error)
        }
    }
}
