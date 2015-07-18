package c24w.chainreaction;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
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
    private TimerView timer;
    private TextView incBtn;
    private TextView decBtn;
    private boolean timerRunning = true;
    private boolean counterEnabled = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.turn);

        countText = (TextView) findViewById(R.id.counter_value);
        timer = (TimerView) findViewById(R.id.timer);
        incBtn = (TextView) findViewById(R.id.counter_inc);
        decBtn = (TextView) findViewById(R.id.counter_dec);
        initViews(savedInstanceState);
    }

    private void initViews(Bundle state) {
        state = state == null ? new Bundle() : state;

        timer.setState(state);

        if (state.getBoolean("timerRunning", timerRunning)) { // TODO: Moved this to setState, so remove from here
            counterEnabled = true;
            enabled(counterEnabled, incBtn, decBtn);
        }
        // TODO: Create CounterView and move this inside to setState method, same as TimerView?
        counterEnabled = state.getBoolean("counterEnabled", counterEnabled);
        enabled(counterEnabled, incBtn, decBtn);
        count = state.getInt("count", 0);
        showCount(count);
        // Fuck this messy shit, use Fragments or something
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putInt("count", count);
        savedInstanceState.putBoolean("counterEnabled", counterEnabled);
        timer.saveState(savedInstanceState);
    }

    @Override
    protected void onPause() {
        super.onPause();
        timer.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        timer.resume();
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
}