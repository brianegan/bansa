package com.brianegan.bansaDevTools;

import com.brianegan.bansa.Action;
import com.brianegan.bansa.Reducer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DevToolsReducer<S> implements Reducer<DevToolsState<S>> {
    private final Reducer<S> appReducer;

    public DevToolsReducer(Reducer<S> appReducer) {
        this.appReducer = appReducer;
    }

    @Override
    public DevToolsState<S> reduce(DevToolsState<S> state, Action action) {
        if (!(action instanceof DevToolsAction)) {
            throw new IllegalArgumentException("When using the Dev Tools, all actions must be wrapped as a DevToolsAction");
        }

        DevToolsAction devToolsAction = (DevToolsAction) action;

        switch (devToolsAction.getType()) {
            case DevToolsAction.INIT:
                final S initialState = appReducer.reduce(state.getCurrentAppState(), action);

                return new DevToolsState<>(
                        initialState,
                        Collections.<S>singletonList(initialState),
                        Collections.singletonList(action),
                        0,
                        Collections.<Integer>emptySet());

            case DevToolsAction.PERFORM_ACTION:
                final boolean isAddingToEnd = state.getCurrentStateIndex() == state.getComputedStates().size() - 1;
                final List<Action> actions = isAddingToEnd ? state.getStagedActions() : state.getStagedActions().subList(0, state.getCurrentStateIndex());
                final List<S> states = isAddingToEnd ? state.getComputedStates() : state.getComputedStates().subList(0, state.getCurrentStateIndex());
                List<Action> stagedActions = new ArrayList<>(actions);
                List<S> computedStates = new ArrayList<>(states);
                if (!isAddingToEnd) {
                    computedStates.add(state.getCurrentAppState());
                    stagedActions.add(devToolsAction.getAppAction());
                }

                computedStates.add(appReducer.reduce(state.getCurrentAppState(), devToolsAction.getAppAction()));
                stagedActions.add(devToolsAction.getAppAction());

                return new DevToolsState<>(
                        state.getCommittedState(),
                        computedStates,
                        stagedActions,
                        computedStates.size() - 1,
                        Collections.<Integer>emptySet());

            case DevToolsAction.ROLLBACK:
                return new DevToolsState<>(
                        state.getCommittedState(),
                        Collections.<S>singletonList(state.getCommittedState()),
                        Collections.<Action>singletonList(devToolsAction),
                        0,
                        Collections.<Integer>emptySet());

            case DevToolsAction.COMMIT:
                return new DevToolsState<>(
                        state.getCurrentAppState(),
                        Collections.<S>singletonList(state.getCurrentAppState()),
                        Collections.singletonList(action),
                        0,
                        Collections.<Integer>emptySet());

            case DevToolsAction.JUMP_TO_STATE:
                return new DevToolsState<>(
                        state.getCommittedState(),
                        state.getComputedStates(),
                        state.getStagedActions(),
                        devToolsAction.getIndex(),
                        Collections.<Integer>emptySet());

            default:
                return state;
        }
    }
}
