package com.jiaoshizige.teacherexam.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jwenfeng.library.pulltorefresh.BaseRefreshListener;
import com.jwenfeng.library.pulltorefresh.ViewStatus;
import com.umeng.analytics.MobclickAgent;
import com.jiaoshizige.teacherexam.R;
import com.jiaoshizige.teacherexam.activity.CoursesDetialActivity;
import com.jiaoshizige.teacherexam.base.GloableConstant;
import com.jiaoshizige.teacherexam.base.MPBaseFragment;
import com.jiaoshizige.teacherexam.http.ApiUrl;
import com.jiaoshizige.teacherexam.http.MyCallBack;
import com.jiaoshizige.teacherexam.http.SupportResponse;
import com.jiaoshizige.teacherexam.http.Xutil;
import com.jiaoshizige.teacherexam.model.CourseCollectResponse;
import com.jiaoshizige.teacherexam.utils.PullToRefreshLayoutRewrite;
import com.jiaoshizige.teacherexam.utils.SPKeyValuesUtils;
import com.jiaoshizige.teacherexam.utils.SPUtils;
import com.jiaoshizige.teacherexam.utils.ToastUtil;
import com.jiaoshizige.teacherexam.utils.ToolUtils;
import com.jiaoshizige.teacherexam.widgets.SelfDialog;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/a1/7 0007.
 * 课程fragment
 */

public class CourseFragment extends MPBaseFragment {
    @BindView(R.id.course_recycler)
    RecyclerView mCourseRecycler;
    @BindView(R.id.pulltorefresh)
    PullToRefreshLayoutRewrite mPullToRefresh;
    @BindView(R.id.screen_ly)
    LinearLayout mScreenLy;
    private String user_id,course_id;
    private String page = "1", pagesize = "5";
    private SelfDialog selfDialog;
    private Intent intent;
    private Context mContext;
    View view;
    private int mPage=1;
    private List<CourseCollectResponse.mData> mList;

    boolean isPrepared = false;//判定view是否inflater

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            view = inflater.inflate(R.layout.course_fragment, container, false);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        CollectCourse();
        MobclickAgent.onPageStart("课程收藏");
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("课程收藏");   }

    @Override
    protected void onInitViews(Bundle savedInstanceState) {
        mScreenLy.setVisibility(View.GONE);
        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) mCourseRecycler.getLayoutParams();
        lp.setMargins(0, ToolUtils.dip2px(getActivity(),10), 0, 0);
        mCourseRecycler.setLayoutParams(lp);
        user_id = (String) SPUtils.getSpValues(SPKeyValuesUtils.SP_USER_ID, SPUtils.TYPE_STRING);
        mList=new ArrayList<>();
        mPullToRefresh.setRefreshListener(new BaseRefreshListener() {
            @Override
            public void refresh() {
                mPage=1;
                page="1";
                CollectCourse();
            }

            @Override
            public void loadMore() {
                mPage++;
                page=String.valueOf(mPage);
                CollectCourse();

            }
        });
        mPullToRefresh.setOnErrorListener(new PullToRefreshLayoutRewrite.onClickCallBackListener() {
            @Override
            public void onCallBackListener(View v) {
                page="1";
                mPage=1;
                CollectCourse();
            }
        });
        CollectCourse();//访问网络请求数据
//        mCourseRecycler.set

    /*    CommonAdapter mAdapter = new CommonAdapter<String>(getActivity(),R.layout.item_my_course_fragment,mDatas) {
            @Override
            protected void convert(ViewHolder holder, String s, int position) {
                holder.setText(R.id.course_name,mDatas.get(position));
            }
        };
        EmptyWrapper mEmptyWrapper = new EmptyWrapper(mAdapter);
        mEmptyWrapper.setEmptyView(R.layout.layout_error);
        HeaderAndFooterWrapper mHeaderAndFooterWrapper = new HeaderAndFooterWrapper(mEmptyWrapper);
        TextView t1 = new TextView(getActivity());
        t1.setText("Header 1");
        TextView t2 = new TextView(getActivity());
        t2.setText("Header 2");

        mHeaderAndFooterWrapper.addHeaderView(t1);
        mHeaderAndFooterWrapper.addHeaderView(t2);
        LoadMoreWrapper mLoadMoreWrapper = new LoadMoreWrapper(mAdapter);
        mLoadMoreWrapper.setLoadMoreView(R.layout.layout_footer);
        mLoadMoreWrapper.setOnLoadMoreListener(new LoadMoreWrapper.OnLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                Log.e("TAG","#####");
            }
        });*/
//        mCourseRecycler.setAdapter(mHeaderAndFooterWrapper);
//        mCourseRecycler.setAdapter(mLoadMoreWrapper);
//        mHeaderAndFooterWrapper.notifyDataSetChanged();
//        mCourseRecycler.setAdapter(new LoadMoreWrapper(new HeaderAndFooterWrapper(new EmptyWrapper(mAdapter).setEmptyView(R.layout.layout_error)).addHeaderView(t1)).setLoadMoreView(R.layout.layout_footer));
//        mCourseRecycler.setAdapter(new EmptyWrapper( new LoadMoreWrapper(new HeaderAndFooterWrapper(mAdapter))));

    }

