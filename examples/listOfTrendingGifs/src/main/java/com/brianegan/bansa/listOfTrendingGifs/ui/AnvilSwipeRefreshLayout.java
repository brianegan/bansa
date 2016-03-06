package com.brianegan.bansa.listOfTrendingGifs.ui;

import trikita.anvil.Anvil;
import trikita.anvil.DSL;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.View;

public class AnvilSwipeRefreshLayout {
    private AnvilSwipeRefreshLayout() {
    }

    public static void swipeRefreshLayout(Anvil.Renderable r) {
        DSL.v(SwipeRefreshLayout.class, r);
    }

    public static Void onRefresh(OnRefreshListener onRefreshListener) {
        return DSL.attr(OnRefreshFunc.instance, onRefreshListener);
    }

    public static Void refreshing(boolean refreshing) {
        return DSL.attr(SetRefreshingFunc.instance, refreshing);
    }

    public static Void distanceToTriggerSync(int distance) {
        return DSL.attr(SetDistanceToTriggerSyncFunc.instance, distance);
    }

    public static Void colorSchemeColors(int... colors) {
        return DSL.attr(SetColorSchemeFunc.instance, colors);
    }

    public static Void colorSchemeResources(int... colorResIds) {
        return DSL.attr(ColorSchemeResourcesFun.instance, colorResIds);
    }

    public static Void progressBackgroundColorSchemeColor(int color) {
        return DSL.attr(SetProgressBackgroundColorSchemeColorFunc.instance, color);
    }

    public static Void progressBackgroundColorSchemeResource(int colorRes) {
        return DSL.attr(SetProgressBackgroundColorSchemeResourceFunc.instance, colorRes);
    }

    public static Void nestedScrollingEnabled(boolean nestedScrollingEnabled) {
        return DSL.attr(SetNestedScrollingEnabledFunc.instance, nestedScrollingEnabled);
    }

    public static Void setProgressViewEndTarget(boolean scale, int end) {
        return DSL.attr(SetProgressViewEndTargetFunc.instance, new SetProgressViewEndTargetFunc.Param(scale, end));
    }

    public static Void setProgressViewOffset(boolean scale, int start, int end) {
        return DSL.attr(SetProgressViewOffsetFunc.instance, new SetProgressViewOffsetFunc.Param(scale, start, end));
    }

    public static Void size(int size) {
        return DSL.attr(SetSizeFunc.instance, size);
    }

    private static final class OnRefreshFunc implements Anvil.AttrFunc<OnRefreshListener> {
        public static final OnRefreshFunc instance = new OnRefreshFunc();
        public void apply(View view, OnRefreshListener onRefreshListener, OnRefreshListener prevOnRefreshListener) {
            if (view instanceof SwipeRefreshLayout) {
                ((SwipeRefreshLayout) view).setOnRefreshListener(onRefreshListener);
            }
        }
    }

    private static final class SetRefreshingFunc implements Anvil.AttrFunc<Boolean> {
        public static final SetRefreshingFunc instance = new SetRefreshingFunc();
        public void apply(View view, Boolean refreshing, Boolean prevRefreshing) {
            if (view instanceof SwipeRefreshLayout) {
                ((SwipeRefreshLayout) view).setRefreshing(refreshing);
            }
        }
    }

    private static final class ColorSchemeResourcesFun implements Anvil.AttrFunc<int[]> {
        public static final ColorSchemeResourcesFun instance = new ColorSchemeResourcesFun();
        public void apply(View view, int[] colorResIds, int[] prevColorResIds) {
            if (view instanceof SwipeRefreshLayout) {
                ((SwipeRefreshLayout) view).setColorSchemeResources(colorResIds);
            }
        }
    }

    private static final class SetColorSchemeFunc implements Anvil.AttrFunc<int[]> {
        public static final SetColorSchemeFunc instance = new SetColorSchemeFunc();
        public void apply(View view, int[] colors, int[] prevColors) {
            if (view instanceof SwipeRefreshLayout) {
                ((SwipeRefreshLayout) view).setColorSchemeColors(colors);
            }
        }
    }

