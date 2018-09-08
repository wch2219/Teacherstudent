package com.jiaoshizige.teacherexam.fragment;

import android.content.Intent;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jwenfeng.library.pulltorefresh.BaseRefreshListener;
import com.jwenfeng.library.pulltorefresh.ViewStatus;
import com.jiaoshizige.teacherexam.R;
import com.jiaoshizige.teacherexam.activity.CoursesDetialActivity;
import com.jiaoshizige.teacherexam.base.GloableConstant;
import com.jiaoshizige.teacherexam.base.MBaseFragment;
import com.jiaoshizige.teacherexam.http.ApiUrl;
import com.jiaoshizige.teacherexam.http.MyCallBack;
import com.jiaoshizige.teacherexam.http.Xutil;
import com.jiaoshizige.teacherexam.model.CourseResponse;
import com.jiaoshizige.teacherexam.model.NoteaWorkChapterResponse;
import com.jiaoshizige.teacherexam.utils.PullToRefreshLayoutRewrite;
import com.jiaoshizige.teacherexam.utils.SPKeyValuesUtils;
import com.jiaoshizige.teacherexam.utils.SPUtils;
import com.jiaoshizige.teacherexam.utils.ToastUtil;
import com.jiaoshizige.teacherexam.widgets.AllPopupwindow;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
/**
 * Created by Administrator on 2017/a1/17.
 * 私塾课程
 */
