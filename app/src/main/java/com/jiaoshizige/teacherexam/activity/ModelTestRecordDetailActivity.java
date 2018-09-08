package com.jiaoshizige.teacherexam.activity;


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

import com.jiaoshizige.teacherexam.R;
import com.jiaoshizige.teacherexam.adapter.ViewPagerAdapter;
import com.jiaoshizige.teacherexam.base.BaseActivity;
import com.jiaoshizige.teacherexam.base.GloableConstant;
import com.jiaoshizige.teacherexam.fragment.ModelTestRecordDetailFragment;
import com.jiaoshizige.teacherexam.fragment.TestResultDetailFragment;
import com.jiaoshizige.teacherexam.http.ApiUrl;
import com.jiaoshizige.teacherexam.http.MyCallBack;
import com.jiaoshizige.teacherexam.http.SupportResponse;
import com.jiaoshizige.teacherexam.http.Xutil;
import com.jiaoshizige.teacherexam.model.RecordDetailResponse;
import com.jiaoshizige.teacherexam.utils.SPKeyValuesUtils;
import com.jiaoshizige.teacherexam.utils.SPUtils;
import com.jiaoshizige.teacherexam.utils.ToastUtil;
import com.jiaoshizige.teacherexam.zz.activity.MoniRecordSheetActivity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/8/3.
 */

public class ModelTestRecordDetailActivity extends BaseActivity implements View.OnClickListener {
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
    @BindView(R.id.wrong_pager)
    ViewPager mWrongPager;


    private Context mContext;
    private String id;
    private String name;
    private String user_id;
    private String tiku_id;
    private ViewPagerAdapter mAdapter;
    private List<Fragment> mListFragment;
    private ViewPagerAdapter mWrongAdpter;
    private List<Fragment> mWrongListFragment;
    private ModelTestRecordDetailFragment mFragment;

    private String size;
    private List<RecordDetailResponse.mData> mListData;//错题
    private String tag;
    private List<RecordDetailResponse.mData> mList;//全部
    RecordDetailResponse.mData mModel;
    private String collection;

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
        if (getIntent().getStringExtra("name") != null) {
            name = getIntent().getStringExtra("name");
        } else {
            name = "";
        }
        if (getIntent().getStringExtra("tag") != null) {
            tag = getIntent().getStringExtra("tag");
        } else {
            tag = "";
        }
        setSubTitle().setText("");
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (!size.equals("")) {
                    setToolbarTitle().setText(String.valueOf(position + 1) + "/" + size);
                } else {
                    setToolbarTitle().setText("0/0");
                }
                mModel = mList.get(position);
                if (mModel.getIs_collect().equals("1")) {
                    Drawable drawable = ContextCompat.getDrawable(mContext, R.mipmap.icon_collect_selected);
                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                    mCollectionText.setCompoundDrawables(null, drawable, null, null);
                    collection = "1";
                } else {
                    Drawable drawable = ContextCompat.getDrawable(mContext, R.mipmap.wrong_topic_collection);
                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                    mCollectionText.setCompoundDrawables(null, drawable, null, null);
                    collection = "0";
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
                if (!String.valueOf(mListData.size()).equals("")) {
                    setToolbarTitle().setText(String.valueOf(position + 1) + "/" + mListData.size());
                } else {
                    setToolbarTitle().setText("0/0");
                }
                mModel = mListData.get(position);
                if (mModel.getIs_collect().equals("1")) {
                    Drawable drawable = ContextCompat.getDrawable(mContext, R.mipmap.icon_collect_selected);
                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                    mCollectionText.setCompoundDrawables(null, drawable, null, null);
                    collection = "1";
                } else {
                    Drawable drawable = ContextCompat.getDrawable(mContext, R.mipmap.wrong_topic_collection);
                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                    mCollectionText.setCompoundDrawables(null, drawable, null, null);
                    collection = "0";
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
        Log.d("//////////tag", tag + "------------");


        if (!tiku_id.equals("") && !id.equals("")) {
            getRecordDatail("");
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
                            mListData.get(i).setQid(String.valueOf(i + 1));
                        }
                    }

                    showWrongItem();

                }
                break;
            case R.id.sheet:


                try {
                    if (mListData != null || mList != null) {

                        Intent intent = new Intent();
                        intent.setClass(ModelTestRecordDetailActivity.this, MoniRecordSheetActivity.class);
                        intent.putExtra("name", name);
                        intent.putExtra("flag", "1");
                        if (!tag.equals("")) {
                            intent.putExtra("data", (Serializable) mListData);
                        } else {
                            intent.putExtra("data", (Serializable) mList);
                        }
                        startActivityForResult(intent, 2);
                    }
                } catch (Exception e) {
                }


                break;
            case R.id.collection:
                Log.e("--------------getId", mModel.getId() + "----------");

                if (collection.equals("0")) {
                    getCollection(mModel.getId());
                } else {
                    getCancelCollection(mModel.getId());
                }


                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2) {
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

    private void getRecordDatail(String is_zhenti) {
        Map<String, String> map = new HashMap<>();
        map.put("user_id", user_id);
        map.put("moni_id", id);
        map.put("tiku_id", tiku_id);
        map.put("is_zhenti", is_zhenti);
        GloableConstant.getInstance().startProgressDialog(mContext);
        Xutil.GET(ApiUrl.RECORDDETAIL, map, new MyCallBack<RecordDetailResponse>() {
            @Override
            public void onSuccess(RecordDetailResponse result) {
                super.onSuccess(result);
                GloableConstant.getInstance().stopProgressDialog();
                if (result.getStatus_code().equals("200")) {
                    if (result.getData() != null && result.getData().size() > 0) {
                        mList = result.getData();
                        for (int k = 0; k < mList.size(); k++) {
                            mList.get(k).setQid(String.valueOf(k + 1));
                            if (TextUtils.isEmpty(mList.get(k).getUser_answer())) {
                                //没答题
                                mListData.add(mList.get(k));
                            } else {
                                //答了题
                                if (mList.get(k).getType().equals("1")) {
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

                    }
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
                Log.e("*************ex", ex.getMessage() + "----------");
            }
        });
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
                mFragment = new ModelTestRecordDetailFragment(ModelTestRecordDetailActivity.this, mList.get(i));
                mListFragment.add(mFragment);
            }
            mAdapter = new ViewPagerAdapter(getSupportFragmentManager(), mListFragment);
            mViewPager.setAdapter(mAdapter);
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
                mFragment = new ModelTestRecordDetailFragment(ModelTestRecordDetailActivity.this, mListData.get(i));
                mWrongListFragment.add(mFragment);
            }
            mWrongAdpter = new ViewPagerAdapter(getSupportFragmentManager(), mWrongListFragment);
            mWrongPager.setAdapter(mWrongAdpter);
        } else {
            mWrongPager.setVisibility(View.GONE);
            mNoData.setVisibility(View.VISIBLE);
        }


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
