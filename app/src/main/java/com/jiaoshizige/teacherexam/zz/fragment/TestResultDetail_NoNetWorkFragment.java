package com.jiaoshizige.teacherexam.zz.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.jiaoshizige.teacherexam.R;
import com.jiaoshizige.teacherexam.activity.ErrorCorrectActivity;
import com.jiaoshizige.teacherexam.activity.TestResultDetailActivity;
import com.jiaoshizige.teacherexam.adapter.ErPracticeResultDetailAdapter;
import com.jiaoshizige.teacherexam.base.MBaseFragment;
import com.jiaoshizige.teacherexam.model.TestResultDetailResponse;
import com.jiaoshizige.teacherexam.utils.NoScrollListView;
import com.jiaoshizige.teacherexam.zz.activity.TestResultDetail_NonetWorkActivity;
import com.jiaoshizige.teacherexam.zz.database.ZhenTi;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/8/10.
 */

public class TestResultDetail_NoNetWorkFragment extends MBaseFragment {
    @BindView(R.id.title)
    WebView mTitle;
    @BindView(R.id.list_view)
    NoScrollListView mListView;
    @BindView(R.id.correct_options)
    TextView mCorrectOptions;
    @BindView(R.id.answer_result)
    TextView mResult;
    @BindView(R.id.answer_number)
    TextView mNumber;
    @BindView(R.id.easy_wrong_option)
    TextView mEasyWrong;
    @BindView(R.id.parse)
    WebView mParse;
    @BindView(R.id.exam_site)
    TextView mSite;
    @BindView(R.id.error_correct)
    TextView mErrorCorrect;

    @BindView(R.id.subjective)
    LinearLayout mSubjective;
    @BindView(R.id.answer_refer_content)
    WebView mAnswerRefer;
    @BindView(R.id.pfbz_content)
    WebView mPfbz;
    @BindView(R.id.choose)
    LinearLayout mChoose;
    @BindView(R.id.user_score)
    TextView myscore;

    private ZhenTi mData;
    TestResultDetail_NonetWorkActivity mActivity;
    private ErPracticeResultDetailAdapter mAdapter;
    private String[] letter = {"A", "B", "C", "D"};

    @SuppressLint("ValidFragment")
    public TestResultDetail_NoNetWorkFragment(TestResultDetail_NonetWorkActivity testResultDetailActivity, ZhenTi mData) {
        this.mActivity = testResultDetailActivity;
        this.mData = mData;
        Log.d("//////////////data", mData.getId() + "------------");
    }

    public TestResultDetail_NoNetWorkFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.my_practice_note_detail_fragment_layout, container, false);
    }

    @Override
    protected void onInitViews(Bundle savedInstanceState) {
        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(getActivity()));
        if (mData != null) {

            if (mData.getType() == 1) {
                mSubjective.setVisibility(View.GONE);
                mChoose.setVisibility(View.VISIBLE);
                mAdapter = new ErPracticeResultDetailAdapter(getActivity(), mData.getAnswer(), mData.getUser_answer());
                mListView.setAdapter(mAdapter);
                List<TestResultDetailResponse.mMetas> metas = JSON.parseArray(mData.getMetas(), TestResultDetailResponse.mMetas.class);
                mAdapter.setmList(metas);
                for (int i = 0; i < letter.length; i++) {
                    if (mData.getAnswer().equals(letter[i])) {
                        mAdapter.setSelection(i);
                        mAdapter.notifyDataSetChanged();
                        Log.d("----------user_answer", mData.getAnswer() + "--****" + i);
                    }
                }


//              WebSettings webSettings = mTitle.getSettings();
//              webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
//              webSettings.setJavaScriptEnabled(true);
//              mTitle.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
//              mTitle.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
//              webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK); //设置缓存
//              webSettings.setDomStorageEnabled(true);
//              webSettings.setLoadWithOverviewMode(true);
//              webSettings.setSupportZoom(true);
//              String title = mData.getStem();
//              mTitle.loadDataWithBaseURL(null, title, "text/html", "utf-8", null);
                mCorrectOptions.setText(mData.getAnswer());
                if (!mData.getUser_answer().equals("") && mData.getUser_answer() != null) {
                    if (mData.getUser_answer().equals(mData.getAnswer())) {
                        mResult.setText("回答正确");
                        mResult.setTextColor(ContextCompat.getColor(getActivity(), R.color.submitbtn_color));
                    } else {
                        mResult.setText("回答错误");
                        mResult.setTextColor(ContextCompat.getColor(getActivity(), R.color.red2));

                    }
                } else {
                    mResult.setText("您未作答");
                    mResult.setTextColor(ContextCompat.getColor(getActivity(), R.color.red2));
                }
                mNumber.setText("本题共被答 " + mData.getTotle_post() + " 次 , 正确率: " + mData.getSuccess() + " %");
                mEasyWrong.setText(mData.getEasy_error());

            } else {
                if (!mData.getUser_answer().equals("") && mData.getUser_answer() != null) {
                    myscore.setText("您得了" + mData.getUser_answer() + "分");
                    myscore.setTextColor(ContextCompat.getColor(getActivity(), R.color.text_color3));
                } else {
                    myscore.setText("您未作答");
                    myscore.setTextColor(ContextCompat.getColor(getActivity(), R.color.text_color3));
                }
                mSubjective.setVisibility(View.VISIBLE);
                mChoose.setVisibility(View.GONE);
                WebSettings settingpfbz = mPfbz.getSettings();
                settingpfbz.setCacheMode(WebSettings.LOAD_NO_CACHE);
                settingpfbz.setJavaScriptEnabled(true);
                mPfbz.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
                mPfbz.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
                settingpfbz.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK); //设置缓存
                settingpfbz.setDomStorageEnabled(true);
                settingpfbz.setLoadWithOverviewMode(true);
                settingpfbz.setSupportZoom(true);
                String pfbz = mData.getPfbz();
                mPfbz.loadDataWithBaseURL(null, pfbz, "text/html", "utf-8", null);
                WebSettings settings = mAnswerRefer.getSettings();
                settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
                settings.setJavaScriptEnabled(true);
                mAnswerRefer.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
                mAnswerRefer.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
                settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK); //设置缓存
                settings.setDomStorageEnabled(true);
                settings.setLoadWithOverviewMode(true);
                settings.setSupportZoom(true);
                String parse = mData.getAnalysis();
                mAnswerRefer.loadDataWithBaseURL(null, parse, "text/html", "utf-8", null);
            }
            WebSettings webSettings = mTitle.getSettings();
            webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
            webSettings.setJavaScriptEnabled(true);
            mTitle.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
            mTitle.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
            webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK); //设置缓存
            webSettings.setDomStorageEnabled(true);
            webSettings.setLoadWithOverviewMode(true);
            webSettings.setSupportZoom(true);
            String title = mData.getStem();
            mTitle.loadDataWithBaseURL(null, title, "text/html", "utf-8", null);

            WebSettings settings = mParse.getSettings();
            settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
            settings.setJavaScriptEnabled(true);
            mParse.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
            mParse.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
            settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK); //设置缓存
            settings.setDomStorageEnabled(true);
            settings.setLoadWithOverviewMode(true);
            settings.setSupportZoom(true);
            String parse = mData.getAnalysis();
            mParse.loadDataWithBaseURL(null, parse, "text/html", "utf-8", null);
            mSite.setText(mData.getKaodian_name());

            mErrorCorrect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setClass(getActivity(), ErrorCorrectActivity.class);
                    intent.putExtra("id", mData.getId());
                    startActivity(intent);
                }
            });
        }
    }
}
