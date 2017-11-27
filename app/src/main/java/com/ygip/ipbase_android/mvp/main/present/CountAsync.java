package com.ygip.ipbase_android.mvp.main.present;

import android.content.Context;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.widget.ProgressBar;

import com.orhanobut.logger.Logger;

/**
 * Created by LockyLuo on 2017/10/30.
 */

public class CountAsync<T> extends AsyncTask<OnLoadListener<T>, Long, Boolean> {
    public Context context;
    private Long countTime;
    private Long remindTime;
    private Boolean isTimeUp = false;
    private CountDownTimer countDownTimer;
    private ProgressBar progressBar;
    private OnLoadListener<T> onLoadListener;
    private int progress;

    public void initTask(Context context, Long countTimeMill, ProgressBar progressBar) {
        this.context = context;
        this.countTime = countTimeMill;
        this.progressBar = progressBar;
        reset();
        countDownTimer = new CountDownTimer(countTime, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                remindTime = millisUntilFinished;
                publishProgress(countTimeMill-remindTime);
            }

            @Override
            public void onFinish() {

            }
        };
    }

    private void reset() {
        remindTime = 0L;
        isTimeUp = false;
        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
        }
    }

    @Override
    protected void onPreExecute() {

        super.onPreExecute();
    }

    @Override
    protected Boolean doInBackground(OnLoadListener<T>... params) {
        countDownTimer.start();
        onLoadListener=params[0];

        if(onLoadListener!=null){
            new Handler(Looper.getMainLooper()).post(()->{
                onLoadListener.onStart();
            });

        }else {
            Logger.e("onLoadListener is null");
        }
        return isTimeUp;
    }

    @Override
    protected void onProgressUpdate(Long... values) {

        super.onProgressUpdate(values);
    }

    @Override
    protected void onCancelled() {
        changeProgressBar(0, 100);
        reset();
        super.onCancelled();
    }

    private void changeProgressBar(long progress, long max) {
        if (progressBar != null) {
            this.progress = (int) (progress / max) * 100;
            progressBar.setMax(this.progress);
            progressBar.setProgress(100);
        } else {
            Logger.e("progressBar is null");
        }
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        reset();
        if(onLoadListener!=null){
            onLoadListener.onFinish(null,null);
        }else {
            Logger.e("onLoadListener is null");
        }
        super.onPostExecute(aBoolean);
    }
}
