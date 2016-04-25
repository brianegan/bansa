package com.brianegan.bansa;

public interface Reducer<S, A> {
    S reduce(S state, A action);
}
