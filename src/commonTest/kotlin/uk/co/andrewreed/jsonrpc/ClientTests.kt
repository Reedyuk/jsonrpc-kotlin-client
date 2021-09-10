package uk.co.andrewreed.jsonrpc

import co.touchlab.kermit.Kermit
import kotlinx.serialization.json.*
import uk.co.andrewreed.jsonrpc.Client.ExecuteException
import uk.co.andrewreed.jsonrpc.Client.RPCClient
import uk.co.andrewreed.jsonrpc.Service.RPCService
import kotlin.test.*

expect fun runTest(test: suspend () -> Unit)

private val ropsten = "https://ropsten.infura.io/v3/9aa3d95b3bc440fa88ea12eaa4456161"
private val local = "http://127.0.0.1:7545"

// Tests are using Ganache
class ClientTests {
    private val kermit = Kermit()

    private val clientUrl = local

    @Test
    fun testPost() = runTest {
        val client = RPCClient(clientUrl)
        val service = object : RPCService(client) {
            suspend fun gasPrice(): String {
                val gasPrice = invoke("eth_gasPrice")
                kermit.v("$gasPrice")
                return gasPrice.content
            }
        }
        val price = service.gasPrice()
        // assertEquals("0x4a817c800", price)
    }

    //invoke("eth_getBalance", arrayOf(address)).content
    @Test
    fun testBalance() = runTest {
        val client = RPCClient(clientUrl)
        val service = object : RPCService(client) {
            suspend fun balance(address: String): String {
                val bal = invoke("eth_getBalance", JsonArray(listOf(JsonPrimitive(address))))
                kermit.v("$bal")
                return bal.content
            }
        }
        val balance = service.balance("0xFa5fDa418364C2CA452EBD467644d23EE0d8bd80")
        assertEquals("0x56ba9300511b21000", balance)
    }

    @Test
    fun testPostWithParams() = runTest {
        val client = RPCClient(clientUrl)
        val service = object : RPCService(client) {
            suspend fun sha(): String {
                val sha = invoke("web3_sha3", JsonArray(listOf(JsonPrimitive("0x68656c6c6f20776f726c64"))))
                kermit.v("$sha")
                return sha.content
            }
        }
        val shaResult = service.sha()
        assertEquals("0x59ec0bfb9d986ae04ea83e7cb8204c22a2ae445ac86a9cbfd793c9d5ae0e6299", shaResult)
    }

    @Test
    fun testPostCallWithParams() = runTest {
        val client = RPCClient(clientUrl)
        val map = mapOf(
            "to" to JsonPrimitive("0xF7e4B57862EC47A9B059b8D2D051bBd3A8A64A14"),
            "data" to JsonPrimitive("0xfe50cc72")
        )
        val service = object : RPCService(client) {
            suspend fun call(): String {
                val resp = invoke(
                    "eth_call",
                    JsonArray(
                        listOf(JsonObject(map))
                    )
                )
                kermit.v("$resp")
                return resp.content
            }
        }
        val result = service.call()
        assertEquals("0x0000000000000000000000000000000000000000000000000000000000000020000000000000000000000000000000000000000000000000000000000000000b48656c6c6f20576f726c64000000000000000000000000000000000000000000", result)
    }

    @Test
    fun testPostSerializableParams() = runTest {
        val client = RPCClient(clientUrl)
        val data = CallObject("0xF7e4B57862EC47A9B059b8D2D051bBd3A8A64A14", "0xfe50cc72")
        val service = object : RPCService(client) {
            suspend fun call(): String {
                val resp = invoke(
                    "eth_call",
                    JsonArray(listOf(data.toJsonObject()))
                )
                kermit.v("$resp")
                return resp.content
            }
        }
        val result = service.call()
        assertEquals("0x0000000000000000000000000000000000000000000000000000000000000020000000000000000000000000000000000000000000000000000000000000000b48656c6c6f20576f726c64000000000000000000000000000000000000000000", result)
    }

    data class CallObject(
        val to: String,
        val data: String
    ) {
        fun toJsonObject(): JsonObject =
            JsonObject(
                mapOf(
                    "to" to JsonPrimitive(to),
                    "data" to JsonPrimitive(data)
                )
            )
    }

    @Test
    fun testPostInvalid() = runTest {
        try {
            val client = RPCClient(clientUrl)
            val service = object : RPCService(client) {
                suspend fun execute() { invoke("web3_sha3") }
            }
            service.execute()
            fail("Should of thrown exception")
        } catch (exception: Throwable) {
            assertTrue(exception is ExecuteException)
            assertEquals(-32602, exception.error.code)
        }
    }

    @Test
    fun testPostNoEndPoint() = runTest {
        try {
            val client = RPCClient("http://x")
            val service = object : RPCService(client) {
                suspend fun execute() { invoke("fsfdsfdfsdf") }
            }
            service.execute()
            fail("Should of thrown exception")
        } catch (exception: Throwable) {
            assertFalse(exception is ExecuteException)
        }
    }
}
