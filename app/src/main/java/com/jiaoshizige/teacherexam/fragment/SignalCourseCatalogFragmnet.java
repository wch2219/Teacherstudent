package com.jiaoshizige.teacherexam.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.jiaoshizige.teacherexam.R;
import com.jiaoshizige.teacherexam.activity.aaa.xx.adapter.SanJiDeLeiBiaoAdapter;
import com.jiaoshizige.teacherexam.activity.aaa.xx.bean.NewCourseListBean;
import com.jiaoshizige.teacherexam.activity.aaa.xx.utils.WcHLogUtils;
import com.jiaoshizige.teacherexam.adapter.CourseCatalogFragmentAdapter;
import com.jiaoshizige.teacherexam.base.GloableConstant;
import com.jiaoshizige.teacherexam.base.MBaseFragment;
import com.jiaoshizige.teacherexam.http.ApiUrl;
import com.jiaoshizige.teacherexam.http.MyCallBack;
import com.jiaoshizige.teacherexam.http.Xutil;
import com.jiaoshizige.teacherexam.model.NewCourseCatalogResponse;
import com.jiaoshizige.teacherexam.palyer.PolyvDownloadInfo;
import com.jiaoshizige.teacherexam.palyer.PolyvDownloadSQLiteHelper;
import com.jiaoshizige.teacherexam.utils.SPKeyValuesUtils;
import com.jiaoshizige.teacherexam.utils.SPUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Administrator on 2017/a1/15.
 * 课程 目录
 */

public class SignalCourseCatalogFragmnet extends MBaseFragment {
    @BindView(R.id.course_no_data)
    LinearLayout mCourseNoData;
    @BindView(R.id.course_error)
    LinearLayout mCourseError;
    @BindView(R.id.nodata)
    ImageView nodata;
    @BindView(R.id.rv_list)
    RecyclerView rvList;
    Unbinder unbinder;
    /*    @BindView(R.id.task)
        RecyclerView mTask;
        @BindView(R.id.pulltorefresh)
        PullToRefreshLayoutRewrite mPullToRefresh;
        @BindView(R.id.layout_swipe_refresh)
        SwipeRefreshView mSwipeRefreshView;*/
    ///////////////////////////////
    private String state = "";
    private static final String DOWNLOADED = "已下载";
    private static final String DOWNLOADING = "正在下载";
    private static final String PAUSEED = "暂停下载";
    private static final String WAITED = "等待下载";
    private int progress = 0;
    private int bitrate = 1;
    private String vid = "c538856dde2600e0096215c16592d4d3_c";
    private static PolyvDownloadSQLiteHelper mDownloadSQLiteHelper;
    PolyvDownloadInfo downloadInfo;
    private long mTotal;
    ///////////////////////////////////////
    private List<NewCourseCatalogResponse.mCourseList> mCourseList;
    private List<NewCourseCatalogResponse.mCatalog> mCourseTitle;
    private List<NewCourseCatalogResponse.mSon> mCourseContent;
    private List<List<NewCourseCatalogResponse.mSon>> childArray;
    private CourseCatalogFragmentAdapter mExpandableViewAdapter;
    private String mCourseId;
    private String mType; //1班级2课程
    private String isBuy;

    private boolean isshow;
    @SuppressLint("ValidFragment")
    public SignalCourseCatalogFragmnet(String course_id, String type, String is_buy,boolean isshow) {
        this.mCourseId = course_id;
        this.mType = type;
        this.isBuy = is_buy;
        this.isshow = isshow;
    }

    public SignalCourseCatalogFragmnet() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.course_catalog_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    Handler handler = new Handler();

