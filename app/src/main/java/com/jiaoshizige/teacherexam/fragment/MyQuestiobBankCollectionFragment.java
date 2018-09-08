package com.jiaoshizige.teacherexam.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jiaoshizige.teacherexam.R;
import com.jiaoshizige.teacherexam.activity.CoursesDetialActivity;
import com.jiaoshizige.teacherexam.activity.ErrorCorrectActivity;
import com.jiaoshizige.teacherexam.activity.MyQuestionBankCollectionActivity;
import com.jiaoshizige.teacherexam.activity.RecordingActivity;
import com.jiaoshizige.teacherexam.adapter.MyQuestionBankCollectionAdapter;
import com.jiaoshizige.teacherexam.base.GloableConstant;
import com.jiaoshizige.teacherexam.base.MBaseFragment;
import com.jiaoshizige.teacherexam.model.MyQuestionBankCollectionResponse;
import com.jiaoshizige.teacherexam.utils.NoScrollListView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Administrator on 2018/7/31.
 */

@SuppressLint("ValidFragment")
public class MyQuestiobBankCollectionFragment extends MBaseFragment {
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
    @BindView(R.id.button)
    TextView mButton;
    @BindView(R.id.subjective)
    LinearLayout mSubjective;
    @BindView(R.id.answer_refer_content)
    WebView mAnswerRefer;
    @BindView(R.id.pfbz_content)
    WebView mPfbz;
    @BindView(R.id.correct)
    LinearLayout mCorrect;
    @BindView(R.id.easy_error)
    LinearLayout mErrorEasy;
    @BindView(R.id.parse_title)
    TextView mParseTitle;
    @BindView(R.id.site_name)
    TextView mExamSite;
    @BindView(R.id.view)
    View mView;
    @BindView(R.id.choose)
    LinearLayout mChoose;
    @BindView(R.id.tv_more_video)
    TextView mTvMoreVideo;
    @BindView(R.id.iv_video)
    ImageView mIvVideo;
    @BindView(R.id.iv_video_play)
    ImageView mIvVideoPlay;
    @BindView(R.id.tv_video_title)
    TextView mTvVideoTitle;
    @BindView(R.id.tv_video_kaodian)
    TextView mTvVideoKaodian;
    @BindView(R.id.ll_video)
    LinearLayout mLlVideo;
    Unbinder unbinder;


    private MyQuestionBankCollectionResponse.mData mData;
    MyQuestionBankCollectionActivity mActivity;
    private MyQuestionBankCollectionAdapter mAdapter;
    private String[] letter = {"A", "B", "C", "D"};

    @SuppressLint("ValidFragment")
    public MyQuestiobBankCollectionFragment(MyQuestionBankCollectionActivity myQuestionBankCollectionActivity, MyQuestionBankCollectionResponse.mData mData) {
        this.mActivity = myQuestionBankCollectionActivity;
        this.mData = mData;
        Log.d("////////", mData.getAnswer() + "---------");
    }

    public MyQuestiobBankCollectionFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.my_collection_bank_layout, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    protected void onInitViews(Bundle savedInstanceState) {
        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(getActivity()));
        if (mData != null) {
            mButton.setVisibility(View.GONE);
            if (mData.getType().equals("1")) {
                mSubjective.setVisibility(View.GONE);
                mChoose.setVisibility(View.VISIBLE);
                mAdapter = new MyQuestionBankCollectionAdapter(getActivity());
                mListView.setAdapter(mAdapter);
                mAdapter.setmList(mData.getMetas());
                for (int i = 0; i < letter.length; i++) {
                    if (mData.getAnswer().equals(letter[i])) {
                        mAdapter.setSelection(i);
                        mAdapter.notifyDataSetChanged();
                        Log.d("----------", mData.getEasy_error() + "--****" + i);
                    }
                }
//         WebSettings webSettings = mTitle.getSettings();
//         webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
//         webSettings.setJavaScriptEnabled(true);
//         mTitle.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
//         mTitle.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
//         webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK); //设置缓存
//         webSettings.setDomStorageEnabled(true);
//         webSettings.setLoadWithOverviewMode(true);
//         webSettings.setSupportZoom(true);
//         String title = mData.getStem();
//         mTitle.loadDataWithBaseURL(null, title, "text/html", "utf-8", null);
                mCorrectOptions.setText(mData.getAnswer());
                mResult.setVisibility(View.GONE);
                mNumber.setText("本题共被答 " + mData.getTotle_post() + " 次 , 正确率: " + mData.getSuccess() + " %");
                mEasyWrong.setText(mData.getEasy_error());


            } else {
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
            mErrorCorrect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setClass(getActivity(), ErrorCorrectActivity.class);
                    intent.putExtra("id", mData.getId());
                    startActivity(intent);
                }
            });

            //视频
            if (!TextUtils.isEmpty(mData.getVideo())) {
                mLlVideo.setVisibility(View.VISIBLE);
                Glide.with(getActivity()).load(mData.getFirst_image()).apply(GloableConstant.getInstance().getDefaultOption()).into(mIvVideo);
                mTvVideoTitle.setText(mData.getCourse_name());
                mTvVideoKaodian.setText(mData.getK_name());
            } else {
                mLlVideo.setVisibility(View.GONE);
            }


            mTvMoreVideo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent();
                    intent.putExtra("course_id", mData.getCourse_id());
                    intent.putExtra("type", "2");//以前传的是1 2 3 4 视频 图文等 现在type变成1班级2课堂
                    intent.setClass(getActivity(), CoursesDetialActivity.class);
                    startActivity(intent);
                }
            });

            mIvVideoPlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String lb_url = mData.getVideo();
                    Intent intent = new Intent();
                    intent = RecordingActivity.newIntent(getActivity(), RecordingActivity.PlayMode.portrait, lb_url, 1, true, true);
                    intent.setClass(getActivity(), RecordingActivity.class);
                    Log.e("TYY", lb_url);
                    intent.putExtra("live_url", lb_url);
                    intent.putExtra("no_course_id", "1");
                    startActivity(intent);
                }
            });
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
