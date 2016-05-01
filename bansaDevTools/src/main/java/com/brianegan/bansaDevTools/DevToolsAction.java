package com.brianegan.bansaDevTools;

import com.brianegan.bansa.Action;

public class DevToolsAction implements Action {
    public static final String PERFORM_ACTION = "PERFORM_ACTION";
    public static final String JUMP_TO_STATE = "JUMP_TO_STATE";
    public static final String SWEEP = "SWEEP";
    public static final String RESET = "RESET";
    public static final String COMMIT = "COMMIT";
    public static final String ROLLBACK = "ROLLBACK";
    public static final String TOGGLE_ACTION = "TOGGLE_ACTION";

    private final String type;
    private final Action appAction;
    private final Integer index;
    private final Boolean isEnabled;

    private DevToolsAction(String type,
                           Action appAction,
                           Integer index,
                           Boolean isEnabled) {
        this.type = type;
        this.appAction = appAction;
        this.index = index;
        this.isEnabled = isEnabled;
    }

    public static DevToolsAction createPerformAction(Action appAction) {
        return new DevToolsAction(PERFORM_ACTION, appAction, null, null);
    }

    public static DevToolsAction createJumpToStateAction(int index) {
        return new DevToolsAction(JUMP_TO_STATE, null, index, null);
    }

    public static DevToolsAction createSweepAction() {
        return new DevToolsAction(SWEEP, null, null, null);
    }

    public static DevToolsAction createResetAction() {
        return new DevToolsAction(RESET, null, null, null);
    }

    public static DevToolsAction createCommitAction() {
        return new DevToolsAction(COMMIT, null, null, null);
    }

    public static DevToolsAction createRollbackAction() {
        return new DevToolsAction(ROLLBACK, null, null, null);
    }

    public static DevToolsAction createToggleAction(int position, boolean isEnabled) {
        return new DevToolsAction(TOGGLE_ACTION, null, position, isEnabled);
    }

    public String getType() {
        return type;
    }

    public Action getAppAction() {
        return appAction;
    }

    public int getIndex() {
        return index;
    }

    public boolean isEnabled() {
        return isEnabled;
    }
}
