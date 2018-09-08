package com.jiaoshizige.teacherexam.fragment;

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
import com.jiaoshizige.teacherexam.http.Xutil;
import com.jiaoshizige.teacherexam.model.ClassListResponse;
import com.jiaoshizige.teacherexam.utils.PullToRefreshLayoutRewrite;
import com.jiaoshizige.teacherexam.utils.SPKeyValuesUtils;
import com.jiaoshizige.teacherexam.utils.SPUtils;
import com.jiaoshizige.teacherexam.utils.ToastUtil;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/a1/17.
 * 私塾班级列表
 */
public class PrivateSchoolClassFragment extends MBaseFragment{
    @BindView(R.id.class_list)
    RecyclerView mClassList;
    @BindView(R.id.pulltorefresh)
    PullToRefreshLayoutRewrite mPullToRefresh;
    private String mPageNum = "1";
    private String mPageSize = "15";
    private int mPage = 1;
    List<ClassListResponse.mData> mList = new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.class_fragment_layout,container,false);
    }

    @Override
    protected void onInitViews(Bundle savedInstanceState) {
        mPullToRefresh.setRefreshListener(new BaseRefreshListener() {
            @Override
            public void refresh() {
                mPage =1;
                mPageNum = "1";
                requestClassList();
            }

            @Override
            public void loadMore() {
                mPage++;
                mPageNum = String.valueOf(mPage);
                requestClassList();
            }
        });
        mPullToRefresh.setOnErrorListener(new PullToRefreshLayoutRewrite.onClickCallBackListener() {
            @Override
            public void onCallBackListener(View v) {
                mPage =1;
                mPageNum = "1";
                requestClassList();
            }
        });
        requestClassList();
    }
    private void requestClassList(){
        Map<String,String> map = new HashMap<>();
        map.put("page",mPageNum);
        map.put("page_size",mPageSize);
        map.put("user_id", (String) SPUtils.getSpValues(SPKeyValuesUtils.SP_USER_ID,SPUtils.TYPE_STRING));
        Log.e("********map",map.toString());
        GloableConstant.getInstance().showmeidialog(getActivity());
        Xutil.GET(ApiUrl.CLASSLIST,map,new MyCallBack<ClassListResponse>(){
            @Override
            public void onSuccess(ClassListResponse result) {
                super.onSuccess(result);

                GloableConstant.getInstance().stopProgressDialog();
                mPullToRefresh.finishRefresh();
                mPullToRefresh.finishLoadMore();
                mPullToRefresh.showView(ViewStatus.CONTENT_STATUS);

                if(result.getStatus_code().equals("200")){
                    if(result.getData() != null && result.getData().size() > 0){
                        mClassList.setLayoutManager(new LinearLayoutManager(getActivity()));
                        if(mPageNum.equals("1")){
                            mList.clear();
                            mList = result.getData();
                        }else{
                            if (null == mList){
                                mList = result.getData();
                            } else{
                                mList.addAll(result.getData());
                            }
                        }
                        mClassList.setAdapter(new CommonAdapter<ClassListResponse.mData>(getActivity(),R.layout.item_class_fragment,mList) {
                            @Override
                            protected void convert(ViewHolder holder, final ClassListResponse.mData s, int position) {
                                holder.setText(R.id.class_title,s.getGroup_name());
                                holder.setText(R.id.class_price,s.getPrice());
                                ImageView mCOver = (ImageView) holder.getConvertView().findViewById(R.id.class_cover);
                                Glide.with(getActivity()).load(s.getImages()).apply(GloableConstant.getInstance().getDefaultOptionssmall()).into(mCOver);
                                holder.setText(R.id.class_introduce,"课程："+s.getCourse_num()+"/"+s.getSale_num()+"人在学习"+"/"+s.getComment_num()+"个评价");
                                holder.setOnClickListener(R.id.class_item, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent();
                                        intent.setClass(getActivity(), ClassDetialActivity.class);
                                        intent.putExtra("class_id",s.getId());
                                        startActivity(intent);
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
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
                GloableConstant.getInstance().stopProgressDialog();
                mPullToRefresh.finishRefresh();
                mPullToRefresh.finishLoadMore();
                mPullToRefresh.showView(ViewStatus.EMPTY_STATUS);
            }
        });
    }
}
