package com.brianegan.bansa;

public class BansaUtils {
    @SafeVarargs
    public static <S, A> Reducer<S, A> combineReducers(final Reducer<S, A>... reducers) {
        return new Reducer<S, A>() {
            @Override
            public S reduce(S state, A action) {
                for (Reducer<S, A> reducer : reducers) {
                    state = reducer.reduce(state, action);
                }

                return state;
            }
        };
    }
}
