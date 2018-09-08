package com.jiaoshizige.teacherexam.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

import com.bumptech.glide.Glide;
import com.jwenfeng.library.pulltorefresh.BaseRefreshListener;
import com.jwenfeng.library.pulltorefresh.ViewStatus;
import com.nex3z.flowlayout.FlowLayout;
import com.jiaoshizige.teacherexam.R;
import com.jiaoshizige.teacherexam.base.GloableConstant;
import com.jiaoshizige.teacherexam.base.MBaseFragment;
import com.jiaoshizige.teacherexam.http.ApiUrl;
import com.jiaoshizige.teacherexam.http.MyCallBack;
import com.jiaoshizige.teacherexam.http.Xutil;
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

/**
 * Created by Administrator on 2017/a1/13.
 * 评价
 */

public class EvaluateFragment extends MBaseFragment{
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
    private String mPageNum = "1";
    private String mPageSize = "10";
    private int mPage = 1;
    private String mGoodId;
    private String mTyp;////1课程2图书3班级
    private String mTagId;
    @SuppressLint("ValidFragment")
    public EvaluateFragment(String good_id, String type) {
        this.mGoodId = good_id;
        this.mTyp = type;
    }

    public EvaluateFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.evaluate_fragment,container,false);
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
        requestEvaluate(mGoodId,mTyp);
        mEvaluateList.setNestedScrollingEnabled(false);
        NestedScrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                mPullToRefresh.setEnabled
                        (NestedScrollView.getScrollY() == 0);
            }
        });
        mPullToRefresh.setRefreshListener(new BaseRefreshListener() {
            @Override
            public void refresh() {
                mPageNum = "1";
                mPage = 1;
                requestEvaluate(mGoodId,mTyp);
            }
            @Override
            public void loadMore() {
                mPage++;
                mPageNum = String.valueOf(mPage);
                requestEvaluate(mGoodId,mTyp);
            }
        });
        mPullToRefresh.setOnErrorListener(new PullToRefreshLayoutRewrite.onClickCallBackListener() {
            @Override
            public void onCallBackListener(View v) {
                mPageNum = "1";
                mPage = 1;
                requestEvaluate(mGoodId,mTyp);
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
    private void requestEvaluate(String id,String type){
        Map<String ,String> map = new HashMap<>();
        map.put("goods_id",id);//图书id或课程id或班级id
        if(type.equals("1")){//在评价的fragment中 因为课程是1班级是2  所以type为1传入评价为3 type为2传入评价为1  所以设定图书type为3 评价传入为2
            map.put("type","3");//1课程2图书3班级
        }else if(type.equals("2")){
            map.put("type","1");//1课程2图书3班级
        }else if(type.equals("3")){
            map.put("type","2");//1课程2图书3班级
        }
//        map.put("type",type);//1课程2图书3班级
//        map.put("type",type);//1课程2图书3班级
//        map.put("goods_id","16");//图书id或课程id或班级id
        map.put("page",mPageNum);
        map.put("page_size",mPageSize);
        map.put("tag_id",mTagId);//	1全部，2满意，3不满意，4有图，其他标签传对应id
        map.put("user_id", (String) SPUtils.getSpValues(SPKeyValuesUtils.SP_USER_ID, SPUtils.TYPE_STRING));
        Log.e("TGs",map.toString());
        GloableConstant.getInstance().startProgressDialog(getActivity());
        Xutil.GET(ApiUrl.EVALUATELIST,map,new MyCallBack<EvaluateResponse>(){
            @Override
            public void onSuccess(final EvaluateResponse result) {
                super.onSuccess(result);
                GloableConstant.getInstance().stopProgressDialog1();
                mPullToRefresh.finishRefresh();
                mPullToRefresh.finishLoadMore();
                mPullToRefresh.showView(ViewStatus.CONTENT_STATUS);
                if(result.getStatus_code().equals("200")){

                    if(result.getData() != null){
                        if(result.getData().getComment() != null && result.getData().getComment().size() > 0){
                            Log.e("TAGsssw","SSSc22222");
//                            mCourseNoData.setVisibility(View.GONE);
//                            mCourseError.setVisibility(View.GONE);
                            mEvaluateList.setLayoutManager(new LinearLayoutManager(getActivity()));
                            CommonAdapter mAdapter  = new CommonAdapter<EvaluateResponse.mComment>(getActivity(),R.layout.item_evaluate_layout,result.getData().getComment()) {
                                @Override
                                protected void convert(final ViewHolder holder, final EvaluateResponse.mComment mComment, int position) {
                                    holder.setText(R.id.nickname,mComment.getNick_name());
                                    holder.setText(R.id.time,mComment.getCreated_at());
                                    holder.setText(R.id.content,mComment.getContent());
                                    holder.setText(R.id.zan_num,mComment.getCount_zan());
                                    if(mComment.getIs_like().equals("1")){
                                        holder.setBackgroundRes(R.id.zan,R.mipmap.common_like_pre);
                                    }else{
                                        holder.setBackgroundRes(R.id.zan,R.mipmap.common_like);
                                    }
                                    RatingBar mRatingBar = (RatingBar) holder.getConvertView().findViewById(R.id.ratingbar);
                                    mRatingBar.setRating(Float.valueOf(mComment.getRank()));
                                    ImageView mPhoto = (ImageView) holder.getConvertView().findViewById(R.id.photo);
                                    Glide.with(getActivity()).load(mComment.getAvatar()).apply(GloableConstant.getInstance().getOptions()).into(mPhoto);
                                    LinearLayout mInags = (LinearLayout) holder.getConvertView().findViewById(R.id.view_group);
                                    if(mComment.getImage() != null && mComment.getImage().size() > 0){
                                        WindowManager wm = (WindowManager) mContext
                                                .getSystemService(getActivity().WINDOW_SERVICE);
                                        int width = wm.getDefaultDisplay().getWidth();
                                        int height = wm.getDefaultDisplay().getHeight();
                                        for (int i = 0 ;i < mComment.getImage().size(); i++){
                                            ImageView imageView = new ImageView(mContext);
                                            imageView.setLayoutParams(new ViewGroup.LayoutParams((width / 5), (width / 5)));
                                            Glide.with(getActivity()).load(mComment.getImage().get(i).getImages()).apply(GloableConstant.getInstance().getDefaultOptionssmall()).into(imageView);
                                            imageView.setPadding(10,0,10,0);
                                            mInags.addView(imageView);
                                        }
                                    }else{
                                        mInags.removeAllViews();
                                    }
                                    holder.setOnClickListener(R.id.zan, new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            requestGetZan(mComment.getId(),holder);
                                            notifyDataSetChanged();
                                        }
                                    });
                                }
                            };
                            HeaderAndFooterWrapper mHeaderAndFooterWrapper = new HeaderAndFooterWrapper(mAdapter);
                            LayoutInflater inflater = LayoutInflater.from(getActivity());
                            View mHeader = LayoutInflater.from(getActivity()).inflate(R.layout.evaluate_header_layout,null);
                            LinearLayout item_header = (LinearLayout) mHeader.findViewById(R.id.item_header);
                            LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(LinearLayoutCompat.LayoutParams.MATCH_PARENT, LinearLayoutCompat.LayoutParams.WRAP_CONTENT);
                            item_header.setLayoutParams(params);
                            mHeaderAndFooterWrapper.addHeaderView(mHeader);
                            mEvaluateList.setAdapter(mHeaderAndFooterWrapper);
                            mHeaderAndFooterWrapper.notifyDataSetChanged();
                            mFlowLayout = (FlowLayout) mHeader.findViewById(R.id.flow_layout);
                            RatingBar mRatingBar = (RatingBar) mHeader.findViewById(R.id.toatal_ratingbar);
                            TextView mTotalEvaluate = (TextView) mHeader.findViewById(R.id.total_evaluate);
                            mRatingBar.setRating(Float.valueOf(result.getData().getAvg_rank()));
                            mTotalEvaluate.setText(result.getData().getAvg_rank()+"分");
                            if(result.getData().getLabel() != null && result.getData().getLabel().size() > 0){
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
                            }
                        }else{
                            Log.e("TAG","SSSc22222");
                            if(mPage > 1){
                                ToastUtil.showShortToast("没有更多数据了");
                            }else {
                                Log.e("TAG","SSSc22222");
                                mPullToRefresh.showView(ViewStatus.EMPTY_STATUS);
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
                }else if(result.getStatus_code().equals("204")){
                    if(mPage > 1){
                        ToastUtil.showShortToast("没有更多数据了");
                    }else {
                        mPullToRefresh.showView(ViewStatus.EMPTY_STATUS);
//                        mCourseNoData.setVisibility(View.VISIBLE);
                    }
                }
            }
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
                Log.e("TAG",ex.getMessage()+"");
                GloableConstant.getInstance().stopProgressDialog();
//                mCourseError.setVisibility(View.VISIBLE);
//                mCourseNoData.setVisibility(View.GONE);
                mPullToRefresh.finishRefresh();
                mPullToRefresh.finishLoadMore();
                mPullToRefresh.showView(ViewStatus.EMPTY_STATUS);

            }
        });
    }
    /**
     * 点赞
     */
    private void requestGetZan(String evaluateId, final ViewHolder holder){
    Map<String,Object> map = new HashMap<>();
//    map.put("user_id","1");
    map.put("user_id", SPUtils.getSpValues(SPKeyValuesUtils.SP_USER_ID, SPUtils.TYPE_STRING));
    map.put("be_like_id",evaluateId);
    map.put("type","1");

    Xutil.Post(ApiUrl.GETZAN,map,new MyCallBack<GetZanReplyResponse>(){
        @Override
        public void onSuccess(GetZanReplyResponse result) {
            super.onSuccess(result);
            ImageView mGiveZan = (ImageView) holder.getConvertView().findViewById(R.id.zan);
            if(result.getStatus_code().equals("204")){
                ToastUtil.showShortToast("点赞成功");
                holder.setText(R.id.zan_num,result.getData().getCount());
                mGiveZan.setBackground(ContextCompat.getDrawable(getActivity(),R.mipmap.common_like_pre));
            }else if(result.getStatus_code().equals("205")){
                ToastUtil.showShortToast("取消点赞成功");
                holder.setText(R.id.zan_num,result.getData().getCount());
                mGiveZan.setBackground(ContextCompat.getDrawable(getActivity(),R.mipmap.common_like));
            }
        }

        @Override
        public void onError(Throwable ex, boolean isOnCallback) {
            super.onError(ex, isOnCallback);
        }
    });
}
}
