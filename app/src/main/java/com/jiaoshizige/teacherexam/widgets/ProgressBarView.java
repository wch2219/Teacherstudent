package com.jiaoshizige.teacherexam.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.widget.ProgressBar;

import com.jiaoshizige.teacherexam.R;
import com.jiaoshizige.teacherexam.utils.AllUtils;

public class ProgressBarView extends ProgressBar {
	private int mFlag;
	private static final int DEFAULT_TEXT_SIZE = 20;
//	private static final int DEFAULT_TEXT_COLOR = 0XFFFC00D1;
	private static final int DEFAULT_TEXT_COLOR = 0xFFA07A;
//	private static final int DEFAULT_COLOR_UNREACHED_COLOR = 0xFFd3d6da;
	private static final int DEFAULT_COLOR_UNREACHED_COLOR = 0xFFd3d6da;
	private static final int DEFAULT_HEIGHT_REACHED_PROGRESS_BAR = 8;
	private static final int DEFAULT_HEIGHT_UNREACHED_PROGRESS_BAR = 8;
	private static final int DEFAULT_SIZE_TEXT_OFFSET = 4;
	/**
	 * painter of all drawing things
	 */
	protected Paint mPaint = new Paint();
	Paint p1 = new Paint();
	Paint p = new Paint();
	/**
	 * color of progress number
	 */
//	protected int mTextColor = DEFAULT_TEXT_COLOR;
	protected int mTextColor;
	protected int mNewTextColor;
	protected String mText = "抢";
	/**
	 * size of text (sp)
	 */
	protected int mTextSize = sp2px(DEFAULT_TEXT_SIZE);

	/**
	 * offset of draw progress
	 */
	protected int mTextOffset = dp2px(DEFAULT_SIZE_TEXT_OFFSET);

	/**
	 * height of reached progress bar
	 */
	protected int mReachedProgressBarHeight = dp2px(DEFAULT_HEIGHT_REACHED_PROGRESS_BAR);

	/**
	 * color of reached bar
	 */
	protected int mReachedBarColor = DEFAULT_TEXT_COLOR;
	protected int mNewReachedBarColor;
	private int mNewReachedBarColor1;
	/**
	 * color of unreached bar
	 */
	protected int mUnReachedBarColor = DEFAULT_COLOR_UNREACHED_COLOR;
	/**
	 * height of unreached progress bar
	 */
	protected int mUnReachedProgressBarHeight = dp2px(DEFAULT_HEIGHT_UNREACHED_PROGRESS_BAR);
	/**
	 * view width except padding
	 */
	protected int mRealWidth;

	protected boolean mIfDrawText = true;
	private int mRadius = dp2px(46);
	private int mMaxPaintWidth;
	protected static final int VISIBLE = 0;

