package com.jiaoshizige.teacherexam.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;

import com.jwenfeng.library.pulltorefresh.BaseRefreshListener;
import com.jwenfeng.library.pulltorefresh.ViewStatus;
import com.umeng.analytics.MobclickAgent;
import com.jiaoshizige.teacherexam.R;
import com.jiaoshizige.teacherexam.base.BaseActivity;
import com.jiaoshizige.teacherexam.base.GloableConstant;
import com.jiaoshizige.teacherexam.http.ApiUrl;
import com.jiaoshizige.teacherexam.http.MyCallBack;
import com.jiaoshizige.teacherexam.http.SupportResponse;
import com.jiaoshizige.teacherexam.http.Xutil;
import com.jiaoshizige.teacherexam.model.SelectAddressRespomse;
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
 * Created by Administrator on 2017/a1/25 0025.
 */

/***
 * 地址管理
 */

public class AddressListActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.address_list)
    RecyclerView mRecyclerView;
    @BindView(R.id.layout)
    RelativeLayout mAdd_adress;
    @BindView(R.id.pull_refresh)
    PullToRefreshLayoutRewrite mPtr;


    private Context mContext;
    private String address_id;
    private CheckBox mCheckBox;
    private int clickTemp = -1;
    private String user_id;
    private Intent intent;
    private String tag;
    private String mPageNum="1",mPageSize="5";
    private int mPage=1;
    private List<SelectAddressRespomse.mData> mList;


    @Override
    protected int getLayoutId() {
        return R.layout.address_list_layout;
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("地址管理");
        MobclickAgent.onResume(this);
        address();
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("地址管理");
        MobclickAgent.onPause(this);
    }

    @Override
    protected void initView() {
        setSubTitle().setText("");
        setToolbarTitle().setText("地址管理");
        mContext = this;
        intent=new Intent();
        mList=new ArrayList<>();
        mAdd_adress.setOnClickListener(this);
        user_id = (String) SPUtils.getSpValues(SPKeyValuesUtils.SP_USER_ID, SPUtils.TYPE_STRING);
        mPtr.setRefreshListener(new BaseRefreshListener() {
            @Override
            public void refresh() {
                mPage=1;
                mPageNum="1";
                address();
            }

            @Override
            public void loadMore() {
                mPage++;
                mPageNum=String.valueOf(mPage);
                address();
            }
        });
        mPtr.setOnErrorListener(new PullToRefreshLayoutRewrite.onClickCallBackListener() {
            @Override
            public void onCallBackListener(View v) {
                mPage=1;
                mPageNum="1";
                address();
            }
        });
        address();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout:
                intent.putExtra("tag","1");
                intent.setClass(AddressListActivity.this,AddAddressActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    private void address() {
        final Map<String, Object> map = new HashMap<>();
//        map.put("user_id", 1);
        map.put("user_id", (String) SPUtils.getSpValues(SPKeyValuesUtils.SP_USER_ID, SPUtils.TYPE_STRING));
        map.put("page",mPageNum);
        map.put("page_size",mPageSize);
        GloableConstant.getInstance().startProgressDialog(this);
        Xutil.Post(ApiUrl.SELECTADRESS, map, new MyCallBack<SelectAddressRespomse>() {
            @Override
            public void onSuccess(final SelectAddressRespomse result) {
                super.onSuccess(result);
                GloableConstant.getInstance().stopProgressDialog();
                mPtr.finishRefresh();
                mPtr.finishLoadMore();
                mPtr.showView(ViewStatus.CONTENT_STATUS);
                if (result.getStatus_code().equals("200") ) {
                    if (result.getData().size() > 0){
                        if (mPageNum.equals("1")){
                            mList.clear();
                            mList=result.getData();
                        }else {
                            if (null==mList){
                                mList=result.getData();
                            }else {
                                mList.addAll(result.getData());
                            }
                        }
                        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
                        mRecyclerView.setAdapter(new CommonAdapter<SelectAddressRespomse.mData>(mContext, R.layout.item_address_list_layout, result.getData()) {

                            @Override
                            protected void convert(final ViewHolder holder, final SelectAddressRespomse.mData mData, final int position) {

                                holder.setText(R.id.address_name, mData.getName());
                                holder.setText(R.id.address_phone, mData.getMobile());
                                holder.setText(R.id.address_site, mData.getProvince() + mData.getCity() + mData.getCounty());
                                mCheckBox = (CheckBox) holder.getConvertView().findViewById(R.id.checkbox_address);
                                if (mData.getStatus().equals("1")) {
                                    clickTemp=position;
                                    mCheckBox.setChecked(true);
                                } else {
                                    mCheckBox.setChecked(false);
                                }

                                //删除地址
                                holder.setOnClickListener(R.id.delete_address, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        address_id=mData.getId();
                                        CancelOrOkDialog dialog = new CancelOrOkDialog(AddressListActivity.this, "确定要删除此地址吗?") {
                                            @Override
                                            public void ok() {
                                                super.ok();
                                                delAddress();
                                                result.getData().remove(position);
                                                notifyDataSetChanged();
                                                dismiss();
                                            }
                                        };
                                        dialog.show();
                                    }
                                });
                                //编辑地址
                                holder.setOnClickListener(R.id.edit_address, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        intent.putExtra("tag","2");
                                        address_id=mData.getId();
                                        intent.putExtra("address_id",address_id);
                                        intent.putExtra("address_name",mData.getName());
                                        intent.putExtra("address_phone",mData.getMobile());
                                        intent.putExtra("address_province",mData.getProvince()+mData.getCity());
                                        intent.putExtra("address_county",mData.getCounty());
                                        Log.e("********address_id",address_id);
                                        intent.setClass(AddressListActivity.this,AddAddressActivity.class);
                                        startActivity(intent);
                                    }
                                });

                                //设置默认收货地址
                                holder.setOnClickListener(R.id.checkbox_address, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        defaultAdress(mData.getId());
                                        holder.setChecked(R.id.checkbox_address,true);
                                        mData.setStatus("1");
                                        if (clickTemp!=position){
                                            result.getData().get(clickTemp).setStatus("0");
                                        }
                                        notifyDataSetChanged();
                                        finish();
                                    }
                                });

                                //设置默认收货地址
                                holder.setOnClickListener(R.id.layout, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        defaultAdress(mData.getId());
                                        holder.setChecked(R.id.checkbox_address,true);
                                        mData.setStatus("1");
                                        if (clickTemp!=position){
                                            result.getData().get(clickTemp).setStatus("0");
                                        }
                                        notifyDataSetChanged();
                                        finish();

                                    }
                                });

                            }

                        });
                    }else {
                        if (mPage>1){
                            ToastUtil.showShortToast("没有更多数据了");
                        }else {
                            mPtr.showView(ViewStatus.EMPTY_STATUS);
                        }
                    }

                }else if (result.getStatus_code().equals("203")){
                    if (mPage>1){
                        ToastUtil.showShortToast("没有更多数据了");
                    }else {
                        mPtr.showView(ViewStatus.EMPTY_STATUS);
                    }
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
                GloableConstant.getInstance().stopProgressDialog();
                mPtr.finishRefresh();
                mPtr.finishLoadMore();
                mPtr.showView(ViewStatus.EMPTY_STATUS);
            }

            @Override
            public void onFinished() {
                super.onFinished();
                GloableConstant.getInstance().stopProgressDialog();
                mPtr.finishRefresh();
                mPtr.finishLoadMore();
            }
        });
    }

    /**
     * 删除地址
     */
    private void delAddress() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", address_id);
