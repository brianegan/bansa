package com.brianegan.bansaDevTools;

import com.brianegan.bansa.Action;
import com.brianegan.bansa.Reducer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DevToolsReducer<S> implements Reducer<DevToolsState<S>> {
    private final S initialState;
    private final Reducer<S> appReducer;

    public DevToolsReducer(S initialState, Reducer<S> appReducer) {
        this.initialState = initialState;
        this.appReducer = appReducer;
    }

    @Override
    public DevToolsState<S> reduce(DevToolsState<S> state, Action action) {
        if (!(action instanceof DevToolsAction)) {
            throw new IllegalArgumentException("When using the Dev Tools, all actions must be wrapped as a DevToolsAction");
        }

        DevToolsAction devToolsAction = (DevToolsAction) action;
        S committedState = state.getCommittedState();

        switch (devToolsAction.getType()) {
            case DevToolsAction.PERFORM_ACTION:
                List<Action> stagedActions = new ArrayList<>(state.getStagedActions());
                List<S> computedStates = new ArrayList<>(state.getComputedStates());
                computedStates.add(appReducer.reduce(state.getCurrentAppState(), devToolsAction.getAppAction()));
                stagedActions.add(devToolsAction.getAppAction());

                return new DevToolsState<>(
                        initialState,
                        computedStates,
                        stagedActions,
                        state.getCurrentStateIndex() + 1,
                        Collections.<Integer>emptySet());

            case DevToolsAction.ROLLBACK:
                return new DevToolsState<>(
                        initialState,
                        Collections.<S>singletonList(initialState),
                        Collections.<Action>emptyList(),
                        0,
                        Collections.<Integer>emptySet());

            case DevToolsAction.JUMP_TO_STATE:
                return new DevToolsState<>(
                        initialState,
                        state.getComputedStates(),
                        state.getStagedActions(),
                        devToolsAction.getIndex(),
                        Collections.<Integer>emptySet());

            default:
                return state;
        }
    }
}
