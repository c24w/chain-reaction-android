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
    private PauseableTimer timer;
    private boolean autoStartTimer = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.turn);

        countText = (TextView) findViewById(R.id.counter_value);
        timerText = (TextView) findViewById(R.id.timer_value);
        initViews(savedInstanceState);
    }

    private void initViews(Bundle savedInstanceState) {
        savedInstanceState = savedInstanceState == null ? new Bundle() : savedInstanceState;
        count = savedInstanceState.getInt("count", 0);
        showCount(count);
        timer = createTimer(savedInstanceState.getLong("remainingTime", 30000));
        if (savedInstanceState.getBoolean("timerRunning", autoStartTimer)){
            timer.start();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putInt("count", count);
        savedInstanceState.putLong("remainingTime", timer.timeLeft());
        savedInstanceState.putBoolean("timerRunning", autoStartTimer);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Storing this state in a field sucks, but pause fires before
        // saveInstance so need to capture it before pausing the timer
        autoStartTimer = timer.isRunning();
        timer.pause();
    }

    private PauseableTimer createTimer(long duration) {
        timerText.setText(formatForDisplay(duration));
        return new PauseableTimer(duration, 100, new PauseableTimer.Callback() {
            @Override
            public void onTick(long latest) {
                timerText.setText(formatForDisplay(latest));
            }

            @Override
            public void onPause() {
            }

            @Override
            public void onResume() {
            }

            @Override
            public void onFinish() {
                timerText.setOnClickListener(null);
                timerText.setText(getString(R.string.stop));
            }
        });
    }

    @Override
    public void onBackPressed() {}

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