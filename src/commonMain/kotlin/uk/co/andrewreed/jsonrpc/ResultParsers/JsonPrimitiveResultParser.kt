package uk.co.andrewreed.jsonrpc.ResultParsers

import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.jsonObject
import uk.co.andrewreed.jsonrpc.Client.InvalidFormatResultParserError

class JsonPrimitiveResultParser : AnyResultParser<JsonPrimitive>() {
    override fun parse(obj: String): JsonPrimitive {
        try {
            return JsonPrimitive(obj)
        } catch (error: Throwable) {
            throw InvalidFormatResultParserError(error)
        }
    }
}
