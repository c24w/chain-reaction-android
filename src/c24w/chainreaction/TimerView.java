package c24w.chainreaction;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;

/**
 * Created by Chris on 10/05/2014.
 */
public class TimerView extends Button {
    private final String finishText;
    private long remainingTime;
    private PauseableTimer timer;

    public TimerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.TimerView, 0, 0);
        try {
            this.finishText = a.getString(R.styleable.TimerView_finishText);
        } finally {
            a.recycle();
        }
    }

    private void init(long duration) {
        remainingTime = duration;
        setTextTime(duration);

        timer = new PauseableTimer(duration, 100, new PauseableTimer.Callback() {

            @Override
            public void onTick(long latest) {
                remainingTime = latest;
                setTextTime(latest);
            }

            @Override
            public void onFinish() {
                setOnClickListener(null);
                setText(TimerView.this.finishText);
            }
        });

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (timer.isRunning()) {
                    timer.pause();
                } else {
                    timer.resume();
                }
            }
        });
    }
    private void setTextTime(long millis) {
        setText(formatForDisplay(millis));
    }

    private String formatForDisplay(long millis) {
        double value = ((double) Math.round(millis / 100)) / 10; // Seconds to 1 DP
        return String.valueOf(value);
    }

    public void saveState(Bundle savedInstanceState) {
        savedInstanceState.putLong("remainingTime", remainingTime);
        savedInstanceState.putBoolean("timerRunning", timer.isRunning());
    }

    public void setState(Bundle savedInstanceState) {
        // TODO: Move defaults to config
        init(savedInstanceState.getLong("remainingTime", 30000));

        if (savedInstanceState.getBoolean("timerRunning", true)) {
            timer.start();
        }
    }

    public void pause() {
        timer.pause();
    }

    public void resume() {
        timer.resume();
    }
}
