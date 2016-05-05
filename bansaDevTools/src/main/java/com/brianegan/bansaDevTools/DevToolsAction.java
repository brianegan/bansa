package com.brianegan.bansaDevTools;

import com.brianegan.bansa.Action;

final public class DevToolsAction implements Action {
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

    private DevToolsAction(String type,
                           Action appAction,
                           Integer position) {
        this.type = type;
        this.appAction = appAction;
        this.position = position;
    }

    public static DevToolsAction createPerformAction(Action appAction) {
        return new DevToolsAction(PERFORM_ACTION, appAction, null);
    }

    public static DevToolsAction createJumpToStateAction(int index) {
        return new DevToolsAction(JUMP_TO_STATE, null, index);
    }

    public static DevToolsAction createSaveAction() {
        return new DevToolsAction(SAVE, null, null);
    }

    public static DevToolsAction createResetAction() {
        return new DevToolsAction(RESET, null, null);
    }

    public static DevToolsAction createRecomputeAction() {
        return new DevToolsAction(RECOMPUTE, null, null);
    }

    static DevToolsAction createInitAction() {
        return new DevToolsAction(INIT, null, null);
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DevToolsAction that = (DevToolsAction) o;

        return type != null
                ? type.equals(that.type)
                : that.type == null && (appAction != null
                ? appAction.equals(that.appAction)
                : that.appAction == null && (position != null
                ? position.equals(that.position)
                : that.position == null));

    }

    @Override
    public int hashCode() {
        int result = type != null ? type.hashCode() : 0;
        result = 31 * result + (appAction != null ? appAction.hashCode() : 0);
        result = 31 * result + (position != null ? position.hashCode() : 0);
        return result;
    }
}
