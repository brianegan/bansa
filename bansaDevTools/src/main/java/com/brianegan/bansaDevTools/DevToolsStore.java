package com.brianegan.bansaDevTools;

import com.brianegan.bansa.Action;
import com.brianegan.bansa.BaseStore;
import com.brianegan.bansa.Middleware;
import com.brianegan.bansa.Reducer;
import com.brianegan.bansa.Store;
import com.brianegan.bansa.Subscriber;
import com.brianegan.bansa.Subscription;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class DevToolsStore<S> implements Store<S> {
    private final BaseStore<DevToolsState<S>> devToolsStore;

    @SafeVarargs
    public DevToolsStore(S initialState, Reducer<S> reducer, final Middleware<S>... middlewares) {
        final DevToolsState<S> devToolsState = new DevToolsState<>(
                Collections.singletonList(initialState),
                Collections.<Action>emptyList(),
                0
        );

        final DevToolsReducer<S> devToolsReducer =
                new DevToolsReducer<>(reducer);

        devToolsStore = new BaseStore<>(devToolsState, devToolsReducer, toDevToolsMiddlewares(middlewares));
        dispatch(DevToolsAction.createInitAction());
    }

    private List<Middleware<DevToolsState<S>>> toDevToolsMiddlewares(Middleware<S>[] middlewares) {
        List<Middleware<DevToolsState<S>>> devToolsMiddlewares = new LinkedList<>();

        for (final Middleware<S> middleware : middlewares) {
            devToolsMiddlewares.add(new DevToolsMiddleware<>(this, middleware));
        }

        return devToolsMiddlewares;
    }

    public DevToolsState<S> getDevToolsState() {
        return devToolsStore.getState();
    }

    @Override
    public S getState() {
        return devToolsStore.getState().getCurrentAppState();
    }

    @Override
    public S dispatch(Action action) {
        if (action instanceof DevToolsAction) {
            devToolsStore.dispatch(action);
        } else {
            devToolsStore.dispatch(DevToolsAction.createPerformAction(action));
        }

        return getState();
    }

    @Override
    public Subscription subscribe(final Subscriber<S> subscriber) {
        return devToolsStore.subscribe(new Subscriber<DevToolsState<S>>() {
            @Override
            public void onStateChange(DevToolsState<S> state) {
                subscriber.onStateChange(getState());
            }
        });
    }
}
