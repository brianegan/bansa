package com.brianegan.bansaDevToolsUi;

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
    private Button save;
    private Button reset;
    private Subscription subscription;
    private boolean jumpActionFired;

    private final SeekBar.OnSeekBarChangeListener onSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            jumpActionFired = true;
            devToolsStore.dispatch(DevToolsAction.createJumpToStateAction(progress));
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    };

    private View.OnClickListener onSaveClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            devToolsStore.dispatch(DevToolsAction.createCommitAction());
        }
    };

    private View.OnClickListener onResetClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            devToolsStore.dispatch(DevToolsAction.createRollbackAction());
        }
    };

    public BansaDevToolsPresenter(Store<S> devToolsStore) {
        this.devToolsStore = devToolsStore;
    }

    public void bind(View view) {
        final DevToolsStore devToolsStore = (DevToolsStore) this.devToolsStore;

        seekBar = (SeekBar) view.findViewById(R.id.time_travel_seek_bar);
        save = (Button) view.findViewById(R.id.time_travel_save);
        reset = (Button) view.findViewById(R.id.time_travel_reset);
        action = (TextView) view.findViewById(R.id.time_travel_action);

        seekBar.setOnSeekBarChangeListener(onSeekBarChangeListener);
        seekBar.setMax(devToolsStore.getDevToolsState().getComputedStates().size() - 1);
        seekBar.setProgress(devToolsStore.getDevToolsState().getComputedStates().size() - 1);

        save.setOnClickListener(onSaveClickListener);
        reset.setOnClickListener(onResetClickListener);


        action.setText(devToolsStore.getDevToolsState().getCurrentAction().toString());
        subscription = this.devToolsStore.subscribe(new Subscriber<S>() {
            @Override
            public void onStateChange(S state) {
                action.setText(devToolsStore.getDevToolsState().getCurrentAction().toString());

                if (!jumpActionFired) {
                    seekBar.setMax(devToolsStore.getDevToolsState().getComputedStates().size() - 1);
                    seekBar.setProgress(devToolsStore.getDevToolsState().getComputedStates().size() - 1);
                } else {
                    jumpActionFired = false;
                }
            }
        });
    }

    public void unbind() {
        subscription.unsubscribe();
    }
}