//    @Override
//    public void setUserVisibleHint(boolean isVisibleToUser) {
//        super.setUserVisibleHint(isVisibleToUser);
//        mContext=getActivity();
//        if (isVisibleToUser){
//
//        }
//    }

    private void CollectCourse() {
        Map<String,Object> map=new HashMap<>();
//        map.put("user_id",1);
        map.put("user_id",SPUtils.getSpValues(SPKeyValuesUtils.SP_USER_ID, SPUtils.TYPE_STRING));
        map.put("type","1");
        map.put("page",page);
        map.put("page_size",pagesize);
        Log.e("*******map",map.toString());
        GloableConstant.getInstance().startProgressDialog(getActivity());
        Xutil.Post(ApiUrl.COLLECT,map,new MyCallBack<CourseCollectResponse>(){
            @Override
            public void onSuccess(final CourseCollectResponse result) {
                super.onSuccess(result);
                GloableConstant.getInstance().stopProgressDialog();
                mPullToRefresh.finishRefresh();
                mPullToRefresh.finishLoadMore();
                mPullToRefresh.showView(ViewStatus.CONTENT_STATUS);
                if (result.getStatus_code().equals("200")) {
                    if (result.getData() != null && result.getData().size() > 0){
                        if (page.equals("1")) {
                            mList.clear();
                            mList = result.getData();
                        } else {
                            if (null == mList) {
                                mList = result.getData();
                            } else {
                                mList.addAll(result.getData());
                            }
                        }
                    mCourseRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
                    mCourseRecycler.setAdapter(new CommonAdapter<CourseCollectResponse.mData>(getActivity(), R.layout.item_my_course_fragment, result.getData()) {
                        @Override
                        protected void convert(ViewHolder holder, final CourseCollectResponse.mData mData, final int position) {
                            holder.setText(R.id.course_name, mData.getName());
                            holder.setText(R.id.course_price, "￥" + mData.getPrice() + "元");
                            holder.setText(R.id.student_num, mData.getSale_num() + "人在学习");
                            holder.setText(R.id.original_price, "￥" + mData.getMarket_price() + "元");

                            TextView mOrginalPrice = (TextView) holder.getConvertView().findViewById(R.id.original_price);
//                                mOrginalPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                            mOrginalPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
                            ImageView mCourseCover = (ImageView) holder.getConvertView().findViewById(R.id.cover_photo);
                            Glide.with(getActivity()).load(mData.getCover_image()).apply(GloableConstant.getInstance().getDefaultOptionssmall()).into(mCourseCover);
                            TextView textView = (TextView) holder.getConvertView().findViewById(R.id.give_book);
                            textView.setVisibility(View.GONE);
                            holder.setOnClickListener(R.id.course_item, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    course_id = mData.getCourse_id();
                                    intent = new Intent(getActivity(), CoursesDetialActivity.class);
                                    intent.putExtra("course_id", course_id);
                                    intent.putExtra("type",mData.getType());
                                    startActivity(intent);

                                }
                            });
                            holder.setOnLongClickListener(R.id.course_item, new View.OnLongClickListener() {
                                @Override
                                public boolean onLongClick(final View v) {
                                    selfDialog = new SelfDialog(getActivity());
                                    selfDialog.setTitle("是否取消收藏");
                                    selfDialog.setYesOnclickListener("确定", new SelfDialog.onYesOnclickListener() {
                                        @Override
                                        public void onYesClick() {
                                            course_id = result.getData().get(position).getCourse_id();
                                            delCollection();
                                            result.getData().remove(position);
                                            notifyDataSetChanged();
                                            selfDialog.dismiss();
                                        }
                                    });
                                    selfDialog.setNoOnclickListener("取消", new SelfDialog.onNoOnclickListener() {
                                        @Override
                                        public void onNoClick() {
                                            ToastUtil.showShortToast("取消");
                                            selfDialog.dismiss();
                                        }
                                    });
                                    selfDialog.show();

                                    return false;
                                }
                            });
                        }
                    });
                }else{
                        if(mPage > 1){
                            ToastUtil.showShortToast("没有更多数据了");
                        }else{
                            mPullToRefresh.showView(ViewStatus.EMPTY_STATUS);
                        }
                    }
                }else if(result.getStatus_code().equals("203")){
                    if(mPage > 1){
                        ToastUtil.showShortToast("没有更多数据了");
                    }else{
                        mPullToRefresh.showView(ViewStatus.EMPTY_STATUS);
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
                GloableConstant.getInstance().stopProgressDialog();
                mPullToRefresh.finishRefresh();
                mPullToRefresh.finishLoadMore();
                mPullToRefresh.showView(ViewStatus.EMPTY_STATUS);
            }
        });
    }

    /**
     * 取消收藏
     */
    private void delCollection(){
        Map<String,Object> map=new HashMap<>();
        map.put("type", "1");
        map.put("goods_id",course_id);
//        map.put("user_id", 1);
        map.put("user_id",SPUtils.getSpValues(SPKeyValuesUtils.SP_USER_ID, SPUtils.TYPE_STRING));
        Log.e("*******map", map.toString());
        GloableConstant.getInstance().startProgressDialog(getActivity());
        Xutil.Post(ApiUrl.DELCOLLECT,map,new MyCallBack<SupportResponse>(){
            @Override
            public void onSuccess(SupportResponse result) {
                super.onSuccess(result);
                GloableConstant.getInstance().stopProgressDialog();
                Log.e("*********result",result.getStatus_code());
                if (result.getStatus_code().equals("204")){
                    ToastUtil.showShortToast(result.getMessage());
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
            }
        });
    }

}
