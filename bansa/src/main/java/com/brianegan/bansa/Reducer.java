package com.brianegan.bansa;

public interface Reducer<S> {
    S reduce(S state, Action action);
}
