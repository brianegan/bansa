package com.brianegan.bansaDevToolsUi;

import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.brianegan.bansa.Store;
import com.brianegan.bansa.Subscriber;
import com.brianegan.bansa.Subscription;
import com.brianegan.bansaDevTools.DevToolsAction;
import com.brianegan.bansaDevTools.DevToolsStore;

public class BansaDevToolsPresenter<S> {
    private final Store<S> devToolsStore;
    private TextView action;
    private SeekBar seekBar;
    private Subscription subscription;
    private boolean jumpActionFired = false;
    private boolean isBound;
    private final SeekBar.OnSeekBarChangeListener onSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if (isBound) {
                jumpActionFired = true;
                devToolsStore.dispatch(DevToolsAction.createJumpToStateAction(progress));
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    };
    private View.OnTouchListener onSeekTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            seekBar.getParent().requestDisallowInterceptTouchEvent(true);
            return false;
        }
    };
    private View.OnClickListener onSaveClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            devToolsStore.dispatch(DevToolsAction.createSaveAction());
        }
    };
    private View.OnClickListener onResetClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            devToolsStore.dispatch(DevToolsAction.createResetAction());
        }
    };

    public BansaDevToolsPresenter(Store<S> devToolsStore) {
        this.devToolsStore = devToolsStore;
    }

    public void bind(final View view) {
        final DevToolsStore devToolsStore = (DevToolsStore) this.devToolsStore;

        seekBar = (SeekBar) view.findViewById(R.id.time_travel_seek_bar);
        Button save = (Button) view.findViewById(R.id.time_travel_save);
        Button reset = (Button) view.findViewById(R.id.time_travel_reset);
        action = (TextView) view.findViewById(R.id.time_travel_action);

        seekBar.setOnSeekBarChangeListener(onSeekBarChangeListener);
        seekBar.setMax(devToolsStore.getDevToolsState().getComputedStates().size() - 1);
        seekBar.setProgress(devToolsStore.getDevToolsState().getComputedStates().size() - 1);

        save.setOnClickListener(onSaveClickListener);
        reset.setOnClickListener(onResetClickListener);
        seekBar.setOnTouchListener(onSeekTouchListener);

        action.setText(devToolsStore.getDevToolsState().getCurrentAction().toString());
        subscription = this.devToolsStore.subscribe(new Subscriber<S>() {
            @Override
            public void onStateChange(S state) {
                Handler mainHandler = new Handler(view.getContext().getMainLooper());

                Runnable myRunnable = new Runnable() {
                    @Override
                    public void run() {
                        action.setText(devToolsStore.getDevToolsState().getCurrentAction().toString());

                        if (jumpActionFired) {
                            jumpActionFired = false;
                        } else {
                            seekBar.setMax(devToolsStore.getDevToolsState().getComputedStates().size() - 1);
                            seekBar.setProgress(devToolsStore.getDevToolsState().getComputedStates().size() - 1);
                        }
                    }
                };

                mainHandler.post(myRunnable);
            }
        });
        isBound = true;
    }

    public void unbind() {
        if (isBound) {
            subscription.unsubscribe();
            seekBar.setOnTouchListener(null);
            isBound = false;
        }
    }
}
