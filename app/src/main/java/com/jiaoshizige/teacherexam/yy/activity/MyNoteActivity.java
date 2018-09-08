package com.jiaoshizige.teacherexam.yy.activity;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.bumptech.glide.Glide;
import com.jiaoshizige.teacherexam.base.CommSelectorRecyclerViewAdapter;
import com.jwenfeng.library.pulltorefresh.BaseRefreshListener;
import com.jwenfeng.library.pulltorefresh.ViewStatus;
import com.umeng.analytics.MobclickAgent;
import com.jiaoshizige.teacherexam.R;
import com.jiaoshizige.teacherexam.activity.AddNoteActivity;
import com.jiaoshizige.teacherexam.activity.CoursesNoteDetialActivity;
import com.jiaoshizige.teacherexam.base.BaseActivity;
import com.jiaoshizige.teacherexam.base.CommRecyclerViewAdapter;
import com.jiaoshizige.teacherexam.base.GloableConstant;
import com.jiaoshizige.teacherexam.base.ViewHolderZhy;
import com.jiaoshizige.teacherexam.http.ApiUrl;
import com.jiaoshizige.teacherexam.http.MyCallBack;
import com.jiaoshizige.teacherexam.http.Xutil;
import com.jiaoshizige.teacherexam.model.NewCourseCatalogResponse;
import com.jiaoshizige.teacherexam.model.NoteListResponse;
import com.jiaoshizige.teacherexam.model.NoteaWorkChapterResponse;
import com.jiaoshizige.teacherexam.utils.PullToRefreshLayoutRewrite;
import com.jiaoshizige.teacherexam.utils.SPKeyValuesUtils;
import com.jiaoshizige.teacherexam.utils.SPUtils;
import com.jiaoshizige.teacherexam.utils.ToastUtil;
import com.jiaoshizige.teacherexam.widgets.AllPopupwindow;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/a1/17.
 * 我的笔记
 */

