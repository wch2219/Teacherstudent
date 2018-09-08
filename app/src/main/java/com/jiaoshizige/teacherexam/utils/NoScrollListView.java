package com.jiaoshizige.teacherexam.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

import com.jiaoshizige.teacherexam.adapter.MyQuestionBankCollectionAdapter;

/**
 * Created by Administrator on 2017/8/8.
 */

public class NoScrollListView extends ListView {

    /***
     * 自定义ListView子类，继承ListView
     * @author Administrator
     *
     */
        public NoScrollListView(Context context) {
            super(context);
        }

        public NoScrollListView(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        public NoScrollListView(Context context, AttributeSet attrs, int defStyle) {
            super(context, attrs, defStyle);
        }

        @Override
        public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
            super.onMeasure(widthMeasureSpec, expandSpec);
        }


}