//        map.put("user_id", 1);
        map.put("user_id", (String) SPUtils.getSpValues(SPKeyValuesUtils.SP_USER_ID, SPUtils.TYPE_STRING));
        Log.e("**********del",map.toString());
        GloableConstant.getInstance().startProgressDialog(this);
        Xutil.Post(ApiUrl.DELADRESS, map, new MyCallBack<SupportResponse>() {
            @Override
            public void onSuccess(SupportResponse result) {
                super.onSuccess(result);
                GloableConstant.getInstance().stopProgressDialog();
                Log.e("*******delresult",result.getStatus_code());
                if (result.getStatus_code().equals("204")) {
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
    /**
     * 设置默认收货地址
     */
    private void defaultAdress(String id){
        Map<String,Object> map=new HashMap<>();
//        map.put("user_id",1);
        map.put("user_id", (String) SPUtils.getSpValues(SPKeyValuesUtils.SP_USER_ID, SPUtils.TYPE_STRING));
        map.put("id",id);
        map.put("status",1);
        Log.e("*********map",map.toString());
        GloableConstant.getInstance().startProgressDialog(this);
        Xutil.Post(ApiUrl.EDIT,map,new MyCallBack<SupportResponse>(){
            @Override
            public void onSuccess(SupportResponse result) {
                super.onSuccess(result);
                GloableConstant.getInstance().stopProgressDialog();
                Log.e("*********result",result.getStatus_code());
                if (result.getStatus_code().equals("204")){
                    ToastUtil.showShortToast("设置收货地址成功");
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
                GloableConstant.getInstance().stopProgressDialog();
            }

            @Override
            public void onFinished() {
                super.onFinished();
                GloableConstant.getInstance().stopProgressDialog();
            }
        });

    }


}
