package com.brianegan.bansa;

public interface Reducer<S, A> {
    S invoke(S state, A action);
}
