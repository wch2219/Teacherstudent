package com.jiaoshizige.teacherexam.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.jiaoshizige.teacherexam.base.GloableConstant;
import com.jiaoshizige.teacherexam.base.MBaseFragment;
import com.jiaoshizige.teacherexam.http.ApiUrl;
import com.jiaoshizige.teacherexam.http.MyCallBack;
import com.jiaoshizige.teacherexam.http.Xutil;
import com.jiaoshizige.teacherexam.model.ClassIntroductResponse;
import com.jiaoshizige.teacherexam.model.IntroduceResponse;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/a1/13.
 * 班级介绍
 */

public class ClassIntroduceFragment extends MBaseFragment {
    @BindView(R.id.teacher_ly)
    LinearLayout mTeacherLy;//如果没有不显示
    @BindView(R.id.class_teacher_icon)
    ImageView mTaecherIcon;
    @BindView(R.id.teacher_name)
    TextView mTeacherName;
    @BindView(R.id.class_characteristic_ly)
    LinearLayout mClassCharacteristicLy;///如果没有不显示
    @BindView(R.id.class_characteristic)
    RecyclerView mClassCharacteristic;
    @BindView(R.id.class_instroduce)
    WebView mWebView;
    @BindView(R.id.normaol_instro)
    LinearLayout mNormalInstro;
    @BindView(R.id.webview_ly)
    LinearLayout mWebViewLy;
    @BindView(R.id.webview_instro)
    WebView mWebViewInstro;
    private String mId;
    private String mType = "0";//1班级介绍 2 课程介绍 3.图书..

    public ClassIntroduceFragment() {
    }

