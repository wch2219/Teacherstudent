package com.jiaoshizige.teacherexam.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jiaoshizige.teacherexam.R;
import com.jiaoshizige.teacherexam.activity.ExamSubjectSelect;
import com.jiaoshizige.teacherexam.activity.StartPracticaActivity;
import com.jiaoshizige.teacherexam.adapter.StartPracticeAdapter;
import com.jiaoshizige.teacherexam.adapter.StartPracticePopAdapter;
import com.jiaoshizige.teacherexam.base.GloableConstant;
import com.jiaoshizige.teacherexam.base.MBaseFragment;
import com.jiaoshizige.teacherexam.model.MoNiResponse;
import com.jiaoshizige.teacherexam.utils.ToastUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.opengles.GL;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/8/6.
 */

public class StartPracticeFragment extends MBaseFragment {
    @BindView(R.id.title)
    WebView mTitle;
    @BindView(R.id.listView)
    ListView mListView;
    @BindView(R.id.button)
    TextView mButton;
    @BindView(R.id.subjective)
    LinearLayout mSubjective;
    @BindView(R.id.pfbz_content)
    WebView mPfbz;
    @BindView(R.id.answer_refer_content)
    WebView mAnswerRefer;
    @BindView(R.id.score_since)
    RelativeLayout mScoreSince;
    @BindView(R.id.score_since_text)
    TextView mScoreSinceText;
    @BindView(R.id.save_score)
    TextView mSaveScore;

    private MoNiResponse.mData mData;
    private StartPracticaActivity mActivity;
    private StartPracticeAdapter mAdapter;
    private Context mContext;
    private View mContentView;
    private PopupWindow mPopupWindow;
    private ListView mPopList;
    private List<String> mList;
    private StartPracticePopAdapter mPopAdapter;
    private String[] letter = {"A", "B", "C", "D"};


    private int tag;
    private String size;

    @SuppressLint("ValidFragment")
    public StartPracticeFragment(StartPracticaActivity startPracticaActivity, MoNiResponse.mData mData, int i, String size) {
        this.mActivity = startPracticaActivity;
        this.mData = mData;
        this.tag = i;
        this.size = size;

    }

    public StartPracticeFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.start_practice_fragment_layout, container, false);
    }

    @Override
    protected void onInitViews(Bundle savedInstanceState) {
        mContext = getActivity();

        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(getActivity()));
        if (mData != null) {

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
            if (mData.getType().equals("1")) {
                mListView.setVisibility(View.VISIBLE);
                mButton.setVisibility(View.GONE);
                mSubjective.setVisibility(View.GONE);
                mAdapter = new StartPracticeAdapter(getActivity());
                mListView.setAdapter(mAdapter);
                mAdapter.setmList(mData.getMetas());

                if (!mData.getUser_answer().equals("")){
                    for (int i=0;i<letter.length;i++){
                        if (letter[i].equals(mData.getUser_answer())){
                            mAdapter.setSelection(i);
                            mAdapter.notifyDataSetChanged();
                        }
                    }
                }else {
                    mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            mAdapter.setSelection(position);
                            mAdapter.notifyDataSetChanged();
                            mData.setIs_done("1");
                            mData.setUser_answer(letter[position]);
                            if (size.equals(String.valueOf(tag + 1))) {
                                ToastUtil.showShortToast("11111111");

                            } else {
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        final StartPracticaActivity mainActivity = (StartPracticaActivity) getActivity();
                                        mainActivity.setFragmentSkipInterface(new StartPracticaActivity.FragmentSkipInterface() {
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
                    });
                }



            } else {
                mButton.setVisibility(View.VISIBLE);
                mListView.setVisibility(View.GONE);
                mSubjective.setVisibility(View.GONE);
                if (!mData.getUser_answer().equals("")){
                    mScoreSinceText.setText(mData.getUser_answer()+"分");
                    mButton.setVisibility(View.GONE);
                    mSubjective.setVisibility(View.VISIBLE);
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
                    String parse = mData.getAnswer();
                    mAnswerRefer.loadDataWithBaseURL(null, parse, "text/html", "utf-8", null);
                }else {
                    mButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mButton.setVisibility(View.GONE);
                            mSubjective.setVisibility(View.VISIBLE);
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
                            String parse = mData.getAnswer();
                            mAnswerRefer.loadDataWithBaseURL(null, parse, "text/html", "utf-8", null);

                        }
                    });
                }


                mScoreSince.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showPopWindow(mData.getScore());
                    }
                });

                mSaveScore.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (size.equals(String.valueOf(tag + 1))) {
                            ToastUtil.showShortToast("没有更多题目了");

                        } else {
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    final StartPracticaActivity mainActivity = (StartPracticaActivity) getActivity();
                                    mainActivity.setFragmentSkipInterface(new StartPracticaActivity.FragmentSkipInterface() {
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
                });



            }
        }
    }


    private void showPopWindow(String score) {
        LayoutInflater mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mContentView = mInflater.inflate(R.layout.start_practice_pop_layout, null);
        mPopList = mContentView.findViewById(R.id.listView);
        mPopupWindow = new PopupWindow(mContentView, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setFocusable(true);// 取得焦点
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());

        //点击外部消失
        mPopupWindow.setOutsideTouchable(true);
        //设置可以点击
        mPopupWindow.setTouchable(true);
        setBackgroundAlpha(0.5f);//设置屏幕透明度
        mPopupWindow.showAtLocation(mContentView, Gravity.BOTTOM, 0, 0);
        mPopupWindow.setOnDismissListener(new poponDismissListener());
        mList = new ArrayList<>();
        for (int i = 0; i < Integer.valueOf(score); i++) {
            mList.add(String.valueOf(i + 1));
        }
        Log.d("*********mList", mList.size() + "----------*******-" + score);
        GloableConstant.getInstance().setMomi_position(null);
        mPopAdapter = new StartPracticePopAdapter(mList, mContext);
        mPopList.setAdapter(mPopAdapter);
        if (GloableConstant.getInstance().getMomi_position() != null) {
            mPopAdapter.setSelection(Integer.valueOf(GloableConstant.getInstance().getMomi_position()));
            mPopAdapter.notifyDataSetChanged();
        }

        mPopList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mPopAdapter.setSelection(position);
                mPopAdapter.notifyDataSetChanged();
                mData.setIs_done("1");
                GloableConstant.getInstance().setMomi_position(position + "");
                mScoreSinceText.setText((position+1)+"分");
                mData.setUser_answer(String.valueOf(position+1));
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (mPopupWindow.isShowing()){
                            mPopupWindow.dismiss();
                        }
                    }
                },500);
            }
        });

    }

    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha 屏幕透明度0.0-1.0 1表示完全不透明
     */
    public void setBackgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.alpha = bgAlpha;
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        getActivity().getWindow().setAttributes(lp);
    }


    class poponDismissListener implements PopupWindow.OnDismissListener {

        @Override
        public void onDismiss() {

            setBackgroundAlpha(1f);
        }
    }

}
