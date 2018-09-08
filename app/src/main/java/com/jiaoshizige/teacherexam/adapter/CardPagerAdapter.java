package com.jiaoshizige.teacherexam.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import com.jiaoshizige.teacherexam.widgets.CardItem;
import com.jiaoshizige.teacherexam.widgets.CardViewPager;

import java.util.List;


import static com.jiaoshizige.teacherexam.widgets.CardViewPager.CACHE_COUNT;
/**
 * Created by Administrator on 2018/7/23.
 */

public class CardPagerAdapter extends FragmentStatePagerAdapter {

    private static final int MAX_VALUE = Integer.MAX_VALUE;

    static final int DIFF_COUNT = CACHE_COUNT / 2;

    private List<CardItem> mCardItems;
    private boolean mIsLoop;

  public   CardPagerAdapter(FragmentManager fm, List<CardItem> cardItems, boolean isLoop) {
        super(fm);
        mCardItems = cardItems;
        mIsLoop = isLoop;
    }

  public  void setCardMode(@CardViewPager.TransformerMode int mode) {
        if (mCardItems == null || mCardItems.isEmpty()) {
            return;
        }
        for (CardItem cardItem : mCardItems) {
            cardItem.currentMode = mode;
        }
    }

    @Override
    public Fragment getItem(int position) {
        return mCardItems.get(position);
    }

    @Override
    public int getCount() {
        final int realCount = getRealCount();
        if (realCount == 0) {
            return 0;
        }
        return mIsLoop ? MAX_VALUE : realCount;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        final int j = position % getRealCount();
        return super.instantiateItem(container, mIsLoop ? j : position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        if (mIsLoop) {
            CardViewPager viewPager = (CardViewPager) container;
            int pos = viewPager.getCurrentItem();
            int i = pos % getRealCount();
            int j = position % getRealCount();
            if (Math.abs(i - j) != DIFF_COUNT && !viewPager.isNotify) {
                return;
            }
            super.destroyItem(container, j, object);
            return;
        }
        super.destroyItem(container, position, object);
    }

    @Override
    public void startUpdate(ViewGroup container) {
        super.startUpdate(container);
        final int realCount = getRealCount();
        if (realCount == 0) {
            return;
        }
        if (mIsLoop) {
            CardViewPager viewPager = (CardViewPager) container;
            int position = viewPager.getCurrentItem();
            if (position == 0) {
                position = getFirstItem(realCount);
            } else if (position == getCount() - 1) {
                position = getLastItem(realCount, position % realCount);
            }
            viewPager.setCurrentItem(position, false);
        }
    }

    private int getRealCount() {
        return mCardItems == null ? 0 : mCardItems.size();
    }

    private int getFirstItem(int realCount) {
        return MAX_VALUE / realCount / 2 * realCount;
    }

    private int getLastItem(int realCount, int index) {
        return MAX_VALUE / realCount / 2 * realCount + index;
    }
}