public class MyNoteActivity extends BaseActivity {
    @BindView(R.id.my_note_list)
    RecyclerView mMyNoteList;
    @BindView(R.id.note_type)
    TextView mNoteTYpe;
    @BindView(R.id.note_switch)
    ToggleButton mNoteSwitch;
    @BindView(R.id.note_type_ly)
    LinearLayout mNoteTypeLy;
    @BindView(R.id.float_action_btn)
    FloatingActionButton mFlaotActionBtn;
    @BindView(R.id.pulltorefresh)
    PullToRefreshLayoutRewrite mPullToRefresh;
    AllPopupwindow mNoteTypePopUpWindow;
    private int hasFilter = 0;//有无分类
    private String mCourseId;
    private String mPageNum = "1";
    private String mPageSize = "15";
    private int mPage = 1;
    private String mType;
    private CommRecyclerViewAdapter<NoteListResponse.mData> mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.my_note_activity;
    }

    @Override
    protected void initView() {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        if (getIntent().getExtras().get("course_id") != null) {
            mCourseId = (String) getIntent().getExtras().get("course_id");
        } else {
            mCourseId = "";
        }
        if (getIntent().getExtras().get("type") != null) {
            mType = (String) getIntent().getExtras().get("type");
        } else {
            mType = "";
        }
        setSubTitle().setText("");
        setToolbarTitle().setText("我的笔记");

        mPullToRefresh.setRefreshListener(new BaseRefreshListener() {
            @Override
            public void refresh() {
                mPageNum = "1";
                mPage = 1;
                requestMyNote(GloableConstant.getInstance().getMuNoteChapter());
            }

            @Override
            public void loadMore() {
                mPage++;
                mPageNum = String.valueOf(mPage);
                requestMyNote(GloableConstant.getInstance().getMuNoteChapter());
            }
        });
        mPullToRefresh.setOnErrorListener(new PullToRefreshLayoutRewrite.onClickCallBackListener() {
            @Override
            public void onCallBackListener(View v) {
                mPageNum = "1";
                mPage = 1;
                requestMyNote(GloableConstant.getInstance().getMuNoteChapter());
            }
        });
        requestMyNote(GloableConstant.getInstance().getMuNoteChapter());
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void getEventBus(String num) {
        if (num != null) {
            if (num.equals("添加笔记成功")) {
                requestMyNote(GloableConstant.getInstance().getMuNoteChapter());
            }
        }
    }


    @OnClick(R.id.note_switch)
    public void noteSwitchClick() {
        if (hasFilter != 1) {
            if (mNoteSwitch.isChecked()) {
                showPop(mNoteSwitch);
//                mNoteTypePopUpWindow.setOnDismissListener(new MyNoteActivity.poponDismissListenermDepartment());
//                mNoteTypePopUpWindow.showFilterPopup(mNoteTypeLy);
            } else {
                mNoteSwitch.setChecked(false);
                dismiss();
//                mNoteTypePopUpWindow.dismiss();
            }
        } else {
            ToastUtil.showShortToast("暂无分类");
        }
    }

    @OnClick(R.id.float_action_btn)
    public void onActionClick() {
        Intent intent = new Intent();
        intent.putExtra("course_id", mCourseId);
        intent.putExtra("type", mType);
        intent.setClass(this, AddNoteActivity.class);
        startActivity(intent);
    }

    class poponDismissListenermDepartment implements PopupWindow.OnDismissListener {
        @Override
        public void onDismiss() {
            setBackgroundAlpha(1f);
            mNoteSwitch.setChecked(false);
            if (GloableConstant.getInstance().getMuNoteChapter() != null) {
                mPage = 1;//新加
                mPageNum = "1";
                requestMyNote(GloableConstant.getInstance().getMuNoteChapter());
            }
            if (GloableConstant.getInstance().getmNoteChapterTitle() != null) {
                mNoteTYpe.setText(GloableConstant.getInstance().getmNoteChapterTitle());
            }

        }
    }

    public void setBackgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha;
        getWindow().setAttributes(lp);
    }

    @Override
    protected void onResume() {
        super.onResume();
//        requestNoteShapter();
        mPage = 1;
        mPageNum = "1";
        requestShapter();
        requestMyNote(GloableConstant.getInstance().getMuNoteChapter());
        MobclickAgent.onPageStart("我的笔记");
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("我的笔记");
        MobclickAgent.onPause(this);
    }

    /**
     * 我的笔记列表
     *
     * @param chapter_id
     */
    private void requestMyNote(String chapter_id) {
        Map<String, String> map = new HashMap<>();
//        map.put("user_id","1");
        map.put("user_id", (String) SPUtils.getSpValues(SPKeyValuesUtils.SP_USER_ID, SPUtils.TYPE_STRING));
        map.put("chapter_id", chapter_id);
        map.put("course_id", mCourseId);
        map.put("page", mPageNum);
        map.put("page_size", mPageSize);
        Log.e("Tag", map.toString());
        GloableConstant.getInstance().startProgressDialog(this);
        Xutil.GET(ApiUrl.NOTELIS, map, new MyCallBack<NoteListResponse>() {
            @Override
            public void onSuccess(NoteListResponse result) {
                super.onSuccess(result);
                GloableConstant.getInstance().stopProgressDialog();
                mPullToRefresh.finishRefresh();
                mPullToRefresh.finishLoadMore();
                mPullToRefresh.showView(ViewStatus.CONTENT_STATUS);
                if (result.getStatus_code().equals("200")) {
                    if (mPage == 1) {
                        getAdapter().updateData(result.getData());
                    } else {
                        getAdapter().appendData(result.getData());
                    }
                    if (result.getData() != null && result.getData().size() > 0) {
//                        if(mPageNum.equals("1")){
//                            mDataList.clear();
//                            mDataList = result.getData();
//                        }else{
//                            if (null == mDataList){
//                                mDataList = result.getData();
//                            }
//                            else{
//                                mDataList.addAll(result.getData());
//                            }
//                        }
//                        mMyNoteList.setLayoutManager(new LinearLayoutManager(MyNoteActivity.this));
//                        mMyNoteList.setAdapter(new CommonAdapter<NoteListResponse.mData>(MyNoteActivity.this,R.layout.item_coursera_note,mDataList) {
//                            @Override
//                            protected void convert(ViewHolder holder,final NoteListResponse.mData mData, int position) {
//                                holder.setText(R.id.note_type,mData.getTitle());
//                                holder.setText(R.id.note_content,mData.getContent());
//                                holder.setText(R.id.author_nickname,mData.getName());
//                                holder.setText(R.id.note_time,mData.getCreated_at());
//                                holder.setText(R.id.zan_num,mData.getCount_zan());
//                                ImageView mPhoto = (ImageView) holder.getConvertView().findViewById(R.id.author_photo);
//                                Glide.with(MyNoteActivity.this).load(mData.getAvatar()).apply(GloableConstant.getInstance().getOptions()).into(mPhoto);
//                                holder.setOnClickListener(R.id.item_note, new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(View v) {
//                                        Intent intent = new Intent();
//                                        intent.putExtra("note_id",mData.getId());
//                                        intent.setClass(MyNoteActivity.this, CoursesNoteDetialActivity.class);
//                                        startActivity(intent);
//                                    }
//                                });
//                            }
//                        });
                    } else {
                        if (mPage > 1) {
                            mPage--;
                            ToastUtil.showShortToast("没有更多数据了");
                        } else {
                            mPullToRefresh.showView(ViewStatus.EMPTY_STATUS);
                        }
                    }
                } else {
                    if (result.getStatus_code().equals("203")) {
                        if (mPage > 1) {
                            mPage--;
                            ToastUtil.showShortToast("没有更多数据了");
                        } else {
                            mPullToRefresh.showView(ViewStatus.EMPTY_STATUS);
                        }

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

    /**
     * 课时列表 作业 笔记
     *
     * @param
     */
    private void requestNoteShapter() {
        Map<String, String> map = new HashMap<>();
        map.put("course_id", mCourseId);
        map.put("type", "2");//1考试，2笔记,作业
        Log.e("TAGmap", map.toString());
//        GloableConstant.getInstance().showmeidialog(this);
        Xutil.GET(ApiUrl.CHAPTER, map, new MyCallBack<NoteaWorkChapterResponse>() {
            @Override
            public void onSuccess(NoteaWorkChapterResponse result) {
                super.onSuccess(result);
                if (result.getStatus_code().equals("200")) {
                    if (result.getData() != null) {
                        mNoteTypePopUpWindow = new AllPopupwindow(MyNoteActivity.this, result.getData(), "mynote");
                    } else {
                        hasFilter = 1;
                    }
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
            }
        });
    }

    /**
     * 新课时列表
     */
    public void requestShapter() {
        Map<String, String> map = new HashMap<>();
        map.put("course_id", mCourseId);
        map.put("user_id", (String) SPUtils.getSpValues(SPKeyValuesUtils.SP_USER_ID, SPUtils.TYPE_STRING));
        map.put("type_id", mType);
        Log.e("NOte", map.toString());
        GloableConstant.getInstance().startProgressDialog(this);
        Xutil.GET(ApiUrl.COURSEDETAILCATALOGS, map, new MyCallBack<NewCourseCatalogResponse>() {
            @Override
            public void onSuccess(NewCourseCatalogResponse result) {
                super.onSuccess(result);
                GloableConstant.getInstance().stopProgressDialog();
                if (result.getData().getCourse_list() != null && result.getData().getCourse_list().size() > 0) {
                    List<NewCourseCatalogResponse.mCourseList> courseList = result.getData().getCourse_list();
                    List<NewCourseCatalogResponse.mCatalog> catalogList = new ArrayList<>();
                    for (int i = 0, count = courseList.size(); i < count; i++) {
                        catalogList.addAll(courseList.get(i).getCatalog());
                    }
                    getAdapterPop(catalogList);
//                    List<NoteaWorkChapterResponse.mData> mChapterList = new ArrayList<>();
//                    for (int i = 0; i < result.getData().getCourse_list().size(); i++) {
//                        NoteaWorkChapterResponse.mData response = new NoteaWorkChapterResponse.mData();
//                        response.setId(result.getData().getCourse_list().get(i).getId());
//                        response.setTitle(result.getData().getCourse_list().get(i).getName());
////                      response.setPractice(result.getData().getCourse_list().get(i).getId());
//                        mChapterList.add(response);
//                    }
//                    mNoteTypePopUpWindow = new AllPopupwindow(MyNoteActivity.this, mChapterList, "addnote");
                } else {
                    hasFilter = 1;
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
                GloableConstant.getInstance().stopProgressDialog();
            }
        });
    }

    private CommRecyclerViewAdapter<NoteListResponse.mData> getAdapter() {
        if (null == mAdapter) {
            mAdapter = new CommRecyclerViewAdapter<NoteListResponse.mData>(this, R.layout.item_coursera_note, null) {
                @Override
                protected void convert(ViewHolderZhy holder, final NoteListResponse.mData mData, final int position) {
                    holder.setText(R.id.note_type, mData.getTitle());
                    holder.setText(R.id.note_content, mData.getContent());
                    holder.setText(R.id.author_nickname, mData.getName());
                    holder.setText(R.id.note_time, mData.getCreated_at());
                    holder.setText(R.id.zan_num, mData.getCount_zan());
                    ImageView mPhoto = (ImageView) holder.getConvertView().findViewById(R.id.author_photo);
                    Glide.with(MyNoteActivity.this).load(mData.getAvatar()).apply(GloableConstant.getInstance().getOptions()).into(mPhoto);
                    holder.setOnClickListener(R.id.item_note, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent();
                            intent.putExtra("note_id", mData.getId());
                            intent.setClass(MyNoteActivity.this, CoursesNoteDetialActivity.class);
                            startActivity(intent);
                        }
                    });

                    /*holder.setOnLongClickListener(R.id.item_note, new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {


                            return true;
                        }
                    });*/
                }
            };
            mMyNoteList.setLayoutManager(new LinearLayoutManager(this));
            mMyNoteList.setAdapter(mAdapter);
        }
        return mAdapter;
    }

    private PopupWindow mPopWindChoose;
    private CommSelectorRecyclerViewAdapter<NewCourseCatalogResponse.mCatalog> mAdapterPop;

    private void getAdapterPop(List<NewCourseCatalogResponse.mCatalog> datas) {
        if (mAdapterPop == null) {
            mAdapterPop = new CommSelectorRecyclerViewAdapter<NewCourseCatalogResponse.mCatalog>(this, R.layout.item_pop_add_note, datas, CommSelectorRecyclerViewAdapter.ESelectMode.SINGLE_MUST_ONE_SELECTED) {
                @Override
                protected void convert(ViewHolderZhy holder, NewCourseCatalogResponse.mCatalog item, final int position) {
                    holder
                            .setText(R.id.item_tv_title, item.getTitle())
                            .setOnClickListener(R.id.item_tv_title, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
//                                    doSelected(position);
                                    requestMyNote(getItem(position).getId());
                                    mNoteTYpe.setText(getItem(position).getTitle());
                                    dismiss();
                                }
                            });
                }
            };
        }
        initChoosePop();
    }

    private void initChoosePop() {
        if (mPopWindChoose != null) {
            return;
        }
        View menuView = LayoutInflater.from(this).inflate(R.layout.pop_choose_course_add_note, null);

        mPopWindChoose = new PopupWindow(menuView, -1, -2);
//        mPopWindChoose.setAnimationStyle(R.style.popwin_anim_style);
        mPopWindChoose.setTouchable(true);
        mPopWindChoose.setFocusable(true);
        mPopWindChoose.setBackgroundDrawable(new BitmapDrawable());
        mPopWindChoose.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                mNoteSwitch.setChecked(false);
            }
        });
        TextView pop_dismiss = (TextView) menuView.findViewById(R.id.pop_dismiss);
        pop_dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mNoteTYpe.setText("全部课程");
                requestMyNote("");
                mPopWindChoose.dismiss();
            }
        });
        RecyclerView recyclerView = (RecyclerView) menuView.findViewById(R.id.pop_listView);
        recyclerView.setLayoutManager(new LinearLayoutManager(MyNoteActivity.this));
        recyclerView.setAdapter(mAdapterPop);
    }

    private void showPop(View view) {
        if (mPopWindChoose != null) {
            mPopWindChoose.showAsDropDown(view);
        }
    }

    private void dismiss() {
        if (mPopWindChoose != null) {
            mPopWindChoose.dismiss();
        }
    }


}
