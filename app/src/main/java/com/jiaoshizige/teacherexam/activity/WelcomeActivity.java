package com.jiaoshizige.teacherexam.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.jiaoshizige.teacherexam.R;
import com.jiaoshizige.teacherexam.adapter.GuidePageAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/3/5.
 * 引导页面
 */

public class WelcomeActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {

    @BindView(R.id.view_pager)
    ViewPager mViewPager;

    private GuidePageAdapter mGuidePageAdapter;
    private int[] imageIdArray;//图片资源的数组
    private List<View> viewList;//图片资源的集合

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_layout);
        ButterKnife.bind(this);
        initView();
    }

    protected void initView() {
        imageIdArray = new int[]{R.mipmap.user_guide_one, R.mipmap.user_guide_two, R.mipmap.user_guide_three};
        viewList = new ArrayList<>();
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        for (int i = 0; i < imageIdArray.length; i++) {
            ImageView image = new ImageView(this);
            image.setLayoutParams(params);
            image.setBackgroundResource(imageIdArray[i]);
            viewList.add(image);
        }
        mViewPager.setAdapter(new GuidePageAdapter(viewList,this));
        mViewPager.setOnPageChangeListener(this);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if (position == 2) {
         /*   mViewPager.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()){
                        case  MotionEvent.ACTION_UP:
                            Sphelper.save(WelcomeActivity.this, "isFirstUse", "isfiestuser", true);
                            if(SPUtils.getSpValues(SPKeyValuesUtils.SP_IS_LOGIN, SPUtils.TYPE_STRING).equals("1") && SPUtils.getSpValues(SPKeyValuesUtils.SP_IS_LOGIN, SPUtils.TYPE_STRING) != null){
                                Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            }else{
                                Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
                                startActivity(intent);
                                finish();
                            }
                            finish();
                            break;
                    }
                    return false;
                }
            });*/
        }

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

}
