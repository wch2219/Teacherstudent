package com.jiaoshizige.teacherexam.adapter;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;


/**
 * Created by Administrator on 2017/7/28 0028.
 */

public class LoginViewPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragmentLists;

    public LoginViewPagerAdapter(FragmentManager fm, List<Fragment> fragmentLists) {
        super(fm);
        this.fragmentLists = fragmentLists;
    }



    @Override
    public Fragment getItem(int position) {
        return fragmentLists.get(position);
    }

    @Override
    public int getCount() {
        return fragmentLists.size();
    }
}