public class PrivateSchoolCourseFragment extends MBaseFragment{
    @BindView(R.id.course_recycler)
    RecyclerView mCourseRecycler;
    @BindView(R.id.type)
    RadioButton mType;
    @BindView(R.id.sort)
    RadioButton mSort;
    @BindView(R.id.price)
    RadioButton mPrice;
    @BindView(R.id.screen_ly)
    LinearLayout mScreenLy;
    @BindView(R.id.pulltorefresh)
    PullToRefreshLayoutRewrite mPullToRefresh;
    AllPopupwindow mCourseTypePopupWindow;
    AllPopupwindow mSourseSortPopupWindow;
    private String mPageNum = "1";
    private String mPageSize = "15";
    private int mPage = 1;
    private int priceFlag = 1;
    private String type;
    private String sortField;
    private String sortOrder;
    private String nameTag;
    List< NoteaWorkChapterResponse.mData> mTypeList;
    List< NoteaWorkChapterResponse.mData> mSortList;
    List<CourseResponse.mData> mList = new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.course_fragment,container,false);
    }

    @Override
    public void onResume() {
        super.onResume();
        if(GloableConstant.getInstance().getInstance().getSearchCourseTag() !=null){
            nameTag = GloableConstant.getInstance().getSearchCourseTag();
            requestCourseList(type,sortField,sortOrder,nameTag);
            Log.e("Shool",nameTag);
        }
        /*if(GloableConstant.getInstance().getInstance().getmCourseName() !=null){
            nameTag = GloableConstant.getInstance().getmCourseName();
            requestCourseList(type,sortField,sortOrder,nameTag);
            Log.e("Shool",nameTag);
        }*/

    }

    @Override
    protected void onInitViews(Bundle savedInstanceState) {
         NoteaWorkChapterResponse.mData tdata1 = new  NoteaWorkChapterResponse.mData();
        tdata1.setTitle("基础");
        tdata1.setId("1");
         NoteaWorkChapterResponse.mData tdata2 = new  NoteaWorkChapterResponse.mData();
        tdata2.setTitle("拔高");
        tdata2.setId("2");
         NoteaWorkChapterResponse.mData tdata3 = new  NoteaWorkChapterResponse.mData();
        tdata3.setTitle("冲刺");
        tdata3.setId("3");
        mTypeList = new ArrayList<>();
        mTypeList.add(tdata1);
        mTypeList.add(tdata2);
        mTypeList.add(tdata3);

         NoteaWorkChapterResponse.mData sdata2 = new  NoteaWorkChapterResponse.mData();
        sdata2.setTitle("最新");
         NoteaWorkChapterResponse.mData sdata3 = new  NoteaWorkChapterResponse.mData();
        sdata3.setTitle("销量升序");
         NoteaWorkChapterResponse.mData sdata4 = new  NoteaWorkChapterResponse.mData();
        sdata4.setTitle("销量降序");
        mSortList = new ArrayList<>();
        mSortList.add(sdata2);
        mSortList.add(sdata3);
        mSortList.add(sdata4);
        mCourseTypePopupWindow = new AllPopupwindow(getActivity(),mTypeList,"couesetype");
        mSourseSortPopupWindow = new AllPopupwindow(getActivity(),mSortList,"couesesort");

//        requestCourseList("","","","");
        mPullToRefresh.setRefreshListener(new BaseRefreshListener() {
            @Override
            public void refresh() {
                mPageNum = "1";
                requestCourseList(type,sortField,sortOrder,nameTag);
            }

            @Override
            public void loadMore() {
                mPage++;
                mPageNum = String.valueOf(mPage);
                requestCourseList(type,sortField,sortOrder,nameTag);
            }
        });
        mPullToRefresh.setOnErrorListener(new PullToRefreshLayoutRewrite.onClickCallBackListener() {
            @Override
            public void onCallBackListener(View v) {
                mPageNum = "1";
                requestCourseList(type,sortField,sortOrder,nameTag);
            }
        });
        requestCourseList(type,sortField,sortOrder,nameTag);
    }
    public void setmTypeChecked() {
        mType.setChecked(false);
        mPrice.setChecked(false);
        Drawable drawable = ContextCompat.getDrawable(getActivity(), R.mipmap.home_arrow_double);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        mPrice.setCompoundDrawables(null, null, drawable, null);
    }

    public void setmSortChecked() {
//        mSort.setChecked(false);
        mPrice.setChecked(false);
        Drawable drawable = ContextCompat.getDrawable(getActivity(), R.mipmap.home_arrow_double);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        mPrice.setCompoundDrawables(null, null, drawable, null);
    }

    public void setmPriceChecked() {
//        mType.setChecked(false);
        GloableConstant.getInstance().setSort(null);
        mSort.setText("排序");
        mSort.setChecked(false);
    }
    @OnClick({R.id.type,R.id.sort,R.id.price})
    public void onClick(View v){
        if(v.getId() == R.id.type){
            mCourseTypePopupWindow.setOnDismissListener(new poponDismissListenermDepartment());
            mCourseTypePopupWindow.showFilterPopup(mScreenLy);
        }else if(v.getId() == R.id.sort){
            mSourseSortPopupWindow.setOnDismissListener(new poponDismissListenermDepartment());
            mSourseSortPopupWindow.showFilterPopup(mScreenLy);
        }else if(v.getId() == R.id.price){
            if (priceFlag == 1) {
                Drawable drawable = ContextCompat.getDrawable(getActivity(), R.mipmap.home_arrow_double_up);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                mPrice.setCompoundDrawables(null, null, drawable, null);
                priceFlag = 0;
                setmPriceChecked();
                sortField = "price";
                sortOrder ="asc";
                requestCourseList(type,sortField,sortOrder,"");
            }else {
                Drawable drawable = ContextCompat.getDrawable(getActivity(), R.mipmap.home_arrow_double_down);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                mPrice.setCompoundDrawables(null, null, drawable, null);
                priceFlag = 1;
                setmPriceChecked();
                sortField = "price";
                sortOrder ="desc";
                requestCourseList(type,sortField,sortOrder,nameTag);
            }
        }

    }
    class poponDismissListenermDepartment implements PopupWindow.OnDismissListener {
        @Override
        public void onDismiss() {
            if(GloableConstant.getInstance().getSort() != null){
                mSort.setChecked(true);
                mSort.setText(GloableConstant.getInstance().getSort());
                Drawable drawable= ContextCompat.getDrawable(getActivity(),R.mipmap.home_arrow_small_down_pre);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                mSort.setCompoundDrawables(null,null,drawable,null);
                setmSortChecked();

            }else{
                mSort.setChecked(false);
                mSort.setText("排序");
                Drawable drawable= ContextCompat.getDrawable(getActivity(),R.mipmap.home_arrow_small_down);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                mSort.setCompoundDrawables(null,null,drawable,null);
                setmSortChecked();
            }

            if(GloableConstant.getInstance().getType() != null){
                mType.setChecked(true);
                mType.setText(GloableConstant.getInstance().getType());
                Drawable drawable= ContextCompat.getDrawable(getActivity(),R.mipmap.home_arrow_small_down_pre);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                mType.setCompoundDrawables(null,null,drawable,null);
                if(GloableConstant.getInstance().getType().equals("基础")){
                    type = "1";
                    requestCourseList(type,sortField,sortOrder,nameTag);
                }else if(GloableConstant.getInstance().getType().equals("拔高")){
                    type = "2";
                    requestCourseList(type,sortField,sortOrder,nameTag);
                }else if(GloableConstant.getInstance().getType().equals("冲刺")){
                    type = "3";
                    requestCourseList(type,sortField,sortOrder,nameTag);
                }
            }else{
                mType.setChecked(false);
                mType.setText("类型");
                Drawable drawable= ContextCompat.getDrawable(getActivity(),R.mipmap.home_arrow_small_down);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                mType.setCompoundDrawables(null,null,drawable,null);
            }
            for(int i = 0;i < mTypeList.size();i++){
                if(mType.getText().toString().equals(mTypeList.get(i).getTitle())){
                    type = mTypeList.get(i).getId();
                }
            }
            if(mSort.getText().toString().equals("最新")){
                sortField = "created_at";
                sortOrder ="";
                requestCourseList(type,sortField,sortOrder,nameTag);
            }else if(mSort.getText().toString().equals("销量升序")){
                sortField = "sale_num";
                sortOrder ="asc";
                requestCourseList(type,sortField,sortOrder,nameTag);
            }else if(mSort.getText().toString().equals("销量降序")){
                sortField = "sale_num";
                sortOrder ="desc";
                requestCourseList(type,sortField,sortOrder,nameTag);
            }

            setBackgroundAlpha(1f);
        }
    }
    public void setBackgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.alpha = bgAlpha;
        getActivity().getWindow().setAttributes(lp);
    }
    private void requestCourseList(String type,String sort_field,String sort_order,String name){
        Map<String,String> map = new HashMap<>();
        map.put("user_id",(String) SPUtils.getSpValues(SPKeyValuesUtils.SP_USER_ID, SPUtils.TYPE_STRING));
        map.put("type",type);
        map.put("sort_field",sort_field);
        map.put("sort_order",sort_order);
        map.put("name",name);
        map.put("page_size",mPageSize);
        map.put("page",mPageNum);
        Log.e("TAGG",map.toString());
        GloableConstant.getInstance().showmeidialog(getActivity());
        Xutil.GET(ApiUrl.COURSELIST,map,new MyCallBack<CourseResponse>(){
            @Override
            public void onSuccess(CourseResponse result) {
                super.onSuccess(result);
                GloableConstant.getInstance().stopProgressDialog();
                mPullToRefresh.finishRefresh();
                mPullToRefresh.finishLoadMore();
                mPullToRefresh.showView(ViewStatus.CONTENT_STATUS);
                if(result.getStatus_code().equals("200")){
                    if(result.getData() != null && result.getData().size() > 0){
                        mCourseRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
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
                        mCourseRecycler.setAdapter(new CommonAdapter<CourseResponse.mData>(getActivity(),R.layout.item_my_course_fragment,mList) {
                            @Override
                            protected void convert(ViewHolder holder, final CourseResponse.mData s, int position) {
                                holder.setText(R.id.course_name,s.getName());
                                holder.setText(R.id.course_price,s.getPrice());
                                holder.setText(R.id.student_num,s.getSale_num()+"人在学习");
                                holder.setText(R.id.original_price,"￥"+s.getMarket_price());
                                TextView mOrginalPrice = (TextView) holder.getConvertView().findViewById(R.id.original_price);
//                                mOrginalPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                                mOrginalPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG|Paint.ANTI_ALIAS_FLAG);
                                ImageView mCourseCover = (ImageView) holder.getConvertView().findViewById(R.id.cover_photo);
                                Glide.with(getActivity()).load(s.getImages()).apply(GloableConstant.getInstance().getDefaultOptionssmall()).into(mCourseCover);
                                holder.setOnClickListener(R.id.course_item, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent();
                                        intent.setClass(getActivity(), CoursesDetialActivity.class);
                                        intent.putExtra("course_id",s.getId());
                                        intent.putExtra("type","2");
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
