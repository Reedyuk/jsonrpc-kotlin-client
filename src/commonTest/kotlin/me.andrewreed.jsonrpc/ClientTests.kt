package me.andrewreed.jsonrpc

import kotlin.test.Test

expect fun runTest(test: suspend () -> Unit)

class ClientTests {

    @Test
    fun testGet() = runTest {
        val client = RPCClient("https://ktor.io/")
        // need to implement a json rpc request using service.
    }
}
