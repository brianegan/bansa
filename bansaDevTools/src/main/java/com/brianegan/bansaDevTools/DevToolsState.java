package com.brianegan.bansaDevTools;

import com.brianegan.bansa.Action;

import java.util.List;
import java.util.Set;

public class DevToolsState<S> {
    private final S committedState; // The last known committed state.
    private final List<S> computedStates; // List of computed states after committed.
    private final List<Action> stagedActions; // List of all currently staged actions.
    private final Integer currentStateIndex; // Current state index in the computedStates List.
    private final Set<Integer> skippedActionIndexes; // Set of action indexes that should be skipped.

    public DevToolsState(S committedState,
                         List<S> computedStates,
                         List<Action> stagedActions,
                         Integer currentStateIndex,
                         Set<Integer> skippedActionIndexes) {
        this.committedState = committedState;
        this.computedStates = computedStates;
        this.stagedActions = stagedActions;
        this.currentStateIndex = currentStateIndex;
        this.skippedActionIndexes = skippedActionIndexes;
    }

    public S getCommittedState() {
        return committedState;
    }

    public List<S> getComputedStates() {
        return computedStates;
    }

    public List<Action> getStagedActions() {
        return stagedActions;
    }

    public Integer getCurrentStateIndex() {
        return currentStateIndex;
    }

    public Set<Integer> getSkippedActionIndexes() {
        return skippedActionIndexes;
    }

    public S getCurrentAppState() {
        return getComputedStates().get(getCurrentStateIndex());
    }
}
