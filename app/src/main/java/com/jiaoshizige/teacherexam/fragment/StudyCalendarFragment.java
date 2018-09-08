package com.jiaoshizige.teacherexam.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.jwenfeng.library.pulltorefresh.BaseRefreshListener;
import com.jwenfeng.library.pulltorefresh.ViewStatus;
import com.jiaoshizige.teacherexam.R;
import com.jiaoshizige.teacherexam.base.MBaseFragment;
import com.jiaoshizige.teacherexam.http.ApiUrl;
import com.jiaoshizige.teacherexam.http.MyCallBack;
import com.jiaoshizige.teacherexam.http.Xutil;
import com.jiaoshizige.teacherexam.model.StudyCalendarresponse;
import com.jiaoshizige.teacherexam.utils.PullToRefreshLayoutRewrite;
import com.jiaoshizige.teacherexam.utils.SPKeyValuesUtils;
import com.jiaoshizige.teacherexam.utils.SPUtils;
import com.jiaoshizige.teacherexam.utils.ToastUtil;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/a1/a1.
 * 学习日历fragment
 */

public class StudyCalendarFragment extends MBaseFragment{
    private List<String> mDatas;
    private List<String> mContentDatas;
    @BindView(R.id.study_calendar_listview)
    RecyclerView mRecyclerView;
    @BindView(R.id.pulltorefresh)
    PullToRefreshLayoutRewrite mPullToRefresh;
    private int id;
    private String mStartTime;
    private String mEndTime;
    private String mCLassId;
    private String mPageSize = "10";
    private String mPageNum = "1";
    private int mPage = 1;
    private String mType = "";
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.study_calendar_fragment,container,false);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser){
            Log.e("ATG","setUserVisibleHint");
            Bundle args = getArguments();
            if (args != null) {
                id = args.getInt("id");
                mStartTime = args.getString("startTime");
                mEndTime = args.getString("end_time");
                mCLassId = args.getString("class_id");
                mType = args.getString("type");
            }
            requestStudyCalandar();

        }
    }

    @Override
    protected void onInitViews(Bundle savedInstanceState) {
        requestStudyCalandar();
          mPullToRefresh.setRefreshListener(new BaseRefreshListener() {
                @Override
                public void refresh() {
                    mPageNum = "1";
                    requestStudyCalandar();
                }

                @Override
                public void loadMore() {
                    mPage++;
                    mPageNum = String.valueOf(mPage);
                    requestStudyCalandar();
                }
            });
    }
    public static StudyCalendarFragment getInstance(int id,String startTime,String endTime,String class_id,String type){
        Log.e("ATG","StudyCalendarFragment");
        StudyCalendarFragment instance = new StudyCalendarFragment();
        Bundle args = new Bundle();
        args.putInt("id", id);
        args.putString("startTime", startTime);
        args.putString("end_time", endTime);
        args.putString("class_id", class_id);
        args.putString("type",type);
        instance.setArguments(args);
        return instance;
    }
    private void  requestStudyCalandar(){
        Map<String,Object> map = new HashMap<>();
//        map.put("user_id","1");
        map.put("user_id",  SPUtils.getSpValues(SPKeyValuesUtils.SP_USER_ID, SPUtils.TYPE_STRING));
        map.put("group_id",mCLassId);
        map.put("start_time",mStartTime);
        map.put("end_time",mEndTime);
        map.put("type_id",mType);
        Log.e("TTg",map.toString());
//        GloableConstant.getInstance().startProgressDialog(getActivity());
        Xutil.Post(ApiUrl.STUDYCALEND,map,new MyCallBack<StudyCalendarresponse>(){
            @Override
            public void onSuccess(final StudyCalendarresponse result) {
                super.onSuccess(result);
//                GloableConstant.getInstance().stopProgressDialog1();
                mPullToRefresh.finishRefresh();
                mPullToRefresh.finishLoadMore();
                mPullToRefresh.showView(ViewStatus.CONTENT_STATUS);
                if(result.getStatus_code().equals("200")){
                    if(result.getData() != null && result.getData().size() > 0){
                        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                        mRecyclerView.setAdapter(new CommonAdapter<StudyCalendarresponse.mData>(getActivity(),R.layout.item_study_calendar,result.getData()) {
                            @Override
                            protected void convert(ViewHolder holder, StudyCalendarresponse.mData mData, int position) {
                                holder.setText(R.id.date,mData.getDate());
                                RecyclerView mContentRecyclerView = (RecyclerView) holder.getView(R.id.date_list);
                                mContentRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                                mContentRecyclerView.setAdapter(new CommonAdapter<StudyCalendarresponse.mChild>(getActivity(), R.layout.item_study_calendar_content, mData.getChild()) {
                                    @Override
                                    protected void convert(ViewHolder holder, StudyCalendarresponse.mChild mChild, int position) {
                                        holder.setText(R.id.title,mChild.getCourse_name());
                                        holder.setText(R.id.content,mChild.getSection_name());
                                        holder.setText(R.id.time,mChild.getAdd_time());
                                        ImageView mType = (ImageView) holder.getConvertView().findViewById(R.id.type);
                                        if(mChild.getType().equals("1")){//1视频2试题3图文
                                            mType.setBackground(ContextCompat.getDrawable(getActivity(),R.mipmap.video));
                                        }else if(mChild.getType().equals("2")){
                                            mType.setBackground(ContextCompat.getDrawable(getActivity(),R.mipmap.test));
                                        }else if(mChild.getType().equals("3")){
                                            mType.setBackground(ContextCompat.getDrawable(getActivity(),R.mipmap.imagetext));
                                        }
                                    }
                                });
                            }
                        });
                    }else{
                        mPullToRefresh.showView(ViewStatus.EMPTY_STATUS);
                    }
                }else{
                    if(result.getStatus_code().equals("203")){
                        if(mPage > 1){
                            ToastUtil.showShortToast("没有更多数据了");
                        }else{
                            mPullToRefresh.showView(ViewStatus.EMPTY_STATUS);
                        }
                    }

                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
                Log.e("TTG",ex.getMessage()+"pp");
//                GloableConstant.getInstance().stopProgressDialog1();
                mPullToRefresh.finishRefresh();
                mPullToRefresh.finishLoadMore();
                mPullToRefresh.showView(ViewStatus.EMPTY_STATUS);
            }
        });
    }

}
