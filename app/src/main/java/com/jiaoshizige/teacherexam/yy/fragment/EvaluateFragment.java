package com.jiaoshizige.teacherexam.yy.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.jiaoshizige.teacherexam.widgets.HeadViewCourseEvaluateList;
import com.jwenfeng.library.pulltorefresh.BaseRefreshListener;
import com.jwenfeng.library.pulltorefresh.ViewStatus;
import com.nex3z.flowlayout.FlowLayout;
import com.jiaoshizige.teacherexam.R;
import com.jiaoshizige.teacherexam.base.CommRecyclerViewAdapter;
import com.jiaoshizige.teacherexam.base.GloableConstant;
import com.jiaoshizige.teacherexam.base.MBaseFragment;
import com.jiaoshizige.teacherexam.base.ViewHolderZhy;
import com.jiaoshizige.teacherexam.http.ApiUrl;
import com.jiaoshizige.teacherexam.http.MyCallBack;
import com.jiaoshizige.teacherexam.http.Xutil;
import com.jiaoshizige.teacherexam.model.CourseCollectResponse;
import com.jiaoshizige.teacherexam.model.EvaluateResponse;
import com.jiaoshizige.teacherexam.model.GetZanReplyResponse;
import com.jiaoshizige.teacherexam.utils.PullToRefreshLayoutRewrite;
import com.jiaoshizige.teacherexam.utils.SPKeyValuesUtils;
import com.jiaoshizige.teacherexam.utils.SPUtils;
import com.jiaoshizige.teacherexam.utils.ToastUtil;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;
import com.zhy.adapter.recyclerview.wrapper.HeaderAndFooterWrapper;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;

import static com.jwenfeng.library.pulltorefresh.ViewStatus.EMPTY_STATUS;

/**
 * Created by Administrator on 2017/a1/13.
 * 评价
 */

public class EvaluateFragment extends MBaseFragment {
    @BindView(R.id.evaluate_list)
    RecyclerView mEvaluateList;
    //    @BindView(R.id.course_no_data)
//    LinearLayout mCourseNoData;
//    @BindView(R.id.course_error)
//    LinearLayout mCourseError;
    @BindView(R.id.pulltorefresh)
    PullToRefreshLayoutRewrite mPullToRefresh;
    @BindView(R.id.scroll)
    android.support.v4.widget.NestedScrollView NestedScrollView;
    //    @BindView(R.id.layout_swipe_refresh)
//    SwipeRefreshView mSwipeRefreshView;
    FlowLayout mFlowLayout;
    private String mPageSize = "10";
    private int mPage = 1;
    private String mGoodId;
    private String mTyp;////1课程2图书3班级
    private String mTagId;
    private CommRecyclerViewAdapter<EvaluateResponse.mComment> mAdapter;

    @SuppressLint("ValidFragment")
    public EvaluateFragment(String good_id, String type) {
        this.mGoodId = good_id;
        this.mTyp = type;
    }

    @Override
    public void onResume() {
        super.onResume();
        mPage = 1;
        requestEvaluate(mGoodId, mTyp);
    }

    public EvaluateFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.evaluate_fragment_new, container, false);
    }

    @Override
    protected void onInitViews(Bundle savedInstanceState) {
//        mSwipeRefreshView.setEnabled(false);
       /* mSwipeRefreshView.setItemCount(2);
        mSwipeRefreshView.measure(0, 0);*/
       /* mSwipeRefreshView.setOnLoadMoreListener(new SwipeRefreshView.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                loadMoreData();
            }
        });*/
        mPullToRefresh.setCanLoadMore(false);
        mPullToRefresh.setCanRefresh(false);
        requestEvaluate(mGoodId, mTyp);
        mEvaluateList.setNestedScrollingEnabled(false);
        NestedScrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                mPullToRefresh.setEnabled
                        (NestedScrollView.getScrollY() == 0);
            }
        });

        NestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY > oldScrollY) {
                    // 向下滑动
                }

                if (scrollY < oldScrollY) {
                    // 向上滑动
                }

                if (scrollY == 0) {
                    // 顶部
                }

                if (scrollY == (v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight())) {
                    // 底部
                    mPage++;
                    requestEvaluate(mGoodId, mTyp);
                }
            }
        });

