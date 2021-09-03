package me.andrewreed.jsonrpc

import co.touchlab.kermit.Kermit
import kotlinx.coroutines.CoroutineDispatcher
import kotlin.native.concurrent.SharedImmutable

@SharedImmutable
internal expect val ApplicationDispatcher: CoroutineDispatcher

internal val kermit = Kermit()
