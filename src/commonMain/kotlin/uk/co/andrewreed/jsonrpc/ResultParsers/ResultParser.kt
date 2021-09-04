package uk.co.andrewreed.jsonrpc.ResultParsers

interface ResultParser<Result> {
    fun parse(obj: Any): Result
}

open class AnyResultParser<T> : ResultParser<T> {
    override fun parse(obj: Any): T {
        // try to parse, maybe could use kotlin serialisation.
        return obj as T
    }
}
