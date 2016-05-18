package com.brianegan.bansaKotlin.asyncaction

import com.brianegan.bansa.*
import org.assertj.core.api.Assertions
import org.junit.Test

/**
 * Created by Dario on 3/22/2016.
 */
class AsyncActionMiddlewareTest {
    class IncrementCounterAction:Action;
    data class TestState(val counter:Int=0,val lastAsyncActionMessage: String = "none",val lastAsyncActionError: String? =null, val lastAsyncActionResult:Int?=null)
    val actionDifficultTag = "A very difficult mathematical problem"
    val actionDifficultError ="Sometimes difficult problems cannot be solved"
    val reducer = Reducer<TestState> { state, action ->
        when (action) {
            is IncrementCounterAction -> TestState(counter=state.counter+1)
            is AsyncAction.Completed<*> ->
                when (action.type) {
                    actionDifficultTag -> TestState(
                            lastAsyncActionMessage = actionDifficultTag,
                            lastAsyncActionError = null,
                            lastAsyncActionResult = action.payload as Int)
                    else -> state
                }
            is AsyncAction.Failed ->
                when (action.type) {
                    actionDifficultTag -> TestState(
                            lastAsyncActionMessage = actionDifficultTag,
                            lastAsyncActionError = action.error.message,
                            lastAsyncActionResult = null)
                    else -> state
                }
            else -> state
        }
    }
    @Test
    fun `test an async action for a very difficult and computation heavy operation`() {
        val store = BaseStore(TestState(), reducer, AsyncActionMiddleWare())

        val asyncAction = AsyncAction.Started(actionDifficultTag) { 2 + 2 }
        store.dispatch(asyncAction)

        //asyncAction.resolve() //block until async action executed
        store.subscribe { //on state change
            Assertions.assertThat(store.state.lastAsyncActionMessage).isEqualTo(actionDifficultTag)
            Assertions.assertThat(store.state.lastAsyncActionError).isNull()
            Assertions.assertThat(store.state.lastAsyncActionResult).isEqualTo(2+2)
        }
    }
    @Test
    fun `test an async action for a very difficult and computation heavy operation that fails`() {

        val store = BaseStore(TestState(), reducer, AsyncActionMiddleWare())


        val asyncAction = AsyncAction.Started(actionDifficultTag) {
            throw Exception(actionDifficultError)
        }

        store.subscribe { //on state change
            Assertions.assertThat(store.state.lastAsyncActionMessage).isEqualTo(actionDifficultTag)
            Assertions.assertThat(store.state.lastAsyncActionError).isEqualTo(actionDifficultError)
            Assertions.assertThat(store.state.lastAsyncActionResult).isNull()
        }
    }
    @Test
    fun `test that normal actions pass through the middleware`() {

        val store = BaseStore(TestState(), reducer, AsyncActionMiddleWare())

        store.dispatch(IncrementCounterAction())

        store.subscribe { //on state change
            Assertions.assertThat(store.state.counter).isEqualTo(1);
            Assertions.assertThat(store.state.lastAsyncActionMessage).isEqualTo("none")
            Assertions.assertThat(store.state.lastAsyncActionError).isNull()
            Assertions.assertThat(store.state.lastAsyncActionResult).isNull()
        }
    }}

