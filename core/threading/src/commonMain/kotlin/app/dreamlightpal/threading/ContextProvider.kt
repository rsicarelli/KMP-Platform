package app.dreamlightpal.threading

import kotlin.coroutines.CoroutineContext

interface ContextProvider {

    val io: CoroutineContext
    val default: CoroutineContext
    val main: CoroutineContext
}
