import com.brianegan.bansa.counter.ApplicationState
import com.brianegan.bansa.counter.CounterReducer
import com.brianegan.bansaDevTools.DevToolsStore

val store = DevToolsStore(ApplicationState(), CounterReducer())
