package com.jiaoshizige.teacherexam.zz.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.jiaoshizige.teacherexam.R;
import com.jiaoshizige.teacherexam.adapter.ViewPagerAdapter;
import com.jiaoshizige.teacherexam.base.BaseActivity;
import com.jiaoshizige.teacherexam.base.GloableConstant;
import com.jiaoshizige.teacherexam.http.ApiUrl;
import com.jiaoshizige.teacherexam.http.MyCallBack;
import com.jiaoshizige.teacherexam.http.SupportResponse;
import com.jiaoshizige.teacherexam.http.Xutil;
import com.jiaoshizige.teacherexam.utils.SPKeyValuesUtils;
import com.jiaoshizige.teacherexam.utils.SPUtils;
import com.jiaoshizige.teacherexam.utils.ToastUtil;
import com.jiaoshizige.teacherexam.zz.database.UserAnswer;
import com.jiaoshizige.teacherexam.zz.database.UserAnswerDao;
import com.jiaoshizige.teacherexam.zz.database.ZhenTi;
import com.jiaoshizige.teacherexam.zz.database.ZhenTiDao;
import com.jiaoshizige.teacherexam.zz.fragment.TestResultDetail_NoNetWorkFragment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/8/10.
 */

public class TestResultDetail_NonetWorkActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.view_pager)
    ViewPager mViewPager;
    @BindView(R.id.wrong_topic)
    RelativeLayout mWrong;
    @BindView(R.id.sheet)
    RelativeLayout mSheet;
    @BindView(R.id.collection)
    RelativeLayout mCollection;
    @BindView(R.id.nodata)
    RelativeLayout mNoData;
    @BindView(R.id.wrong_topic_text)
    TextView mWrongText;
    @BindView(R.id.collection_text)
    TextView mCollectionText;
    @BindView(R.id.bottom)
    LinearLayout mBottom;
    @BindView(R.id.wrong_pager)
    ViewPager mWrongPager;

    private String name;
    private String tiku_id;
    private ViewPagerAdapter mAdpter;
    private List<Fragment> mListFragment;
    private ViewPagerAdapter mWrongAdpter;
    private List<Fragment> mWrongListFragment;
    private String user_id;
    private String id;
    private Context mContext;
    private String size;
    private String tag;//""是全部题目，1是错题
    private String is_zhenti;
    private String collection;

    private List<ZhenTi> mListData;//错题
    private List<ZhenTi> mList;//全部题目
    private List<UserAnswer> userAnswerList;
    //private ZhenTi mModel;

    private TestResultDetail_NoNetWorkFragment mFragment;
    private String result;

    @Override
    protected int getLayoutId() {
        return R.layout.my_practice_note_detail_layout;
    }

    @Override
    protected void initView() {
        mContext = this;
        mListData = new ArrayList<>();
        mList = new ArrayList<>();
        mListFragment = new ArrayList<>();
        mWrongListFragment = new ArrayList<>();

        user_id = (String) SPUtils.getSpValues(SPKeyValuesUtils.SP_USER_ID, SPUtils.TYPE_STRING);
        if (GloableConstant.getInstance().getTiku_id() != null) {
            tiku_id = GloableConstant.getInstance().getTiku_id();
        } else {
            tiku_id = "";
        }
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

        if (getIntent().getStringExtra("result") != null) {
            result = getIntent().getStringExtra("result");
        } else {
            result = "";
        }

        if (getIntent().getStringExtra("name") != null) {
            name = getIntent().getStringExtra("name");
        } else {
            name = "";
        }
        if (getIntent().getStringExtra("is_zhenti") != null) {
            is_zhenti = getIntent().getStringExtra("is_zhenti");
        } else {
            is_zhenti = "";
        }
        setSubTitle().setText("");

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (mList.size() > 0) {
                    setToolbarTitle().setText(String.valueOf(position + 1) + "/" + mList.size());
                } else {
                    setToolbarTitle().setText("0/0");
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mWrongPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                if (mListData.size() > 0) {
                    setToolbarTitle().setText(String.valueOf(position + 1) + "/" + mListData.size());
                } else {
                    setToolbarTitle().setText("0/0");
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mWrong.setOnClickListener(this);
        mSheet.setOnClickListener(this);
        mCollection.setOnClickListener(this);

        if (!id.equals("")) {
            if (!id.equals("") && !is_zhenti.equals("")) {
                getLocalData();
            }
        }


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.wrong_topic:
                if (tag.equals("1")) {
                    tag = "";
                    Drawable drawable = ContextCompat.getDrawable(mContext, R.mipmap.wrong_topic);
                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                    mWrongText.setCompoundDrawables(null, drawable, null, null);
                    mWrongText.setText("全部题目");
                    showItem();
                } else {
                    tag = "1";
                    Drawable drawable = ContextCompat.getDrawable(mContext, R.mipmap.icon_mistakes_selected);
                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                    mWrongText.setCompoundDrawables(null, drawable, null, null);
                    mWrongText.setText("只看错题");

                    if (mListData.size() > 0) {
                        //重新排列qid
                        for (int i = 0; i < mListData.size(); i++) {
                            mListData.get(i).setQid(i + 1);
                        }
                    }

                    showWrongItem();


                }

                break;
            case R.id.sheet:
                String time;
                if (userAnswerList.size() > 0) {
                    time = userAnswerList.get(0).getTime();
                } else {
                    time = " 00 分钟 00 秒";
                }
                Intent intent = new Intent();
                intent.setClass(TestResultDetail_NonetWorkActivity.this, ZhenTiSheetLook_NoNetworkActivity.class);
                intent.putExtra("name", name);
                intent.putExtra("zhenti_id", id);
                intent.putExtra("hour", time);
                if (tag.equals("1")) {
                    intent.putExtra("data", (Serializable) mListData);
                } else {
                    intent.putExtra("data", (Serializable) mList);
                }
                startActivityForResult(intent, 3);
                break;
            case R.id.collection:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 3) {
            if (resultCode == RESULT_OK) {
                String result = data.getExtras().getString("result");//得到新Activity 关闭后返回的数据
                if (!TextUtils.isEmpty(result)) {
                    if (tag.equals("1")) {
                        mWrongPager.setCurrentItem(Integer.parseInt(result));
                    } else {
                        mViewPager.setCurrentItem(Integer.parseInt(result));
                    }


                }

            }
        }
    }


    /*收藏*/
    private void getCollection(String shiti_id) {
        Map<String, String> map = new HashMap<>();
        map.put("user_id", user_id);
        map.put("tiku_id", tiku_id);
        map.put("shiti_id", shiti_id);
        Log.d("*************map", map.toString());
        GloableConstant.getInstance().startProgressDialog(mContext);
        Xutil.GET(ApiUrl.SHITICOLLECTION, map, new MyCallBack<SupportResponse>() {
            @Override
            public void onSuccess(SupportResponse result) {
                super.onSuccess(result);
                GloableConstant.getInstance().stopProgressDialog();
                if (result.getStatus_code().equals("204")) {
                    ToastUtil.showShortToast(result.getMessage());
                    Drawable drawable = ContextCompat.getDrawable(mContext, R.mipmap.icon_collect_selected);
                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                    mCollectionText.setCompoundDrawables(null, drawable, null, null);
                    collection = "1";
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
                Log.d("----------------", ex.getMessage() + "-----------");
            }
        });
    }

    /*取消收藏*/
    private void getCancelCollection(String shiti_id) {
        Map<String, String> map = new HashMap<>();
        map.put("user_id", user_id);
        map.put("shiti_id", shiti_id);
        GloableConstant.getInstance().startProgressDialog(mContext);
        Xutil.GET(ApiUrl.SHITICANCELCOLLECTION, map, new MyCallBack<SupportResponse>() {
            @Override
            public void onSuccess(SupportResponse result) {
                super.onSuccess(result);
                if (result.getStatus_code().equals("204")) {
                    ToastUtil.showShortToast(result.getMessage());
                    Drawable drawable = ContextCompat.getDrawable(mContext, R.mipmap.wrong_topic_collection);
                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                    mCollectionText.setCompoundDrawables(null, drawable, null, null);
                    collection = "0";
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
            }

            @Override
            public void onFinished() {
                super.onFinished();
            }
        });
    }

    private void getLocalData() {
        //题目
        mList = new ZhenTiDao(TestResultDetail_NonetWorkActivity.this).orderBy("qid", true, "zhenti_id", id, "tiku_id", tiku_id);
        //答案
        userAnswerList = new UserAnswerDao(TestResultDetail_NonetWorkActivity.this).queryByColumnNames("userid", user_id, "ztid", id, "tiku_id", tiku_id);

        if (mList.size() > 0 && userAnswerList.size() > 0) {
            String useranswer = userAnswerList.get(0).getData();

            List<String> answers = getValue(useranswer);
            for (int i = 0; i < answers.size(); i++) {
                mList.get(i).setUser_answer(answers.get(i));
            }

            for (int k = 0; k < mList.size(); k++) {
                if (TextUtils.isEmpty(mList.get(k).getUser_answer())) {
                    //没答题
                    mList.get(k).setIs_done("0");
                    mListData.add(mList.get(k));
                } else {
                    //答了题
                    mList.get(k).setIs_done("1");
                    if (mList.get(k).getType() == 1) {
                        //答案错误, 主观题答了就算正确
                        if (!mList.get(k).getAnswer().equals(mList.get(k).getUser_answer())) {
                            mListData.add(mList.get(k));
                        }
                    } else {
                        if (Integer.parseInt(mList.get(k).getUser_answer()) == 0) {
                            mListData.add(mList.get(k));
                        }
                    }
                }
            }

            showItem();


        } else

        {
            mBottom.setVisibility(View.GONE);
            mViewPager.setVisibility(View.GONE);
            mNoData.setVisibility(View.VISIBLE);
        }

    }


    private void showItem() {
        mViewPager.setVisibility(View.VISIBLE);
        mWrongPager.setVisibility(View.GONE);
        mListFragment = new ArrayList<>();
        if (mList.size() > 0) {
            mNoData.setVisibility(View.GONE);

            size = String.valueOf(mList.size());
            setToolbarTitle().setText("1/" + size);
            for (int i = 0; i < mList.size(); i++) {
                mFragment = new TestResultDetail_NoNetWorkFragment(TestResultDetail_NonetWorkActivity.this, mList.get(i));
                mListFragment.add(mFragment);
            }
            mAdpter = new ViewPagerAdapter(getSupportFragmentManager(), mListFragment);
            mViewPager.setAdapter(mAdpter);
        } else {
            mViewPager.setVisibility(View.GONE);
            mNoData.setVisibility(View.VISIBLE);
        }

    }


    private void showWrongItem() {
        mViewPager.setVisibility(View.GONE);
        mWrongPager.setVisibility(View.VISIBLE);
        mWrongListFragment = new ArrayList<>();
        if (mListData.size() > 0) {
            mNoData.setVisibility(View.GONE);

            size = String.valueOf(mListData.size());
            setToolbarTitle().setText("1/" + size);
            for (int i = 0; i < mListData.size(); i++) {
                mFragment = new TestResultDetail_NoNetWorkFragment(TestResultDetail_NonetWorkActivity.this, mListData.get(i));
                mWrongListFragment.add(mFragment);
            }
            mWrongAdpter = new ViewPagerAdapter(getSupportFragmentManager(), mWrongListFragment);
            mWrongPager.setAdapter(mWrongAdpter);
        } else {
            mWrongPager.setVisibility(View.GONE);
            mNoData.setVisibility(View.VISIBLE);
        }


    }


    private List<String> getValue(String json) {
        List<String> answer = new ArrayList<>();
        try {
            LinkedHashMap map = JSON.parseObject(json, LinkedHashMap.class);
            if (map.size() > 0) {
                for (Object obj : map.keySet()) {
                    String value = String.valueOf(map.get(obj));
                    String[] str = value.split("#");
                    if (str[0] != null) {
                        answer.add(str[0]);
                    } else {
                        answer.add("");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return answer;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }


}
