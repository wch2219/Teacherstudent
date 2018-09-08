package com.jiaoshizige.teacherexam.widgets;



import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.ProgressBar;

import com.jiaoshizige.teacherexam.R;

/**
 * Created by Administrator on 2018/1/6 0006.
 */

public class HorizonalPrograssBarWithPrograss extends ProgressBar {

    //这里声明默认值
    private static final int DEFAULT_TEXT_SIZE = 10;//sp
    private static final int DEFAULT_TEXT_COLOR = 0xF8F72DB;
    private static final int DEFAULT_COLOR_UNREACH = 0xFD3D6DA;
    private static final int DEFAULT_HEIGHT_UNREACH = 2;//dp
    private static final int DEFAULT_COLOR_REACH = 0xF8F72DB;
    private static final int DEFAULT_HRIGHT_REACH = 2;//dp
    private static final int DEFAULT_TEXT_OFFSET = 10;//dp

    //编写我们的值
    private int mTextSize = dp2px(DEFAULT_TEXT_SIZE);
    private int mTextColor = DEFAULT_TEXT_COLOR;
    private int mUnReachColor = DEFAULT_COLOR_UNREACH;
    private int mUnReachHeight = DEFAULT_HEIGHT_UNREACH;
    private int mReachColor = DEFAULT_COLOR_REACH;
    private int mReachHeight = DEFAULT_HRIGHT_REACH;
    private int mTextOffset = dp2px(DEFAULT_TEXT_OFFSET);

    private Paint mPaint = new Paint();
    private int mRealWidth; //进度条实际宽度

    //2个参数法
    public HorizonalPrograssBarWithPrograss(Context context, AttributeSet attrs) {
        this(context, attrs, 0);//2个参数继承3个参数的方法
    }

    public HorizonalPrograssBarWithPrograss(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        obtainStyledAttrs(attrs);


    }

    //获取自定义属性
    private void obtainStyledAttrs(AttributeSet attrs) {
        TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.horizonalPrograssBarWithPrograss);

        mTextSize = (int) ta.getDimension(R.styleable.horizonalPrograssBarWithPrograss_text_size, mTextSize);
        mTextColor = ta.getColor(R.styleable.horizonalPrograssBarWithPrograss_text_color, mTextColor);
        mTextOffset = (int) ta.getDimension(R.styleable.horizonalPrograssBarWithPrograss_prograss_text_offset, mTextOffset);
        mUnReachColor = ta.getColor(R.styleable.horizonalPrograssBarWithPrograss_prograss_unreach_color, mUnReachColor);
        mUnReachHeight = (int) ta.getDimension(R.styleable.horizonalPrograssBarWithPrograss_prograss_unreach_height, mUnReachHeight);
        mReachColor = ta.getColor(R.styleable.horizonalPrograssBarWithPrograss_prograss_reach_color, mReachColor);
        mReachHeight = (int) ta.getDimension(R.styleable.horizonalPrograssBarWithPrograss_prograss_reach_height, mReachHeight);

        mPaint.setTextSize(mTextSize);
        ta.recycle();
    }

    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        int widthMode = MeasureSpec.getMode(widthMeasureSpec);//因为是水平进度条 默认宽度为用户的输入 所以不需要判断
        int widthVal = MeasureSpec.getSize(widthMeasureSpec);

        int height = measureHeight(heightMeasureSpec);

        setMeasuredDimension(widthVal, height);//完成测量
        //实际绘制区域的宽度
        mRealWidth = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
    }

    //高度测量的方法
    private int measureHeight(int heightMeasureSpec) {
        int result = 0;
        int mode = MeasureSpec.getMode(heightMeasureSpec);
        int size = MeasureSpec.getSize(heightMeasureSpec);

        if (mode == MeasureSpec.EXACTLY) {//精确值，用户给的精确值 如200dp marchparent
            result = size;
        } else {//否则宽度 是由文字的距离确定的
            int textHeight = (int) (mPaint.descent() - mPaint.ascent());
            result = getPaddingTop() //上边距
                    + getPaddingBottom() //下边距
                    + Math.max(Math.max(mReachHeight, mUnReachHeight), Math.abs(textHeight));//三者最大值
            if (mode == MeasureSpec.AT_MOST) {
                result = Math.min(result, size);
            }
        }

        return result;
    }

    //1个参法
    public HorizonalPrograssBarWithPrograss(Context context) {
        this(context, null);//继承两个参数的构造方法，当用户在使用时new的时候用，但是使用时一般是用两个参数的构造方法的
    }



    /**
     *
     下面是绘制进度条的方法
     * @param canvas
     */
    @Override
    protected synchronized void onDraw(Canvas canvas) {
//        super.onDraw(canvas);
        canvas.save();
        canvas.translate(getPaddingLeft(), getHeight() / 2);//移动画布到 最左边 正中间

        //判断是否需要绘制unReachBar
        boolean noNeedUnReach = false;
        //DrawReachBar如下：

        //拿到文本宽度
        String text = getProgress() + "";
        int textWidth = (int) mPaint.measureText(text);

        float ratio = getProgress() * 1.0f / getMax();
        float prograssX = ratio * mRealWidth;
        if (prograssX + textWidth > mRealWidth) {
            prograssX = mRealWidth - textWidth;//防止进度条到100文本出去
            noNeedUnReach = true;
        }

        float endX = prograssX - mTextOffset / 2;
        if (endX > 0) {
            mPaint.setColor(mReachColor);
            mPaint.setStrokeWidth(mReachHeight);
            canvas.drawLine(0, 0, endX, 0, mPaint);

        }

        //Draw Text
        mPaint.setColor(mTextColor);
        int y = (int) (-(mPaint.descent()+mPaint.ascent())/2);
        canvas.drawText(text,prograssX,y,mPaint);

        //Draw UnReachBar
        if (!noNeedUnReach){
            float start = prograssX + mTextOffset/2 +textWidth;
            mPaint.setColor(mUnReachColor);
            mPaint.setStrokeWidth(mReachHeight);
            canvas.drawLine(start,0,mRealWidth,0,mPaint);
        }

        canvas.restore();
    }

    //转换方法dp 2 px
    private int dp2px(int dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, getResources().getDisplayMetrics());
    }

    private int sp2px(int spVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                spVal, getResources().getDisplayMetrics());
    }


}