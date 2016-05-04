package com.brianegan.bansa.counter

import com.brianegan.bansaDevToolsUi.BansaDevToolsPresenter
import store
import trikita.anvil.Anvil
import trikita.anvil.DSL.*

val devToolsPresenter = BansaDevToolsPresenter<ApplicationState>(store)

internal fun counterScreen(model: RootView.CounterViewModel) {
    scrollView {
        relativeLayout {
            counterView(model)

            xml(R.layout.bansa_dev_tools) {
                below(R.id.counter_view)

                init {
                    devToolsPresenter.unbind()
                    devToolsPresenter.bind(Anvil.currentView())
                }
            }
        }
    }
}
