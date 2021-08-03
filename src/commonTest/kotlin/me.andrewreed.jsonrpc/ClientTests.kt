package me.andrewreed.jsonrpc

import kotlin.test.Test

expect fun runTest(test: suspend () -> Unit)

class ClientTests {

    @Test
    fun testGet() = runTest {
        val client = Client()
        client.get("https://ktor.io/")
    }
}
