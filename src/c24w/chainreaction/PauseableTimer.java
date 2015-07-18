package c24w.chainreaction;

import android.os.CountDownTimer;
import android.util.Log;

/**
 * Created by Chris on 24/04/2014.
 */
public class PauseableTimer {
    private final String TAG = PauseableTimer.class.getName();
    private long remainingTime;
    private final long tickInterval;
    private final Callback callback;
    private CountDownTimer timer;

    public interface Callback {
        void onTick(long timeLeft);
        void onFinish();
    }

    public PauseableTimer(long duration, final long tickInterval, final Callback callback) {
        this.remainingTime = duration;
        this.tickInterval = tickInterval;
        this.callback = callback;
        start();
    }

    public void start() {
        timer = new CountDownTimer(remainingTime, tickInterval) {
            @Override
            public void onTick(long timeLeft) {
                remainingTime = timeLeft;
                callback.onTick(timeLeft);
            }

            @Override
            public void onFinish() {
                callback.onFinish();
            }
        }.start();
    }

    public boolean isRunning() {
        return timer != null;
    }

    public void pause() {
        if (isRunning()) timer.cancel();
        else Log.w(TAG, "Cannot pause timer which hasn't been started");
    }

    public void resume() {
        if (isRunning()) Log.w(TAG, "Cannot resume timer which hasn't been paused");
        else start();
    }
}
