package com.brianegan.bansaDevTools

import com.brianegan.bansa.Action
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class DevToolsReducerTest {
    data class TestState(val message: String = "initial state")
    data class TestAction(val message: String = "test action") : Action

    val testReducer = DevToolsTestReducer()

    @Test
    fun `perform action should update the dev tools store`() {
        val store = DevToolsStore<TestState>(TestState(), testReducer)
        val message = "test"

        store.dispatch(TestAction(message))

        assertThat(store.devToolsState.committedState).isEqualTo(TestState())
        assertThat(store.devToolsState.computedStates.size).isEqualTo(2)
        assertThat(store.devToolsState.stagedActions.size).isEqualTo(2)
        assertThat(store.devToolsState.currentAppState).isEqualTo(TestState(message))
        assertThat(store.devToolsState.currentAction).isEqualTo(TestAction(message))
    }

    @Test
    fun `when back in time, the perform action should overwrite all future actions`() {
        val store = DevToolsStore<TestState>(TestState(), testReducer)
        val first = "first"
        val second = "second"
        val third = "third"

        store.dispatch(TestAction(first))
        store.dispatch(TestAction(second))
        store.dispatch(TestAction(third))

        assertThat(store.devToolsState.committedState).isEqualTo(TestState())
        assertThat(store.devToolsState.computedStates.size).isEqualTo(4)
        assertThat(store.devToolsState.stagedActions.size).isEqualTo(4)
        assertThat(store.devToolsState.computedStates[1]).isEqualTo(TestState(first))
        assertThat(store.devToolsState.stagedActions[1]).isEqualTo(TestAction(first))
        assertThat(store.devToolsState.computedStates[2]).isEqualTo(TestState(second))
        assertThat(store.devToolsState.stagedActions[2]).isEqualTo(TestAction(second))
        assertThat(store.devToolsState.computedStates[3]).isEqualTo(TestState(third))
        assertThat(store.devToolsState.stagedActions[3]).isEqualTo(TestAction(third))
        assertThat(store.devToolsState.currentAppState).isEqualTo(TestState(third))
        assertThat(store.devToolsState.currentAction).isEqualTo(TestAction(third))

        store.dispatch(DevToolsAction.createJumpToStateAction(2))
        store.dispatch(TestAction(first))

        assertThat(store.devToolsState.committedState).isEqualTo(TestState())
        assertThat(store.devToolsState.computedStates.size).isEqualTo(4)
        assertThat(store.devToolsState.stagedActions.size).isEqualTo(4)
        assertThat(store.devToolsState.computedStates[1]).isEqualTo(TestState(first))
        assertThat(store.devToolsState.stagedActions[1]).isEqualTo(TestAction(first))
        assertThat(store.devToolsState.computedStates[2]).isEqualTo(TestState(second))
        assertThat(store.devToolsState.stagedActions[2]).isEqualTo(TestAction(second))
        assertThat(store.devToolsState.computedStates[3]).isEqualTo(TestState(first))
        assertThat(store.devToolsState.stagedActions[3]).isEqualTo(TestAction(first))
        assertThat(store.devToolsState.currentAppState).isEqualTo(TestState(first))
        assertThat(store.devToolsState.currentAction).isEqualTo(TestAction(first))
    }

    @Test
    fun `reset action should roll the current state of the app back to the previously saved state`() {
        val store = DevToolsStore<TestState>(TestState(), testReducer)

        store.dispatch(TestAction("action that will be lost when store is reset"))
        store.dispatch(DevToolsAction.createResetAction())

        assertThat(store.devToolsState.committedState).isEqualTo(TestState())
        assertThat(store.devToolsState.computedStates.size).isEqualTo(1)
        assertThat(store.devToolsState.stagedActions.size).isEqualTo(1)
        assertThat(store.devToolsState.currentAppState).isEqualTo(TestState())
        assertThat(store.devToolsState.currentAction).isEqualTo(DevToolsAction.createResetAction())
    }

    @Test
    fun `save action should commit the current state of the app`() {
        val store = DevToolsStore<TestState>(TestState(), testReducer)
        val message = "action to save"

        store.dispatch(TestAction(message))
        store.dispatch(DevToolsAction.createSaveAction())

        assertThat(store.devToolsState.committedState).isEqualTo(TestState(message))
        assertThat(store.devToolsState.computedStates.size).isEqualTo(1)
        assertThat(store.devToolsState.stagedActions.size).isEqualTo(1)
        assertThat(store.devToolsState.currentAppState).isEqualTo(TestState(message))
        assertThat(store.devToolsState.currentAction).isEqualTo(DevToolsAction.createSaveAction())
    }

    @Test
    fun `jump to state action should set the current state of the app to a given time in the past`() {
        val store = DevToolsStore<TestState>(TestState(), testReducer)
        val jumpToMessage = "action to jump to"
        val finalMessage = "final action"

        store.dispatch(TestAction(jumpToMessage))
        store.dispatch(TestAction(finalMessage))
        store.dispatch(DevToolsAction.createJumpToStateAction(1))

        assertThat(store.devToolsState.computedStates.size).isEqualTo(3)
        assertThat(store.devToolsState.stagedActions.size).isEqualTo(3)
        assertThat(store.devToolsState.currentAppState).isEqualTo(TestState(jumpToMessage))
        assertThat(store.devToolsState.currentAction).isEqualTo(TestAction(jumpToMessage))
    }

    @Test
    fun `recompute action should run all actions through the app reducer again`() {
        val recomputeTestReducer = DevToolsTestReducer()
        val store = DevToolsStore<TestState>(TestState(), recomputeTestReducer)
        val first = "first"
        val second = "second"

        store.dispatch(TestAction(first))
        store.dispatch(TestAction(second))

        assertThat(store.devToolsState.computedStates.size).isEqualTo(3)
        assertThat(store.devToolsState.stagedActions.size).isEqualTo(3)
        assertThat(store.devToolsState.currentAppState).isEqualTo(TestState(second))
        assertThat(store.devToolsState.currentAction).isEqualTo(TestAction(second))

        recomputeTestReducer.updated = true
        store.dispatch(DevToolsAction.createRecomputeAction())

        assertThat(store.devToolsState.computedStates.size).isEqualTo(3)
        assertThat(store.devToolsState.stagedActions.size).isEqualTo(3)
        assertThat(store.devToolsState.computedStates[1]).isEqualTo(TestState("updated $first"))
        assertThat(store.devToolsState.currentAppState).isEqualTo(TestState("updated $second"))
        assertThat(store.devToolsState.currentAction).isEqualTo(TestAction(second))
    }
}
