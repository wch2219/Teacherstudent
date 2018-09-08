package com.jiaoshizige.teacherexam.activity;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.widget.TextView;

import com.jiaoshizige.teacherexam.yy.fragment.CourseFragment;
import com.umeng.analytics.MobclickAgent;
import com.jiaoshizige.teacherexam.R;
import com.jiaoshizige.teacherexam.adapter.TabPagerAdapter;
import com.jiaoshizige.teacherexam.base.BaseActivity;
import com.jiaoshizige.teacherexam.utils.ToolUtils;
import com.jiaoshizige.teacherexam.yy.fragment.BooksFragment;
import com.jiaoshizige.teacherexam.yy.fragment.InformationFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/a1/7 0007.
 */

public class MyCollectionActivity extends BaseActivity {
    @BindView(R.id.back_im)
    TextView back_im;
    @BindView(R.id.tab_trends_title)
    TabLayout mTableLayout;
    @BindView(R.id.vp_trends)
    ViewPager mViewPager;
    private TabPagerAdapter mTabPagerAdapter;
    private List<String> tabTitles;
    private List<Fragment> fragments;
    private int number=0;

    @Override
    protected void onStart() {
        super.onStart();
        ToolUtils.setIndicator(mTableLayout,25,25);
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.my_collection_activity;
    }


    @Override
    protected void initView() {
        setSubTitle().setText("");
        setToolbarTitle().setText("我的收藏");
        fragments=new ArrayList<>();
//        fragments.add(new ClassFragment());
        fragments.add(new CourseFragment());
        fragments.add(new BooksFragment());
        fragments.add(new InformationFragment());
        tabTitles=new ArrayList<>();
//        tabTitles.add("班级");
        tabTitles.add("课程");
        tabTitles.add("图书");
        tabTitles.add("资讯");

        for(int i=0;i<tabTitles.size();i++){
            mTableLayout.addTab(mTableLayout.newTab().setText(tabTitles.get(i)));
        }
        //设置tablayout模式
        mTableLayout.setTabMode(TabLayout.MODE_FIXED);
        mTabPagerAdapter = new TabPagerAdapter(this.getSupportFragmentManager(), fragments, tabTitles);
        mViewPager.setAdapter(mTabPagerAdapter);
        mViewPager.setOffscreenPageLimit(1);
        mViewPager.setCurrentItem(number);
        mTableLayout.setupWithViewPager(mViewPager);

    }
}
