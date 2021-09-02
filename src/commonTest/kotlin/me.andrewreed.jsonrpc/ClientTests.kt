package me.andrewreed.jsonrpc

import co.touchlab.kermit.Kermit
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.fail

expect fun runTest(test: suspend () -> Unit)

class ClientTests {
    private val kermit = Kermit()

    @Test
    fun testPost() = runTest {
        val client = RPCClient("http://127.0.0.1:7545")
        // need to implement a json rpc request using service.
        val service = object : RPCService(client) {
            suspend fun gasPrice(): String {
                val gasPrice = invoke("eth_gasPrice")
                kermit.v("$gasPrice")
                return gasPrice.content
            }
        }
        val price = service.gasPrice()
        assertEquals("0x4a817c800", price)
    }

    @Test
    fun testPostInvalid() = runTest {
        try {
            val client = RPCClient("http://127.0.0.1:7545")
            val service = object : RPCService(client) {
                suspend fun gasPrice() {
                    val gasPrice = invoke("fsfdsfdfsdf")
                    kermit.v("$gasPrice")
                }
            }
            service.gasPrice()
            fail("Should of thrown exception")
        } catch (exception: Throwable) {
            assertTrue(exception is ExecuteException)
        }
    }
}
