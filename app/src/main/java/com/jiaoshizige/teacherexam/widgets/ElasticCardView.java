package com.jiaoshizige.teacherexam.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;

import com.jiaoshizige.teacherexam.R;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/7/23.
 */

public class ElasticCardView extends CardView {

    private final float RATIO;

    public ElasticCardView(Context context) {
        this(context, null);
    }

    public ElasticCardView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ElasticCardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.ElasticCardView);
        RATIO = array.getFloat(R.styleable.ElasticCardView_ratio, 0.1f);
        array.recycle();

        setPreventCornerOverlap(true);
        setUseCompatPadding(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        if (RATIO > 0) {
            int newHeightMeasureSpec = MeasureSpec.makeMeasureSpec((int) (MeasureSpec.getSize(widthMeasureSpec) * RATIO), MeasureSpec.EXACTLY);
            super.onMeasure(widthMeasureSpec, newHeightMeasureSpec);
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }
}
