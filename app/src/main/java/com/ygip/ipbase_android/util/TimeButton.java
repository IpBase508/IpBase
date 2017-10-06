package com.ygip.ipbase_android.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by lockyluo on 2017/10/5.
 * 倒计时button
 */

    public class TimeButton extends android.support.v7.widget.AppCompatButton implements View.OnClickListener {
        private long length = 60 * 1000;// 倒计时长度,这里给了默认60秒
        private String textafter = "秒后重新获取";
        private String textbefore = "点击获取验证码";

        private OnClickListener mOnclickListener;
        private Timer t;
        private TimerTask tt;
        private long time;
        private Context mContext;

        public TimeButton(Context context) {
            super(context);
            setOnClickListener(this);

        }

        public TimeButton(Context context, AttributeSet attrs) {
            super(context, attrs);
            mContext = context;
            setOnClickListener(this);
        }

        public void init(long length,OnClickListener listener){
            setLength(length);
            setOnClickListener(listener);
        }

        public TimeButton getInstance(){
            return this;
        }

        @SuppressLint("HandlerLeak")
        Handler han = new Handler() {
            public void handleMessage(android.os.Message msg) {
                TimeButton.this.setText(time / 1000 + textafter);
                time -= 1000;
                if (time < 0) {
                    TimeButton.this.setEnabled(true);
                    TimeButton.this.setText(textbefore);
                    clearTimer();
                }
            }
        };

        private void initTimer() {
            time = length;
            t = new Timer();
            tt = new TimerTask() {
                @Override
                public void run() {
                    Log.e("timeButton", time / 1000 + "");
                    han.sendEmptyMessage(0x01);
                }
            };
        }

        private void clearTimer() {
            Log.e("timeButton", "计时结束");
            if (tt != null) {
                tt.cancel();
                tt = null;
            }
            if (t != null)
                t.cancel();
            t = null;
        }

        @Override
        public void setOnClickListener(OnClickListener l) {
            if (l instanceof TimeButton) {
                super.setOnClickListener(l);
            } else
                this.mOnclickListener = l;
        }

        @Override
        public void onClick(View v) {
            if (mOnclickListener != null)
                mOnclickListener.onClick(v);
            initTimer();
            getInstance().setText(time / 1000 + textafter);
            getInstance().setEnabled(false);
            t.schedule(tt, 0, 1000);
        }


        /**
         * 设置计时时候显示的文本
         */
        public TimeButton setTextCounting(String text1) {
            this.textafter = text1;
            return this;
        }

        /**
         * 设置点击之前的文本
         */
        public TimeButton setTextDefault(String text0) {
            this.textbefore = text0;
            this.setText(textbefore);
            return this;
        }

        /**
         * 设置到计时长度
         *
         * @param length 时间 默认毫秒
         * @return
         */
        public TimeButton setLength(long length) {
            this.length = length;
            return this;
        }
    }

