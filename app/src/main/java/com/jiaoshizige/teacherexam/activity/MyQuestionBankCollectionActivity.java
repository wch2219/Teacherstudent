package com.jiaoshizige.teacherexam.activity;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.jiaoshizige.teacherexam.R;
import com.jiaoshizige.teacherexam.adapter.ViewPagerAdapter;
import com.jiaoshizige.teacherexam.base.BaseActivity;
import com.jiaoshizige.teacherexam.base.GloableConstant;
import com.jiaoshizige.teacherexam.fragment.MyQuestiobBankCollectionFragment;
import com.jiaoshizige.teacherexam.http.ApiUrl;
import com.jiaoshizige.teacherexam.http.MyCallBack;
import com.jiaoshizige.teacherexam.http.SupportResponse;
import com.jiaoshizige.teacherexam.http.Xutil;
import com.jiaoshizige.teacherexam.model.MyQuestionBankCollectionResponse;
import com.jiaoshizige.teacherexam.model.PracticeRecordsResponse;
import com.jiaoshizige.teacherexam.utils.SPKeyValuesUtils;
import com.jiaoshizige.teacherexam.utils.SPUtils;
import com.jiaoshizige.teacherexam.utils.ToastUtil;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/7/31.
 */

public class MyQuestionBankCollectionActivity extends BaseActivity implements View.OnClickListener {
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
    private ViewPagerAdapter mAdapter;
    private String tiku_id;
    private String user_id;
    private List<Fragment> mListFragment;
    private MyQuestiobBankCollectionFragment mFragment;
    private MyQuestionBankCollectionResponse.mData mModel;
    private List<MyQuestionBankCollectionResponse.mData> mList;

    private int posi;

    @Override
    protected int getLayoutId() {
        return R.layout.my_question_bank_collection_layout;
    }

