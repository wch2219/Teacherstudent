package com.jiaoshizige.teacherexam.widgets;


import android.content.Context;
import android.database.ContentObserver;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.Settings;
import android.util.AttributeSet;
import android.widget.TextClock;

import java.util.Calendar;

/**
 * Created by Administrator on 2017/8/23 0023.
 */

public class CustomDigitalClock extends TextClock {

    Calendar mCalendar;
    private final static String m12 = "h:mm aa";
    private final static String m24 = "k:mm";
    private FormatChangeObserver mFormatChangeObserver;

    private Runnable mTicker;
    private Handler mHandler;
    private long endTime;
    private ClockListener mClockListener;
    private boolean mTickerStopped = false;

    @SuppressWarnings("unused")
    private String mFormat;

    public CustomDigitalClock(Context context) {
        super(context);
        initClock(context);
    }

    public CustomDigitalClock(Context context, AttributeSet attrs) {
        super(context, attrs);
        initClock(context);
    }

    private void initClock(Context context) {

        if (mCalendar == null) {
            mCalendar = Calendar.getInstance();
        }

        mFormatChangeObserver = new FormatChangeObserver();
        getContext().getContentResolver().registerContentObserver(Settings.System.CONTENT_URI, true, mFormatChangeObserver);

        setFormat();
    }

    @Override
    protected void onAttachedToWindow() {
        mTickerStopped = false;
        super.onAttachedToWindow();
        mHandler = new Handler();

        /**
         * requests a tick on the next hard-second boundary
         */
        mTicker = new Runnable() {
            public void run() {
                if (mTickerStopped)
                    return;
                long currentTime = System.currentTimeMillis();
                long distanceTime = endTime - currentTime;
                distanceTime /= 1000;
                if (distanceTime == 0) {
                    setText("00:00:00");
                    onDetachedFromWindow();
                    if (mClockListener != null) {
                        mClockListener.timeEnd();
                    }
                } else if (distanceTime < 0) {
                    setText("00:00:00");
                } else {
                    setText(dealTime(distanceTime));
                }
//                invalidate();
                long now = SystemClock.uptimeMillis();
                long next = now + (1000 - now % 1000);
                mHandler.postAtTime(mTicker, next);
            }
        };
        mTicker.run();
    }

    /**
     * deal time string
     *
     * @param time
     * @return
     */
    public static String dealTime(long time) {
        StringBuffer returnString = new StringBuffer();
//        long day = time / (24 * 60 * 60);
        long hours = (time % (24 * 60 * 60)) / (60 * 60);
        long minutes = ((time % (24 * 60 * 60)) % (60 * 60)) / 60;
        long second = ((time % (24 * 60 * 60)) % (60 * 60)) % 60;
//        String dayStr = String.valueOf(day);
        String hoursStr = timeStrFormat(String.valueOf(hours));
        String minutesStr = timeStrFormat(String.valueOf(minutes));
        String secondStr = timeStrFormat(String.valueOf(second));
//        returnString.append("剩余").append(dayStr).append("天").append(hoursStr).append("小时").append(minutesStr).append("分钟").append(secondStr).append("秒");
        returnString.append(hoursStr).append(":").append(minutesStr).append(":").append(secondStr);
//        returnString.append(dayStr).append(":").append(hoursStr).append(":").append(minutesStr).append(":").append(secondStr);
        return returnString.toString();
    }

    /**
     * format time
     *
     * @param timeStr
     * @return
     */
    private static String timeStrFormat(String timeStr) {
        switch (timeStr.length()) {
            case 1:
                timeStr = "0" + timeStr;
                break;
        }
        return timeStr;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mTickerStopped = true;
    }

    /**
     * Clock end time from now on.
     *
     * @param endTime
     */
    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    /**
     * Pulls 12/24 mode from system settings
     */
    private boolean get24HourMode() {
        return android.text.format.DateFormat.is24HourFormat(getContext());
    }

    private void setFormat() {
        if (get24HourMode()) {
            mFormat = m24;
        } else {
            mFormat = m12;
        }
    }

    private class FormatChangeObserver extends ContentObserver {
        public FormatChangeObserver() {
            super(new Handler());
        }

        @Override
        public void onChange(boolean selfChange) {
            setFormat();
        }
    }

    public void setClockListener(ClockListener clockListener) {
        this.mClockListener = clockListener;
    }

    public interface ClockListener {
        void timeEnd();

        void remainFiveMinutes();
    }

}
