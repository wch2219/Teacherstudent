package com.jiaoshizige.teacherexam.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ToggleButton;

import com.jiaoshizige.teacherexam.model.busmodel.BusGetCcatalog;
import com.jiaoshizige.teacherexam.mycourse.activity.MyNewCoursesActivity;
import com.umeng.analytics.MobclickAgent;
import com.jiaoshizige.teacherexam.R;
import com.jiaoshizige.teacherexam.adapter.CourseCatalogFragmentAdapter;
import com.jiaoshizige.teacherexam.adapter.MainAdapter;
import com.jiaoshizige.teacherexam.adapter.SingleMainAdapter;
import com.jiaoshizige.teacherexam.base.BaseActivity;
import com.jiaoshizige.teacherexam.base.GloableConstant;
import com.jiaoshizige.teacherexam.http.ApiUrl;
import com.jiaoshizige.teacherexam.http.MyCallBack;
import com.jiaoshizige.teacherexam.http.Xutil;
import com.jiaoshizige.teacherexam.model.NewCourseCatalogResponse;
import com.jiaoshizige.teacherexam.model.SingleBuyResponse;
import com.jiaoshizige.teacherexam.utils.SPKeyValuesUtils;
import com.jiaoshizige.teacherexam.utils.SPUtils;
import com.jiaoshizige.teacherexam.utils.ToastUtil;
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
 * Created by Administrator on 2018/3/15.
 * 二级的单科购买
 */

