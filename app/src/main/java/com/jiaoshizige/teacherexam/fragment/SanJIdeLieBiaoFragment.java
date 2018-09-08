package com.jiaoshizige.teacherexam.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.jiaoshizige.teacherexam.R;
import com.jiaoshizige.teacherexam.activity.aaa.xx.adapter.SanJiDeLeiBiaoAdapter;
import com.jiaoshizige.teacherexam.adapter.CourseCatalogFragmentAdapter;
import com.jiaoshizige.teacherexam.adapter.SingleMainAdapter;
import com.jiaoshizige.teacherexam.base.GloableConstant;
import com.jiaoshizige.teacherexam.base.MBaseFragment;
import com.jiaoshizige.teacherexam.http.ApiUrl;
import com.jiaoshizige.teacherexam.http.MyCallBack;
import com.jiaoshizige.teacherexam.http.Xutil;
import com.jiaoshizige.teacherexam.model.NewCourseCatalogResponse;
import com.jiaoshizige.teacherexam.model.busmodel.BusGetCcatalog;
import com.jiaoshizige.teacherexam.utils.SDOtherUtil;
import com.jiaoshizige.teacherexam.utils.SPKeyValuesUtils;
import com.jiaoshizige.teacherexam.utils.SPUtils;
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
 * Created by Administrator on 2018/4/2.
 * 三级列表的目录  测试用
 */

public class SanJIdeLieBiaoFragment extends MBaseFragment{
    @BindView(R.id.single_buy)
    RecyclerView mSingleBuyList;
    private String mCourseId;//用于请去的id 可以是套餐课程 可以是单科课程
    private String mType; //1班级2课程
    @BindView(R.id.course_no_data)
    LinearLayout mCourseNoData;
    @BindView(R.id.course_error)
    LinearLayout mCourseError;
    SingleMainAdapter mSingleAdapter;
    private List<NewCourseCatalogResponse.mCourseList> mCourseLists = new ArrayList<>();
    private List<NewCourseCatalogResponse.mCatalog> mCourseTitle;
    private List<NewCourseCatalogResponse.mSon> mCourseContent;
    private List<List<NewCourseCatalogResponse.mSon>> childArray;
    private CourseCatalogFragmentAdapter mExpandableViewAdapter;
    private NewCourseCatalogResponse mNewCourseCatalogResponse;
    private String isBuy;
    @SuppressLint("ValidFragment")
    public SanJIdeLieBiaoFragment(String group_id, String type,String is_buy) {
        this.mCourseId = group_id;
        this.mType = type;
        this.isBuy=is_buy;

        Log.d("******is_buy", is_buy+"/////");
    }

    @Subscribe
    public void bus(BusGetCcatalog busGetCcatalog){
        if(mNewCourseCatalogResponse != null){
            EventBus.getDefault().post(mNewCourseCatalogResponse);
        } else {
            NewCourseCatalogResponse response = new NewCourseCatalogResponse();
            response.setStatus_code("-1199");
            EventBus.getDefault().post(response);
        }
    }