    @Override
    protected void onInitViews(Bundle savedInstanceState) {
//        mSwipeRefreshView.setEnabled(false);
        mDownloadSQLiteHelper = PolyvDownloadSQLiteHelper.getInstance(getActivity());/////////////////////////////
        mCourseError.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestCatalog();
            }
        });
      /*  mPullToRefresh.setRefreshListener(new BaseRefreshListener() {
            @Override
            public void refresh() {
            }

            @Override
            public void loadMore() {
            }
        });
        mPullToRefresh.setOnErrorListener(new PullToRefreshLayoutRewrite.onClickCallBackListener() {
            @Override
            public void onCallBackListener(View v) {

            }
        });*/

    }

    @Override
    public void onResume() {
        super.onResume();
        requestCatalog();
    }

    public void requestCatalog() {
        Map<String, String> map = new HashMap<>();
        map.put("course_id", mCourseId);
        map.put("type_id", mType);
      /*  map.put("course_id","16");
        map.put("type_id","1");*/
        map.put("user_id", (String) SPUtils.getSpValues(SPKeyValuesUtils.SP_USER_ID, SPUtils.TYPE_STRING));
        Log.e("****************MAP", map.toString());
        GloableConstant.getInstance().startProgressDialog(getActivity());
        Xutil.GET(ApiUrl.COURSEDETAILCATALOGS, map, new MyCallBack<NewCourseListBean>() {
            @Override
            public void onSuccess(NewCourseListBean result) {
                super.onSuccess(result);
                GloableConstant.getInstance().stopProgressDialog1();
//                mPullToRefresh.finishRefresh();
//                mPullToRefresh.finishLoadMore();
//                mPullToRefresh.showView(ViewStatus.CONTENT_STATUS);
                WcHLogUtils.I(new Gson().toJson(result));
                if (result.getStatus_code()==200) {
                    mCourseTitle = new ArrayList<>();
                    childArray = new ArrayList<>();
                    if (result.getData() != null) {
                        if (result.getData().getCourse_list() != null && result.getData().getCourse_list().size() > 0) {
                            if (result.getData().getCourse_list().get(0).getCatalog() != null && result.getData().getCourse_list().get(0).getCatalog().size() > 0) {
//                                if (result.getData().getCourse_list().get(0).getCatalog().get(0).getSon() != null && result.getData().getCourse_list().get(0).getCatalog().get(0).getSon().size() > 0) {
                                    mCourseNoData.setVisibility(View.GONE);
                                    mCourseError.setVisibility(View.GONE);
//                                    String className = result.getData().getCourse_list().get(0).getName();
//                                    String mSingleCourserId =  result.getData().getCourse_list().get(0).getId();//单个套餐的id
//
//                                 /*   mAdapter = new MainAdapter(getActivity(),result.getData().getCourse_list());
//                                    mList.setAdapter(mAdapter);
//                                    mAdapter.notifyDataSetChanged();*/
//                                    mCourseList = new ArrayList<>();
//                                    for(int i = 0;i < result.getData().getCourse_list().size();i++){
//                                        mCourseList.add(result.getData().getCourse_list().get(i));
//                                    }
//                                /* for(int i=0;i< result.getData().getCourse_list().size();i++){
//                                     for(int j =0;j< result.getData().getCourse_list().get(i).getCatalog().size();j++){
//                                         mCourseTitle.add(result.getData().getCourse_list().get(i).getCatalog().get(j));
//                                     }
//                                 }*/
//                                    for(int i=0;i< mCourseList.size();i++){
//                                        for(int j =0;j< mCourseList.get(i).getCatalog().size();j++){
//                                            mCourseTitle.add(result.getData().getCourse_list().get(i).getCatalog().get(j));
//                                        }
//                                    }
//                               for(int j=0;j< mCourseTitle.size();j++){
//                                    for(int k=0;k<mCourseTitle.get(j).getSon().size();k++){
//                                        mCourseContent = new ArrayList<>();
//                                        mCourseContent.add(mCourseTitle.get(j).getSon().get(k));
//                                        childArray.add(mCourseContent);
//                                    }
//                               }
//                                    int mIsbuy=0;
//                                    if (isBuy.equals("1")){
//                                        mIsbuy=1;
//                                    }else {
//                                        mIsbuy = Integer.valueOf( result.getData().getCourse_list().get(0).getIs_buy());
//                                    }
//                                    mExpandableViewAdapter = new CourseCatalogFragmentAdapter(getActivity(),mCourseTitle,childArray,result.getData().getCourse_list(),mType,mCourseId,mSingleCourserId,mIsbuy,className);
//                                    mExpandableView.setAdapter(mExpandableViewAdapter);

                                    LinearLayoutManager manager = new LinearLayoutManager(getActivity());
                                    rvList.setLayoutManager(manager);
                                List<NewCourseListBean.DataBean.CourseListBean> course_list = result.getData().getCourse_list();
                                NewCourseListBean.DataBean.LastLearnBean last_learn = result.getData().getLast_learn();
                                if (lastLeanBackListener != null) {
                                    lastLeanBackListener.backPlay(last_learn);
                                }
                                Log.i("wch",new Gson().toJson(course_list));
                                    rvList.setAdapter(new SanJiDeLeiBiaoAdapter(getActivity(), course_list,false,false,mType,mCourseId,Integer.valueOf(isBuy)));
//                                } else {
//                                    mCourseNoData.setVisibility(View.VISIBLE);
////                                    mPullToRefresh.showView(ViewStatus.EMPTY_STATUS);
//                                }
                            } else {
                                mCourseNoData.setVisibility(View.VISIBLE);
                            }
                        } else {
                            mCourseNoData.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
                GloableConstant.getInstance().stopProgressDialog1();
                mCourseError.setVisibility(View.VISIBLE);
                mCourseNoData.setVisibility(View.GONE);
               /* mPullToRefresh.finishRefresh();
                mPullToRefresh.finishLoadMore();
                mPullToRefresh.showView(ViewStatus.ERROR_STATUS);*/
            }
        });
    }

    public void refreshCatalog() {
        requestCatalog();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
    public interface LastLeanBackListener{
       void backPlay(NewCourseListBean.DataBean.LastLearnBean last_learn);
    }

    private static LastLeanBackListener lastLeanBackListener;

    public static void setLastLeanBackListener(LastLeanBackListener backListener){
        lastLeanBackListener = backListener;
    }

}
