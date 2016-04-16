package com.brianegan.bansa;

interface NextDispatcher<A> {
    void invoke(A action);
}
