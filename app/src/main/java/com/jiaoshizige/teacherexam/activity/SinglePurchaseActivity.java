package com.jiaoshizige.teacherexam.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ExpandableListView;

import com.jiaoshizige.teacherexam.R;
import com.jiaoshizige.teacherexam.adapter.MainAdapter;
import com.jiaoshizige.teacherexam.adapter.SingleMainAdapter;
import com.jiaoshizige.teacherexam.base.BaseActivity;
import com.jiaoshizige.teacherexam.base.GloableConstant;
import com.jiaoshizige.teacherexam.http.ApiUrl;
import com.jiaoshizige.teacherexam.http.MyCallBack;
import com.jiaoshizige.teacherexam.http.Xutil;
import com.jiaoshizige.teacherexam.model.NewCourseCatalogResponse;
import com.jiaoshizige.teacherexam.model.SingleBuyResponse;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/3/15.
 * 单科购买(废弃)
 */

public class SinglePurchaseActivity extends BaseActivity implements
        MainAdapter.OnExpandClickListener{
    @BindView(R.id.single_buy)
    RecyclerView mSingleBuyList;
    List<SingleBuyResponse.Data>  datas = new ArrayList<>();
//    SingleBuyAdapter mAdapter;
private String mCourseId;
    private String mType; //1班级2课程
    SingleMainAdapter mSingleAdapter;
    @Override
    protected int getLayoutId() {
        return R.layout.single_purchase_layout;
    }

    @Override
    protected void initView() {
        setSubTitle().setText("");
        setToolbarTitle().setText("单科购买");
        add();
        requestCatalog();
      /*  SinglePurchaseAdapter mSinglePurchaseAdapter = new SinglePurchaseAdapter(this);
        mSinglePurchaseAdapter.setmList(datas);
        mSingleBuyList.setAdapter(mSinglePurchaseAdapter);*/
       /* mSingleBuyList.setLayoutManager(new LinearLayoutManager(this));
        mSingleBuyList.setAdapter(new CommonAdapter<SingleBuyResponse.Data>(this,R.layout.item_single_buy_ex,datas) {
            @Override
            protected void convert(ViewHolder holder, SingleBuyResponse.Data mData, int position) {
                ExpandableListView mExpandableView = (ExpandableListView) holder.getConvertView().findViewById(R.id.expandable_list_view);
//                Log.e("TAG",mData.getTitle()+mData.getmChild().size());
//                Log.e("TAG","eeeee"+mData.getmChild().get(0).getTitle());
                Log.e("TAG","eeeee"+"执行了不知道几次");
                List<List<SingleBuyResponse.Child>> allChild = new ArrayList<>();
                allChild.add(mData.getmChild());
//                allChild.add(datas.get(1).getmChild());
                SingleBuyAdapter mAdapter = new SingleBuyAdapter(SinglePurchaseActivity.this,mData,allChild);
                mExpandableView.setAdapter(mAdapter);
            }
        });*/
    }
    public void add(){

        SingleBuyResponse.Data data1 = new SingleBuyResponse.Data();
        data1.setTitle("第一");
        List<SingleBuyResponse.Child> Child1 = new ArrayList<>();
        SingleBuyResponse.Child child1 = new SingleBuyResponse.Child();
        child1.setTitle("one child");
        Child1.add(child1);
        SingleBuyResponse.Child child2 = new SingleBuyResponse.Child();
        child2.setTitle("2 child");
        Child1.add(child2);
        data1.setmChild(Child1);
        datas.add(data1);
        SingleBuyResponse.Data data2 = new SingleBuyResponse.Data();
        data2.setTitle("第2");
        List<SingleBuyResponse.Child> Child2 = new ArrayList<>();
        SingleBuyResponse.Child child3 = new SingleBuyResponse.Child();
        child3.setTitle("3 child");
        Child2.add(child3);
        SingleBuyResponse.Child child4 = new SingleBuyResponse.Child();
        child4.setTitle("4 child");
        Child2.add(child4);
        data2.setmChild(Child2);
//        allChild.add(Child1);
//        allChild.add(Child2);
        datas.add(data2);
        Log.e("TAG",datas.size()+"d"+datas.get(0).getmChild().size());
        Log.e("TAG1",datas.get(0).getmChild().get(0).getTitle());

    }
    public void requestCatalog(){
        Map<String,String> map = new HashMap<>();
        map.put("course_id","16");
        map.put("type_id","1");
        Log.e("MAP",map.toString());
        GloableConstant.getInstance().startProgressDialog(SinglePurchaseActivity.this);
        Xutil.GET(ApiUrl.COURSEDETAILCATALOGS,map,new MyCallBack<NewCourseCatalogResponse>(){
            @Override
            public void onSuccess(NewCourseCatalogResponse result) {
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
//                                    mAdapter = new MainAdapter(getActivity(),result.getData().getCourse_list());
//                                    mList.setAdapter(mAdapter);
//                                    mAdapter.notifyDataSetChanged();
                                    LinearLayoutManager LayoutManager = new LinearLayoutManager(SinglePurchaseActivity.this);
//                                    mSingleBuyList.setLayoutManager(new LinearLayoutManager(SinglePurchaseActivity.this));
                                    LayoutManager.setAutoMeasureEnabled(true);
                                    mSingleBuyList.setLayoutManager(LayoutManager);
                                    mSingleBuyList.setAdapter(new CommonAdapter<NewCourseCatalogResponse.mCourseList>(SinglePurchaseActivity.this,R.layout.item_single_buy_ex,result.getData().getCourse_list()) {
                                        @Override
                                        protected void convert(ViewHolder holder, NewCourseCatalogResponse.mCourseList mCourseList, int position) {
                                            ExpandableListView mExpandableView = (ExpandableListView) holder.getConvertView().findViewById(R.id.expandable_list_view);
                                            mSingleAdapter = new SingleMainAdapter(SinglePurchaseActivity.this,mCourseList);
                                            mExpandableView.setAdapter(mSingleAdapter);
                                            mSingleAdapter.notifyDataSetChanged();
//                                            mAdapter = new MainAdapter(SinglePurchaseActivity.this,mCourseList);
                                          /*  List<List<SingleBuyResponse.Child>> allChild = new ArrayList<>();
                                            allChild.add(mData.getmChild());
//                allChild.add(datas.get(1).getmChild());
                                            SingleBuyAdapter mAdapter = new SingleBuyAdapter(SinglePurchaseActivity.this,mData,allChild);
                                            mExpandableView.setAdapter(mAdapter);*/
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
