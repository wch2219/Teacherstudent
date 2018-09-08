package com.jiaoshizige.teacherexam.yy.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.jiaoshizige.teacherexam.R;
import com.jiaoshizige.teacherexam.activity.CoursesNoteDetialActivity;
import com.jiaoshizige.teacherexam.base.CommRecyclerViewAdapter;
import com.jiaoshizige.teacherexam.base.GloableConstant;
import com.jiaoshizige.teacherexam.base.MBaseFragment;
import com.jiaoshizige.teacherexam.base.ViewHolderZhy;
import com.jiaoshizige.teacherexam.http.ApiUrl;
import com.jiaoshizige.teacherexam.http.MyCallBack;
import com.jiaoshizige.teacherexam.http.Xutil;
import com.jiaoshizige.teacherexam.model.NoteListResponse;
import com.jiaoshizige.teacherexam.utils.PullToRefreshLayoutRewrite;
import com.jiaoshizige.teacherexam.utils.ToastUtil;
import com.jwenfeng.library.pulltorefresh.BaseRefreshListener;
import com.jwenfeng.library.pulltorefresh.ViewStatus;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/a1/15.
 * 课程笔记
 */

public class CourseraNoteFragment extends MBaseFragment {
    @BindView(R.id.note_list)
    RecyclerView mNoteList;
//    @BindView(R.id.course_no_data)
//    LinearLayout mCourseNoData;
//    @BindView(R.id.course_error)
//    LinearLayout mCourseError;
    @BindView(R.id.pulltorefresh)
    PullToRefreshLayoutRewrite mPullToRefresh;
    @BindView(R.id.scroll)
    NestedScrollView NestedScrollView;
    private String mPageSize = "10";
    private int mPage = 1;
    /*@BindView(R.id.item_note)
    LinearLayout mItemNoteLy;*/
    private String mCourseId;
    private String mChapterId = "";
    private CommRecyclerViewAdapter<NoteListResponse.mData> mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.coursera_note_fragment,container,false);
    }

    public CourseraNoteFragment() {
    }

    @SuppressLint("ValidFragment")
    public CourseraNoteFragment(String course_id) {
        this.mCourseId = course_id;
    }

    @Override
    protected void onInitViews(Bundle savedInstanceState) {
//        mPullToRefresh.setNestedScrollingEnabled(false);
        mPullToRefresh.setRefreshListener(new BaseRefreshListener() {
            @Override
            public void refresh() {
                mPage = 1;
                requestNoteList(mChapterId);
            }

            @Override
            public void loadMore() {
                mPage++;
                requestNoteList(mChapterId);
            }
        });
        mPullToRefresh.setOnErrorListener(new PullToRefreshLayoutRewrite.onClickCallBackListener() {
            @Override
            public void onCallBackListener(View v) {
                mPage = 1;
                requestNoteList(mChapterId);
            }
        });
//        requestNoteList(mChapterId);
        mNoteList.setNestedScrollingEnabled(false);
        NestedScrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                mPullToRefresh.setEnabled
                        (NestedScrollView.getScrollY() == 0);
            }
        });
    }
    public void changeChapter(String chapter_id){
        mChapterId = chapter_id;
        requestNoteList(chapter_id);
    }

    @Override
    public void onResume() {
        super.onResume();
        mPage = 1;
        requestNoteList(mChapterId);
    }

    private void requestNoteList(String chapter_id){
        Map<String,String> map = new HashMap<>();
        map.put("course_id",mCourseId);
        map.put("chapter_id",chapter_id);
        map.put("page",mPage+"");
        map.put("page_size",mPageSize);
        Log.e("TAGmap",map.toString());
        Log.e("TAG","3");
//        GloableConstant.getInstance().startProgressDialog(getActivity());
        Xutil.GET(ApiUrl.NOTELIS,map,new MyCallBack<NoteListResponse>(){
            @Override
            public void onSuccess(NoteListResponse result) {
                super.onSuccess(result);
//                GloableConstant.getInstance().stopProgressDialog1();
                mPullToRefresh.finishRefresh();
                mPullToRefresh.finishLoadMore();
                mPullToRefresh.showView(ViewStatus.CONTENT_STATUS);
                Log.e("TAG","33");
//                mCourseNoData.setVisibility(View.GONE);
//                mCourseError.setVisibility(View.GONE);
                if(result.getStatus_code().equals("200")){
                    if(mPage == 1){
                        getAdapter().updateData(result.getData());
                    } else {
                        getAdapter().appendData(result.getData());
                    }
                    if(result.getData() != null && result.getData().size() > 0){
//                        mNoteList.setLayoutManager(new LinearLayoutManager(getActivity()));
//                        mNoteList.setAdapter(new CommonAdapter<NoteListResponse.mData>(getActivity(),R.layout.item_coursera_note,mDataList) {
//                            @Override
//                            protected void convert(ViewHolder holder, final NoteListResponse.mData mData, int position) {
//                                holder.setText(R.id.note_type,mData.getTitle());
//                                holder.setText(R.id.note_content,mData.getContent());
//                                holder.setText(R.id.author_nickname,mData.getName());
//                                holder.setText(R.id.note_time,mData.getCreated_at());
//                                holder.setText(R.id.zan_num,mData.getCount_zan());
//                                ImageView mPhoto = (ImageView) holder.getConvertView().findViewById(R.id.author_photo);
//                                Glide.with(getActivity()).load(mData.getAvatar()).apply(GloableConstant.getInstance().getOptions()).into(mPhoto);
//                                holder.setOnClickListener(R.id.item_note, new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(View v) {
//                                        Intent intent = new Intent();
//                                        intent.putExtra("note_id",mData.getId());
//                                        intent.setClass(getActivity(), CoursesNoteDetialActivity.class);
//                                        startActivity(intent);
//                                    }
//                                });
//                            }
//                        });
                    }else{
                        if(mPage > 1){
                            mPage--;
                            ToastUtil.showShortToast("没有更多数据了");
                        }else{
                            mPullToRefresh.showView(ViewStatus.EMPTY_STATUS);
//                            mCourseNoData.setVisibility(View.VISIBLE);
                        }

                    }
                }else if(result.getStatus_code().equals("203")){
                    if(mPage > 1){
                        mPage--;
                        ToastUtil.showShortToast("没有更多数据了");
                    }else{
                        mPullToRefresh.showView(ViewStatus.EMPTY_STATUS);
//                        mCourseNoData.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
//                GloableConstant.getInstance().stopProgressDialog1();
//                mCourseError.setVisibility(View.VISIBLE);
//                mCourseNoData.setVisibility(View.GONE);
                mPullToRefresh.finishRefresh();
                mPullToRefresh.finishLoadMore();
                mPullToRefresh.showView(ViewStatus.EMPTY_STATUS);
            }
        });
    }



    private CommRecyclerViewAdapter<NoteListResponse.mData> getAdapter() {
        if (null == mAdapter) {
            mAdapter = new CommRecyclerViewAdapter<NoteListResponse.mData>(getActivity(), R.layout.item_coursera_note, null) {
                @Override
                protected void convert(ViewHolderZhy holder, final NoteListResponse.mData mData, final int position) {
                    holder.setText(R.id.note_type,mData.getTitle());
                    holder.setText(R.id.note_content,mData.getContent());
                    holder.setText(R.id.author_nickname,mData.getName());
                    holder.setText(R.id.note_time,mData.getCreated_at());
                    holder.setText(R.id.zan_num,mData.getCount_zan());
                    ImageView mPhoto = (ImageView) holder.getConvertView().findViewById(R.id.author_photo);
                    Glide.with(getActivity()).load(mData.getAvatar()).apply(GloableConstant.getInstance().getOptions()).into(mPhoto);
                    holder.setOnClickListener(R.id.item_note, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent();
                            intent.putExtra("note_id",mData.getId());
                            intent.setClass(getActivity(), CoursesNoteDetialActivity.class);
                            startActivity(intent);
                        }
                    });
                }
            };
            mNoteList.setLayoutManager(new LinearLayoutManager(getActivity()));
            mNoteList.setAdapter(mAdapter);
        }
        return mAdapter;
    }
}
