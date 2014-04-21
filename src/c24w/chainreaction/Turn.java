package c24w.chainreaction;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

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
        setCount(count);

        new CountDownTimer(timeLeft, 100) {

            @Override
            public void onTick(long remaining) {
                timeLeft = remaining;
                timerText.setText(String.valueOf(remaining));
            }

            @Override
            public void onFinish() {
            }
        }.start();
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putInt("count", count);
        savedInstanceState.putLong("timeLeft", timeLeft);
    }

    @Override
    public void onBackPressed() {
        DialogInterface.OnClickListener promptHandler = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        goBack();
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        goBack();
                        break;
                }
            }
        };

        new AlertDialog.Builder(this)
                .setPositiveButton("End turn", promptHandler)
                .setNegativeButton("Cancel turn", promptHandler)
                .show();
    }

    public void goBack() {
        super.onBackPressed();
    }

    public void increment(View view) {
        setCount(++count);
    }

    public void decrement(View view) {
        setCount(count == 0 ? count : --count);
    }

    public void setCount(int count) {
        countText.setText(String.valueOf(count));
    }
}

// Game setup
//      -> turn || mid-turn: menu -> finish turn
//            -> time up: 'x scored n', 'start y's turn'
