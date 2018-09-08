package com.jiaoshizige.teacherexam.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.jiaoshizige.teacherexam.R;
import com.jiaoshizige.teacherexam.activity.RecruitProblemActivity;
import com.jiaoshizige.teacherexam.adapter.RecruitProblemAdapter;
import com.jiaoshizige.teacherexam.adapter.SubjectAdapter;
import com.jiaoshizige.teacherexam.base.MBaseFragment;
import com.jiaoshizige.teacherexam.model.MessageEvent;
import com.jiaoshizige.teacherexam.model.RecruitProblemResponse;
import com.jiaoshizige.teacherexam.utils.NoScrollListView;
import com.jiaoshizige.teacherexam.utils.ToastUtil;
import com.jiaoshizige.teacherexam.zz.activity.StartZhenTiPracticaActivity;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.greenrobot.eventbus.EventBus;

import java.util.LinkedHashMap;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/7/24.
 */

@SuppressLint("ValidFragment")
public class RecruitProblemFragment extends MBaseFragment {
    @BindView(R.id.now_qid)
    TextView mNowQid;
    @BindView(R.id.all_qid)
    TextView mAllQid;
    @BindView(R.id.title)
    WebView mTitle;
    @BindView(R.id.listView)
    ListView mListView;


    private int tag;
    private RecruitProblemResponse.mData mData;
    private Context mContext;
    private RecruitProblemActivity mActivity;
    private String size;
    private String answer;
    private RecruitProblemAdapter mAdapter;
    private String[] letter = {"A", "B", "C", "D"};


    public RecruitProblemFragment() {
    }

    @SuppressLint("ValidFragment")
    public RecruitProblemFragment(RecruitProblemActivity recruitProblemActivity, RecruitProblemResponse.mData data, int i, String size) {
        this.mActivity = recruitProblemActivity;
        this.mData = data;
        Log.e("********mQuestion", mData.getStem() + "----");
        this.tag = i;
        this.size = size;

        Log.e("-----------tag", tag + "--------------" + size);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.recruit_problem_fragment_layout, container, false);
    }

    @Override
    protected void onInitViews(Bundle savedInstanceState) {
        mContext = getActivity();
        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(getActivity()));
        mListView.setChoiceMode(ListView.CHOICE_MODE_NONE);
        if (mData != null) {
            WebSettings webSettings = mTitle.getSettings();
            webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
            webSettings.setJavaScriptEnabled(true);
            mTitle.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
            mTitle.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
            webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK); //设置缓存
            webSettings.setDomStorageEnabled(true);
//            webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
//            webSettings.setUseWideViewPort(true);
            webSettings.setLoadWithOverviewMode(true);
            webSettings.setSupportZoom(true);
            String title = mData.getStem();
            mTitle.loadDataWithBaseURL(null, title, "text/html", "utf-8", null);
            answer = mData.getAnswer();
            mAdapter = new RecruitProblemAdapter(getActivity(), answer);
            mListView.setAdapter(mAdapter);
            mAdapter.setmList(mData.getMetas());

        }


        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (TextUtils.isEmpty(mData.getUser_answer())) {
                    mAdapter.setSelection(position);
                    mAdapter.notifyDataSetChanged();
                    mData.setUser_answer(letter[position]);

                    if (size.equals(String.valueOf(tag + 1))) {
                        // EventBus.getDefault().post(new MessageEvent("完成"));
                        RecruitProblemActivity recruitProblemActivity = (RecruitProblemActivity) getActivity();
                        recruitProblemActivity.sumitData();

                    } else {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                final RecruitProblemActivity mainActivity = (RecruitProblemActivity) getActivity();
                                mainActivity.setFragmentSkipInterface(new RecruitProblemActivity.FragmentSkipInterface() {
                                    @Override
                                    public void gotoFragment(ViewPager viewPager) {
                                        /** 跳转到第二个页面的逻辑 */
                                        viewPager.setCurrentItem((tag + 1));
                                    }
                                });
                                /** 进行跳转 */
                                mainActivity.skipToFragment();
                            }
                        }, 1000);
                    }
                }


            }
        });

    }


}
