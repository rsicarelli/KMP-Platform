package app.dreamlightpal.threading

import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.Dispatchers
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.singleton

val ThreadingModule = DI.Module("Threading") {
    bind<ContextProvider>() with singleton {
        object : ContextProvider {
            override val io: CoroutineContext = Dispatchers.IO
            override val default: CoroutineContext = Dispatchers.Default
            override val main: CoroutineContext = Dispatchers.Main
        }
    }
}
