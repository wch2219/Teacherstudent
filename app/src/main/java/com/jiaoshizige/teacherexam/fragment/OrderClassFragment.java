package com.jiaoshizige.teacherexam.fragment;

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
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jwenfeng.library.pulltorefresh.BaseRefreshListener;
import com.jwenfeng.library.pulltorefresh.ViewStatus;
import com.jiaoshizige.teacherexam.R;
import com.jiaoshizige.teacherexam.activity.MyOrderCommentActivity;
import com.jiaoshizige.teacherexam.activity.MyOrderDetailActivity;
import com.jiaoshizige.teacherexam.base.GloableConstant;
import com.jiaoshizige.teacherexam.base.MBaseFragment;
import com.jiaoshizige.teacherexam.http.ApiUrl;
import com.jiaoshizige.teacherexam.http.MyCallBack;
import com.jiaoshizige.teacherexam.http.SupportResponse;
import com.jiaoshizige.teacherexam.http.Xutil;
import com.jiaoshizige.teacherexam.model.ClassOrderReaponse;
import com.jiaoshizige.teacherexam.utils.PullToRefreshLayoutRewrite;
import com.jiaoshizige.teacherexam.utils.SPKeyValuesUtils;
import com.jiaoshizige.teacherexam.utils.SPUtils;
import com.jiaoshizige.teacherexam.utils.ToastUtil;
import com.jiaoshizige.teacherexam.widgets.CancelOrOkDialog;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/a1/a1 0011.
 */

public class OrderClassFragment extends MBaseFragment {

    @BindView(R.id.class_list)
    RecyclerView mRecyclerView;
    @BindView(R.id.pulltorefresh)
    PullToRefreshLayoutRewrite mPullToRefresh;
    TextView mStatus;
    TextView mStatua_bar;
    private String user_id;
    private String group_id;
    private Intent intent;
    private String flag = "2";
    private String order_id;


