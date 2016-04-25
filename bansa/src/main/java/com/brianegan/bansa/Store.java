package com.brianegan.bansa;

public interface Store<S, A> {
    S getState();
    S dispatch(A action);
    Subscription subscribe(Subscriber<S> subscriber);
}
