import com.brianegan.bansa.BaseStore
import com.brianegan.bansa.counter.ApplicationState
import com.brianegan.bansa.counter.CounterReducer

val store = BaseStore(ApplicationState(), CounterReducer())
