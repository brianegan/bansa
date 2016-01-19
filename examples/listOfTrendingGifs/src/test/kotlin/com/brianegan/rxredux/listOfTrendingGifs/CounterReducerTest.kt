package com.brianegan.rxredux.listOfTrendingGifs

import com.brianegan.RxRedux.Store
import com.brianegan.RxRedux.createStore
import rx.schedulers.Schedulers

class CounterReducerTest {

    fun createTestStore(): Store<ApplicationState, AppAction> =
            createStore(ApplicationState(), counterReducer, Schedulers.immediate())
}
