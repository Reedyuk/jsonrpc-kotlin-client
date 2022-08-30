package uk.co.andrewreed.jsonrpc.ResultParsers

interface ResultParser<Result> {
    fun parse(obj: String): Result
}

open class AnyResultParser<T> : ResultParser<T> {
    override fun parse(obj: String): T {
        // try to parse, maybe could use kotlin serialisation.
        return obj as T
    }
}
