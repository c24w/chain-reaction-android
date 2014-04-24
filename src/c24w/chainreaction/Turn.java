package c24w.chainreaction;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Chris on 19/04/2014.
 */
public class Turn extends Activity {
    private int count;
    private long timeLeft;
    private TextView countText;
    private TextView timerText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.turn);
        init(savedInstanceState);
    }

    private void init(Bundle savedInstanceState) {
        // savedInstanceState is always null on first creation - stub it so defaults are used for getX calls
        savedInstanceState = savedInstanceState == null ? new Bundle() : savedInstanceState;
        count = savedInstanceState.getInt("count", 0);
        timeLeft = savedInstanceState.getLong("timeLeft", 30000);
        countText = (TextView) findViewById(R.id.counter_value);
        timerText = (TextView) findViewById(R.id.timer_value);
        showCount(count);

        new CountDownTimer(timeLeft, 100) {

            @Override
            public void onTick(long remaining) {
                showTimeLeft(timeLeft = remaining);
            }

            @Override
            public void onFinish() {
                showTimeLeft(timeLeft = 0);
            }
        }.start();
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putInt("count", count);
        savedInstanceState.putLong("timeLeft", timeLeft);
    }

    private void restart() {
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        DialogInterface.OnClickListener dialogHandler = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int selection) {
                switch (selection) {
                    case DialogInterface.BUTTON_POSITIVE:
                        goBack();
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        restart();
                        break;
                }
            }
        };

        new AlertDialog.Builder(this)
                .setPositiveButton("Finish turn", dialogHandler)
                .setNegativeButton("Restart turn", dialogHandler)
                .show();
    }

    public void goBack() {
        super.onBackPressed();
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

    private void showTimeLeft(long timeLeft) {
        timerText.setText(timeLeft == 0
                ? getString(R.string.stop)
                : formatForDisplay(timeLeft));
    }

    private String formatForDisplay(long millis) {
        // Seconds to one DP
        double value = ((double)Math.round(millis / 100)) / 10;
        return String.valueOf(value);
    }
}