    @SuppressLint("ValidFragment")
    public ClassIntroduceFragment(String type, String id) {
        this.mType = type;
        this.mId = id;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.class_introduce_fragment, container, false);
    }

    @Override
    protected void onInitViews(Bundle savedInstanceState) {
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        if (mType.equals("1")) {
            mTeacherLy.setVisibility(View.VISIBLE);
            mNormalInstro.setVisibility(View.VISIBLE);
            mWebViewLy.setVisibility(View.GONE);
            requestClassIntroduce();
        } else if (mType.equals("2")) {
            mNormalInstro.setVisibility(View.GONE);
            mWebViewLy.setVisibility(View.VISIBLE);
            mTeacherLy.setVisibility(View.GONE);
            requestCourseIntroduce();
        } else if (mType.equals("3")) {
            mNormalInstro.setVisibility(View.GONE);
            mWebViewLy.setVisibility(View.VISIBLE);
            mTeacherLy.setVisibility(View.GONE);
            requestBookIntroduce();
        }

    }

    /**
     * 班级介绍
     */
    public void requestClassIntroduce() {
        Map<String, Object> map = new HashMap<>();
        map.put("group_id", mId);
        Log.e("TAGb", mId);
        GloableConstant.getInstance().startProgressDialog(getActivity());
        Xutil.Post(ApiUrl.CLASSINSTRODUCE, map, new MyCallBack<ClassIntroductResponse>() {
            @Override
            public void onSuccess(ClassIntroductResponse result) {
                super.onSuccess(result);
                GloableConstant.getInstance().stopProgressDialog1();
                if (result.getStatus_code().equals("200")) {
                    if (result.getData() != null) {
                        Glide.with(getActivity()).load(result.getData().getTeacher().getHeadImg()).apply(GloableConstant.getInstance().getOptions()).into(mTaecherIcon);
                        mTeacherName.setText(result.getData().getTeacher().getName());
                        int size = 4;
                        if (result.getData().getTags().size() > 4) {
                            size = result.getData().getTags().size();
                        }
                        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
                        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
                        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), size);
                        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                            @Override
                            public int getSpanSize(int position) {
                                return 1;
                            }
                        });
                        mClassCharacteristic.setLayoutManager(gridLayoutManager);
                        if (result.getData().getTags() != null && result.getData().getTags().size() > 0) {
                            mClassCharacteristic.setAdapter(new CommonAdapter<ClassIntroductResponse.mTags>(getActivity(), R.layout.item_courses_detial_tag, result.getData().getTags()) {
                                @Override
                                protected void convert(ViewHolder holder, ClassIntroductResponse.mTags mTagList, int position) {
                                    holder.setText(R.id.tag_name, mTagList.getTag_name());
                                    ImageView mTagImg = (ImageView) holder.getConvertView().findViewById(R.id.tag_img);
                                    Glide.with(getActivity()).load(mTagList.getImg_path()).into(mTagImg);
                                }
                            });
                        } else {
                            mClassCharacteristicLy.setVisibility(View.GONE);
                        }
                        WebSettings webSettings = mWebView.getSettings();
                        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
                        webSettings.setJavaScriptEnabled(true);
                        String test = "<style type='text/css'>p{margin: 0;padding: 0;}img {max-width:100%!important;height:auto!important;font-size:14px;}</style>" + result.getData().getMobile_detail();
                        mWebView.loadDataWithBaseURL(null, test, "text/html", "utf-8", null);
                    }
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
                GloableConstant.getInstance().stopProgressDialog1();
            }
        });
    }

    /**
     * 课程详情
     */
    public void requestCourseIntroduce() {
        Map<String, String> map = new HashMap<>();
        map.put("course_id", mId);
        Log.e("TAGk", mId);
        GloableConstant.getInstance().showmeidialog(getContext());
        Xutil.GET(ApiUrl.COURSEINTRODUCE, map, new MyCallBack<IntroduceResponse>() {
            @Override
            public void onSuccess(IntroduceResponse result) {
                super.onSuccess(result);
                GloableConstant.getInstance().stopProgressDialog();
                if (result.getStatus_code().equals("200")) {
                    if (result.getData().getDescription() != null && !result.getData().getDescription().equals("")) {
                        WebSettings webSettings = mWebViewInstro.getSettings();
                        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
                        webSettings.setJavaScriptEnabled(true);
                        webSettings.setDomStorageEnabled(true);//有可能是DOM储存API没有打开
                        webSettings.setBlockNetworkImage(false);
                        webSettings.setUseWideViewPort(true);
                        webSettings.setNeedInitialFocus(false);
                        Log.e("TAGk", result.getData().getDescription());
                        mWebViewInstro.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
                        String test = "<style type='text/css'><body style='margin:0;padding:0'>p{margin: 0;padding: 0;}img {margin: 0;padding: 0;max-width:100%!important;height:auto!important;font-size:14px;}</style>" + result.getData().getDescription();
                        Log.e("*****.getDescription()", result.getData().getDescription());
//                        String test = "<style type='text/css'><body style='margin:0;padding:0'>p{margin: 0;padding: 0;}img {margin: 0;padding: 0;max-width:100%!important;height:auto!important;font-size:14px;}</style>"+ ApiUrl.BASEIMAGE +"storage/images/editor/upload/image/20180330/1522393491677545.jpg";
//                        mWebViewInstro.loadDataWithBaseURL(null,test, "text/html", "utf-8", null);
                        mWebViewInstro.loadData(test, "text/html", "utf-8");
                /*      mWebViewInstro.setWebViewClient(new WebViewClient(){
                            @Override
                            public void onPageFinished(WebView view, String url) {
                                super.onPageFinished(view, url);

                                //这个是一定要加上那个的,配合scrollView和WebView的height=wrap_content属性使用
                                int w = View.MeasureSpec.makeMeasureSpec(0,
                                        View.MeasureSpec.UNSPECIFIED);
                                int h = View.MeasureSpec.makeMeasureSpec(0,
                                        View.MeasureSpec.UNSPECIFIED);
                                //重新测量
                                mWebViewInstro.measure(w, h);
                                Log.e("YUYU",h+"HEIG"+w);

                            }
                        });*/
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
            }

        });

    }

    public void requestBookIntroduce() {
        Map<String, Object> map = new HashMap<>();
        map.put("book_id", mId);
        Log.e("TAGs", mId);
        GloableConstant.getInstance().showmeidialog(getActivity());
        Xutil.Post(ApiUrl.BOOKINTRODUCE, map, new MyCallBack<IntroduceResponse>() {
            @Override
            public void onSuccess(IntroduceResponse result) {
                super.onSuccess(result);
                if (result.getStatus_code().equals("200")) {

                    GloableConstant.getInstance().stopProgressDialog();
                    if (result.getData().getMobile_detail() != null && !result.getData().getMobile_detail().equals("")) {
                        WebSettings webSettings = mWebViewInstro.getSettings();
                        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
                        webSettings.setJavaScriptEnabled(true);
                        Log.e("TAGss", result.getData().getMobile_detail());
                        String test = "<style type='text/css'>p{margin: 0;padding: 0;}img {max-width:100%!important;height:auto!important;font-size:14px;}</style>" + result.getData().getMobile_detail();
                        mWebViewInstro.loadDataWithBaseURL(null, test, "text/html", "utf-8", null);
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
            }
        });
    }
}
