package c24w.chainreaction;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Chris on 19/04/2014.
 */
public class Round extends Activity {
    private int count;
    private TextView countText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.round);
        init();
    }

    private void init() {
        count = 0;
        countText = (TextView) findViewById(R.id.counter_value);
        setCount(count);
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putInt("count", count);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        count = savedInstanceState.getInt("count");
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
                .setPositiveButton("End round", promptHandler)
                .setNegativeButton("Cancel round", promptHandler)
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