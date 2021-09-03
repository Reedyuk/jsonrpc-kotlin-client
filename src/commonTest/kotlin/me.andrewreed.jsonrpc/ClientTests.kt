package me.andrewreed.jsonrpc

import co.touchlab.kermit.Kermit
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.fail

expect fun runTest(test: suspend () -> Unit)

// Tests are using Ganache
class ClientTests {
    private val kermit = Kermit()

    @Test
    fun testPost() = runTest {
        val client = RPCClient("http://127.0.0.1:7545")
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
    fun testPostWithParams() = runTest {
        val client = RPCClient("http://127.0.0.1:7545")
        val service = object : RPCService(client) {
            suspend fun sha(): String {
                val sha = invoke("web3_sha3", arrayOf("0x68656c6c6f20776f726c64"))
                kermit.v("$sha")
                return sha.content
            }
        }
        val shaResult = service.sha()
        assertEquals("0xdbf426f3c534816dd14e5e2f888d77bfa2ad01d17a538d4fce73d3267c5a15ef", shaResult)
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
