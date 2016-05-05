package com.brianegan.bansaDevTools;

import com.brianegan.bansa.Action;

public class DevToolsAction implements Action {
    public static final String PERFORM_ACTION = "PERFORM_ACTION";
    public static final String JUMP_TO_STATE = "JUMP_TO_STATE";
    public static final String SAVE = "SAVE";
    public static final String RESET = "RESET";
    public static final String RECOMPUTE = "RECOMPUTE";
    public static final String TOGGLE_ACTION = "TOGGLE_ACTION";
    static final String INIT = "INIT";
    private final String type;
    private final Action appAction;
    private final Integer position;
    private final Boolean isEnabled;

    private DevToolsAction(String type,
                           Action appAction,
                           Integer position,
                           Boolean isEnabled) {
        this.type = type;
        this.appAction = appAction;
        this.position = position;
        this.isEnabled = isEnabled;
    }

    public static DevToolsAction createPerformAction(Action appAction) {
        return new DevToolsAction(PERFORM_ACTION, appAction, null, null);
    }

    public static DevToolsAction createJumpToStateAction(int index) {
        return new DevToolsAction(JUMP_TO_STATE, null, index, null);
    }

    public static DevToolsAction createSaveAction() {
        return new DevToolsAction(SAVE, null, null, null);
    }

    public static DevToolsAction createResetAction() {
        return new DevToolsAction(RESET, null, null, null);
    }

    public static DevToolsAction createRecomputeAction() {
        return new DevToolsAction(RECOMPUTE, null, null, null);
    }

    static DevToolsAction createInitAction() {
        return new DevToolsAction(INIT, null, null, null);
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

    public int getPosition() {
        return position;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DevToolsAction that = (DevToolsAction) o;

        return type.equals(that.type)
                && (appAction != null ? appAction.equals(that.appAction)
                : that.appAction == null && (position != null
                ? position.equals(that.position)
                : that.position == null && (isEnabled != null
                ? isEnabled.equals(that.isEnabled)
                : that.isEnabled == null)));
    }

    @Override
    public int hashCode() {
        int result = type.hashCode();
        result = 31 * result + (appAction != null ? appAction.hashCode() : 0);
        result = 31 * result + (position != null ? position.hashCode() : 0);
        result = 31 * result + (isEnabled != null ? isEnabled.hashCode() : 0);
        return result;
    }
}
