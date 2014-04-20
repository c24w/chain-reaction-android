package c24w.chainreaction;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by Chris on 19/04/2014.
 */
public class Counter extends Activity {
    int count;
    TextView counter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.counter);

        SharedPreferences prefs = getSharedPreferences("Preferences", MODE_PRIVATE);
        count = 0;//prefs.getInt("counterValue", R.integer.counter_value);
        counter = (TextView) findViewById(R.id.counter_value);
        setCount(count);
    }

    public void increment(View view) {
        setCount(++count);
    }

    public void decrement(View view) {
        setCount(count == 0 ? count : --count);
    }

    public void setCount(int count) {
        counter.setText(String.valueOf(count));
    }
}