package com.jiaoshizige.teacherexam.activity;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.jiaoshizige.teacherexam.utils.ToastUtil;
import com.umeng.analytics.MobclickAgent;
import com.jiaoshizige.teacherexam.R;
import com.jiaoshizige.teacherexam.adapter.TabPagerAdapter;
import com.jiaoshizige.teacherexam.base.BaseActivity;
import com.jiaoshizige.teacherexam.base.GloableConstant;
import com.jiaoshizige.teacherexam.fragment.StudyCalendarFragment;
import com.jiaoshizige.teacherexam.http.ApiUrl;
import com.jiaoshizige.teacherexam.http.MyCallBack;
import com.jiaoshizige.teacherexam.http.Xutil;
import com.jiaoshizige.teacherexam.model.CalendarDateReponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/a1/a1.
 * 学习日历
 */

public class StudyCalendarActivity extends BaseActivity {
    List<Fragment> mFragments;
    private List<String> tabTitles;
    @BindView(R.id.tabLayout)
    TabLayout mTabLayout;
    @BindView(R.id.study_calendar_pager)
    ViewPager mStudyCalendarViewPager;
    private int thisWeek;
    private String mClassId;
    private String mType = "";
    @Override
    protected int getLayoutId() {
        return R.layout.study_calendar_activity;
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("学习日历");
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("学习日历");
        MobclickAgent.onPause(this);
    }

    @Override
    protected void initView() {
        setSubTitle().setText("");
        setToolbarTitle().setText("学习日历");
        mFragments = new ArrayList<>();
        tabTitles= new ArrayList<>();
        if(getIntent().getExtras().get("class_id") != null){
            mClassId = (String) getIntent().getExtras().get("class_id");
        }else{
            mClassId = "";
        }
        if(getIntent().getExtras().get("type") != null){
            mType = (String) getIntent().getExtras().get("type");
        }
        requestDate();
    }
    private void requestDate(){
        Map<String,Object> map = new HashMap<>();
//        GloableConstant.getInstance().showmeidialog(this);

        GloableConstant.getInstance().startProgressDialog(this);
        Xutil.Post(ApiUrl.GETWEEK,map,new MyCallBack<CalendarDateReponse>(){
            @Override
            public void onSuccess(CalendarDateReponse result) {
                super.onSuccess(result);
//                GloableConstant.getInstance().stopProgressDialog();
                GloableConstant.getInstance().stopProgressDialog1();
                if(result.getStatus_code().equals("200")){
                    if(result.getData() != null && result.getData().size() > 0){
                            for (int i = 0;i < result.getData().size();i++){
                                if(result.getData().get(i).getCurrent().equals("1")){
                                    tabTitles.add(i,"本周");
                                    thisWeek = i;
                                }else{
                                    tabTitles.add(i,result.getData().get(i).getStart_date()+"-"+result.getData().get(i).getEnd_date());
                                }
                                mFragments.add(new StudyCalendarFragment().getInstance(i,result.getData().get(i).getStart_time(),result.getData().get(i).getEnd_time(),mClassId,mType));
                            }
                        TabPagerAdapter adapter =new TabPagerAdapter(getSupportFragmentManager(), mFragments, tabTitles);
                        mStudyCalendarViewPager.setAdapter(adapter);
                        mTabLayout.setupWithViewPager(mStudyCalendarViewPager);
                        mStudyCalendarViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                            @Override
                            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                            }
                            @Override
                            public void onPageSelected(int position) {
                                mStudyCalendarViewPager.setCurrentItem(position);
                            }
                            @Override
                            public void onPageScrollStateChanged(int state) {
                            }
                        });
                        mStudyCalendarViewPager.setCurrentItem(thisWeek);
                    }
                }else if (result.getStatus_code().equals("401")) {
                    GloableConstant.getInstance().clearAll();
                    ToastUtil.showShortToast("请重新登录");
                    startActivity(new Intent(StudyCalendarActivity.this, LoginActivity.class));
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
                GloableConstant.getInstance().stopProgressDialog1();
            }
        });
    }
}
