package com.brianegan.bansaDevTools;

import com.brianegan.bansa.Action;

import java.util.List;

public class DevToolsState<S> {
    private final List<S> computedStates; // List of computed states after committed.
    private final List<Action> stagedActions; // List of all currently staged actions.
    private final Integer currentPosition; // Current state index in the computedStates List.

    public DevToolsState(List<S> computedStates,
                         List<Action> stagedActions,
                         Integer currentPosition) {
        this.computedStates = computedStates;
        this.stagedActions = stagedActions;
        this.currentPosition = currentPosition;
    }

    public S getCommittedState() {
        return getComputedStates().get(0);
    }

    public List<S> getComputedStates() {
        return computedStates;
    }

    public List<Action> getStagedActions() {
        return stagedActions;
    }

    public Integer getCurrentPosition() {
        return currentPosition;
    }

    public S getCurrentAppState() {
        return getComputedStates().get(getCurrentPosition());
    }

    public Action getCurrentAction() {
        return getStagedActions().get(getCurrentPosition());
    }
}
