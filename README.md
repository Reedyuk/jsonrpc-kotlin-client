# JSON-RPC Kotlin Client 

[![Kotlin](https://img.shields.io/badge/kotlin-1.8.10-blue.svg)](http://kotlinlang.org) 
![badge][badge-android] 
![badge][badge-native] 
![badge][badge-js]

A Kotlin Multiplatform Library for performing JSON-RPC 2.0 commands. 

Taken inspiration from [SwiftJSONRPC](https://github.com/kolyasev/SwiftJSONRPC)

## Running
The library works on the following platforms: `Android`, `iOS`, `JavaScript`. 

## Usage
Create client object with base url:

```kotlin
val client = RPCClient(clientUrl)
```

Create a subclass of the RPCService object

```kotlin
val service = object : RPCService(client) {
    suspend fun sha(): String {
        val sha = invoke("web3_sha3", arrayOf("0x68656c6c6f20776f726c64"))
        return sha.content
    }
}
```

Invoke the service function call to get the result.

```kotlin
val shaResult = service.sha()
```

I would recommend you look at the unit tests to get a better idea. I will be working on another library which uses this library, so will link back to here for more inspiration.

[badge-android]: http://img.shields.io/badge/platform-android-brightgreen.svg?style=flat
[badge-native]: http://img.shields.io/badge/platform-native-lightgrey.svg?style=flat
[badge-js]: http://img.shields.io/badge/platform-js-yellow.svg?style=flat
