package com.jiaoshizige.teacherexam.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jiaoshizige.teacherexam.R;
import com.jiaoshizige.teacherexam.adapter.TabPagerAdapter;
import com.jiaoshizige.teacherexam.base.GloableConstant;
import com.jiaoshizige.teacherexam.fragment.ClassIntroduceFragment;
import com.jiaoshizige.teacherexam.fragment.DetialCourseFragment;
import com.jiaoshizige.teacherexam.yy.fragment.EvaluateFragment;
import com.jiaoshizige.teacherexam.http.ApiUrl;
import com.jiaoshizige.teacherexam.http.MyCallBack;
import com.jiaoshizige.teacherexam.http.Xutil;
import com.jiaoshizige.teacherexam.model.ClassDetailResponse;
import com.jiaoshizige.teacherexam.utils.SPKeyValuesUtils;
import com.jiaoshizige.teacherexam.utils.SPUtils;
import com.jiaoshizige.teacherexam.utils.ToastUtil;
import com.jiaoshizige.teacherexam.utils.ToolUtils;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.zhouzhuo.zzhorizontalprogressbar.ZzHorizontalProgressBar;

/**
 * Created by Administrator on 2017/a1/9.
 * 已拥有的班级详情
 */

public class PurchaseClassDetialActivity extends AppCompatActivity {
    @BindView(R.id.completion_degree)
    TextView mCompletionDegree;
    @BindView(R.id.add_group)
    TextView mAddGroup;
    @BindView(R.id.study_prodress)
    ZzHorizontalProgressBar mStudyProgress;
    @BindView(R.id.headmaster_photo)
    ImageView mHeadmasterPhoto;
    @BindView(R.id.headmaster_nickname)
    TextView mHeadmasterNickname;
    @BindView(R.id.assistant_photo)
    RecyclerView mAssistantPhoto;
    @BindView(R.id.tablayout)
    TabLayout mTabLayout;
    @BindView(R.id.viewpager)
    ViewPager mViewPager;
    @BindView(R.id.back_im)
    TextView mBack;
    @BindView(R.id.toolbar_title)
    TextView mToolbar_title;
    @BindView(R.id.toolbar_subtitle)
    TextView mToolbar_subtitle;