//        mEvaluateList.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                //当前RecyclerView显示出来的最后一个的item的position
//                int lastPosition = -1;
//
//                //当前状态为停止滑动状态SCROLL_STATE_IDLE时
//                if(newState == RecyclerView.SCROLL_STATE_IDLE){
//                    RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
//                    if(layoutManager instanceof GridLayoutManager){
//                        //通过LayoutManager找到当前显示的最后的item的position
//                        lastPosition = ((GridLayoutManager) layoutManager).findLastVisibleItemPosition();
//                    }else if(layoutManager instanceof LinearLayoutManager){
//                        lastPosition = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
//                    }else if(layoutManager instanceof StaggeredGridLayoutManager){
//                        //因为StaggeredGridLayoutManager的特殊性可能导致最后显示的item存在多个，所以这里取到的是一个数组
//                        //得到这个数组后再取到数组中position值最大的那个就是最后显示的position值了
//                        int[] lastPositions = new int[((StaggeredGridLayoutManager) layoutManager).getSpanCount()];
//                        ((StaggeredGridLayoutManager) layoutManager).findLastVisibleItemPositions(lastPositions);
//                        lastPosition = findMax(lastPositions);
//                    }
//
//                    //时判断界面显示的最后item的position是否等于itemCount总数-1也就是最后一个item的position
//                    //如果相等则说明已经滑动到最后了
//                    if(lastPosition == recyclerView.getLayoutManager().getItemCount()-1){
//                        mPage++;
//                        requestEvaluate(mGoodId, mTyp);
//                    }
//                }
//            }
//
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//
//            }
//
//        });
        mPullToRefresh.setRefreshListener(new BaseRefreshListener() {
            @Override
            public void refresh() {
                mPage = 1;
                requestEvaluate(mGoodId, mTyp);
            }

            @Override
            public void loadMore() {
                mPage++;
                requestEvaluate(mGoodId, mTyp);
            }
        });
        mPullToRefresh.setOnErrorListener(new PullToRefreshLayoutRewrite.onClickCallBackListener() {
            @Override
            public void onCallBackListener(View v) {
                mPage = 1;
                requestEvaluate(mGoodId, mTyp);
            }
        });

    /*    for (final String b: mDatas) {
            TextView textView = new TextView(getActivity());
            textView.setPadding(16,8,16,8);
            textView.setText(b);
            textView.setTextSize(16);
            textView.setBackgroundResource(R.drawable.gray_shap_5);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ToastUtil.showShortToast(b.toString());
                }
            });
            mFlowLayout.addView(textView);
        }*/
    }

    //找到数组中的最大值
    private int findMax(int[] lastPositions) {
        int max = lastPositions[0];
        for (int value : lastPositions) {
            if (value > max) {
                max = value;
            }
        }
        return max;
    }

    private void requestEvaluate(String id, String type) {
        Map<String, String> map = new HashMap<>();
        map.put("goods_id", id);//图书id或课程id或班级id
        if (type.equals("1")) {//在评价的fragment中 因为课程是1班级是2  所以type为1传入评价为3 type为2传入评价为1  所以设定图书type为3 评价传入为2
            map.put("type", "3");//1课程2图书3班级
        } else if (type.equals("2")) {
            map.put("type", "1");//1课程2图书3班级
        } else if (type.equals("3")) {
            map.put("type", "2");//1课程2图书3班级
        }
//        map.put("type",type);//1课程2图书3班级
//        map.put("type",type);//1课程2图书3班级
//        map.put("goods_id","16");//图书id或课程id或班级id
        map.put("page", mPage+"");
        map.put("page_size", mPageSize);
        map.put("tag_id", mTagId);//	1全部，2满意，3不满意，4有图，其他标签传对应id
        map.put("user_id", (String) SPUtils.getSpValues(SPKeyValuesUtils.SP_USER_ID, SPUtils.TYPE_STRING));
        Log.e("TGs", map.toString());
        GloableConstant.getInstance().startProgressDialog(getActivity());
        Xutil.GET(ApiUrl.EVALUATELIST, map, new MyCallBack<EvaluateResponse>() {
            @Override
            public void onSuccess(final EvaluateResponse result) {
                super.onSuccess(result);
                GloableConstant.getInstance().stopProgressDialog1();
                mPullToRefresh.finishRefresh();
                mPullToRefresh.finishLoadMore();
                mEvaluateList.setNestedScrollingEnabled(false);
                mPullToRefresh.showView(ViewStatus.CONTENT_STATUS);
                if (result.getStatus_code().equals("200")) {
                    if (result.getData() != null) {
                        if(mPage == 1){
                            getAdapter().updateData(result.getData().getComment());
                        } else {
                            getAdapter().appendData(result.getData().getComment());
                        }
                        if (result.getData().getComment() != null && result.getData().getComment().size() > 0) {
                            Log.e("TAGsssw", "SSSc22222");
//                            mEvaluateList.setLayoutManager(new LinearLayoutManager(getActivity()));
//                            CommonAdapter mAdapter = new CommonAdapter<EvaluateResponse.mComment>(getActivity(), R.layout.item_evaluate_layout, result.getData().getComment()) {
//                                @Override
//                                protected void convert(final ViewHolder holder, final EvaluateResponse.mComment mComment, int position) {
//                                    holder.setText(R.id.nickname, mComment.getNick_name());
//                                    holder.setText(R.id.time, mComment.getCreated_at());
//                                    holder.setText(R.id.content, mComment.getContent());
//                                    holder.setText(R.id.zan_num, mComment.getCount_zan());
//                                    if (mComment.getIs_like().equals("1")) {
//                                        holder.setBackgroundRes(R.id.zan, R.mipmap.common_like_pre);
//                                    } else {
//                                        holder.setBackgroundRes(R.id.zan, R.mipmap.common_like);
//                                    }
//                                    RatingBar mRatingBar = (RatingBar) holder.getConvertView().findViewById(R.id.ratingbar);
//                                    mRatingBar.setRating(Float.valueOf(mComment.getRank()));
//                                    ImageView mPhoto = (ImageView) holder.getConvertView().findViewById(R.id.photo);
//                                    Glide.with(getActivity()).load(mComment.getAvatar()).apply(GloableConstant.getInstance().getOptions()).into(mPhoto);
//                                    LinearLayout mInags = (LinearLayout) holder.getConvertView().findViewById(R.id.view_group);
//                                    if (mComment.getImage() != null && mComment.getImage().size() > 0) {
//                                        WindowManager wm = (WindowManager) mContext
//                                                .getSystemService(getActivity().WINDOW_SERVICE);
//                                        int width = wm.getDefaultDisplay().getWidth();
//                                        int height = wm.getDefaultDisplay().getHeight();
//                                        for (int i = 0; i < mComment.getImage().size(); i++) {
//                                            ImageView imageView = new ImageView(mContext);
//                                            imageView.setLayoutParams(new ViewGroup.LayoutParams((width / 5), (width / 5)));
//                                            Glide.with(getActivity()).load(mComment.getImage().get(i).getImages()).apply(GloableConstant.getInstance().getDefaultOptionssmall()).into(imageView);
//                                            imageView.setPadding(10, 0, 10, 0);
//                                            mInags.addView(imageView);
//                                        }
//                                    } else {
//                                        mInags.removeAllViews();
//                                    }
//                                    holder.setOnClickListener(R.id.zan, new View.OnClickListener() {
//                                        @Override
//                                        public void onClick(View v) {
//                                            requestGetZan(mComment.getId(), holder);
//                                            notifyDataSetChanged();
//                                        }
//                                    });
//                                }
//                            };
                            HeadViewCourseEvaluateList headView = new HeadViewCourseEvaluateList(getActivity());
                            headView.bindData(result)
                                    .setOnEvaluateTabClick(new HeadViewCourseEvaluateList.onEvaluateTabClick() {
                                        @Override
                                        public void onTabClick(View view, String tagId) {
                                            mPage = 1;
                                            mTagId = tagId;
                                            requestEvaluate(mGoodId, mTyp);
                                        }
                                    });
                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayoutCompat.LayoutParams.MATCH_PARENT, LinearLayoutCompat.LayoutParams.WRAP_CONTENT);
                            headView.setLayoutParams(params);
                            HeaderAndFooterWrapper mHeaderAndFooterWrapper = new HeaderAndFooterWrapper(getAdapter());
                            mHeaderAndFooterWrapper.addHeaderView(headView);
                            mEvaluateList.setAdapter(mHeaderAndFooterWrapper);
                            mHeaderAndFooterWrapper.notifyDataSetChanged();

//                            LayoutInflater inflater = LayoutInflater.from(getActivity());
//                            View mHeader = LayoutInflater.from(getActivity()).inflate(R.layout.evaluate_header_layout, null);
//                            LinearLayout item_header = (LinearLayout) mHeader.findViewById(R.id.item_header);
//                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayoutCompat.LayoutParams.MATCH_PARENT, LinearLayoutCompat.LayoutParams.WRAP_CONTENT);
//                            item_header.setLayoutParams(params);

//                            mHeaderAndFooterWrapper.addHeaderView(mHeader);
//                            mEvaluateList.setAdapter(mHeaderAndFooterWrapper);
//                            mHeaderAndFooterWrapper.notifyDataSetChanged();
//                            mFlowLayout = (FlowLayout) mHeader.findViewById(R.id.flow_layout);
//                            RatingBar mRatingBar = (RatingBar) mHeader.findViewById(R.id.toatal_ratingbar);
//                            TextView mTotalEvaluate = (TextView) mHeader.findViewById(R.id.total_evaluate);
//                            mRatingBar.setRating(Float.valueOf(result.getData().getAvg_rank()));
//                            mTotalEvaluate.setText(result.getData().getAvg_rank() + "分");
//                            if (result.getData().getLabel() != null && result.getData().getLabel().size() > 0) {
//                                Log.e("TAG`1", "55555");
//                                int i = 0;
//                                for (final EvaluateResponse.mLabel mLabel : result.getData().getLabel()) {
//                                    i++;
//                                    TextView textView = new TextView(getActivity());
//                                    textView.setPadding(16, 10, 16, 10);
//                                    textView.setText(mLabel.getLabel_name() + "(" + mLabel.getCount() + ")");
//                                    textView.setTextSize(12);
//                                    if (i == 3) {
//                                        textView.setBackgroundResource(R.drawable.gray_shap_5);
//                                    } else {
//                                        textView.setBackgroundResource(R.drawable.buy_orange_shape);
//                                    }
//                                    textView.setOnClickListener(new View.OnClickListener() {
//                                        @Override
//                                        public void onClick(View v) {
//                                            mTagId = mLabel.getId();
//                                            requestEvaluate(mGoodId, mTyp);
//                                        }
//                                    });
//                                    mFlowLayout.addView(textView);
//                                }
//                            }
                        } else {
                            Log.e("TAG", "SSSc22222");
                            if (mPage > 1) {
                                mPage--;
                                ToastUtil.showShortToast("没有更多数据了");
                            } else {
                                Log.e("TAG", "SSSc22222");
                                showEmpty(result);

//                                mPullToRefresh.showView(EMPTY_STATUS);
//                                mCourseNoData.setVisibility(View.VISIBLE);
                            }
                        }
                      /*  if(result.getData().getLabel() != null && result.getData().getLabel().size() > 0){
                            Log.e("TAG`1","55555");
                            int i = 0;
                            for (final EvaluateResponse.mLabel mLabel: result.getData().getLabel()) {
                                i++;
                                TextView textView = new TextView(getActivity());
                                textView.setPadding(16,10,16,10);
                                textView.setText(mLabel.getLabel_name()+"("+mLabel.getCount()+")");
                                textView.setTextSize(12);
                                if(i == 3){
                                    textView.setBackgroundResource(R.drawable.gray_shap_5);
                                }else{
                                    textView.setBackgroundResource(R.drawable.buy_orange_shape);
                                }
                                textView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        mTagId = mLabel.getId();
                                        requestEvaluate(mGoodId,mTyp);
                                    }
                                });
                                mFlowLayout.addView(textView);
                            }
                        }*/
                    }
                } else if (result.getStatus_code().equals("204")) {
                    if (mPage > 1) {
                        mPage--;
                        ToastUtil.showShortToast("没有更多数据了");
                    } else {
                        showEmpty(result);
//                        mPullToRefresh.showView(EMPTY_STATUS);
//                        mCourseNoData.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
                Log.e("TAG", ex.getMessage() + "");
                GloableConstant.getInstance().stopProgressDialog();
//                mCourseError.setVisibility(View.VISIBLE);
//                mCourseNoData.setVisibility(View.GONE);
                mPullToRefresh.finishRefresh();
                mPullToRefresh.finishLoadMore();
                mPullToRefresh.showView(EMPTY_STATUS);

            }
        });
    }

    private HeadViewCourseEvaluateList mEmptyTopEvaluateList;
    private void showEmpty(EvaluateResponse result){
        mPullToRefresh.showView(EMPTY_STATUS);
        mEvaluateList.setNestedScrollingEnabled(true);
        if(mEmptyTopEvaluateList == null){
            mEmptyTopEvaluateList = (HeadViewCourseEvaluateList) mPullToRefresh.getView(EMPTY_STATUS).findViewById(R.id.layout_empty_head);
            mEmptyTopEvaluateList.bindData(result)
                    .setOnEvaluateTabClick(new HeadViewCourseEvaluateList.onEvaluateTabClick() {
                        @Override
                        public void onTabClick(View view, String tagId) {
                            mPage = 1;
                            mTagId = tagId;
                            requestEvaluate(mGoodId, mTyp);
                        }
                    });
        }
    }
    /**
     * 点赞
     */
    private void requestGetZan(final EvaluateResponse.mComment comment, final ViewHolderZhy holder) {
        Map<String, Object> map = new HashMap<>();
//    map.put("user_id","1");
        map.put("user_id", SPUtils.getSpValues(SPKeyValuesUtils.SP_USER_ID, SPUtils.TYPE_STRING));
        map.put("be_like_id", comment.getId());
        map.put("type", "1");

        Xutil.Post(ApiUrl.GETZAN, map, new MyCallBack<GetZanReplyResponse>() {
            @Override
            public void onSuccess(GetZanReplyResponse result) {
                super.onSuccess(result);
                if (result.getStatus_code().equals("204")) {
                    ToastUtil.showShortToast("点赞成功");
                    comment.setIs_like("1");
                    holder.setText(R.id.zan_num,result.getData().getCount());
                    holder.setBackgroundRes(R.id.zan, R.mipmap.common_like_pre);
//                    mAdapter.notifyItemChanged(mAdapter.getDatas().indexOf(comment));
                } else if (result.getStatus_code().equals("205")) {
                    ToastUtil.showShortToast("取消点赞成功");
                    comment.setIs_like("0");
                    holder.setText(R.id.zan_num,result.getData().getCount());
                    holder.setBackgroundRes(R.id.zan, R.mipmap.common_like);
//                    mAdapter.notifyDataSetChanged();
//                    mAdapter.notifyItemChanged(mAdapter.getDatas().indexOf(comment));
//                    mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
            }
        });
    }

    private CommRecyclerViewAdapter<EvaluateResponse.mComment> getAdapter() {
        if (null == mAdapter) {
            mAdapter = new CommRecyclerViewAdapter<EvaluateResponse.mComment>(getActivity(), R.layout.item_evaluate_layout, null) {
                @Override
                protected void convert(final ViewHolderZhy holder, final EvaluateResponse.mComment mComment, final int position) {
                    holder.setText(R.id.nickname, mComment.getNick_name());
                    holder.setText(R.id.time, mComment.getCreated_at());
                    holder.setText(R.id.content, mComment.getContent());
                    holder.setText(R.id.zan_num, mComment.getCount_zan());
                    if (mComment.getIs_like().equals("1")) {
                        holder.setBackgroundRes(R.id.zan, R.mipmap.common_like_pre);
                    } else {
                        holder.setBackgroundRes(R.id.zan, R.mipmap.common_like);
                    }
                    RatingBar mRatingBar = (RatingBar) holder.getConvertView().findViewById(R.id.ratingbar);
                    mRatingBar.setRating(Float.valueOf(mComment.getRank()));
                    ImageView mPhoto = (ImageView) holder.getConvertView().findViewById(R.id.photo);
                    Glide.with(getActivity()).load(mComment.getAvatar()).apply(GloableConstant.getInstance().getOptions()).into(mPhoto);
                    LinearLayout mInags = (LinearLayout) holder.getConvertView().findViewById(R.id.view_group);
                    if (mComment.getImage() != null && mComment.getImage().size() > 0) {
                        WindowManager wm = (WindowManager) mContext
                                .getSystemService(getActivity().WINDOW_SERVICE);
                        int width = wm.getDefaultDisplay().getWidth();
                        int height = wm.getDefaultDisplay().getHeight();
                        for (int i = 0; i < mComment.getImage().size(); i++) {
                            ImageView imageView = new ImageView(mContext);
                            imageView.setLayoutParams(new ViewGroup.LayoutParams((width / 5), (width / 5)));
                            Glide.with(getActivity()).load(mComment.getImage().get(i).getImages())
                                    .apply(GloableConstant.getInstance().getDefaultOptionssmall()).into(imageView);
                            imageView.setPadding(10, 0, 10, 0);
                            mInags.addView(imageView);
                        }
                    } else {
                        mInags.removeAllViews();
                    }
                    holder.setOnClickListener(R.id.zan, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            requestGetZan(mComment,holder);
                            notifyDataSetChanged();
                        }
                    });
                }
            };
            mEvaluateList.setLayoutManager(new LinearLayoutManager(getActivity()));
            mEvaluateList.setAdapter(mAdapter);
        }
        return mAdapter;
    }
}