public class NewSinglePurchaseActivity extends BaseActivity implements
        MainAdapter.OnExpandClickListener{
    @BindView(R.id.single_buy)
    RecyclerView mSingleBuyList;
    List<SingleBuyResponse.Data>  datas = new ArrayList<>();
//    SingleBuyAdapter mAdapter;
//private String mCourseId;
    private String mType; //1班级2课程
    private String mCourseId;
    SingleMainAdapter mSingleAdapter;
    private List<NewCourseCatalogResponse.mCourseList> mCourseLists = new ArrayList<>();
    private List<NewCourseCatalogResponse.mCatalog> mCourseTitle;
    private List<NewCourseCatalogResponse.mSon> mCourseContent;
    private List<List<NewCourseCatalogResponse.mSon>> childArray;
    private CourseCatalogFragmentAdapter mExpandableViewAdapter;
    @Override
    protected int getLayoutId() {
        return R.layout.single_purchase_layout;
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("二级的单科购买");
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("二级的单科购买");
        MobclickAgent.onPause(this);
    }

    @Override
    protected void initView() {
        setSubTitle().setText("");
        setToolbarTitle().setText("单科购买");
        if(getIntent().getExtras().get("cc_type") != null){
            mType = (String) getIntent().getExtras().get("cc_type");
        }else{
            mType = "";
        }
        if(getIntent().getExtras().get("course_id") != null){
            mCourseId = (String) getIntent().getExtras().get("course_id");
        }else{
            mCourseId = "";
        }

        requestCatalogByBus();
    }
    private void requestCatalogByBus(){
        GloableConstant.getInstance().startProgressDialog(this);
        EventBus.getDefault().post(new BusGetCcatalog());
    }


    @Subscribe
    public void requestCatalog(final NewCourseCatalogResponse result){
        if (result == null || "-1199".equals(result.getStatus_code())) {
            requestCatalog();
            return;
        }
                if(result.getStatus_code().equals("200")){
                    if(result.getData() != null){
                        if(result.getData().getCourse_list() != null && result.getData().getCourse_list().size() > 0){
                            if(result.getData().getCourse_list().get(0).getCatalog() !=null && result.getData().getCourse_list().get(0).getCatalog().size() > 0){
                                if(result.getData().getCourse_list().get(0).getCatalog().get(0).getSon() != null && result.getData().getCourse_list().get(0).getCatalog().get(0).getSon().size()>0){
                                    mSingleBuyList.setLayoutManager(new LinearLayoutManager(NewSinglePurchaseActivity.this));
                                    mSingleBuyList.setAdapter(new CommonAdapter<NewCourseCatalogResponse.mCourseList>(NewSinglePurchaseActivity.this,R.layout.item_single_buy_ex,result.getData().getCourse_list()) {
                                        @Override
                                        protected void convert(final ViewHolder holder, final NewCourseCatalogResponse.mCourseList mCourseList, int position) {
                                            final ExpandableListView mExpandableView = (ExpandableListView) holder.getConvertView().findViewById(R.id.expandable_list_view);
                                            final ToggleButton mSingleSwitch = (ToggleButton) holder.getConvertView().findViewById(R.id.single_switch);
                                            holder.setText(R.id.single_class_name,mCourseList.getName());
                                            holder.setText(R.id.single_class_price,"￥"+mCourseList.getPrice());
                                            String className = mCourseList.getName();
                                            String mSingleCourserId = mCourseList.getId();//单个套餐的id
                                            int mIsbuy = Integer.valueOf(mCourseList.getIs_buy());
                                            mSingleSwitch.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    if(mSingleSwitch.isChecked()){
                                                        mExpandableView.setVisibility(View.VISIBLE);
                                                    }else{
                                                        mExpandableView.setVisibility(View.GONE);
                                                    }
                                                }
                                            });
                                            if(mCourseList.getIs_buy().equals("1")){
                                                holder.setBackgroundRes(R.id.to_buy,R.drawable.purple_shape_30);
                                                holder.setText(R.id.to_buy,"去学习");
                                            }else if (mCourseList.getIs_buy().equals("2")){
                                                holder.setBackgroundRes(R.id.to_buy,R.drawable.purple_shape_30);
                                                holder.setText(R.id.to_buy,"去续费");
                                            }else{
                                                holder.setBackgroundRes(R.id.to_buy,R.drawable.orange_shape_30);
                                                holder.setText(R.id.to_buy,"去购买");
                                            }
                                            holder.setOnClickListener(R.id.to_buy, new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    if(mCourseList.getIs_buy().equals("1")){
                                                        Intent intent = new Intent();
                                                        intent.putExtra("id", mCourseList.getId());
//                                                        intent.putExtra("progress", "20");
                                                        intent.putExtra("name", mCourseList.getName());
                                                        intent.putExtra("type", mType);
//                                                        intent.setClass(NewSinglePurchaseActivity.this, NewMyClassDetialActivity.class);
                                                        intent.setClass(NewSinglePurchaseActivity.this, MyNewCoursesActivity.class);
                                                        startActivity(intent);
                                                    }else{
                                                        ToastUtil.showShortToast("提交订单");
                                                        Intent intent=new Intent();
                                                        intent.setClass(NewSinglePurchaseActivity.this,ConfirmOrderActivity.class);
                                                        intent.putExtra("course_id",mCourseList.getId());
                                                        intent.putExtra("flag","3");//flag 1 图书确认订单 2班级确认订单  3课程确认订单
                                                        intent.putExtra("type","2");//单科购买只传2  不传1
                                                        startActivity(intent);

                                                        Log.e("*************course_id",mCourseList.getId());
                                                    }
                                                }
                                            });

                                            mCourseTitle = new ArrayList<>();
                                            childArray = new ArrayList<>();
                                            for(int m = 0;m < result.getData().getCourse_list().size();m++){
                                                mCourseLists.add(result.getData().getCourse_list().get(m));
                                                Log.e("TTT",result.getData().getCourse_list().get(m).getIs_buy());
                                            }
                                            for(int i = 0;i< mCourseList.getCatalog().size();i++){
                                                mCourseTitle.add(mCourseList.getCatalog().get(i));
                                            }
                                            for(int j = 0;j < mCourseTitle.size();j++){
                                                for(int k = 0; k< mCourseTitle.get(j).getSon().size();k++){
                                                    mCourseContent = new ArrayList<>();
                                                    mCourseContent.add(mCourseTitle.get(j).getSon().get(k));
                                                    childArray.add(mCourseContent);
                                                }
                                            }
                                            mExpandableViewAdapter = new CourseCatalogFragmentAdapter(NewSinglePurchaseActivity.this,mCourseTitle,childArray,mCourseLists,mType,mCourseId,mSingleCourserId,mIsbuy,className);
                                            mExpandableView.setAdapter(mExpandableViewAdapter);
                                        }
                                    });
                                }else{
//                                    mPullToRefresh.showView(ViewStatus.EMPTY_STATUS);
                                }
                            }
                        }
                    }
                }

        GloableConstant.getInstance().stopProgressDialog1();
    }


    public void requestCatalog(){
        Map<String,String> map = new HashMap<>();
//        map.put("course_id","16");
//        map.put("type_id","1");
        map.put("course_id",mCourseId);
        map.put("type_id",mType);
        map.put("user_id", (String) SPUtils.getSpValues(SPKeyValuesUtils.SP_USER_ID, SPUtils.TYPE_STRING));
        Log.e("MAP",map.toString());
        GloableConstant.getInstance().startProgressDialog(NewSinglePurchaseActivity.this);
        Xutil.GET(ApiUrl.COURSEDETAILCATALOGS,map,new MyCallBack<NewCourseCatalogResponse>(){
            @Override
            public void onSuccess(final NewCourseCatalogResponse result) {
                super.onSuccess(result);
                GloableConstant.getInstance().stopProgressDialog1();
//                mPullToRefresh.finishRefresh();
//                mPullToRefresh.finishLoadMore();
//                mPullToRefresh.showView(ViewStatus.CONTENT_STATUS);
                if(result.getStatus_code().equals("200")){

                    if(result.getData() != null){
                        if(result.getData().getCourse_list() != null && result.getData().getCourse_list().size() > 0){
                            if(result.getData().getCourse_list().get(0).getCatalog() !=null && result.getData().getCourse_list().get(0).getCatalog().size() > 0){
                                if(result.getData().getCourse_list().get(0).getCatalog().get(0).getSon() != null && result.getData().getCourse_list().get(0).getCatalog().get(0).getSon().size()>0){
                                    mSingleBuyList.setLayoutManager(new LinearLayoutManager(NewSinglePurchaseActivity.this));
                                    mSingleBuyList.setAdapter(new CommonAdapter<NewCourseCatalogResponse.mCourseList>(NewSinglePurchaseActivity.this,R.layout.item_single_buy_ex,result.getData().getCourse_list()) {
                                        @Override
                                        protected void convert(final ViewHolder holder, final NewCourseCatalogResponse.mCourseList mCourseList, int position) {
                                            final ExpandableListView mExpandableView = (ExpandableListView) holder.getConvertView().findViewById(R.id.expandable_list_view);
                                            final ToggleButton mSingleSwitch = (ToggleButton) holder.getConvertView().findViewById(R.id.single_switch);
                                            holder.setText(R.id.single_class_name,mCourseList.getName());
                                            holder.setText(R.id.single_class_price,"￥"+mCourseList.getPrice());
                                            String className = mCourseList.getName();
                                            String mSingleCourserId = mCourseList.getId();//单个套餐的id
                                            int mIsbuy = Integer.valueOf(mCourseList.getIs_buy());
                                            mSingleSwitch.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    if(mSingleSwitch.isChecked()){
                                                        mExpandableView.setVisibility(View.VISIBLE);
                                                    }else{
                                                        mExpandableView.setVisibility(View.GONE);
                                                    }
                                                }
                                            });
                                            if(mCourseList.getIs_buy().equals("1")){
                                                holder.setBackgroundRes(R.id.to_buy,R.drawable.purple_shape_30);
                                                holder.setText(R.id.to_buy,"去学习");
                                            }else if (mCourseList.getIs_buy().equals("2")){
                                                holder.setBackgroundRes(R.id.to_buy,R.drawable.purple_shape_30);
                                                holder.setText(R.id.to_buy,"去续费");
                                            }else{
                                                holder.setBackgroundRes(R.id.to_buy,R.drawable.orange_shape_30);
                                                holder.setText(R.id.to_buy,"去购买");
                                            }
                                            holder.setOnClickListener(R.id.to_buy, new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    if(mCourseList.getIs_buy().equals("1")){
                                                        Intent intent = new Intent();
                                                        intent.putExtra("id", mCourseList.getId());
//                                                        intent.putExtra("progress", "20");
                                                        intent.putExtra("name", mCourseList.getName());
                                                        intent.putExtra("type", mType);
//                                                        intent.setClass(NewSinglePurchaseActivity.this, NewMyClassDetialActivity.class);
                                                        intent.setClass(NewSinglePurchaseActivity.this, MyNewCoursesActivity.class);
                                                        startActivity(intent);
                                                    }else{
                                                        ToastUtil.showShortToast("提交订单");
                                                        Intent intent=new Intent();
                                                        intent.setClass(NewSinglePurchaseActivity.this,ConfirmOrderActivity.class);
                                                        intent.putExtra("course_id",mCourseList.getId());
                                                        intent.putExtra("flag","3");//flag 1 图书确认订单 2班级确认订单  3课程确认订单
                                                        intent.putExtra("type","2");//单科购买只传2  不传1
                                                        startActivity(intent);

                                                        Log.e("*************course_id",mCourseList.getId());
                                                    }
                                                }
                                            });

                                            mCourseTitle = new ArrayList<>();
                                            childArray = new ArrayList<>();
                                                for(int m = 0;m < result.getData().getCourse_list().size();m++){
                                                    mCourseLists.add(result.getData().getCourse_list().get(m));
                                                    Log.e("TTT",result.getData().getCourse_list().get(m).getIs_buy());
                                                }
                                            for(int i = 0;i< mCourseList.getCatalog().size();i++){
                                                mCourseTitle.add(mCourseList.getCatalog().get(i));
                                            }
                                            for(int j = 0;j < mCourseTitle.size();j++){
                                                for(int k = 0; k< mCourseTitle.get(j).getSon().size();k++){
                                                    mCourseContent = new ArrayList<>();
                                                    mCourseContent.add(mCourseTitle.get(j).getSon().get(k));
                                                    childArray.add(mCourseContent);
                                                }
                                            }
                                            mExpandableViewAdapter = new CourseCatalogFragmentAdapter(NewSinglePurchaseActivity.this,mCourseTitle,childArray,mCourseLists,mType,mCourseId,mSingleCourserId,mIsbuy,className);
                                            mExpandableView.setAdapter(mExpandableViewAdapter);
                                        }
                                    });
                                }else{
//                                    mPullToRefresh.showView(ViewStatus.EMPTY_STATUS);
                                }
                            }
                        }
                    }
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
                GloableConstant.getInstance().stopProgressDialog1();
//                mPullToRefresh.finishRefresh();
//                mPullToRefresh.finishLoadMore();
//                mPullToRefresh.showView(ViewStatus.ERROR_STATUS);
            }
        });
    }

    @Override
    public void onclick(int parentPosition, int childPosition, int childIndex) {
        mSingleAdapter.notifyDataSetChanged();
    }
}