    private static final class SetDistanceToTriggerSyncFunc implements Anvil.AttrFunc<Integer> {
        public static final SetDistanceToTriggerSyncFunc instance = new SetDistanceToTriggerSyncFunc();
        public void apply(View view, Integer distance, Integer prevDistance) {
            if (view instanceof SwipeRefreshLayout) {
                ((SwipeRefreshLayout) view).setDistanceToTriggerSync(distance);
            }
        }
    }

    private static final class SetProgressBackgroundColorSchemeResourceFunc implements Anvil.AttrFunc<Integer> {
        public static final SetProgressBackgroundColorSchemeResourceFunc instance = new SetProgressBackgroundColorSchemeResourceFunc();
        public void apply(View view, Integer colorRes, Integer prevColorRes) {
            if (view instanceof SwipeRefreshLayout) {
                ((SwipeRefreshLayout) view).setProgressBackgroundColorSchemeResource(colorRes);
            }
        }
    }

    private static final class SetSizeFunc implements Anvil.AttrFunc<Integer> {
        public static final SetSizeFunc instance = new SetSizeFunc();
        public void apply(View view, Integer size, Integer prevSize) {
            if (view instanceof SwipeRefreshLayout) {
                ((SwipeRefreshLayout) view).setSize(size);
            }
        }
    }

    private static final class SetProgressBackgroundColorSchemeColorFunc implements Anvil.AttrFunc<Integer> {
        public static final SetProgressBackgroundColorSchemeColorFunc instance = new SetProgressBackgroundColorSchemeColorFunc();
        public void apply(View view, Integer color, Integer prevColor) {
            if (view instanceof SwipeRefreshLayout) {
                ((SwipeRefreshLayout) view).setProgressBackgroundColorSchemeColor(color);
            }
        }
    }

    private static final class SetNestedScrollingEnabledFunc implements Anvil.AttrFunc<Boolean> {
        public static final SetNestedScrollingEnabledFunc instance = new SetNestedScrollingEnabledFunc();
        public void apply(View view, Boolean nestedScrollingEnabled, Boolean prevNestedScrollingEnabled) {
            if (view instanceof SwipeRefreshLayout) {
                ((SwipeRefreshLayout) view).setNestedScrollingEnabled(nestedScrollingEnabled);
            }
        }
    }

    private static final class SetProgressViewEndTargetFunc implements Anvil.AttrFunc<SetProgressViewEndTargetFunc.Param> {
        public static final SetProgressViewEndTargetFunc instance = new SetProgressViewEndTargetFunc();
        public void apply(View view, SetProgressViewEndTargetFunc.Param param, SetProgressViewEndTargetFunc.Param prevParam) {
            if (view instanceof SwipeRefreshLayout) {
                ((SwipeRefreshLayout) view).setProgressViewEndTarget(param.scale, param.end);
            }
        }

        static final class Param {
            public final boolean scale;
            public final int end;

            public Param(boolean scale, int end) {
                this.scale = scale;
                this.end = end;
            }

            @Override
            public boolean equals(Object o) {
                if (this == o) return true;
                if (o == null || getClass() != o.getClass()) return false;

                Param param = (Param) o;

                return scale == param.scale && end == param.end;
            }

            @Override
            public int hashCode() {
                int result = (scale ? 1 : 0);
                result = 31 * result + end;
                return result;
            }
        }
    }

    private static final class SetProgressViewOffsetFunc implements Anvil.AttrFunc<SetProgressViewOffsetFunc.Param> {
        public static final SetProgressViewOffsetFunc instance = new SetProgressViewOffsetFunc();
        public void apply(View view, SetProgressViewOffsetFunc.Param param, SetProgressViewOffsetFunc.Param prevParam) {
            if (view instanceof SwipeRefreshLayout) {
                ((SwipeRefreshLayout) view).setProgressViewOffset(param.scale, param.start, param.end);
            }
        }

        static final class Param {
            public final boolean scale;
            public final int start;
            public final int end;

            Param(boolean scale, int start, int end) {
                this.scale = scale;
                this.start = start;
                this.end = end;
            }

            @Override
            public boolean equals(Object o) {
                if (this == o) return true;
                if (o == null || getClass() != o.getClass()) return false;

                Param param = (Param) o;

                return scale == param.scale && start == param.start && end == param.end;
            }

            @Override
            public int hashCode() {
                int result = (scale ? 1 : 0);
                result = 31 * result + start;
                result = 31 * result + end;
                return result;
            }
        }
    }
}
