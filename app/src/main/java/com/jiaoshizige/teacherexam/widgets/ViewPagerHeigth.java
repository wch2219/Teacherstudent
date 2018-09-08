package com.jiaoshizige.teacherexam.widgets;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

public class ViewPagerHeigth extends ViewPager{

	    public ViewPagerHeigth(Context context) {  
	        super(context);  
	    }  
	  
	    public ViewPagerHeigth(Context context, AttributeSet attrs) {  
	        super(context, attrs);  
	    }  
	  
	    @Override  
	    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {  
	        super.onMeasure(widthMeasureSpec, heightMeasureSpec);  
	        int childSize = getChildCount();  
	        int maxHeight = 0;  
	        for (int i = 0; i < childSize; i++) {  
	            View child = getChildAt(i);  
	            child.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));  
	            if (child.getMeasuredHeight() > maxHeight) {  
	                maxHeight = child.getMeasuredHeight();  
	            }  
	        }  
	  
	        if (maxHeight > 0) {  
	            setMeasuredDimension(getMeasuredWidth(), maxHeight);  
	        }  
	  
	    }  
}
