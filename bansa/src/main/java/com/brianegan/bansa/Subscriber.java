package com.brianegan.bansa;

public interface Subscriber<S> {
    void onStateChange(S state);
}
