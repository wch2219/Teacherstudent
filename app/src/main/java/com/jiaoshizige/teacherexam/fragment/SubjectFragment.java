package com.jiaoshizige.teacherexam.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.Button;

import android.widget.ListView;
import android.widget.RadioButton;

import com.alibaba.fastjson.JSON;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.jiaoshizige.teacherexam.R;
import com.jiaoshizige.teacherexam.activity.PracticeActivity;
import com.jiaoshizige.teacherexam.adapter.MultipleChoiceAdapter;
import com.jiaoshizige.teacherexam.adapter.SubjectAdapter;
import com.jiaoshizige.teacherexam.base.GloableConstant;
import com.jiaoshizige.teacherexam.base.MBaseFragment;
import com.jiaoshizige.teacherexam.model.PracticeResponse;
import com.jiaoshizige.teacherexam.utils.NoScrollListView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/1/9 0009.
 */

public class SubjectFragment extends MBaseFragment implements View.OnClickListener {
    @BindView(R.id.title)
    WebView mTitle;
    @BindView(R.id.list_view)
    NoScrollListView mListView;
    @BindView(R.id.multiple_list_view)
    NoScrollListView mMultipleListview;
    @BindView(R.id.answer)
    WebView mAnswer;
    @BindView(R.id.analysis)
    WebView mAnalysis;
    @BindView(R.id.affirm_answer)
    Button mAffirm;


    int index;


    private PracticeResponse.mQuestion mQuestion;
    private String position;
    private List<String> answerList;//多选题-正确答案
    private List<String> myAnswer = new ArrayList<>();//多选题-我的答案
    private String[] letter = {"A", "B", "C", "D", "E", "F"};
    private String answer;
    private SubjectAdapter mAdapter;
    private MultipleChoiceAdapter adapter;
    private int mPageSize;
    private PracticeActivity mActivity;

    @SuppressLint("ValidFragment")
    public SubjectFragment(PracticeActivity activity, PracticeResponse.mQuestion mQuestion, int pagesize, int position) {
        this.mQuestion = mQuestion;
        Log.e("********mQuestion", mQuestion.getStem());
        this.mPageSize = pagesize;
        this.mActivity = activity;
        index = position;
    }

