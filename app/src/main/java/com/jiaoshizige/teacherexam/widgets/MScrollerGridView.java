package com.jiaoshizige.teacherexam.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

public class MScrollerGridView extends GridView {   
    private boolean haveScrollbar = true;
    public MScrollerGridView(Context context) {   
        super(context);   
    }   
    public MScrollerGridView(Context context, AttributeSet attrs) {   
        super(context, attrs);   
    }   
    public MScrollerGridView(Context context, AttributeSet attrs, int defStyle) {   
        super(context, attrs, defStyle);   
    }   
    /**  
     * 设置是否有ScrollBar，当要在ScollView中显示时，应当设置为false。 默认为 true  
     *   

     */   
    public void setHaveScrollbar(boolean haveScrollbar) {   
        this.haveScrollbar = haveScrollbar;   
    }   
    @Override   
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {   
       /* if (haveScrollbar == false) {
            int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);   
            super.onMeasure(widthMeasureSpec, expandSpec);   
        } else {   
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);   
        }*/
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);

        super.onMeasure(widthMeasureSpec, expandSpec);
    }


}
