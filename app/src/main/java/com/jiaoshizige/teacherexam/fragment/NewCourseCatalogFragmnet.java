package com.jiaoshizige.teacherexam.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;

import com.jiaoshizige.teacherexam.R;
import com.jiaoshizige.teacherexam.adapter.MainAdapter;
import com.jiaoshizige.teacherexam.base.GloableConstant;
import com.jiaoshizige.teacherexam.base.MBaseFragment;
import com.jiaoshizige.teacherexam.http.ApiUrl;
import com.jiaoshizige.teacherexam.http.MyCallBack;
import com.jiaoshizige.teacherexam.http.Xutil;
import com.jiaoshizige.teacherexam.model.NewCourseCatalogResponse;
import com.jiaoshizige.teacherexam.utils.SPKeyValuesUtils;
import com.jiaoshizige.teacherexam.utils.SPUtils;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/3/14.
 * 新目录
 */

public class NewCourseCatalogFragmnet  extends MBaseFragment implements
        MainAdapter.OnExpandClickListener{
    @BindView(R.id.expand_list)
    ExpandableListView mList;
    @BindView(R.id.course_no_data)
    LinearLayout mCourseNoData;
    @BindView(R.id.course_error)
    LinearLayout mCourseError;
//    @BindView(R.id.neste_scroll)
//    NestedScrollView mNesteSCrollView;
 /*   @BindView(R.id.pulltorefresh)
    PullToRefreshLayoutRewrite mPullToRefresh;*/
//    @BindView(R.id.layout_swipe_refresh)
//    SwipeRefreshView mSwipeRefreshView;
//    private ArrayList<FirstBean> mDatas = new ArrayList<FirstBean>();
    private MainAdapter mAdapter;
    private String mCourseId;
    private String mType; //1班级2课程

    @SuppressLint("ValidFragment")
    public NewCourseCatalogFragmnet(String group_id, String type) {
        this.mCourseId = group_id;
        this.mType = type;
    }

    public NewCourseCatalogFragmnet() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.new_coursecatelog_fragment_layout,container,false);
    }
    @Override
    protected void onInitViews(Bundle savedInstanceState) {
        requestCatalog();
//        mSwipeRefreshView.setEnabled(false);
        mCourseError.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestCatalog();
            }
        });
//        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
//        layoutManager.setSmoothScrollbarEnabled(true);
//        layoutManager.setAutoMeasureEnabled(true);
//        mList.setLayoutManager(layoutManager);
//        mList.setHasFixedSize(true);
//        mList.setNestedScrollingEnabled(false);
    }

   /* @Override
    public void onGroupExpand(int groupPosition) {
        Log.e("xxx","onGroupExpand>>"+groupPosition);
        for (int i = 0; i < mDatas.size(); i++) {
            if (i != groupPosition) {
                mList.collapseGroup(i);
            }
        }
    }*/

    @Override
    public void onclick(int parentPosition, int childPosition, int childIndex) {
        Log.e("xxx","点了"+"parentPosition>>"+"childPosition>>"+childPosition+
                "childIndex>>"+childIndex);
    }
    public void requestCatalog(){
        Map<String,String> map = new HashMap<>();
        map.put("course_id",mCourseId);
        map.put("type_id",mType);
        map.put("user_id", (String) SPUtils.getSpValues(SPKeyValuesUtils.SP_USER_ID, SPUtils.TYPE_STRING));
        Log.e("MAP",map.toString());
        GloableConstant.getInstance().startProgressDialog(getActivity());
        Xutil.GET(ApiUrl.COURSEDETAILCATALOGS,map,new MyCallBack<NewCourseCatalogResponse>(){
            @Override
            public void onSuccess(NewCourseCatalogResponse result) {
                super.onSuccess(result);
                GloableConstant.getInstance().stopProgressDialog1();
//                mPullToRefresh.finishRefresh();
//                mPullToRefresh.finishLoadMore();
//                mPullToRefresh.showView(ViewStatus.CONTENT_STATUS);
                if(result.getStatus_code().equals("200")){
                    if(result.getData() != null){
                        if(result.getData().getCourse_list() != null && result.getData().getCourse_list().size() > 0){
                            if(result.getData().getCourse_list().get(0).getCatalog() !=null && result.getData().getCourse_list().get(0).getCatalog().size() > 0){

                                if(result.getData().getCourse_list().get(0).getCatalog().get(0).getSon() != null && result.getData().getCourse_list().get(0).getCatalog().get(0).getSon().size()>0){
                                    mCourseNoData.setVisibility(View.GONE);
                                    mCourseError.setVisibility(View.GONE);
                                    mAdapter = new MainAdapter(getActivity(),result.getData().getCourse_list(),mCourseId);
                                    mList.setAdapter(mAdapter);
                                    mAdapter.notifyDataSetChanged();
                                }else{
                                    mCourseNoData.setVisibility(View.VISIBLE);
//                                    mPullToRefresh.showView(ViewStatus.EMPTY_STATUS);
                                }
                            }
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
//                mPullToRefresh.finishRefresh();
//                mPullToRefresh.finishLoadMore();
//                mPullToRefresh.showView(ViewStatus.ERROR_STATUS);
            }
        });
    }
}
