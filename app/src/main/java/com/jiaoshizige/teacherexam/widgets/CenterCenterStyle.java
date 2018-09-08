package com.jiaoshizige.teacherexam.widgets;

import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;

import com.guideview.LayoutStyle;
import com.guideview.ViewInfo;

/**
 * Created by Administrator on 2018/3/8.
 */

public class CenterCenterStyle extends LayoutStyle {
    public CenterCenterStyle(int layoutRes, int offset) {
        super(layoutRes, offset);
    }

    public CenterCenterStyle(View decoView, int offset) {
        super(decoView, offset);
    }

    public CenterCenterStyle(int layoutRes) {
        super(layoutRes);
    }

    public CenterCenterStyle(View decoView) {
        super(decoView);
    }

    @Override
    public void showDecorationOnScreen(final ViewInfo viewInfo, ViewGroup parent) {
        if (decoView==null){
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            decoView = inflater.inflate(layoutRes, parent, false);
        }
        parent.addView(decoView);
        decoView.setVisibility(View.INVISIBLE);
        decoView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT < 16) {
                    decoView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                } else {
                    decoView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
                FrameLayout.LayoutParams child_params = (FrameLayout.LayoutParams) decoView.getLayoutParams();
                child_params.leftMargin = viewInfo.offsetX + (viewInfo.width-decoView.getWidth())/2+offset;
                Log.e("TAG","viewInfo.offsetX"+viewInfo.offsetX+"decoViewgetWidth"+decoView.getWidth()+"decoView.getHeight()"+decoView.getHeight()+"viewInfo.offsetY"+viewInfo.offsetY);
                if(offset == 10){
                    child_params.topMargin = viewInfo.offsetY - viewInfo.height*2;
                }else{
                    child_params.topMargin = viewInfo.offsetY/2 + offset /2;
                }

                decoView.requestLayout();
                //多一次布局监听，防止view闪烁
                decoView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        if (Build.VERSION.SDK_INT < 16) {
                            decoView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                        } else {
                            decoView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        }
                        decoView.setVisibility(View.VISIBLE);
                    }
                });
            }
        });
    }
}
