package c24w.chainreaction;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Chris on 19/04/2014.
 */
public class Turn extends Activity {

    private int count;
    private TextView countText;
    private TextView timerText;
    private TextView incBtn;
    private TextView decBtn;
    private PauseableTimer timer;
    private boolean timerRunning = true;
    private boolean counterEnabled = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.turn);

        countText = (TextView) findViewById(R.id.counter_value);
        timerText = (TextView) findViewById(R.id.timer_value);
        incBtn = (TextView) findViewById(R.id.counter_inc);
        decBtn = (TextView) findViewById(R.id.counter_dec);
        initViews(savedInstanceState);
    }

    private void initViews(Bundle state) {
        state = state == null ? new Bundle() : state;

        timer = createTimer(state.getLong("remainingTime", 30000));
        if (state.getBoolean("timerRunning", timerRunning)) {
            timer.start();
        }
        counterEnabled = state.getBoolean("counterEnabled", counterEnabled);
        enabled(counterEnabled, incBtn, decBtn);
        count = state.getInt("count", 0);
        showCount(count);
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putInt("count", count);
        savedInstanceState.putLong("remainingTime", timer.timeLeft());
        savedInstanceState.putBoolean("timerRunning", timerRunning);
        savedInstanceState.putBoolean("counterEnabled", counterEnabled);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Storing this state in a field sucks, but pause fires before
        // saveInstance so need to capture it before pausing the timer
        timerRunning = timer.isRunning();
        timer.pause();
    }

    private PauseableTimer createTimer(long duration) {
        timerText.setText(formatForDisplay(duration));
        return new PauseableTimer(duration, 100, new PauseableTimer.Callback() {

            @Override
            public void onStart() {
                counterEnabled = true;
                enabled(counterEnabled, incBtn, decBtn);
            }

            @Override
            public void onTick(long latest) {
                timerText.setText(formatForDisplay(latest));
            }

            @Override
            public void onFinish() {
                enabled(false, timerText, incBtn, decBtn);
                timerText.setText(getString(R.string.stop));
            }
        });
    }

    private void enabled(boolean enabled, View... views) {
        for (View view : views) {
            view.setEnabled(enabled);
        }
    }

    @Override
    public void onBackPressed() {
    }

    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        DialogInterface.OnClickListener dialogHandler = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int selection) {
                switch (selection) {
                    case DialogInterface.BUTTON_POSITIVE:
                        finish();
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        finish();
                        break;
                }
            }
        };

        new AlertDialog.Builder(this)
                .setPositiveButton("Complete turn", dialogHandler)
                .setNegativeButton("Abandon turn", dialogHandler)
                .show();

        return super.onMenuOpened(featureId, menu);
    }


    public void increment(View view) {
        showCount(++count);
    }

    public void decrement(View view) {
        showCount(count == 0 ? count : --count);
    }

    private void showCount(int count) {
        countText.setText(String.valueOf(count));
    }

    public void toggleTimer(View view) {
        if (timer.isRunning()) {
            timer.pause();
        } else {
            timer.resume();
        }
    }

    private String formatForDisplay(long millis) {
        // Seconds to one DP
        double value = ((double) Math.round(millis / 100)) / 10;
        return String.valueOf(value);
    }
}