    private List<ClassOrderReaponse.mData> mList;
    private String mPageNum="1" ;
    private String mPageSize="15";
    private int mPage=1;
    private String tag;//判断是否赠送图书0是没有，1是有
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.class_fragment_layout, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        ClassOrder();
    }

    @Override
    protected void onInitViews(Bundle savedInstanceState) {
        user_id = (String) SPUtils.getSpValues(SPKeyValuesUtils.SP_USER_ID, SPUtils.TYPE_STRING);
        intent = new Intent();
        mList=new ArrayList<>();
        mPullToRefresh.setRefreshListener(new BaseRefreshListener() {
            @Override
            public void refresh() {
                mPage=1;
                mPageNum="1";
                ClassOrder();
            }

            @Override
            public void loadMore() {
                mPage++;
                mPageNum=String.valueOf(mPage);
                ClassOrder();
            }
        });
        mPullToRefresh.setOnErrorListener(new PullToRefreshLayoutRewrite.onClickCallBackListener() {
            @Override
            public void onCallBackListener(View v) {
                mPage=1;
                mPageNum="1";
                ClassOrder();
            }
        });

        ClassOrder();
    }

    private void ClassOrder() {
        Map<String, Object> map = new HashMap<>();
//        map.put("user_id", 1);
        map.put("user_id",SPUtils.getSpValues(SPKeyValuesUtils.SP_USER_ID, SPUtils.TYPE_STRING));
        map.put("type", 3);
        map.put("page",mPageNum);
        map.put("page_size",mPageSize);
        GloableConstant.getInstance().startProgressDialog(getActivity());
        Xutil.Post(ApiUrl.ORDER, map, new MyCallBack<ClassOrderReaponse>() {
            @Override
            public void onSuccess(final ClassOrderReaponse result) {
                super.onSuccess(result);
                GloableConstant.getInstance().stopProgressDialog();
                mPullToRefresh.finishRefresh();
                mPullToRefresh.finishLoadMore();
                mPullToRefresh.showView(ViewStatus.CONTENT_STATUS);
                if (result.getStatus_code().equals("200")) {
                    if (result.getData() != null && result.getData().size() > 0){
                        if (mPageNum.equals("1")) {
                            mList.clear();
                            mList = result.getData();
                        } else {
                            if (null == mList) {
                                mList = result.getData();
                            } else {
                                mList.addAll(result.getData());
                            }
                        }
                    mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                    mRecyclerView.setAdapter(new CommonAdapter<ClassOrderReaponse.mData>(getActivity(), R.layout.item_order_class_fragment_layout, result.getData()) {

                        @Override
                        protected void convert(ViewHolder holder, final ClassOrderReaponse.mData mData, final int position) {
                            holder.setText(R.id.time, mData.getCreated_at());
                            holder.setText(R.id.price, mData.getPay_price());
                            holder.setText(R.id.class_title, mData.getGoods().getGoods_name());
                            holder.setText(R.id.market_price, "￥" + mData.getMarket_price());
                            holder.setText(R.id.class_price, "￥" + mData.getPrice());
                            TextView mOrginalPrice = (TextView) holder.getConvertView().findViewById(R.id.market_price);
//                                mOrginalPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                            mOrginalPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
                            RecyclerView mGiveBook = (RecyclerView) holder.getConvertView().findViewById(R.id.recycle_view);
                            TextView textView = (TextView) holder.getConvertView().findViewById(R.id.text);
                            Log.e("**********givebook", mData.getGive_books().size() + "");
                            holder.setOnClickListener(R.id.order, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    order_id = mData.getOrder_id();
                                    intent.setClass(getActivity(), MyOrderDetailActivity.class);
                                    intent.putExtra("order_id", order_id);
                                    intent.putExtra("status", mData.getStatus());
                                    intent.putExtra("tag",tag);
                                    intent.putExtra("flag", flag);
                                    startActivity(intent);
                                }
                            });

                            holder.setOnClickListener(R.id.status_delete, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    order_id = mData.getOrder_id();
                                    CancelOrOkDialog dialog = new CancelOrOkDialog(getActivity(), "确定要删除此订单吗?") {
                                        @Override
                                        public void ok() {
                                            super.ok();
                                            result.getData().remove(position);
                                            notifyItemRemoved(position);
                                            //必须调用这行代码
                                            notifyItemRangeChanged(position, result.getData().size());
                                            deleteOrder(order_id);
                                            notifyDataSetChanged();
                                            dismiss();
                                        }
                                    };
                                    dialog.show();
                                }
                            });

                            if (mData.getGive_books().size() > 0) {
                                mGiveBook.setVisibility(View.VISIBLE);
                                textView.setVisibility(View.VISIBLE);
                                tag="1";
                                mGiveBook.setLayoutManager(new LinearLayoutManager(getActivity()));
                                mGiveBook.setAdapter(new CommonAdapter<ClassOrderReaponse.mGive_Books>(getActivity(), R.layout.item_give_book_layout, mData.getGive_books()) {
                                    @Override
                                    protected void convert(ViewHolder holder, ClassOrderReaponse.mGive_Books mGive_books, int position) {
                                        holder.setText(R.id.give_name, mGive_books.getBook_name());
                                        holder.setText(R.id.give_price, "￥" + mGive_books.getPrice());
                                        ImageView imageView = (ImageView) holder.getConvertView().findViewById(R.id.give_image);
                                        Glide.with(getActivity()).load(mGive_books.getImages()).apply(GloableConstant.getInstance().getDefaultOptionssmall()).into(imageView);
                                    }
                                });

                            } else {
                                tag="0";
                                mGiveBook.setVisibility(View.GONE);
                                textView.setVisibility(View.GONE);
                            }


                            ImageView imageView = (ImageView) holder.getConvertView().findViewById(R.id.image_class);
                            mStatus = (TextView) holder.getConvertView().findViewById(R.id.status);
                            mStatua_bar = (TextView) holder.getConvertView().findViewById(R.id.status_bar);
                            Glide.with(getActivity()).load(mData.getGoods().getImages()).into(imageView);
                            switch (mData.getStatus()) {
                                case "1":
                                    holder.setText(R.id.status, "待付款");
                                    holder.setText(R.id.status_bar, "去支付");
                                    group_id = mData.getGroup_id();
                                    holder.setOnClickListener(R.id.status_bar, new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                            order_id = mData.getOrder_id();
                                            intent.setClass(getActivity(), MyOrderDetailActivity.class);
                                            intent.putExtra("order_id", order_id);
                                            intent.putExtra("status", mData.getStatus());
                                            intent.putExtra("tag",tag);
                                            intent.putExtra("flag", flag);
//                                            Log.e("*****status111111", mData.getStatus() + "");
                                            startActivity(intent);

                                        }
                                    });

                                    break;
                                case "2":
                                    holder.setText(R.id.status, "已付款");
                                    holder.setText(R.id.status_bar, "待发货");
//                                    holder.setOnClickListener(R.id.status_bar, new View.OnClickListener() {
//                                        @Override
//                                        public void onClick(View v) {
//                                            order_id = mData.getOrder_id();
//                                            intent.setClass(getActivity(), MyOrderCommentActivity.class);
//                                            intent.putExtra("order_id", order_id);
//                                            intent.putExtra("flag", flag);
//                                            startActivity(intent);
//                                        }
//                                    });
                                    break;
                                case "4":
                                    holder.setText(R.id.status, "已发货");
                                    holder.setText(R.id.status_bar, "待收货");
                                    order_id = mData.getOrder_id();
                                    confirm(order_id);
//                                    holder.setOnClickListener(R.id.status_bar, new View.OnClickListener() {
//                                        @Override
//                                        public void onClick(View v) {
//                                            order_id = mData.getOrder_id();
//                                            intent.setClass(getActivity(), MyOrderCommentActivity.class);
//                                            intent.putExtra("order_id", order_id);
//                                            intent.putExtra("flag", flag);
//                                            startActivity(intent);
//                                        }
//                                    });
                                    break;
                                case "5":
                                    holder.setText(R.id.status, "待评价");
                                    holder.setText(R.id.status_bar, "去评价");
                                    holder.setOnClickListener(R.id.status_bar, new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            order_id = mData.getOrder_id();
                                            intent.setClass(getActivity(), MyOrderCommentActivity.class);
                                            intent.putExtra("order_id", order_id);
                                            intent.putExtra("flag", flag);
                                            startActivity(intent);
                                        }
                                    });
                                    break;
                                case "6":
//                                    holder.setText(R.id.status, "已完成");
//                                    TextView text= (TextView) holder.getConvertView().findViewById(R.id.status_bar);
//                                    text.setVisibility(View.GONE);
                                    holder.setText(R.id.status_bar, "已完成");
                                    break;
                                default:
                                    break;
                            }

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
     * 确认收货
     */
    private void confirm(String order_id){
        Map<String,Object> map=new HashMap();
        map.put("user_id",user_id);
        map.put("order_id",order_id);
        GloableConstant.getInstance().startProgressDialog(getActivity());
        Xutil.Post(ApiUrl.CONFIRM,map,new MyCallBack<SupportResponse>(){
            @Override
            public void onSuccess(SupportResponse result) {
                super.onSuccess(result);
                GloableConstant.getInstance().stopProgressDialog1();
                if (result.getStatus_code().equals("204")){
                    ToastUtil.showShortToast(result.getMessage());
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
                GloableConstant.getInstance().stopProgressDialog1();
            }

            @Override
            public void onFinished() {
                super.onFinished();
                GloableConstant.getInstance().stopProgressDialog1();
            }
        });

    }
    /**
     * 删除订单
     */
    private void deleteOrder(String order_id ){
        Map<String,Object> map=new HashMap<>();
        map.put("user_id",user_id);
        map.put("order_id",order_id);
        GloableConstant.getInstance().startProgressDialog(getActivity());
        Xutil.Post(ApiUrl.DELETEORDER,map,new MyCallBack<SupportResponse>(){
            @Override
            public void onSuccess(SupportResponse result) {
                super.onSuccess(result);
                GloableConstant.getInstance().stopProgressDialog1();
                if (result.getStatus_code().equals("204")){
                    ToastUtil.showShortToast("删除订单成功");
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
                GloableConstant.getInstance().stopProgressDialog1();
            }

            @Override
            public void onFinished() {
                super.onFinished();
                GloableConstant.getInstance().stopProgressDialog1();
            }
        });
    }
}
