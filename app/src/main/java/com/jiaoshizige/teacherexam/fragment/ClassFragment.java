package com.jiaoshizige.teacherexam.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.jwenfeng.library.pulltorefresh.BaseRefreshListener;
import com.jwenfeng.library.pulltorefresh.ViewStatus;
import com.jiaoshizige.teacherexam.R;
import com.jiaoshizige.teacherexam.activity.ClassDetialActivity;
import com.jiaoshizige.teacherexam.base.GloableConstant;
import com.jiaoshizige.teacherexam.base.MBaseFragment;
import com.jiaoshizige.teacherexam.http.ApiUrl;
import com.jiaoshizige.teacherexam.http.MyCallBack;
import com.jiaoshizige.teacherexam.http.SupportResponse;
import com.jiaoshizige.teacherexam.http.Xutil;
import com.jiaoshizige.teacherexam.model.ClassCollectResponse;
import com.jiaoshizige.teacherexam.utils.PullToRefreshLayoutRewrite;
import com.jiaoshizige.teacherexam.utils.SPKeyValuesUtils;
import com.jiaoshizige.teacherexam.utils.SPUtils;
import com.jiaoshizige.teacherexam.utils.ToastUtil;
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
 * 我的收藏 班级
 */

public class ClassFragment extends MBaseFragment {
    @BindView(R.id.class_list)
    RecyclerView mRecyclerView;
    @BindView(R.id.pulltorefresh)
    PullToRefreshLayoutRewrite mPullToRefresh;
    private String user_id;
    private String  page = "1", pagesize = "5";
    private SelfDialog selfDialog;
    private String class_id;
    private Intent intent;
    private Context mContext;
    View view;
    private int mPage=1;
    private List<ClassCollectResponse.mData> mList;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            view = inflater.inflate(R.layout.class_fragment_layout, container, false);
    return view;
}

    @Override
    public void onResume() {
        super.onResume();
        ClassCollect();//访问网络请求数据
    }

    @Override
    protected void onInitViews(Bundle savedInstanceState) {
        mContext=getActivity();
        mList=new ArrayList<>();
        user_id = (String) SPUtils.getSpValues(SPKeyValuesUtils.SP_USER_ID, SPUtils.TYPE_STRING);
        mPullToRefresh.setRefreshListener(new BaseRefreshListener() {
            @Override
            public void refresh() {
                mPage=1;
                page="1";
                ClassCollect();
            }

            @Override
            public void loadMore() {
                mPage++;
                page=String.valueOf(mPage);
                ClassCollect();

            }
        });

        mPullToRefresh.setOnErrorListener(new PullToRefreshLayoutRewrite.onClickCallBackListener() {
            @Override
            public void onCallBackListener(View v) {
                mPage=1;
                page="1";
                ClassCollect();
            }
        });
        ClassCollect();//访问网络请求数据
    }



    private void ClassCollect() {
        Map<String, Object> map = new HashMap<>();
//        map.put("user_id", 1);
        map.put("user_id",SPUtils.getSpValues(SPKeyValuesUtils.SP_USER_ID, SPUtils.TYPE_STRING));
        map.put("type", "3");
        map.put("page", page);
        map.put("page_size", pagesize);
        Log.e("*******map", map.toString());
        GloableConstant.getInstance().startProgressDialog(getActivity());
        Xutil.Post(ApiUrl.COLLECT, map, new MyCallBack<ClassCollectResponse>() {
            @Override
            public void onSuccess(final ClassCollectResponse result) {
                super.onSuccess(result);
                GloableConstant.getInstance().stopProgressDialog();
                mPullToRefresh.finishRefresh();
                mPullToRefresh.finishLoadMore();
                mPullToRefresh.showView(ViewStatus.CONTENT_STATUS);
                Log.e("********result", result.getStatus_code());
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
                    mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                    mRecyclerView.setAdapter(new CommonAdapter<ClassCollectResponse.mData>(getActivity(), R.layout.item_class_fragment, result.getData()) {
                        //课程：30/156人在学习/5个评价
                        @Override
                        protected void convert(final ViewHolder holder, final ClassCollectResponse.mData mData, final int position) {

                            holder.setText(R.id.class_title, mData.getGroup_name());
                            holder.setText(R.id.class_introduce, "课程：" + mData.getCourse_num() + "/" + mData.getSale_num() + "人在学校/" + mData.getComment_num() + "个评价");
                            ImageView imageView = (ImageView) holder.getConvertView().findViewById(R.id.class_cover);
                            Glide.with(getActivity()).load(mData.getImages()).into(imageView);
                            holder.setText(R.id.class_price, mData.getPrice());

                            holder.setOnClickListener(R.id.class_item, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    class_id = result.getData().get(position).getGroup_id();
                                    intent = new Intent(getActivity(), ClassDetialActivity.class);
                                    intent.putExtra("class_id", class_id);
                                    startActivity(intent);
                                }
                            });
                            holder.setOnLongClickListener(R.id.class_item, new View.OnLongClickListener() {
                                @Override
                                public boolean onLongClick(final View v) {
                                    selfDialog = new SelfDialog(getActivity());
                                    selfDialog.setTitle("是否取消收藏");
                                    selfDialog.setYesOnclickListener("确定", new SelfDialog.onYesOnclickListener() {
                                        @Override
                                        public void onYesClick() {
                                            class_id = result.getData().get(position).getGroup_id();
                                            delCollection();
                                            result.getData().remove(position);
                                            Log.e("**********position", position + "");
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
    private void delCollection() {
        Map<String, Object> map = new HashMap<>();
        map.put("type", "3");
        map.put("goods_id", class_id);
//        map.put("user_id", 1);
        map.put("user_id",SPUtils.getSpValues(SPKeyValuesUtils.SP_USER_ID, SPUtils.TYPE_STRING));
        Log.e("*******map", map.toString());
        GloableConstant.getInstance().startProgressDialog(getActivity());
        Xutil.Post(ApiUrl.DELCOLLECT, map, new MyCallBack<SupportResponse>() {
            @Override
            public void onSuccess(SupportResponse result) {
                super.onSuccess(result);
                GloableConstant.getInstance().stopProgressDialog();
                Log.e("*********result", result.getStatus_code());
                if (result.getStatus_code().equals("204")) {
                    ToastUtil.showShortToast(result.getMessage());
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