    @Override
    protected void initView() {
        setToolbarTitle().setText("0/0");
        mContext = this;
        mListFragment = new ArrayList<>();
        mList = new ArrayList<>();
        user_id = (String) SPUtils.getSpValues(SPKeyValuesUtils.SP_USER_ID, SPUtils.TYPE_STRING);
        if (GloableConstant.getInstance().getTiku_id() != null) {
            tiku_id = GloableConstant.getInstance().getTiku_id();
        } else {
            tiku_id = "";
        }
        setSubTitle().setText("");
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                if (!String.valueOf(mList.size()).equals("")) {
                    setToolbarTitle().setText(String.valueOf(position + 1) + "/" + mList.size());
                } else {
                    setToolbarTitle().setText("0/0");
                }
                posi = position;
                mModel = mList.get(position);
                if (mModel.getIs_collection().equals("1")) {
                    Drawable drawable = ContextCompat.getDrawable(mContext, R.mipmap.icon_collect_selected);
                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                    mCollectionText.setCompoundDrawables(null, drawable, null, null);
                } else {
                    Drawable drawable = ContextCompat.getDrawable(mContext, R.mipmap.wrong_topic_collection);
                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                    mCollectionText.setCompoundDrawables(null, drawable, null, null);
                }


                if (popupWindow != null) {
                    if (popupWindow.isShowing()) {
                        setSeekbar(mViewPager.getCurrentItem() + 1);
                    }
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
        if (!tiku_id.equals("")) {
            getCollection();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.collection:
                if (mModel.getIs_collection().equals("1")) {
                    getCancelCollection(mModel.getId());
                } else {
                    getCollection(mModel.getId());
                }
                break;
            case R.id.choose_topic:
                if (popupWindow != null) {
                    if (!popupWindow.isShowing()) {
                        if (mList != null) {
                            if (mList.size() > 0) {
                                Drawable drawable = ContextCompat.getDrawable(mContext, R.mipmap.icon_topic_selected);
                                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                                mTopicText.setCompoundDrawables(null, drawable, null, null);

                                showPopupWindow(mList.size(), mViewPager.getCurrentItem() + 1);
                            }
                        }

                    } else {
                        popupWindow.dismiss();
                        Drawable drawable = ContextCompat.getDrawable(mContext, R.mipmap.icon_topic_default);
                        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                        mTopicText.setCompoundDrawables(null, drawable, null, null);
                    }
                } else {
                    if (mList != null) {
                        if (mList.size() > 0) {
                            Drawable drawable = ContextCompat.getDrawable(mContext, R.mipmap.icon_topic_selected);
                            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                            mTopicText.setCompoundDrawables(null, drawable, null, null);

                            showPopupWindow(mList.size(), mViewPager.getCurrentItem() + 1);
                        }
                    }
                }

                break;
        }
    }

    private void getCollection() {
        Map<String, String> map = new HashMap<>();
        map.put("user_id", user_id);
        map.put("tiku_id", tiku_id);
        GloableConstant.getInstance().startProgressDialog(mContext);
        Log.d("*****************map", map.toString());
        Xutil.GET(ApiUrl.TIKUCOLLECTIONLIST, map, new MyCallBack<MyQuestionBankCollectionResponse>() {
            @Override
            public void onSuccess(MyQuestionBankCollectionResponse result) {
                super.onSuccess(result);
                GloableConstant.getInstance().stopProgressDialog();
                if (result.getStatus_code().equals("200")) {
                    mList = result.getData();
                    mNoData.setVisibility(View.GONE);
                    mViewPager.setVisibility(View.VISIBLE);
                    mBottom.setVisibility(View.VISIBLE);
                    Log.d("------------mList", mList.size() + "-----------");
                    if (result.getData() != null && result.getData().size() > 0) {
                        for (int i = 0; i < result.getData().size(); i++) {
                            mFragment = new MyQuestiobBankCollectionFragment(MyQuestionBankCollectionActivity.this, result.getData().get(i));
                            mListFragment.add(mFragment);
                        }
                        mAdapter = new ViewPagerAdapter(getSupportFragmentManager(), mListFragment);
                        mViewPager.setAdapter(mAdapter);
                        mAdapter.notifyDataSetChanged();
                    }
                } else {
                    setToolbarTitle().setText("0/0");
                    mNoData.setVisibility(View.VISIBLE);
                    mViewPager.setVisibility(View.GONE);
                    mBottom.setVisibility(View.GONE);
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
                GloableConstant.getInstance().stopProgressDialog();
                setToolbarTitle().setText("0/0");
                mNoData.setVisibility(View.VISIBLE);
                mViewPager.setVisibility(View.GONE);
                mBottom.setVisibility(View.GONE);
            }

            @Override
            public void onFinished() {
                super.onFinished();
                GloableConstant.getInstance().stopProgressDialog();
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
                    mModel.setIs_collection("1");
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
                    mModel.setIs_collection("0");
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


    public void DelFragment() {
        mListFragment.remove(posi);
        mList.remove(posi);
        mAdapter.notifyDataSetChanged();

    }


    private int oneprogressWidth = 0;
    private PopupWindow popupWindow;
    private SeekBar mSeekBar;
    private TextView tvDrag;

    private void showPopupWindow(final int num, final int progress) {

        View view = LayoutInflater.from(MyQuestionBankCollectionActivity.this).inflate(R.layout.seekbar, null);
        mSeekBar = view.findViewById(R.id.seekbar);
        tvDrag = view.findViewById(R.id.tv_drag);

        popupWindow = new PopupWindow(MyQuestionBankCollectionActivity.this);
        popupWindow.setContentView(view);
        popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#55000000")));
        popupWindow.setOutsideTouchable(false);
        popupWindow.setTouchable(true);
        //获取bottom 屏幕位置
        int[] location = new int[2];
        mBottom.getLocationOnScreen(location);
        //获取popup高度
        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        int popupHeight = view.getMeasuredHeight();
        popupWindow.showAtLocation(mChooseTopi, 0, 0, location[1] - popupHeight);

        mSeekBar.setMax(num);
        mSeekBar.post(new Runnable() {
            @Override
            public void run() {
               /* getWidth()最终显示的大小
                getMeasuredWidth()获取的是view原始的大小，也就是这个view在XML文件中配置或者是代码中设置的大小*/
                oneprogressWidth = div(mSeekBar.getWidth(), num, 0);
                setSeekbar(progress);
            }
        });


        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int pro, boolean b) {
                if (pro > 1) {
                    setSeekbar(pro);
                } else {
                    seekBar.setProgress(1);
                }

                mViewPager.setCurrentItem(pro - 1);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


    }

    private void setSeekbar(int progress) {
        mSeekBar.setProgress(progress);
        tvDrag.setText(progress + "");
        int tvdragWidth = tvDrag.getWidth();
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) tvDrag.getLayoutParams();
        int marginLeft = (int) ((progress - 0.5) * oneprogressWidth - tvdragWidth / 2);
        params.setMargins(marginLeft, 0, 0, 0);
        tvDrag.setLayoutParams(params);

    }

    private int div(double v1, double v2, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException(
                    "The scale must be a positive integer or zero");
        }
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).intValue();
    }


    /**
     * dp转换成px
     */
    private int dp2px(float dpValue) {
        float scale = getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * px转换成dp
     */
    private int px2dp(float pxValue) {
        float scale = getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

}