    private List<Fragment> mFragments;
    private List<String> tabTitles;
    private TabPagerAdapter pagerAdapter;
    private String mClassId;
    private String mProgress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.purchase_class_detial_activity);
        initView();
    }



    @Override
    protected void onStart() {
        super.onStart();
        ToolUtils.setIndicator(mTabLayout, 25, 25);
    }


    protected void initView() {
        ButterKnife.bind(this);
        if (getIntent().getExtras().get("name") != null) {
            mToolbar_title.setText((String) getIntent().getExtras().get("name"));
        } else {

            mToolbar_title.setText("");
        }
        mToolbar_subtitle.setText("学习日历");

        if (getIntent().getExtras().get("id") != null) {
            mClassId = (String) getIntent().getExtras().get("id");
        } else {
            mClassId = "";
        }
        if (getIntent().getExtras().get("progress") != null) {
            mProgress = (String) getIntent().getExtras().get("progress");
        } else {
            mProgress = "0";
        }
        mCompletionDegree.setText(mProgress);
        mStudyProgress.setProgress(Integer.valueOf(mProgress));
        mFragments = new ArrayList<>();
        tabTitles = new ArrayList<>();
        tabTitles.add("介绍");
        tabTitles.add("课程");
        tabTitles.add("评价");
        mFragments.add(new ClassIntroduceFragment("1", mClassId));
        mFragments.add(new DetialCourseFragment("1", mClassId));
        mFragments.add(new EvaluateFragment(mClassId, "3"));
        for (int i = 0; i < tabTitles.size(); i++) {
            mTabLayout.addTab(mTabLayout.newTab().setText(tabTitles.get(i)));
        }
        pagerAdapter = new TabPagerAdapter(getSupportFragmentManager(), mFragments, tabTitles);
        mViewPager.setAdapter(pagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mAssistantPhoto.setLayoutManager(manager);
        mAssistantPhoto.setAdapter(new CommonAdapter<String>(this, R.layout.item_small_phpto, tabTitles) {
            @Override
            protected void convert(ViewHolder holder, String s, int position) {
                holder.setText(R.id.sss, tabTitles.get(position));
            }
        });
        requestClassDetial();
    }

    @OnClick({R.id.add_group, R.id.toolbar_subtitle,R.id.back_im})
    public void onClickListener(View v) {
        switch (v.getId()) {
            case R.id.add_group:
//                ToastUtil.showShortToast("加入qq群");
                joinQQGroup("391526767");
                break;
            case R.id.toolbar_subtitle:
                Intent intent = new Intent();

                intent.putExtra("class_id", "8");
                intent.setClass(PurchaseClassDetialActivity.this, StudyCalendarActivity.class);
                startActivity(intent);
                break;
            case R.id.back_im:
                finish();
                break;
        }

    }

    /****************
     * 发起添加群流程。群号：2013级梦翔会员交流群(121683601) 的 key 为： 5GuJf0LcgV33u88ZyEh_GnyCyAYKFSJD
     * 调用 joinQQGroup(5GuJf0LcgV33u88ZyEh_GnyCyAYKFSJD) 即可发起手Q客户端申请加群 2013级梦翔会员交流群(121683601)
     *
     * @param key 由官网生成的key
     * @return 返回true表示呼起手Q成功，返回fals表示呼起失败
     ******************/
    public boolean joinQQGroup(String key) {
        Intent intent = new Intent();
        intent.setData(Uri.parse("mqqopensdkapi://bizAgent/qm/qr?url=http%3A%2F%2Fqm.qq.com%2Fcgi-bin%2Fqm%2Fqr%3Ffrom%3Dapp%26p%3Dandroid%26k%3D" + key));
        // 此Flag可根据具体产品需要自定义，如设置，则在加群界面按返回，返回手Q主界面，不设置，按返回会返回到呼起产品界面    //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        try {
            startActivity(intent);
            return true;
        } catch (Exception e) {
            // 未安装手Q或安装的版本不支持
            ToastUtil.showShortToast("未安装手Q或安装的版本不支持");
            return false;
        }
    }

    private void requestClassDetial() {
        Map<String, Object> map = new HashMap<>();
        map.put("group_id", mClassId);
//        map.put("user_id", "1");
        map.put("user_id", SPUtils.getSpValues(SPKeyValuesUtils.SP_USER_ID, SPUtils.TYPE_STRING));
        Xutil.Post(ApiUrl.CLASSSDETAIL, map, new MyCallBack<ClassDetailResponse>() {
            @Override
            public void onSuccess(ClassDetailResponse result) {
                super.onSuccess(result);
                if (result.getStatus_code().equals("200")) {
                    if (result.getData() != null) {
                        Glide.with(PurchaseClassDetialActivity.this).load(result.getData().getTeacher().getHeadImg()).apply(GloableConstant.getInstance().getOptions()).into(mHeadmasterPhoto);
                        mHeadmasterNickname.setText(result.getData().getTeacher().getName());
                        mAssistantPhoto.setAdapter(new CommonAdapter<ClassDetailResponse.mAssistant>(PurchaseClassDetialActivity.this, R.layout.item_small_phpto, result.getData().getAssistant()) {
                            @Override
                            protected void convert(ViewHolder holder, ClassDetailResponse.mAssistant mAssistant, int position) {
                                ImageView mAssistantImg = (ImageView) holder.getConvertView().findViewById(R.id.small_phpto);
                                Glide.with(PurchaseClassDetialActivity.this).load(mAssistant.getHeadImg()).apply(GloableConstant.getInstance().getOptions()).into(mAssistantImg);
                            }
                        });
                    }
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
            }
        });
    }

}
