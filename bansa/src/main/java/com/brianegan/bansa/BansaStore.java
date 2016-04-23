package com.brianegan.bansa;

import java.util.ArrayList;
import java.util.List;

public class BansaStore<S, A> implements Store<S, A> {
    private S currentState;
    private final Reducer<S, A> reducer;
    private final List<Subscriber<S>> subscribers = new ArrayList<>();
    private final List<NextDispatcher<A>> dispatchers = new ArrayList<>();
    private final Middleware<S, A> dispatcher = new Middleware<S, A>() {
        @Override
        public void dispatch(Store<S, A> store, A action, NextDispatcher<A> next) {
            synchronized (this) {
                currentState = reducer.reduce(store.getState(), action);
            }
            for (int i = 0; i < subscribers.size(); i++) {
                subscribers.get(i).onStateChange(currentState);
            }
        }
    };

    @SafeVarargs
    public BansaStore(S initialState, Reducer<S, A> reducer, Middleware<S, A>... middlewares) {
        this.reducer = reducer;
        this.currentState = initialState;
        final Store<S, A> store = this;

        dispatchers.add(new NextDispatcher<A>() {
            public void dispatch(A action) {
                dispatcher.dispatch(store, action, null);
            }
        });

        for (int i = middlewares.length - 1; i >= 0; i--) {
            final Middleware<S, A> middleware = middlewares[i];
            final NextDispatcher<A> next = dispatchers.get(0);
            dispatchers.add(0, new NextDispatcher<A>() {
                @Override
                public void dispatch(A action) {
                    middleware.dispatch(store, action, next);
                }
            });
        };
    }

    @Override
    public synchronized S getState() {
        return currentState;
    }

    @Override
    public S dispatch(A action) {
        this.dispatchers.get(0).dispatch(action);
        return currentState;
    }

    @Override
    public Subscription subscribe(final Subscriber<S> subscriber) {
        this.subscribers.add(subscriber);
        return new Subscription() {
            @Override
            public void unsubscribe() {
                subscribers.remove(subscriber);
            }
        };
    }
}
