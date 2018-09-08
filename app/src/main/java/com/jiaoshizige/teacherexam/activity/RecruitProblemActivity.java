package com.jiaoshizige.teacherexam.activity;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.google.android.gms.common.api.Api;
import com.jiaoshizige.teacherexam.R;
import com.jiaoshizige.teacherexam.adapter.ViewPagerAdapter;
import com.jiaoshizige.teacherexam.base.GloableConstant;
import com.jiaoshizige.teacherexam.fragment.RecruitProblemFragment;
import com.jiaoshizige.teacherexam.http.ApiUrl;
import com.jiaoshizige.teacherexam.http.MyCallBack;
import com.jiaoshizige.teacherexam.http.Xutil;

import com.jiaoshizige.teacherexam.model.MessageEvent;
import com.jiaoshizige.teacherexam.model.RecruitPostAnswerResponse;
import com.jiaoshizige.teacherexam.model.RecruitProblemResponse;
import com.jiaoshizige.teacherexam.utils.SPKeyValuesUtils;
import com.jiaoshizige.teacherexam.utils.SPUtils;
import com.jiaoshizige.teacherexam.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/7/24.
 */
//闯关习题
public class RecruitProblemActivity extends AppCompatActivity {
    private ViewPager mViewpager;
    private List<Fragment> mFragmentList = new ArrayList<>();
    private RecruitProblemFragment mFragment;
    private FragmentManager mManager;
    private ViewPagerAdapter mAdapter;
    private FragmentSkipInterface mFragmentSkipInterface;
    private String id;
    private String tag;
    private String user_id;
    private Context mContext;
    private TextView mNumber;
    private ImageView mSuspended;
    private int num = 0;
    private String name;

    private List<RecruitProblemResponse.mData> mList;

