package com.jiaoshizige.teacherexam.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Administrator on 2017/7/31.
 */

public class SideBar extends View{


    public SideBar(Context context) {
        super(context);
    }

    public SideBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SideBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    private OnTouchingLetterChangedListener onTouchingLetterChangedListener;
    //26个字母
    public static String[] b={ "A", "B", "C", "D", "E", "F", "G", "H", "I",
            "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
            "W", "X", "Y", "Z", "#" };
    private int choose = -1;
    private Paint paint = new Paint();
    private TextView mTextDialog;
    /**
     * 设置点击字体提示框
     * @param mTextDialog
     */
    public void setTextView(TextView mTextDialog){
        this.mTextDialog=mTextDialog;
    }
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);
        int height = getHeight();
        int width = getWidth();
        int singleHeight = height / b.length;
        for(int i = 0; i< b.length; i++){
            paint.setColor(Color.rgb(33,65,98));
            paint.setTypeface(Typeface.DEFAULT.DEFAULT_BOLD);
            paint.setAntiAlias(true);
            paint.setTextSize(30);
            if(i == choose){
                paint.setColor(Color.parseColor("#3399ff"));//设置选中状态颜色
                paint.setFakeBoldText(true);
            }
            //x坐标等于中间-字符串宽度的一半(????????)
            float xPos=width/2-paint.measureText(b[i])/2;
            float yPos=singleHeight*i+singleHeight;
            canvas.drawText(b[i],xPos,yPos,paint);
            paint.reset();//重置画笔
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public boolean dispatchTouchEvent(MotionEvent event){
        int action=event.getAction();
        float y=event.getY();//点击Y坐标
        int oldChoose=choose;
        OnTouchingLetterChangedListener listener=onTouchingLetterChangedListener;
        int c=(int)(y/getHeight()*b.length);//点击y坐标所占总高度的比例*b数组的长度就等于点击b中的个数

        switch (action){
            case MotionEvent.ACTION_UP:
                setBackground(new ColorDrawable(0*00000000));
                choose=-1;//
                invalidate();
                if(mTextDialog!=null)
                {
                    mTextDialog.setVisibility(View.INVISIBLE);
                }
                break;
            default:
//                setBackgroundResource(R.drawable.sidebar_background);
                if(oldChoose!=c)
                {
                    if(c>=0 && c<b.length)
                    {
                        if(listener!=null)
                        {
                            listener.onTouchingLetterChanged(b[c]);
                        }
                        if(mTextDialog!=null)
                        {
                            mTextDialog.setText(b[c]);
                            mTextDialog.setVisibility(View.VISIBLE);
                        }

                        choose=c;
                        invalidate();
                    }
                }

                break;
        }
        return true;
    }
    /**
    * 向外公开的方法
     * @param onTouchingLetterChangedListener
     */
    public void setOnTouchingLetterChangedListener(OnTouchingLetterChangedListener onTouchingLetterChangedListener){
        this.onTouchingLetterChangedListener=onTouchingLetterChangedListener;
    }
    /**
     * 接口
     */
    public interface OnTouchingLetterChangedListener{
        public void onTouchingLetterChanged(String s);
    }







}
