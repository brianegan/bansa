package com.brianegan.bansa;

public interface Middleware<S> {
    void dispatch(Store<S> store, Action action, NextDispatcher next);
}
