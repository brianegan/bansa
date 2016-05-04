package com.brianegan.bansaDevTools;

import com.brianegan.bansa.Action;
import com.brianegan.bansa.Middleware;
import com.brianegan.bansa.NextDispatcher;
import com.brianegan.bansa.Store;

public class DevToolsMiddleware<S> implements Middleware<DevToolsState<S>> {
    private final Store<S> store;
    private final Middleware<S> appMiddleware;

    public DevToolsMiddleware(Store<S> store, Middleware<S> appMiddleware) {
        this.store = store;
        this.appMiddleware = appMiddleware;
    }

    @Override
    public void dispatch(Store<DevToolsState<S>> devToolsStore, Action action, final NextDispatcher next) {
        // Actions are wrapped by the dispatcher as a DevToolsAction. However, the middleware passed
        // into the constructor act on original app actions. Therefore, we must lift the app action
        // out of the DevToolsAction container.
        Action actionToDispatch = action;

        if (action instanceof DevToolsAction && ((DevToolsAction) action).getAppAction() != null) {
            actionToDispatch = ((DevToolsAction) action).getAppAction();
        }

        appMiddleware.dispatch(store, actionToDispatch, new NextDispatcher() {
            @Override
            public void dispatch(Action action) {
                // Since next can be called within any Middleware, we need to wrap the actions
                // as DevToolsActions, in the same way as we wrap the initial dispatch call.
                if (action instanceof DevToolsAction) {
                    next.dispatch(action);
                } else {
                    next.dispatch(DevToolsAction.createPerformAction(action));
                }
            }
        });
    }
}
