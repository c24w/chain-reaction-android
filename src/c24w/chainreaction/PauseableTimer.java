package c24w.chainreaction;

import android.os.CountDownTimer;

/**
 * Created by Chris on 24/04/2014.
 */
public class PauseableTimer {
    private Callback callback;
    private CountDownTimer timer;
    private long tickInterval;
    private long remainingTime;

    public interface Callback {
        public void onTick(long timeLeft);

        public void onPause();

        public void onResume();

        public void onFinish();
    }

    public PauseableTimer(final long duration, final long tickInterval, Callback callback) {
        this.remainingTime = duration;
        this.tickInterval = tickInterval;
        this.callback = callback;
    }

    public PauseableTimer start() {
        startTimer();
        return this;
    }

    private void startTimer() {
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

    public long timeLeft() {
        return remainingTime;
    }

    public void pause() {
        if (timer != null) {
            timer.cancel();
            timer = null;
            callback.onPause();
        }
    }

    public void resume() {
        if (timer == null) {
            startTimer();
            callback.onResume();
        }
    }

    public boolean isRunning() {
        return timer != null;
    }
}
