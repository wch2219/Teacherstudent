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
import com.umeng.analytics.MobclickAgent;
import com.jiaoshizige.teacherexam.R;
import com.jiaoshizige.teacherexam.activity.LogisticsDetailActivity;
import com.jiaoshizige.teacherexam.activity.MyCourseOrderDetailActivity;
import com.jiaoshizige.teacherexam.activity.MyOrderCommentActivity;
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

public class OrderBooksFragment extends MBaseFragment {
    @BindView(R.id.class_list)
    RecyclerView mRecyclerView;
    @BindView(R.id.pulltorefresh)
    PullToRefreshLayoutRewrite mLayout;
    private String user_id;
    private Intent intent;
    private String book_id, order_id;
    private String flag ;
    TextView mStatus;
    TextView mStatua_bar;
    private String status;
    private List<ClassOrderReaponse.mData> mList;
    private String mPageNum="1";
    private String mPageSize="5";
    private int mPage;
    private String tag="1";//判断是否赠送图书0是没有，1是有
    TextView mCheck;
    TextView mDelete;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.class_fragment_layout, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("图书订单");
        BookOrder();
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("图书订单");
    }

    @Override
    protected void onInitViews(Bundle savedInstanceState) {
        user_id = (String) SPUtils.getSpValues(SPKeyValuesUtils.SP_USER_ID, SPUtils.TYPE_STRING);
        intent = new Intent();
        mList = new ArrayList<>();
        mLayout.setRefreshListener(new BaseRefreshListener() {
            @Override
            public void refresh() {
                mPage = 1;
                mPageNum = "1";
                BookOrder();
            }

            @Override
            public void loadMore() {
                mPage++;
                mPageNum = String.valueOf(mPage);
                BookOrder();
            }
        });
        mLayout.setOnErrorListener(new PullToRefreshLayoutRewrite.onClickCallBackListener() {
            @Override
            public void onCallBackListener(View v) {
                mPage = 1;
                mPageNum = "1";
                BookOrder();
            }
        });
//        BookOrder();
    }

    private void BookOrder() {
        final Map<String, Object> map = new HashMap<>();
//        map.put("user_id", 1);
        map.put("user_id", SPUtils.getSpValues(SPKeyValuesUtils.SP_USER_ID, SPUtils.TYPE_STRING));
        map.put("type", 2);
        map.put("page", mPageNum);
        map.put("page_size", mPageSize);
        Log.e("TAG",map.toString());
        GloableConstant.getInstance().startProgressDialog(getActivity());
        Xutil.Post(ApiUrl.ORDER, map, new MyCallBack<ClassOrderReaponse>() {
            @Override
            public void onSuccess(final ClassOrderReaponse result) {
                super.onSuccess(result);
                GloableConstant.getInstance().stopProgressDialog();
                mLayout.finishLoadMore();
                mLayout.finishRefresh();
                mLayout.showView(ViewStatus.CONTENT_STATUS);
                Log.e("*******result",result.getStatus_code());
                if (result.getStatus_code().equals("200")) {
                  if (result.getData().size()>0){
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
                              ImageView imageView = (ImageView) holder.getConvertView().findViewById(R.id.image_class);
                              mStatus = (TextView) holder.getConvertView().findViewById(R.id.status);
                              mStatua_bar = (TextView) holder.getConvertView().findViewById(R.id.status_bar);
                              Glide.with(getActivity()).load(mData.getGoods().getImages()).into(imageView);
                              RecyclerView mGiveBook = (RecyclerView) holder.getConvertView().findViewById(R.id.recycle_view);
                              TextView textView = (TextView) holder.getConvertView().findViewById(R.id.text);
                              mGiveBook.setVisibility(View.GONE);
                              textView.setVisibility(View.GONE);
                              holder.setOnClickListener(R.id.order, new View.OnClickListener() {
                                  @Override
                                  public void onClick(View v) {
                                      flag=mData.getType();
                                      order_id = mData.getOrder_id();
                                      status = mData.getStatus();
                                      intent.setClass(getActivity(), MyCourseOrderDetailActivity.class);
                                      intent.putExtra("order_id", order_id);
                                      intent.putExtra("status", status);
                                      intent.putExtra("tag",tag);
                                      intent.putExtra("flag", "2");
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



                              mCheck= (TextView) holder.getConvertView().findViewById(R.id.check_logistics);
                              mDelete= (TextView) holder.getConvertView().findViewById(R.id.status_delete);
                              switch (mData.getStatus()) {
                                  case "1":
                                      holder.setText(R.id.status, "待付款");
                                      mStatus.setTextColor(getResources().getColor(R.color.red));
                                      holder.setText(R.id.status_bar, "付款");
                                      mCheck.setVisibility(View.GONE);
                                      mDelete.setVisibility(View.VISIBLE);
                                      mStatua_bar.setVisibility(View.VISIBLE);
//                                      course_id = mData.getCourse_id();
                                      holder.setOnClickListener(R.id.status_bar, new View.OnClickListener() {
                                          @Override
                                          public void onClick(View v) {
                                              order_id = mData.getOrder_id();
                                              intent.setClass(getActivity(), MyCourseOrderDetailActivity.class);
                                              intent.putExtra("order_id", order_id);
                                              intent.putExtra("status", mData.getStatus());
                                              intent.putExtra("tag",tag);
                                              intent.putExtra("flag", "2");
                                              startActivity(intent);
                                          }
                                      });
                                      break;
                                  case "2":
                                      holder.setText(R.id.status, "交易成功");
                                      mStatus.setTextColor(getResources().getColor(R.color.green));
                                      holder.setText(R.id.status_bar, "确认收货");
                                      mStatua_bar.setVisibility(View.VISIBLE);
                                      mDelete.setVisibility(View.GONE);
                                      holder.setOnClickListener(R.id.status_bar, new View.OnClickListener() {
                                          @Override
                                          public void onClick(View v) {
                                              order_id = mData.getOrder_id();
                                              confirm(order_id,position);
                                              notifyItemRangeChanged(position, result.getData().size());
                                              notifyDataSetChanged();
                                              notifyItemChanged(position);
                                          }
                                      });
                                      if (mData.getGive_books().size() > 0){
                                          mCheck.setVisibility(View.VISIBLE);
                                          holder.setOnClickListener(R.id.check_logistics, new View.OnClickListener() {
                                              @Override
                                              public void onClick(View v) {
                                                  Intent intent=new Intent();
                                                  intent.setClass(getActivity(), LogisticsDetailActivity.class);
                                                  //传物流编号
                                                  intent.putExtra("courier",mData.getInvoice_no());
                                                  startActivity(intent);
//                                             order_id = mData.getOrder_id();

                                              }
                                          });
                                      }else {
                                          mCheck.setVisibility(View.GONE);
                                      }


                                      break;
                                  case "4":
                                      holder.setText(R.id.status, "交易成功");
                                      mStatus.setTextColor(getResources().getColor(R.color.green));
                                      holder.setText(R.id.status_bar, "确认收货");
                                      mCheck.setVisibility(View.VISIBLE);
                                      mStatua_bar.setVisibility(View.VISIBLE);
                                      mDelete.setVisibility(View.GONE);
                                      holder.setOnClickListener(R.id.status_bar, new View.OnClickListener() {
                                          @Override
                                          public void onClick(View v) {
                                              order_id = mData.getOrder_id();
                                              confirm(order_id,position);
                                              notifyItemRangeChanged(position, result.getData().size());
                                              notifyDataSetChanged();
                                              notifyItemChanged(position);
                                          }
                                      });
                                      if (mData.getGive_books().size() > 0){
                                          mCheck.setVisibility(View.VISIBLE);
                                          holder.setOnClickListener(R.id.check_logistics, new View.OnClickListener() {
                                              @Override
                                              public void onClick(View v) {
                                                  Intent intent=new Intent();
                                                  intent.setClass(getActivity(), LogisticsDetailActivity.class);
                                                  //传物流编号
                                                  intent.putExtra("courier",mData.getInvoice_no());
                                                  startActivity(intent);
//                                             order_id = mData.getOrder_id();

                                              }
                                          });
                                      }else {
                                          mCheck.setVisibility(View.GONE);
                                      }

                                      break;
                                  case "5":
                                      holder.setText(R.id.status, "交易成功");
                                      mStatus.setTextColor(getResources().getColor(R.color.green));
                                      holder.setText(R.id.status_bar, "点击评价");
                                      mDelete.setVisibility(View.VISIBLE);
                                      mStatua_bar.setVisibility(View.VISIBLE);
                                      mCheck.setVisibility(View.GONE);
                                      holder.setOnClickListener(R.id.status_bar, new View.OnClickListener() {
                                          @Override
                                          public void onClick(View v) {
                                              intent.setClass(getActivity(), MyOrderCommentActivity.class);
                                              intent.putExtra("order_id", mData.getOrder_id());
                                              intent.putExtra("flag", "2");
                                              startActivity(intent);
                                          }
                                      });

                                      break;
                                  case "6":
                                      holder.setText(R.id.status, "交易完成");
                                      mStatus.setTextColor(getResources().getColor(R.color.text_color9));
                                      mDelete.setVisibility(View.VISIBLE);
                                      mStatua_bar.setVisibility(View.GONE);
                                      mCheck.setVisibility(View.GONE);
                                      break;
                                  default:
                                      break;
                              }



                          }
                      });
                  }else {
                      if (mPage>1){
                          ToastUtil.showShortToast("没有更多数据了");
                      }else {
//                          mLayout.showView(ViewStatus.EMPTY_STATUS);
                          ToastUtil.showShortToast("没有更多数据了");
                      }
                  }
                }
               else if (result.getStatus_code().equals("203")){
//                    mLayout.showView(ViewStatus.EMPTY_STATUS);
                    if (mPage>1){
                        ToastUtil.showShortToast("没有更多数据了");
                    }else {
//                        mLayout.showView(ViewStatus.EMPTY_STATUS);
                        ToastUtil.showShortToast("没有更多数据了");
                    }
                }
            }

            @Override
            public void onFinished() {
                super.onFinished();
                GloableConstant.getInstance().stopProgressDialog();
                mLayout.finishLoadMore();
                mLayout.finishRefresh();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
                GloableConstant.getInstance().stopProgressDialog();
                mLayout.finishLoadMore();
                mLayout.finishRefresh();
                mLayout.showView(ViewStatus.EMPTY_STATUS);
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            mStatus.setText("已完成");
            mStatua_bar.setText("查看评价");
        }
    }

    /**
     * 确认收货
     */
    private void confirm(String order_id, final int position) {
        Map<String, Object> map = new HashMap<>();
//        map.put("user_id",1);
        map.put("user_id", SPUtils.getSpValues(SPKeyValuesUtils.SP_USER_ID, SPUtils.TYPE_STRING));
        map.put("order_id", order_id);
        Log.e("TAG","map"+map.toString());
        GloableConstant.getInstance().showmeidialog(getActivity());
        Xutil.Post(ApiUrl.AFFIRM, map, new MyCallBack<SupportResponse>() {
            @Override
            public void onSuccess(SupportResponse result) {
                super.onSuccess(result);
                GloableConstant.getInstance().stopProgressDialog();
                if (result.getStatus_code().equals("204")) {
                    ToastUtil.showShortToast(result.getMessage());
                    BookOrder();
                }
            }

            @Override
            public void onFinished() {
                super.onFinished();
                GloableConstant.getInstance().stopProgressDialog();
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
