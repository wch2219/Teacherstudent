package com.jiaoshizige.teacherexam.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by Administrator on 2018/1/9 0009.
 */

public class ViewPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> mFragments;

    public ViewPagerAdapter(FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        // TODO Auto-generated constructor stub
        this.mFragments = fragments;
    }

    @Override
    public Fragment getItem(int arg0) {
        // TODO Auto-generated method stub
        return mFragments.get(arg0);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mFragments.size();
    }

//    @Override
//    public int getItemPosition(Object object) {
//        return POSITION_NONE;
//    }

    /*因为FragmentPagerAdapter.instantiateItem()中mFragmentManager是通过name去寻找是否有缓存的fragment,而name是根据viewId和getItemId(position)确定的，
    我们可以通过修改getItemId(position)方法来让mFragmentManager找不到fragment从而去使用新的fragment替换。
    getItemId(position)默认返回的是position，我这里使用position+时间戳来替换。*/

//    @Override
//    public long getItemId(int position) {
//        Long time = System.currentTimeMillis();
//        return super.getItemId(position) + time;
//    }
}
