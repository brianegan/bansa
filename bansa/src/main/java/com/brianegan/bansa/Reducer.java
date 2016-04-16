package com.brianegan.bansa;

interface Reducer<S, A> {
    S reduce(S state, A action);
}
