package com.jiaoshizige.teacherexam.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;

import com.jiaoshizige.teacherexam.R;
import com.jiaoshizige.teacherexam.adapter.CardPagerAdapter;

import java.io.Serializable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/7/23.
 */

public class CardViewPager  extends ViewPager {

  public  static final int CACHE_COUNT = 8;

    public static final int MODE_CARD = 0;
    public static final int MODE_NORMAL = 1;

    private static final int MARGIN_MIN = -60;
    //调整两侧图片出现的宽度
    private static final int MARGIN_MAX = 0;

    private static final int PADDING_MIN = 0;
    //天正展示图片的大小
    private static final int PADDING_MAX = 50;

    @IntDef({MODE_CARD, MODE_NORMAL})
    @Retention(RetentionPolicy.SOURCE)
    public @interface TransformerMode {

    }

    private int mMaxOffset;
    private float mScaleRate;
    private boolean mIsLoop = false;
    private int mCardPaddingLeft;
    private int mCardPaddingTop;
    private int mCardPaddingRight;
    private int mCardPaddingBottom;
    private CardTransformer mTransformer;
    @TransformerMode
    private int mCurrentMode = MODE_CARD;

  public  boolean isNotify;

    public CardViewPager(Context context) {
        this(context, null);
    }

    public CardViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);

        setClipToPadding(false);
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CardViewPager);
        int padding = typedArray
                .getDimensionPixelOffset(R.styleable.CardViewPager_card_padding,
                        (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 60, displayMetrics));
        mCardPaddingLeft = getPaddingLeft();
        mCardPaddingTop = getPaddingTop();
        mCardPaddingRight = getPaddingRight();
        mCardPaddingBottom = getPaddingBottom();
        final int paddingMin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, PADDING_MIN, displayMetrics);
        final int paddingMax = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, PADDING_MAX, displayMetrics);
        if (padding < paddingMin) {
            padding = paddingMin;
        }
        if (padding > paddingMax) {
            padding = paddingMax;
        }
        setPadding(mCardPaddingLeft + padding, mCardPaddingTop, mCardPaddingRight + padding, mCardPaddingBottom);

        int margin = typedArray
                .getDimensionPixelOffset(R.styleable.CardViewPager_card_margin,
                        (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40, displayMetrics));
        final int marginMin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, MARGIN_MIN, displayMetrics);
        final int marginMax = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, MARGIN_MAX, displayMetrics);
        if (margin < marginMin) {
            margin = marginMin;
        }
        if (margin > marginMax) {
            margin = marginMax;
        }
        setPageMargin(margin);

        mMaxOffset = typedArray
                .getDimensionPixelOffset(R.styleable.CardViewPager_card_max_offset,
                        (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 180, displayMetrics));

        mIsLoop = typedArray.getBoolean(R.styleable.CardViewPager_card_loop, mIsLoop);

        mScaleRate = typedArray.getFloat(R.styleable.CardViewPager_card_scale_rate, 0.15f);

        typedArray.recycle();
        setOffscreenPageLimit(3);
    }

    @Override
    public void setOffscreenPageLimit(int limit) {
        if (limit < 3) {
            limit = 3;
        }
        super.setOffscreenPageLimit(limit);
    }

    /**
     * 请在设置adapter之前调用，即在bind方法之前调用
     *
     * @param maxOffset 移动偏移量
     * @param scaleRate 缩放比例
     */
    public void setCardTransformer(float maxOffset, float scaleRate) {
        int cardMaxOffset = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, maxOffset, getResources().getDisplayMetrics());
        mTransformer = new CardTransformer(cardMaxOffset, scaleRate);
        setPageTransformer(false, mTransformer);
    }

    /**
     * 设置卡片左右padding
     *
     * @param padding 值，自动转dp
     */
    public void setCardPadding(float padding) {
        if (padding < PADDING_MIN) {
            padding = PADDING_MIN;
        }
        if (padding > PADDING_MAX) {
            padding = PADDING_MAX;
        }
        int cardPadding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, padding, getResources().getDisplayMetrics());
        setPadding(mCardPaddingLeft + cardPadding, mCardPaddingTop, mCardPaddingRight + cardPadding, mCardPaddingBottom);
    }

    /**
     * 设置卡片margin
     *
     * @param margin 值，自动转dp
     */
    public void setCardMargin(float margin) {
        if (margin < MARGIN_MIN) {
            margin = MARGIN_MIN;
        }
        if (margin > MARGIN_MAX) {
            margin = MARGIN_MAX;
        }
        int cardMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, margin, getResources().getDisplayMetrics());
        setPageMargin(cardMargin);
    }

    /**
     * 根据模式刷新通知UI刷新
     *
     * @param mode 模式
     */
    public void notifyUI(@TransformerMode int mode) {
        mCurrentMode = mode;
        isNotify = true;
        final CardPagerAdapter adapter = (CardPagerAdapter) getAdapter();
        adapter.setCardMode(mCurrentMode);
        setAdapter(adapter);
        isNotify = false;
    }

    boolean isCardMode() {
        return mCurrentMode == MODE_CARD;
    }

    public int getCurrentMode() {
        return mCurrentMode;
    }

    /**
     * 绑定数据源
     *
     * @param fm      FragmentManager
     * @param handler 数据处理类
     * @param data    数据源
     * @param <T>     泛型，必须实现Serializable
     */
    public <T extends Serializable> void bind(FragmentManager fm, CardHandler<T> handler, List<T> data) {
        List<CardItem> cardItems;
        if (data == null || data.isEmpty()) {
            cardItems = new ArrayList<>();
        } else {
            cardItems = getCardItems(handler, data, mIsLoop);
        }
        if (mTransformer == null) {
            mTransformer = new CardTransformer(mMaxOffset, mScaleRate);
            setPageTransformer(false, mTransformer);
        }
        CardPagerAdapter adapter = new CardPagerAdapter(fm, cardItems, mIsLoop);
        setAdapter(adapter);
    }

    @NonNull
    private <T extends Serializable> List<CardItem> getCardItems(CardHandler<T> handler, List<T> data, boolean isLoop) {
        List<CardItem> cardItems = new ArrayList<>();
        int dataSize = data.size();
        boolean isExpand = isLoop && dataSize < CACHE_COUNT;
        int radio = CACHE_COUNT / dataSize < 2 ? 2 : (int) Math.ceil(CACHE_COUNT * 1.0d / dataSize);
        int size = isExpand ? dataSize * radio : dataSize;
        for (int i = 0; i < size; i++) {
            int position = isExpand ? i % dataSize : i;
            T t = data.get(position);
            CardItem<T> item = new CardItem<>();
            item.bindHandler(handler);
            item.bindData(t, position);
            cardItems.add(item);
        }
        return cardItems;
    }

    @Override
    public void setAdapter(PagerAdapter adapter) {
        if (!(adapter instanceof CardPagerAdapter)) {
            throw new RuntimeException("please set CardPagerAdapter!");
        }
        super.setAdapter(adapter);
    }
}
