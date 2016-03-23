package com.brianegan.bansa.todoList

import android.content.Context
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.widget.LinearLayout
import com.brianegan.bansa.Store
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import trikita.anvil.Anvil
import trikita.anvil.DSL.*
import trikita.anvil.RenderableView
import trikita.anvil.recyclerview.Recycler
import trikita.anvil.recyclerview.Recycler.*

class RootView(c: Context, val store: Store<ApplicationState, Any>) : RenderableView(c) {
    val stateChangeSubscription: Subscription = store
            .stateChanges
            // Yay! We can easily schedule when we perform view updates as to not
            // cause too much churn on the view layer in response to rapid state changes,
            // which could diminish app performance
            .observeOn(AndroidSchedulers.mainThread())
            // This is where the magic happens. The Root view subscribes to state changes,
            // and triggers the Anvil library to re-render the app with the current state.
            .subscribe { Anvil.render() }

    val mapCounterToCounterViewModel = buildMapCounterToCounterViewModel(store)

    val adapter: BansaRenderableRecyclerViewAdapter<Todo, TodoViewModel> = BansaRenderableRecyclerViewAdapter(
            mapCounterToCounterViewModel,
            ::todoView,
            { models, pos ->
                models[pos].id.leastSignificantBits
            }, true
    )

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        stateChangeSubscription.unsubscribe()
    }

    override fun view() {

        frameLayout {
            padding(dip(16), dip(16), dip(16), 0)

            linearLayout {
                size(FILL, dip(48))
                orientation(LinearLayout.HORIZONTAL)
                margin(0, dip(10))

                editText {
                    size(dip(0), FILL)
                    weight(1F)
                    singleLine(true)
                    imeOptions(EditorInfo.IME_ACTION_DONE)

                    text(store.state.newTodoMessage)
                    onTextChanged({ s -> store.dispatch(UPDATE_NEW_TODO_MESSAGE(s.toString())) })
                    onEditorAction { textView, actionId, event ->
                        if (actionId == EditorInfo.IME_ACTION_SEARCH
                                || actionId == EditorInfo.IME_ACTION_DONE
                                || event?.action == KeyEvent.ACTION_DOWN
                                && event?.keyCode == KeyEvent.KEYCODE_ENTER
                                && store.state.newTodoMessage.length > 0
                        ) {
                            store.dispatch(ADD(store.state.newTodoMessage))
                            true
                        } else {
                            false
                        }
                    }
                }

                button {
                    id(R.id.todo_submit)
                    size(WRAP, FILL)
                    text(R.string.add_todo)

                    onClick {
                        if (store.state.newTodoMessage.length > 0) {
                            store.dispatch(ADD(store.state.newTodoMessage))
                        }
                    }
                }
            }

            Recycler.view {
                init {
                    layoutManager(LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false))
                    itemAnimator(DefaultItemAnimator())
                    hasFixedSize(false)
                    margin(dip(0), dip(56), dip(0), dip(0))
                    size(FILL, FILL)
                }

                Recycler.adapter(adapter.update(store.state.todos))
            }
        }
    }
}