    LinkedHashMap<String, String> map = new LinkedHashMap<>();
    String json;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        mList = new ArrayList<>();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        setContentView(R.layout.recruit_problem_layout);
        GloableConstant.getInstance().setCorrectNum(null);
        mViewpager = (ViewPager) findViewById(R.id.viewpager);
        mNumber = (TextView) findViewById(R.id.number);
        mSuspended = (ImageView) findViewById(R.id.suspended);
        mNumber.setText("0");
        user_id = (String) SPUtils.getSpValues(SPKeyValuesUtils.SP_USER_ID, SPUtils.TYPE_STRING);
        if (getIntent().getStringExtra("id") != null) {
            id = getIntent().getStringExtra("id");
        } else {
            id = "";
        }
        if (getIntent().getStringExtra("tag") != null) {
            tag = getIntent().getStringExtra("tag");
        } else {
            tag = "";
        }
        Log.d("************name", tag + "--------------");
        if (getIntent().getStringExtra("name") != null) {
            name = getIntent().getStringExtra("name");
        } else {
            name = "";
        }
        mSuspended.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("************name", name + "--------------");
                if (!name.equals("")) {
                    Intent intent = new Intent();
                    intent.setClass(RecruitProblemActivity.this, SuspendedActivity.class);
                    intent.putExtra("name", name);
                    startActivityForResult(intent, 1);
//                    startActivity(intent);
                }

            }
        });
        mViewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Log.e("AAAAAAAAAdddddddd", "********position" + position);


            }

            @Override
            public void onPageSelected(int position) {
                Log.e("AAAAAAAAABBBBBBBB", "********position" + GloableConstant.getInstance().getCorrectNum());
//                if (GloableConstant.getInstance().getCorrectNum() != null) {
//                    mNumber.setText(GloableConstant.getInstance().getCorrectNum());
//                } else {
//                    mNumber.setText("0");
//                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                Log.e("AAAAAAAAACCCCC", "********state" + GloableConstant.getInstance().getCorrectNum());
            }
        });
        getRecruitProblem(tag);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            switch (resultCode) {
                case 1:
                    tag = "1";
                    refresh();
                    GloableConstant.getInstance().setCorrectNum(null);
                    getRecruitProblem(tag);
                    break;
                case 2:
                    finish();
                    break;
                case 3:
                    break;
                default:
                    break;
            }
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        Log.e("+++++++++++++++", "zhixiong" + event.getMessage());
        if (event.getMessage().equals("ZUIHOU")) {
            Log.e("+++++++++++++++", "zhixiong" + GloableConstant.getInstance().getCorrectNum());
            if (GloableConstant.getInstance().getCorrectNum() != null) {
                Log.e("+++++++++++++++", "ZUIHOU" + GloableConstant.getInstance().getCorrectNum());
                mNumber.setText(GloableConstant.getInstance().getCorrectNum());
            } else {
                mNumber.setText("0");
            }
        } else if (event.getMessage().equals("完成")) {
//            for (int i = 0; i < mList.size(); i++) {//外循环是循环的次数
//                for (int j = mList.size() - 1; j > i; j--) {//内循环是 外循环一次比较的次数
//
//                    if (mList.get(i).getId() == mList.get(j).getId()) {
//                        mList.remove(j);
//                    }
//
//                }
//            }
            for (int i = 0; i < mList.size(); i++) {
                map.put(mList.get(i).getId(), mList.get(i).getUser_answer());
            }
            if (!map.isEmpty()) {
                json = JSON.toJSONString(map);
            } else {
                json = "";
            }

            if (!id.equals("") && !json.equals("")) {
                getAnswer();
            }
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().isRegistered(this);
    }

    public void refresh() {
        finish();
        Intent intent = new Intent(RecruitProblemActivity.this, RecruitProblemActivity.class);
        intent.putExtra("id", id);
        intent.putExtra("name", name);
        startActivity(intent);

    }

    private void getRecruitProblem(String is_replay) {
        Map<String, String> map = new HashMap<>();
        map.put("user_id", user_id);
        map.put("guanqia_id", id);
//        map.put("guanqia_id", "75");
        map.put("is_replay", is_replay);
        Log.d("*************map", map.toString());
        GloableConstant.getInstance().startProgressDialog(mContext);
        Xutil.GET(ApiUrl.RECRUITPROBLEM, map, new MyCallBack<RecruitProblemResponse>() {
            @Override
            public void onSuccess(RecruitProblemResponse result) {
                super.onSuccess(result);
                GloableConstant.getInstance().stopProgressDialog();
                if (result.getStatus_code().equals("200")) {
                    if (result.getData() != null && result.getData().size() > 0) {
                        mList = result.getData();
                        for (int i = 0; i < result.getData().size(); i++) {
                            mFragment = new RecruitProblemFragment(RecruitProblemActivity.this, result.getData().get(i), i, String.valueOf(result.getData().size()));
                            mFragmentList.add(mFragment);
                        }
                        mManager = getSupportFragmentManager();
                        mAdapter = new ViewPagerAdapter(mManager, mFragmentList);
                        mViewpager.setAdapter(mAdapter);
                        // mAdapter.notifyDataSetChanged();
                    } else {
                        ToastUtil.showShortToast("暂无数据");
                    }

                    Log.d("***************", result.getData().get(0).getMetas().get(0).getChoose().getText() + "++++++++++++++");
                }
            }

            @Override
            public void onFinished() {
                super.onFinished();
                GloableConstant.getInstance().stopProgressDialog();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
                Log.d("--------------", ex.getMessage() + "-------------");
                GloableConstant.getInstance().stopProgressDialog();
            }
        });
    }


    /*闯关提交试题*/
    private void getAnswer() {
        Map<String, String> map = new HashMap<>();
        map.put("user_id", user_id);
        map.put("shiti_id", json);
        map.put("guanqia_id", id);
        Log.d("*******map", map.toString());
        GloableConstant.getInstance().startProgressDialog(mContext);
        Xutil.GET(ApiUrl.POSTANSWERT, map, new MyCallBack<RecruitPostAnswerResponse>() {
            @Override
            public void onSuccess(RecruitPostAnswerResponse result) {
                super.onSuccess(result);
                GloableConstant.getInstance().stopProgressDialog();
                if (result.getStatus_code().equals("200")) {

                    Intent intent = new Intent();
                    intent.setClass(RecruitProblemActivity.this, ConfirmResultActivity.class);
                    intent.putExtra("data", result.getData());
                    intent.putExtra("id", id);
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onFinished() {
                super.onFinished();
                GloableConstant.getInstance().stopProgressDialog();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
                GloableConstant.getInstance().stopProgressDialog();
                Log.d("***********ex", ex.getMessage() + "-----------");
            }
        });

    }

    public void setFragmentSkipInterface(FragmentSkipInterface fragmentSkipInterface) {
        mFragmentSkipInterface = fragmentSkipInterface;
    }

    /**
     * Fragment跳转
     */
    public void skipToFragment() {
        if (mFragmentSkipInterface != null) {
            mFragmentSkipInterface.gotoFragment(mViewpager);
        }
    }

    public interface FragmentSkipInterface {
        /**
         * ViewPager中子Fragment之间跳转的实现方法
         */
        void gotoFragment(ViewPager viewPager);
    }


    private static final String URL = "url";

    public static void start(Context context, String id) {
        Intent intent = new Intent(context, RecruitProblemActivity.class);
//        intent.putExtra(URL, id);
        context.startActivity(intent);
    }

    public void sumitData() {
        for (int i = 0; i < mList.size(); i++) {
            map.put(mList.get(i).getId(), mList.get(i).getUser_answer());
        }
        if (!map.isEmpty()) {
            json = JSON.toJSONString(map);
        } else {
            json = "";
        }

        if (!id.equals("") && !json.equals("")) {
            getAnswer();
        }
    }
}