    public SanJIdeLieBiaoFragment() {
    }
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.sanjiliebiao_layout,container,false);
    }
    @Override
    protected void onInitViews(Bundle savedInstanceState) {
        requestCatalog();
    }
    public void requestCatalog(){
        Map<String,String> map = new HashMap<>();
//        map.put("course_id","16");
//        map.put("type_id","1");
        map.put("course_id",mCourseId);
        map.put("type_id",mType);
        map.put("user_id", (String) SPUtils.getSpValues(SPKeyValuesUtils.SP_USER_ID, SPUtils.TYPE_STRING));
        Log.e("MAP****************",map.toString());
        GloableConstant.getInstance().startProgressDialog(getActivity());
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
                        mNewCourseCatalogResponse = result;
                        if(result.getData().getCourse_list() != null && result.getData().getCourse_list().size() > 0){
                            if(result.getData().getCourse_list().get(0).getCatalog() !=null && result.getData().getCourse_list().get(0).getCatalog().size() > 0){
                                if(result.getData().getCourse_list().get(0).getCatalog().get(0).getSon() != null && result.getData().getCourse_list().get(0).getCatalog().get(0).getSon().size()>0){
                                    mCourseNoData.setVisibility(View.GONE);
                                    mCourseError.setVisibility(View.GONE);
                                    mSingleBuyList.setLayoutManager(new LinearLayoutManager(getActivity()));

                                    mSingleBuyList.setAdapter(new CommonAdapter<NewCourseCatalogResponse.mCourseList>(getActivity(),R.layout.item_single_buy_ex,result.getData().getCourse_list()) {
                                        @Override
                                        protected void convert(final ViewHolder holder, final NewCourseCatalogResponse.mCourseList mCourseList, int position) {
                                            final ExpandableListView mExpandableView = (ExpandableListView) holder.getConvertView().findViewById(R.id.expandable_list_view);
                                            final ToggleButton mSingleSwitch = (ToggleButton) holder.getConvertView().findViewById(R.id.single_switch);
                                            TextView mClassName = (TextView) holder.getConvertView().findViewById(R.id.single_class_name);
                                            mClassName.setTextColor(ContextCompat.getColor(getActivity(),R.color.text_color3));
                                            mClassName.setTextSize(13);
                                            mClassName.setText(mCourseList.getName());
                                            String className = mCourseList.getName();
                                            String mSingleCourserId = mCourseList.getId();//单个套餐的id
//
//|||||||
//                                            int mIsbuy = Integer.valueOf(mCourseList.getIs_buy());
//=======
////                                            int mIsbuy = Integer.valueOf(mCourseList.getIs_buy());
//                                            int mIsbuy = SDOtherUtil.strToInt(mCourseList.getIs_buy());

                                         /*   holder.setText(R.id.single_class_name,mCourseList.getName());
                                            holder.setTextColor(R.id.single_class_name,R.color.text_color3);*/
                                            holder.setVisible(R.id.single_class_price,false);
                                            holder.setVisible(R.id.to_buy,false);
                                            holder.setVisible(R.id.background,false);
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
                                            mCourseTitle = new ArrayList<>();
                                            childArray = new ArrayList<>();
                                            for(int m = 0;m < result.getData().getCourse_list().size();m++){
                                                mCourseLists.add(result.getData().getCourse_list().get(m));
                                                Log.e("TTT",result.getData().getCourse_list().get(m).getIs_buy()+"////////");
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
                                            int mIsbuy = 0;
                                            if (isBuy.equals("1")){
                                               mIsbuy = 1;
                                            }else {
                                                if (mCourseList.getIs_buy()!=null){
                                                    mIsbuy = Integer.valueOf(mCourseList.getIs_buy());
                                                }
                                            }



                                            Log.e("************mIsbuy",mIsbuy+"///////////////////");
                                            mExpandableViewAdapter = new CourseCatalogFragmentAdapter(getActivity(),mCourseTitle,childArray,mCourseLists,mType,mCourseId,mSingleCourserId,mIsbuy,className);
                                            mExpandableView.setAdapter(mExpandableViewAdapter);
                                        }
                                    });
                                    //Todo
//                                    List<NewCourseCatalogResponse.mCourseList> course_list = result.getData().getCourse_list();
//                                    mSingleBuyList.setAdapter(new SanJiDeLeiBiaoAdapter(getActivity(),course_list));



                                }else{
//                                    mPullToRefresh.showView(ViewStatus.EMPTY_STATUS);
                                    mCourseNoData.setVisibility(View.VISIBLE);
                                }
                            }else{
                                mCourseNoData.setVisibility(View.VISIBLE);
                            }
                        }else{
                            mCourseNoData.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
                GloableConstant.getInstance().stopProgressDialog1();
                mCourseError.setVisibility(View.VISIBLE);
                mCourseNoData.setVisibility(View.GONE);
//                mPullToRefresh.finishRefresh();

                Log.e("*******ex",ex.getMessage()+"///////");
//                mPullToRefresh.finishLoadMore();
//                mPullToRefresh.showView(ViewStatus.ERROR_STATUS);
            }
        });
    }
    public void refreshCatalog(){
        requestCatalog();
    }
}
