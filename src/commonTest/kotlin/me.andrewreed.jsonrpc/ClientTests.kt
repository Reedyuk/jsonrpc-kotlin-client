package me.andrewreed.jsonrpc

import kotlin.test.Test

expect fun runTest(test: suspend () -> Unit)

class ClientTests {

    @Test
    fun testGet() = runTest {
        val client = RPCClient("http://127.0.0.1:7545")
        // need to implement a json rpc request using service.
        val service = object : RPCService(client) {
            suspend fun gasPrice() {
                val gasPrice = invoke("eth_gasPrice")
                println(gasPrice)
            }
        }
        service.gasPrice()
        // client.
    }
}
