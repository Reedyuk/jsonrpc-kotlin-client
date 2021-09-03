package me.andrewreed.jsonrpc.RequestExecutor

interface RequestExecutor {
    suspend fun <Request> execute(request: Request, completionHandler: (RequestExecutorResult) -> Unit)
}