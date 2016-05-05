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
                        Collections.singletonList(initialState),
                        Collections.singletonList(action),
                        0
                );

            case DevToolsAction.PERFORM_ACTION:
                final boolean addToEnd = state.getCurrentPosition() == state.getComputedStates().size() - 1;

                return performAction(
                        state,
                        devToolsAction,
                        addToEnd ? state.getComputedStates() : state.getComputedStates().subList(0, state.getCurrentPosition() + 1),
                        addToEnd ? state.getStagedActions() : state.getStagedActions().subList(0, state.getCurrentPosition() + 1));

            case DevToolsAction.RESET:
                return new DevToolsState<>(
                        Collections.singletonList(state.getCommittedState()),
                        Collections.<Action>singletonList(devToolsAction),
                        0);

            case DevToolsAction.SAVE:
                return new DevToolsState<>(
                        Collections.singletonList(state.getCurrentAppState()),
                        Collections.singletonList(action),
                        0);

            case DevToolsAction.JUMP_TO_STATE:
                return new DevToolsState<>(
                        state.getComputedStates(),
                        state.getStagedActions(),
                        devToolsAction.getPosition());

            case DevToolsAction.RECOMPUTE:
                return new DevToolsState<>(
                        recomputeStates(state.getComputedStates(), state.getStagedActions()),
                        state.getStagedActions(),
                        state.getStagedActions().size() - 1);

            default:
                return state;
        }
    }

    private DevToolsState<S> performAction(DevToolsState<S> state,
                                           DevToolsAction devToolsAction,
                                           List<S> computedStates,
                                           List<Action> stagedActions) {
        List<S> newStates = new ArrayList<>(computedStates);
        List<Action> newActions = new ArrayList<>(stagedActions);

        newStates.add(appReducer.reduce(state.getCurrentAppState(), devToolsAction.getAppAction()));
        newActions.add(devToolsAction.getAppAction());

        return new DevToolsState<>(
                newStates,
                newActions,
                newStates.size() - 1);
    }

    private List<S> recomputeStates(List<S> computedStates, List<Action> stagedActions) {
        List<S> recomputedStates = new ArrayList<>(computedStates.size());
        S currentState = computedStates.get(0);

        for (int i = 0; i < computedStates.size(); i++) {
            Action currentAction = stagedActions.get(i);
            currentState = appReducer.reduce(currentState, currentAction);
            recomputedStates.add(currentState);
        }

        return recomputedStates;
    }
}
