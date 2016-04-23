package com.brianegan.bansa;

public interface NextDispatcher<A> {
    void dispatch(A action);
}
