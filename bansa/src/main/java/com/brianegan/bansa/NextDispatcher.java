package com.brianegan.bansa;

public interface NextDispatcher<A> {
    void invoke(A action);
}
