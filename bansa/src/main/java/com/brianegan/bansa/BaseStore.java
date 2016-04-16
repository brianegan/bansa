package com.brianegan.bansa;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BaseStore<S, A> implements Store<S, A> {
    public static <S, A> Store<S, A> create(S initialState, List<Reducer<S, A>> reducers, List<Middleware<S, A>> middlewares) {
        return new BaseStore<>(initialState, reducers, middlewares);
    }

    public static <S, A> Store<S, A> create(S initialState, Reducer<S, A> reducer, List<Middleware<S, A>> middlewares) {
        return new BaseStore<>(initialState, Collections.singletonList(reducer), middlewares);
    }

    public static <S, A> Store<S, A> create(S initialState, List<Reducer<S, A>> reducers, Middleware<S, A> middleware) {
        return new BaseStore<>(initialState, reducers, Collections.singletonList(middleware));
    }

    public static <S, A> Store<S, A> create(S initialState, Reducer<S, A> reducer, Middleware<S, A> middleware) {
        return new BaseStore<>(initialState, Collections.singletonList(reducer), Collections.singletonList(middleware));
    }

    public static <S, A> Store<S, A> create(S initialState, Reducer<S, A> reducer) {
        return new BaseStore<>(initialState, Collections.singletonList(reducer), Collections.<Middleware<S, A>>emptyList());
    }

    public static <S, A> Store<S, A> create(S initialState, List<Reducer<S, A>> reducers) {
        return new BaseStore<>(initialState, reducers, Collections.<Middleware<S, A>>emptyList());
    }

    private S currentState;
    private final List<Reducer<S, A>> reducers;
    private final List<Subscriber> subscribers = new ArrayList<>();
    private final List<NextDispatcher<A>> dispatchers = new ArrayList<>();
    private final Middleware<S, A> dispatcher = new Middleware<S, A>() {
        @Override
        public void invoke(Store<S, A> store, A action, NextDispatcher<A> next) {
            synchronized (this) {
                for (Reducer<S, A> reducer : reducers) {
                    currentState = reducer.invoke(store.getState(), action);
                }
            }
            for (int i = 0; i < subscribers.size(); i++) {
                subscribers.get(i).invoke();
            }
        }
    };

    private BaseStore(S initialState, List<Reducer<S, A>> reducers, List<Middleware<S, A>> middlewares) {
        this.reducers = reducers;
        this.currentState = initialState;
        final Store<S, A> store = this;

        dispatchers.add(new NextDispatcher<A>() {
            public void invoke(A action) {
                dispatcher.invoke(store, action, null);
            }
        });

        for (int i = middlewares.size() - 1; i >= 0; i--) {
            final Middleware<S, A> middleware = middlewares.get(i);
            final NextDispatcher<A> next = dispatchers.get(0);
            dispatchers.add(0, new NextDispatcher<A>() {
                @Override
                public void invoke(A action) {
                    middleware.invoke(store, action, next);
                }
            });
        };
    }

    @Override
    public S getState() {
        return currentState;
    }

    @Override
    public S dispatch(A action) {
        this.dispatchers.get(0).invoke(action);
        return currentState;
    }

    @Override
    public Subscription subscribe(final Subscriber subscriber) {
        this.subscribers.add(subscriber);
        return new Subscription() {
            @Override
            public void unsubscribe() {
                subscribers.remove(subscriber);
            }
        };
    }
}
