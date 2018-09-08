package com.jiaoshizige.teacherexam.yy.fragment;

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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.jwenfeng.library.pulltorefresh.BaseRefreshListener;
import com.jwenfeng.library.pulltorefresh.ViewStatus;
import com.umeng.analytics.MobclickAgent;
import com.jiaoshizige.teacherexam.R;
import com.jiaoshizige.teacherexam.activity.BookDetailActivity;
import com.jiaoshizige.teacherexam.activity.CoursesDetialActivity;
import com.jiaoshizige.teacherexam.base.CommRecyclerViewAdapter;
import com.jiaoshizige.teacherexam.base.GloableConstant;
import com.jiaoshizige.teacherexam.base.MPBaseFragment;
import com.jiaoshizige.teacherexam.base.ViewHolderZhy;
import com.jiaoshizige.teacherexam.http.ApiUrl;
import com.jiaoshizige.teacherexam.http.MyCallBack;
import com.jiaoshizige.teacherexam.http.SupportResponse;
import com.jiaoshizige.teacherexam.http.Xutil;
import com.jiaoshizige.teacherexam.model.BookListResponse;
import com.jiaoshizige.teacherexam.model.CourseCollectResponse;
import com.jiaoshizige.teacherexam.utils.AppLog;
import com.jiaoshizige.teacherexam.utils.PullToRefreshLayoutRewrite;
import com.jiaoshizige.teacherexam.utils.SPKeyValuesUtils;
import com.jiaoshizige.teacherexam.utils.SPUtils;
import com.jiaoshizige.teacherexam.utils.ToastUtil;
import com.jiaoshizige.teacherexam.utils.ToolUtils;
import com.jiaoshizige.teacherexam.widgets.SelfDialog;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

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
    private String user_id, course_id;
    private String pagesize = "20";
    private SelfDialog selfDialog;
    private Intent intent;
    private Context mContext;
    View view;
    private int mPage = 1;
    private CommRecyclerViewAdapter<CourseCollectResponse.mData> mAdapter;

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
        mPage = 1;
        CollectCourse();
        MobclickAgent.onPageStart("课程收藏");
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("课程收藏");
    }

    @Override
    protected void onInitViews(Bundle savedInstanceState) {

        EventBus.getDefault().register(this);
        mScreenLy.setVisibility(View.GONE);
        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) mCourseRecycler.getLayoutParams();
        lp.setMargins(0, ToolUtils.dip2px(getActivity(), 10), 0, 0);
        mCourseRecycler.setLayoutParams(lp);
        user_id = (String) SPUtils.getSpValues(SPKeyValuesUtils.SP_USER_ID, SPUtils.TYPE_STRING);
        mPullToRefresh.setRefreshListener(new BaseRefreshListener() {
            @Override
            public void refresh() {
                mPage = 1;
                CollectCourse();
            }

            @Override
            public void loadMore() {
                mPage++;
                CollectCourse();

            }
        });
        mPullToRefresh.setOnErrorListener(new PullToRefreshLayoutRewrite.onClickCallBackListener() {
            @Override
            public void onCallBackListener(View v) {
                mPage = 1;
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


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void getEventBus(String num) {
        if (num.equals("课程取消收藏")) {
            //这里拿到事件之后吐司一下
//            CollectCourse();
//            ToastUtil.showShortToast("num666666");
        }
    }


    private void CollectCourse() {
        Map<String, Object> map = new HashMap<>();
//        map.put("user_id",1);
        map.put("user_id", SPUtils.getSpValues(SPKeyValuesUtils.SP_USER_ID, SPUtils.TYPE_STRING));
        map.put("type", "1");
        map.put("page", mPage);
        map.put("page_size", pagesize);
        Log.e("*******map", map.toString());
        GloableConstant.getInstance().startProgressDialog(getActivity());
        Xutil.Post(ApiUrl.COLLECT, map, new MyCallBack<CourseCollectResponse>() {
            @Override
            public void onSuccess(final CourseCollectResponse result) {
                super.onSuccess(result);
                GloableConstant.getInstance().stopProgressDialog();
                mPullToRefresh.finishRefresh();
                mPullToRefresh.finishLoadMore();
//                mPullToRefresh.showView(ViewStatus.CONTENT_STATUS);
                Log.e("*******map",result.getStatus_code()+"//getStatus_code///");
                if (result.getStatus_code().equals("200")) {

                    Log.e("*******map",result.getData().size()+"/////");
                    if (result.getData() != null && result.getData().size() > 0) {
                        if (mPage == 1) {
                            getAdapter().updateData(result.getData());
                        } else {
                            getAdapter().appendData(result.getData());
                        }
                    } else {
                        if (mPage > 1) {
                            mPage--;
                            ToastUtil.showShortToast("没有更多数据了");
                        } else {
                            mPullToRefresh.showView(ViewStatus.EMPTY_STATUS);
                        }
                    }
                } else if (result.getStatus_code().equals("203")) {
                    if (mPage > 1) {
                        mPage--;
                        ToastUtil.showShortToast("没有更多数据了");
                    } else {
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
                Log.e("*********ex",ex.getMessage()+"///////");
                GloableConstant.getInstance().stopProgressDialog();
                mPullToRefresh.finishRefresh();
                mPullToRefresh.finishLoadMore();
                mPullToRefresh.showView(ViewStatus.ERROR_STATUS);
            }
        });
    }

    /**
     * 获取收藏列表传type:1 包含课程（1）和班级（3）取消时data.getType 1传3 2传1
     * 取消收藏
     */
    private void delCollection(final CourseCollectResponse.mData data) {
        Map<String, Object> map = new HashMap<>();
        map.put("type", "1".equals(data.getType()) ? "3" : "1");
        map.put("goods_id", data.getCourse_id());
//        map.put("user_id", 1);
        map.put("user_id", SPUtils.getSpValues(SPKeyValuesUtils.SP_USER_ID, SPUtils.TYPE_STRING));
        Log.e("*****111111**map", map.toString());
        GloableConstant.getInstance().startProgressDialog(getActivity());
        Xutil.Post(ApiUrl.DELCOLLECT, map, new MyCallBack<SupportResponse>() {
            @Override
            public void onSuccess(SupportResponse result) {
                super.onSuccess(result);
                GloableConstant.getInstance().stopProgressDialog();
                Log.e("*********result", result.getStatus_code());
                if (result.getStatus_code().equals("204")) {
                    getAdapter().removeData(data);
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

    private CommRecyclerViewAdapter<CourseCollectResponse.mData> getAdapter() {
        if (null == mAdapter) {
            mAdapter = new CommRecyclerViewAdapter<CourseCollectResponse.mData>(getActivity(), R.layout.item_my_course_fragment, null) {
                @Override
                protected void convert(ViewHolderZhy holder, final CourseCollectResponse.mData mData, final int position) {
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
                            intent.putExtra("type", mData.getType());
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
                                    delCollection(mData);
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
            };
            mCourseRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
            mCourseRecycler.setAdapter(mAdapter);
        }
        return mAdapter;
    }
}
