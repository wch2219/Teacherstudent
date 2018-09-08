package com.jiaoshizige.teacherexam.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.umeng.analytics.MobclickAgent;
import com.jiaoshizige.teacherexam.R;
import com.jiaoshizige.teacherexam.base.BaseActivity;
import com.jiaoshizige.teacherexam.base.GloableConstant;
import com.jiaoshizige.teacherexam.http.ApiUrl;
import com.jiaoshizige.teacherexam.http.MyCallBack;
import com.jiaoshizige.teacherexam.http.SupportResponse;
import com.jiaoshizige.teacherexam.http.Xutil;
import com.jiaoshizige.teacherexam.model.Trace;
import com.jiaoshizige.teacherexam.utils.SPKeyValuesUtils;
import com.jiaoshizige.teacherexam.utils.SPUtils;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/12/1 0001.
 * 物流详情
 */

public class LogisticsDetailActivity extends BaseActivity {
    @BindView(R.id.logistics_status)
    TextView mStatus;
    @BindView(R.id.logistics_num)
    TextView mNum;
    @BindView(R.id.logistics)
    TextView mLogistics;
    @BindView(R.id.recycle_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.image)
    ImageView imageView;
    @BindView(R.id.status)
    LinearLayout mNoDataStatus;
    @BindView(R.id.data)
    LinearLayout mData;

    private Context mContext;
    private Intent intent;
    private String courier,image_url;
    private String user_id;
    @Override
    protected int getLayoutId() {
        return R.layout.logistics_detail_activity;
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("物流详情");
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("物流详情");
        MobclickAgent.onPause(this);
    }

    @Override
    protected void initView() {
        mContext = this;
        setSubTitle().setText("");
        setToolbarTitle().setText("物流详情");
        user_id= (String) SPUtils.getSpValues(SPKeyValuesUtils.SP_USER_ID,SPUtils.TYPE_STRING);
        intent=getIntent();
//        if (intent.getStringExtra("company")!=null){
//            mLogistics.setText("消息来源"+"  "+intent.getStringExtra("company"));
//        }
        if (intent.getStringExtra("courier")!=null){
            courier=intent.getStringExtra("courier");
            Log.e("OPOwwwwPO",courier);
            mNoDataStatus.setVisibility(View.GONE);
            mData.setVisibility(View.VISIBLE);
        }else {
            mNoDataStatus.setVisibility(View.VISIBLE);
            mData.setVisibility(View.GONE);
        }
//        if (intent.getStringExtra("image_url")!=null){
//            image_url=intent.getStringExtra("image_url");
//            Log.e("***********image",image_url);
//        }
        if (getIntent().getStringExtra("messageId")!= null){
            deleteMessage(getIntent().getStringExtra("messageId"));
        }
        postLogistics();
    }

    private void postLogistics() {
        Map<String, Object> map = new HashMap<>();
        map.put("express_code", courier);
        GloableConstant.getInstance().startProgressDialog(this);
        Xutil.Post(ApiUrl.LOGISTICS, map, new MyCallBack<Trace>() {
            @Override
            public void onSuccess(final Trace result) {
                super.onSuccess(result);
                GloableConstant.getInstance().stopProgressDialog();
                if (result.getStatus_code().equals("200")) {
                    mNoDataStatus.setVisibility(View.GONE);
                    mData.setVisibility(View.VISIBLE);
               if (result.getData().getExpress().getData().size()>0){
                   switch (result.getData().getExpress().getState()) {
                       case 0:
                           mStatus.setText("物流状态  运输中");
                           break;
                       case 1:
                           mStatus.setText("物流状态  揽件中");
                           break;
                       case 2:
                           mStatus.setText("物流状态  疑难件");
                           break;
                       case 3:
                           mStatus.setText("物流状态  收件人已签收");
                           break;
                       case 4:
                           mStatus.setText("物流状态  退签");
                           break;
                       case 5:
                           mStatus.setText("物流状态  派件中");
                           break;
                       case 6:
                           mStatus.setText("物流状态  货物已退回");
                           break;

                   }
                   Glide.with(mContext).load(result.getData().getImages()).into(imageView);
                   mNum.setText("物流编号  "+result.getData().getExpress().getNu());
                   mLogistics.setText("消息来源"+"  "+result.getData().getCompany());
                   mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
                   mRecyclerView.setAdapter(new CommonAdapter<Trace.Data>(mContext, R.layout.item_logistics_detail_activity, result.getData().getExpress().getData()) {

                       @Override
                       protected void convert(ViewHolder holder, Trace.Data data, int position) {
                           TextView status = (TextView) holder.getConvertView().findViewById(R.id.accept_station_tv);
                           ImageView imageView = (ImageView) holder.getConvertView().findViewById(R.id.dot);
                           TextView time = (TextView) holder.getConvertView().findViewById(R.id.accept_time_tv);
                           holder.setText(R.id.accept_station_tv,data.getContext());
                           holder.setText(R.id.accept_time_tv,data.getTime());
                           if (position == 0) {
                               imageView.setImageResource(R.mipmap.wy_png_delivery_default);
                               status.setTextColor(getResources().getColor(R.color.purple4));
                               time.setTextColor(getResources().getColor(R.color.purple4));
                           } else {
                               imageView.setImageResource(R.mipmap.wy_png_gray_default);
                               status.setTextColor(getResources().getColor(R.color.text_color6));
                               time.setTextColor(getResources().getColor(R.color.text_color6));
                           }
                           if (position ==result.getData().getExpress().getData().size()) {
                               //最后一条数据，隐藏时间轴的竖线和水平的分割线
                               View hor_view=holder.getConvertView().findViewById(R.id.divider_line_view);
                               View ver_view=holder.getConvertView().findViewById(R.id.time_line_view);
                               hor_view.setVisibility(View.GONE);
                               ver_view.setVisibility(View.GONE);
                           }
                       }
                   });
               }else {
                   mData.setVisibility(View.GONE);
                   mNoDataStatus.setVisibility(View.VISIBLE);
                   mStatus.setText("物流状态  暂无数据");
                   mNum.setText("物流编号  暂无数据");
                   mLogistics.setText("消息来源  暂无数据");
                   imageView.setImageResource(R.mipmap.home_png_activity_default);
               }
                }else{
                    if(result.getStatus_code().equals("201")){
                        mData.setVisibility(View.GONE);
                        mNoDataStatus.setVisibility(View.VISIBLE);
                        mStatus.setText("物流状态  暂无数据");
                        mNum.setText("物流编号  暂无数据");
                        mLogistics.setText("消息来源  暂无数据");
                        imageView.setImageResource(R.mipmap.home_png_activity_default);
                    }
//                    ToastUtil.showShortToast(result.getMessage());
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

/**
 * 查看推送消息
 */
private void deleteMessage(String message_id) {
    Map<String, Object> map = new HashMap<>();
    map.put("id", message_id);
    map.put("user_id",user_id);
    map.put("type",1);
    Log.e("YY",map.toString());
    Xutil.Post(ApiUrl.DELETE,map,new MyCallBack<SupportResponse>(){
        @Override
        public void onSuccess(SupportResponse result) {
            super.onSuccess(result);
            if (result.getStatus_code().equals("204")){
//                ToastUtil.showShortToast("删除成功");
            }
        }

        @Override
        public void onError(Throwable ex, boolean isOnCallback) {
            super.onError(ex, isOnCallback);
        }
    });
}
}
