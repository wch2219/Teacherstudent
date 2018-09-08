package com.jiaoshizige.teacherexam.activity;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jiaoshizige.teacherexam.R;
import com.jiaoshizige.teacherexam.adapter.ViewPagerAdapter;
import com.jiaoshizige.teacherexam.adapter.ViewPagerAdapterStatus;
import com.jiaoshizige.teacherexam.base.BaseActivity;
import com.jiaoshizige.teacherexam.base.GloableConstant;
import com.jiaoshizige.teacherexam.fragment.ErrorTopicDetailFragment;
import com.jiaoshizige.teacherexam.http.ApiUrl;
import com.jiaoshizige.teacherexam.http.MyCallBack;
import com.jiaoshizige.teacherexam.http.SupportResponse;
import com.jiaoshizige.teacherexam.http.Xutil;
import com.jiaoshizige.teacherexam.model.ErrrorTopicDetailResponse;
import com.jiaoshizige.teacherexam.utils.SPKeyValuesUtils;
import com.jiaoshizige.teacherexam.utils.SPUtils;
import com.jiaoshizige.teacherexam.utils.ToastUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/8/1.
 */

public class ErrorTopicDetailActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.view_pager)
    ViewPager mViewPager;
    @BindView(R.id.nodata)
    RelativeLayout mNoData;
    @BindView(R.id.bottom)
    LinearLayout mBottom;
    @BindView(R.id.collection_text)
    TextView mCollectionText;
    @BindView(R.id.collection)
    RelativeLayout mCollection;
    @BindView(R.id.choose_topic)
    RelativeLayout mChooseTopi;
    @BindView(R.id.choose_topic_text)
    TextView mTopicText;

    private Context mContext;
    private String user_id;
    private String id;
    private ErrorTopicDetailFragment mFragment;
    private ViewPagerAdapterStatus mAdapter;
    private List<Fragment> mListFragment;
    private ErrrorTopicDetailResponse.mData mModle;
    private List<ErrrorTopicDetailResponse.mData> mList;
    private String collection;
    private String tiku_id;
    private int a;

    @Override
    protected int getLayoutId() {
        return R.layout.error_topic_detail_layout;
    }

    @Override
    protected void initView() {
        mContext = this;
        mList = new ArrayList<>();
        user_id = (String) SPUtils.getSpValues(SPKeyValuesUtils.SP_USER_ID, SPUtils.TYPE_STRING);
        mListFragment = new ArrayList<>();
        if (getIntent().getStringExtra("id") != null) {
            id = getIntent().getStringExtra("id");
        } else {
            id = "";
        }

        if (GloableConstant.getInstance().getTiku_id() != null) {
            tiku_id = GloableConstant.getInstance().getTiku_id();
        } else {
            tiku_id = "";
        }
        setSubTitle().setText("");
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                mModle = mList.get(position);
                if (!String.valueOf(mList.size()).equals("")) {
                    setToolbarTitle().setText(String.valueOf(position + 1) + "/" + mList.size());
                } else {
                    setToolbarTitle().setText("0/0");
                }
                a = position;
                if (mModle.getIs_collect().equals("0")) {
                    Drawable drawable = ContextCompat.getDrawable(mContext, R.mipmap.wrong_topic_collection);
                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                    mCollectionText.setCompoundDrawables(null, drawable, null, null);
                } else {
                    Drawable drawable = ContextCompat.getDrawable(mContext, R.mipmap.icon_collect_selected);
                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                    mCollectionText.setCompoundDrawables(null, drawable, null, null);
                }

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mCollection.setOnClickListener(this);
        mChooseTopi.setOnClickListener(this);
        if (!id.equals("")) {
            getErrorDetail();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.collection:
                collection = mModle.getIs_collect();
                if (collection.equals("0")) {
                    getCollection(mModle.getId());
                } else {
                    getCancelCollection(mModle.getId());
                }
                break;
            case R.id.choose_topic:
                getRemove(mModle.getId());
                break;
        }
    }

    public void DelFragment() {
        mListFragment.remove(a);
        mList.remove(a);
        mAdapter.notifyDataSetChanged();

    }

    private void getErrorDetail() {
        Map<String, String> map = new HashMap<>();
        map.put("user_id", user_id);
        map.put("guanqia_id", id);
        Xutil.GET(ApiUrl.ERRORTOPICDETAIL, map, new MyCallBack<ErrrorTopicDetailResponse>() {
            @Override
            public void onSuccess(ErrrorTopicDetailResponse result) {
                super.onSuccess(result);
                if (result.getStatus_code().equals("200")) {
                    if (result.getData() != null && result.getData().size() > 0) {
                        mList = result.getData();

                        for (int i = 0; i < result.getData().size(); i++) {
                            mFragment = new ErrorTopicDetailFragment(ErrorTopicDetailActivity.this, result.getData().get(i));
                            mListFragment.add(mFragment);
                        }
                        mAdapter = new ViewPagerAdapterStatus(getSupportFragmentManager(), mListFragment);
                        mViewPager.setAdapter(mAdapter);
                        mAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onFinished() {
                super.onFinished();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
            }
        });
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
                    mModle.setIs_collect("1");
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
                    mAdapter.notifyDataSetChanged();
                    Drawable drawable = ContextCompat.getDrawable(mContext, R.mipmap.wrong_topic_collection);
                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                    mCollectionText.setCompoundDrawables(null, drawable, null, null);
                    mModle.setIs_collect("0");
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
    /*移除试题*/

    private void getRemove(String shiti_id) {
        Map<String, String> map = new HashMap<>();
        map.put("user_id", user_id);
        map.put("shiti_id", shiti_id);
        map.put("guanqia_id", id);
        GloableConstant.getInstance().startProgressDialog(mContext);
        Xutil.GET(ApiUrl.REMOVEQUESTION, map, new MyCallBack<SupportResponse>() {
            @Override
            public void onSuccess(SupportResponse result) {
                super.onSuccess(result);
                GloableConstant.getInstance().stopProgressDialog();
                if (result.getStatus_code().equals("204")) {
                    ToastUtil.showShortToast(result.getMessage());
                    DelFragment();
                }
            }

            @Override
            public void onFinished() {
                super.onFinished();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
            }
        });
    }
}