    public SubjectFragment() {

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.subject_fragment_layout, container, false);
    }

    @Override
    protected void onInitViews(Bundle savedInstanceState) {
        mAffirm.setOnClickListener(this);
        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(getActivity()));
        if (mQuestion != null) {
            WebSettings webSettings = mTitle.getSettings();
            webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
            webSettings.setJavaScriptEnabled(true);
            mTitle.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
            mTitle.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
            webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK); //设置缓存
            webSettings.setDomStorageEnabled(true);
            webSettings.setLoadWithOverviewMode(true);
            webSettings.setSupportZoom(true);

            position = GloableConstant.getInstance().getPosition() + "";
            String title = mQuestion.getStem();
            mTitle.loadDataWithBaseURL(null, title, "text/html", "utf-8", null);
            mAnalysis.loadDataWithBaseURL(null, mQuestion.getAnalysis(), "text/html", "utf-8", null);
            mAnswer.setVisibility(View.GONE);
            mAnalysis.setVisibility(View.GONE);
            mAnswer.loadDataWithBaseURL(null, "答案 :  " + mQuestion.getAnswer(), "text/html", "utf-8", null);

            switch (mQuestion.getType()) {
                case "1"://单项选择题
                    mListView.setVisibility(View.VISIBLE);
                    mMultipleListview.setVisibility(View.GONE);
                    answer = mQuestion.getAnswer();
                    mAdapter = new SubjectAdapter(getActivity(), answer);
                    mListView.setAdapter(mAdapter);
                    mAdapter.setmList(mQuestion.getMetas());
                    mAffirm.setVisibility(View.GONE);

                    if (!TextUtils.isEmpty(mQuestion.getUser_answer())) {
                        mAnswer.setVisibility(View.VISIBLE);
                        mAnalysis.setVisibility(View.VISIBLE);
                        for (int i = 0; i < letter.length; i++) {
                            if (mQuestion.getUser_answer().equals(letter[i])) {
                                mAdapter.setSelection(i);
                                mAdapter.notifyDataSetChanged();
                            }
                        }

                    }
                    break;
                case "2"://不定项选择
                    mAffirm.setVisibility(View.VISIBLE);
                    mListView.setVisibility(View.GONE);
                    mMultipleListview.setVisibility(View.VISIBLE);
                    mMultipleListview.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
                    answer = mQuestion.getAnswer();
                    String metas[] = answer.split(",");
                    if (metas.length > 0) {
                        answerList = new ArrayList<>();
                        for (int i = 0; i < metas.length; i++) {
                            answerList.add(metas[i]);
                        }
                    }
                    if (mQuestion.getMetas().size() > 0) {
                        adapter = new MultipleChoiceAdapter(getActivity(), mQuestion.getMetas(), answerList, mMultipleListview);
                        mMultipleListview.setAdapter(adapter);
                        adapter.setmList(mQuestion.getMetas());
                    }

                    if (!TextUtils.isEmpty(mQuestion.getUser_answer())) {
                        myAnswer = JSON.parseArray(mQuestion.getUser_answer(), String.class);
                    }
                    break;
                case "3"://材料分析题

                    break;
                case "4"://判断题
//                    mAffirm.setVisibility(View.VISIBLE);
//                    mMultipleListview.setVisibility(View.GONE);
//                    answer = mQuestion.getAnswer();
//                    mAdapter = new SubjectAdapter(getActivity(), answer);
//                    mListView.setAdapter(mAdapter);
//                    mAdapter.setmList(mQuestion.getMetas());
//
//                    if (!TextUtils.isEmpty(mQuestion.getUser_answer())) {
//                        mAnswer.setVisibility(View.VISIBLE);
//                        mAnalysis.setVisibility(View.VISIBLE);
//                        for (int i = 0; i < letter.length; i++) {
//                            if (mQuestion.getUser_answer().equals(letter[i])) {
//                                mAdapter.setSelection(i);
//                                mAdapter.notifyDataSetChanged();
//                            }
//                        }
//
//                    }
                    break;
                case "5"://填空题

                    break;
            }


        }
        //单选list的点击事件
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (TextUtils.isEmpty(mQuestion.getUser_answer())) {
                    mAdapter.setSelection(position);
                    mAdapter.notifyDataSetChanged();
                    mAnswer.setVisibility(View.VISIBLE);
                    mAnalysis.setVisibility(View.VISIBLE);

                    mQuestion.setUser_answer(letter[position]);
                    if (letter[position].equals(mQuestion.getAnswer())) {
                        mActivity.setLastPagerStatus(true);
                    } else {


                        mActivity.setLastPagerStatus(false);
                    }

                }

            }
        });
        //多项选择的点击事件
        mMultipleListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // EventBus.getDefault().post("点击");
                if (TextUtils.isEmpty(mQuestion.getUser_answer())) {

                    if (myAnswer.size() > 0) {
                        for (int i = 0; i < myAnswer.size(); i++) {
                            if (!myAnswer.get(i).equals(adapter.getText(position))) {
                                myAnswer.add(adapter.getText(position));
                            }
                        }
                    } else {
                        myAnswer.add(adapter.getText(position));
                    }
                    adapter.setPosition(myAnswer);
                    adapter.notifyDataSetChanged();

                }

            }
        });


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.affirm_answer:
                switch (mQuestion.getType()) {
                    case "1"://单项选择题

                        break;
                    case "2"://不定项选择
                        mAnswer.setVisibility(View.GONE);
                        mAnalysis.setVisibility(View.VISIBLE);
                        adapter.setResult();
                        adapter.notifyDataSetChanged();
                        if (compare(answerList, myAnswer)) {
                            mActivity.setLastPagerStatus(true);
                        } else {
                            mActivity.setLastPagerStatus(false);
                        }
                        mQuestion.setUser_answer(JSON.toJSONString(myAnswer));

                        break;
                    case "3"://材料分析题
                        mAnswer.setVisibility(View.GONE);
                        mAnalysis.setVisibility(View.VISIBLE);
                        mActivity.setLastPagerStatus(true);
                        break;
                    case "4"://判断题
                        mAnswer.setVisibility(View.GONE);
                        mAnalysis.setVisibility(View.VISIBLE);
                        mActivity.setLastPagerStatus(true);

                        break;
                    case "5"://填空题
                        mAnswer.setVisibility(View.GONE);
                        mAnalysis.setVisibility(View.VISIBLE);
                        mActivity.setLastPagerStatus(true);
                        break;
                }

                break;
            default:
                break;
        }
    }

    //判断两个集合是否相等
    public static <T extends Comparable<T>> boolean compare(List<T> a, List<T> b) {
        if (a.size() != b.size()) {
            return false;
        }

        Collections.sort(a);
        Collections.sort(b);
        for (int i = 0; i < a.size(); i++) {
            if (!a.get(i).equals(b.get(i)))
                return false;
        }
        return true;
    }
}