	public ProgressBarView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		final TypedArray attributes = getContext().obtainStyledAttributes(attrs,
				R.styleable.HorizontalProgressBarWithNumber);
		mText = attributes.getString(R.styleable.HorizontalProgressBarWithNumber_progress_text_text);
		mTextColor = attributes.getColor(R.styleable.HorizontalProgressBarWithNumber_progress_text_color,
				DEFAULT_TEXT_COLOR);
		mTextSize = (int) attributes.getDimension(R.styleable.HorizontalProgressBarWithNumber_progress_text_size,
				mTextSize);
		mReachedBarColor = attributes.getColor(R.styleable.HorizontalProgressBarWithNumber_progress_reached_color,
				mTextColor);
		mUnReachedBarColor = attributes.getColor(R.styleable.HorizontalProgressBarWithNumber_progress_unreached_color,
				DEFAULT_COLOR_UNREACHED_COLOR);
		mReachedProgressBarHeight = (int) attributes.getDimension(
				R.styleable.HorizontalProgressBarWithNumber_progress_reached_bar_height, mReachedProgressBarHeight);
		mUnReachedProgressBarHeight = (int) attributes.getDimension(
				R.styleable.HorizontalProgressBarWithNumber_progress_unreached_bar_height, mUnReachedProgressBarHeight);
		mTextOffset = (int) attributes.getDimension(R.styleable.HorizontalProgressBarWithNumber_progress_text_offset,
				mTextOffset);
		mRadius = (int) attributes.getDimension(R.styleable.HorizontalProgressBarWithNumber_radiues, mRadius);
		int textVisible = attributes.getInt(R.styleable.HorizontalProgressBarWithNumber_progress_text_visibility,
				VISIBLE);
		if (textVisible != VISIBLE) {
			mIfDrawText = false;
		}
		attributes.recycle();
		mPaint.setAntiAlias(true);
		if(AllUtils.getScreenWidth(context) > 1280)
		{
			mPaint.setTextSize(43);
			p.setTextSize(63);
			p1.setTextSize(43);
			Log.e("PPPP","PPPPP1280");
		}else if(AllUtils.getScreenWidth(context) < 1000)
		{
			mPaint.setTextSize(21);
			p.setTextSize(32);
			p1.setTextSize(21);
			Log.e("PPPP","PPPPP100"+AllUtils.getScreenWidth(context));
		}else
		{
			mPaint.setTextSize(31);
			p.setTextSize(51);
			p1.setTextSize(31);
			Log.e("PPPP","PPPPP1280"+AllUtils.getScreenWidth(context));
		}
		/*if(AllUtils.getDesity(context)< 2)
		{
			mPaint.setTextSize(19);
			p.setTextSize(19);
			p1.setTextSize(19);

		}*/
//		mPaint.setTextSize(25);
//		mPaint.setColor(mTextColor);
		p.setAntiAlias(true);
		p.setColor(mNewTextColor);
	}

	public ProgressBarView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	@Override
	protected synchronized void onDraw(Canvas canvas) {
		String text = mText;
		float textWidth = mPaint.measureText(text);
		float textHeight = (mPaint.descent() + mPaint.ascent()) / 2;

		canvas.save();
		canvas.translate(getPaddingLeft() + mMaxPaintWidth / 2, getPaddingTop() + mMaxPaintWidth / 2);
		mPaint.setStyle(Style.STROKE);
		p.setStyle(Style.STROKE);
		mPaint.setColor(mUnReachedBarColor);
		p.setColor(mNewTextColor);
		mPaint.setStrokeWidth(mUnReachedProgressBarHeight);
		p.setStrokeWidth(mUnReachedProgressBarHeight);
		canvas.drawCircle(mRadius, mRadius, mRadius, mPaint);
			mPaint.setColor(mNewReachedBarColor);
			p.setColor(mNewTextColor);
		mPaint.setStrokeWidth(mReachedProgressBarHeight);
		p.setStrokeWidth(mReachedProgressBarHeight);
		float sweepAngle = getProgress() * 1.0f / getMax() * 360;
		canvas.drawArc(new RectF(0, 0, mRadius * 2, mRadius * 2), -90, sweepAngle, false, mPaint);
		// draw text
		mPaint.setStyle(Style.FILL);
		p.setStyle(Style.FILL);


			p1.setStyle(Style.STROKE);
			p1.setColor(getResources().getColor(R.color.buy_color));
			p1.setAntiAlias(true);
//			p1.setTextSize(13);
            float mTextWidth = p1.measureText("%");
			float mTextHeight = (p1.descent() + p1.ascent()) / 2;
		Log.e("TAG",textWidth+"ss");
		if(AllUtils.getScreenWidth(getContext()) == 1280){

		}

			if(textWidth == 17){
				canvas.drawText(text, mRadius - textWidth / 2 - 10 , mRadius - textHeight+8, p);
				canvas.drawText("%", mRadius+textWidth / 2  , mRadius - textHeight+8 , p1);
			}else if(textWidth == 51){
				canvas.drawText(text, mRadius - textWidth / 2 - 27, mRadius - textHeight+8, p);
				canvas.drawText("%", mRadius+textWidth / 2+5  , mRadius - textHeight+8 , p1);
			}else if(textWidth == 12){
				canvas.drawText(text, mRadius - textWidth / 2 - 5, mRadius - textHeight+8, p);
				canvas.drawText("%", mRadius+textWidth / 2  , mRadius - textHeight+8 , p1);
			}else{
				canvas.drawText(text, mRadius - textWidth / 2 - 12, mRadius - textHeight+8, p);
				canvas.drawText("%", mRadius+textWidth / 2+5  , mRadius - textHeight+8 , p1);
			}

		/*TextPaint textPaint = new TextPaint();
		textPaint.setColor(getResources().getColor(R.color.text_color6));
		textPaint.setAntiAlias(true);
		if(AllUtils.getScreenWidth(getContext()) < 1000)
		{
			textPaint.setTextSize(20);
			StaticLayout layout = new StaticLayout("  已完成"+"\n"+text,textPaint,200,
					Layout.Alignment.ALIGN_NORMAL, 1.0F, 0.0F, true);
			canvas.save();
			canvas.translate(mRadius - textWidth / 2 , (mRadius - textHeight) / 2 - 5);
			layout.draw(canvas);

		}else if(AllUtils.getScreenWidth(getContext()) > 1100)
		{
			textPaint.setTextSize(40);
			StaticLayout layout = new StaticLayout("  已完成"+"\n"+text, textPaint,200,
					Layout.Alignment.ALIGN_NORMAL, 1.0F, 0.0F, true);
			canvas.save();
			canvas.translate(mRadius - textWidth / 2 , (mRadius - textHeight) / 2 - 5);
			layout.draw(canvas);
		}
		else {
			textPaint.setTextSize(35);
			StaticLayout layout = new StaticLayout("  已完成"+"\n"+text, textPaint, 300,
					Layout.Alignment.ALIGN_NORMAL, 1.0F, 0.0F, true);
			canvas.save();
			canvas.translate(mRadius - textWidth / 2 - 10, (mRadius - textHeight) / 2 - 10);//从20，20开始画
			layout.draw(canvas);
		}*/
//		}
		canvas.restore();
	}

	@Override
	protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		mMaxPaintWidth = Math.max(mReachedProgressBarHeight, mUnReachedProgressBarHeight);
		int expect = mRadius * 2 + mMaxPaintWidth + getPaddingLeft() + getPaddingRight();
		int width = resolveSize(expect, widthMeasureSpec);
		int height = resolveSize(expect, heightMeasureSpec);
		int realWidth = Math.min(width, height);
		mRadius = (realWidth - getPaddingLeft() - getPaddingRight() - mMaxPaintWidth) / 2;

		setMeasuredDimension(realWidth, realWidth);
	}

	protected int dp2px(int dpVal) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpVal, getResources().getDisplayMetrics());
	}

	protected int sp2px(int spVal) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spVal, getResources().getDisplayMetrics());

	}

	public void setText(String text) {
		mText = text;
	}
	public String gettext()
	{
		return mText;
	}
	public void setReachedBarColor(int color)
	{
		mNewReachedBarColor = color;
	}
	public void setReachedBarColor1(int color)
	{
		mNewReachedBarColor1 = color;
	}
	public void SetTextColor(int color)
	{
		mNewTextColor = color;
	}
	public void setFlag(int flag)
	{
		mFlag = flag;
	}
}
