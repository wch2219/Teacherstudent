package com.jiaoshizige.teacherexam.activity;

import android.graphics.drawable.BitmapDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.jiaoshizige.teacherexam.base.CommSelectorRecyclerViewAdapter;
import com.jiaoshizige.teacherexam.base.ViewHolderZhy;
import com.umeng.analytics.MobclickAgent;
import com.jiaoshizige.teacherexam.R;
import com.jiaoshizige.teacherexam.base.BaseActivity;
import com.jiaoshizige.teacherexam.base.GloableConstant;
import com.jiaoshizige.teacherexam.http.ApiUrl;
import com.jiaoshizige.teacherexam.http.MyCallBack;
import com.jiaoshizige.teacherexam.http.SupportResponse;
import com.jiaoshizige.teacherexam.http.Xutil;
import com.jiaoshizige.teacherexam.model.NewCourseCatalogResponse;
import com.jiaoshizige.teacherexam.model.NoteaWorkChapterResponse;
import com.jiaoshizige.teacherexam.palyer.PolyvDownloadInfo;
import com.jiaoshizige.teacherexam.palyer.PolyvDownloadSQLiteHelper;
import com.jiaoshizige.teacherexam.utils.SPKeyValuesUtils;
import com.jiaoshizige.teacherexam.utils.SPUtils;
import com.jiaoshizige.teacherexam.utils.ToastUtil;
import com.jiaoshizige.teacherexam.widgets.AllPopupwindow;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/a1/17.
 * 添加笔记
 */

public class AddNoteActivity extends BaseActivity{
    @BindView(R.id.note_type)
    TextView mNoteTYpe;
    @BindView(R.id.note_switch)
    ToggleButton mNoteSwitch;
    @BindView(R.id.note_type_ly)
    LinearLayout mNoteTypeLy;
    @BindView(R.id.add_note)
    EditText mAddNote;
    AllPopupwindow mNoteTypePopUpWindow;
    PolyvDownloadInfo model = new PolyvDownloadInfo();
    private String mCourseId;
    private int hasFilter = 0;//有无分类
    private String mChapterId = "";
    private String mSectionId;
    private String mType;
    @Override
    protected int getLayoutId() {
        return R.layout.add_note_activity;
    }

    @Override
    protected void initView() {
        setSubTitle().setText("确定");
        setToolbarTitle().setText("添加笔记");
        if(getIntent().getExtras().get("course_id") !=null){
            mCourseId = (String) getIntent().getExtras().get("course_id");
        }else{
            mCourseId = "";
        }
        if(getIntent().getExtras().get("section_id") != null){
            mSectionId = (String) getIntent().getExtras().get("section_id");

        }else{
            mSectionId = "";
        }
        if(mSectionId != null && !mSectionId.equals("")){
//            mNoteTypeLy.setVisibility(View.GONE);
//            Log.e("TGGGGSSSS","wwwwwww");
        }
        if(getIntent().getExtras().get("type") !=null){
            mType = (String) getIntent().getExtras().get("type");
        }else{
            mType = "";
        }
        if(getIntent().getExtras().get("vid") != null){
            model = PolyvDownloadSQLiteHelper.getInstance(this).getInfo((String) getIntent().getExtras().get("vid"));
            if(model.getVid() != null){
                mCourseId = String.valueOf(model.getCourse_id());
                mType = model.getType();
            }
        }
        setSubTitle().setTextColor(ContextCompat.getColor(this,R.color.purple4));
//        requestNoteShapter();
        requestShapter();
        setSubTitle().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!mAddNote.getText().toString().trim().equals("")){
                    if(!mChapterId.equals("")){
                        reuqestAddNote(mAddNote.getText().toString().trim());
                    }else{
                        ToastUtil.showShortToast("请先选择课时");
                    }

                }else{
                    ToastUtil.showShortToast("笔记内容不能为空");
                }

            }
        });

        mNoteSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(hasFilter == 1){
                    ToastUtil.showShortToast("暂无分类");
                    return;
                }
                if(isChecked){
                    showPop(mNoteTypeLy);
                } else {
                    dismiss();
                }
            }
        });
    }
//    @OnClick(R.id.note_switch)
//    public void noteSwitchClick(){
//        if(hasFilter != 1){
//        if(mNoteSwitch.isChecked()){
//            mChapterId = "";
//            mNoteTypePopUpWindow.setOnDismissListener(new AddNoteActivity.poponDismissListenermDepartment());
//            mNoteTypePopUpWindow.showFilterPopup(mNoteTypeLy);
//        }else{
//            mNoteSwitch.setChecked(false);
//            mNoteTypePopUpWindow.dismiss();
//        }
//        }else{
//            ToastUtil.showShortToast("暂无分类");
//        }
//    }
    class poponDismissListenermDepartment implements PopupWindow.OnDismissListener {
        @Override
        public void onDismiss() {
            setBackgroundAlpha(1f);
            mNoteSwitch.setChecked(false);
            if(GloableConstant.getInstance().getmAddNoteChapterId() !=null){
                mChapterId = GloableConstant.getInstance().getmAddNoteChapterId();
            }else{
                mChapterId = "";
            }
            if(GloableConstant.getInstance().getmAddNoteChapterTitle() != null){
                mNoteTYpe.setText(GloableConstant.getInstance().getmAddNoteChapterTitle());
                Log.e("TGGG",GloableConstant.getInstance().getmAddNoteChapterTitle());
            }else{

            }Log.e("TGGG","wcoaoanin");
        }
    }
    public void setBackgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha;
        getWindow().setAttributes(lp);
    }
    private void reuqestAddNote(String content){
        Map<String,Object> map = new HashMap<>();
//        map.put("user_id","1");
        map.put("user_id", (String) SPUtils.getSpValues(SPKeyValuesUtils.SP_USER_ID, SPUtils.TYPE_STRING));
        map.put("chapter_id",mChapterId);
        map.put("course_id",mCourseId);
        map.put("content",content);
        Log.e("TAGS",map.toString());
        GloableConstant.getInstance().startProgressDialog(this);
        Xutil.Post(ApiUrl.NOTEADD,map,new MyCallBack<SupportResponse>(){
            @Override
            public void onSuccess(SupportResponse result) {
                super.onSuccess(result);
                GloableConstant.getInstance().stopProgressDialog();
                if(result.getStatus_code().equals("204")){
                    ToastUtil.showShortToast("添加笔记成功");
                    EventBus.getDefault().post("添加笔记成功");
                    finish();
                }else{
                    ToastUtil.showShortToast(result.getMessage());
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
                GloableConstant.getInstance().stopProgressDialog();
            }
        });

    }
    /**
     * 课时列表 作业 笔记
     * @param
     */
    private void requestNoteShapter(){
        Map<String,String> map = new HashMap<>();
        map.put("course_id", mCourseId);
        map.put("user_id", (String) SPUtils.getSpValues(SPKeyValuesUtils.SP_USER_ID, SPUtils.TYPE_STRING));
        map.put("type_id",mType);
        Log.e("TAGmap",map.toString());
//        GloableConstant.getInstance().showmeidialog(this);
        Xutil.GET(ApiUrl.CHAPTER,map,new MyCallBack<NoteaWorkChapterResponse>(){
            @Override
            public void onSuccess(NoteaWorkChapterResponse result) {
                super.onSuccess(result);
                GloableConstant.getInstance().stopProgressDialog();
                if(result.getStatus_code().equals("200")){
                    if(result.getData() != null){
                        mNoteTypePopUpWindow = new AllPopupwindow(AddNoteActivity.this,result.getData(),"addnote");
                    }else{
                        hasFilter = 1;
                    }
                }
            }
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
                GloableConstant.getInstance().stopProgressDialog();
            }
        });
    }
    /**
     * 新课时列表
     */
    public void requestShapter(){
        Map<String ,String> map = new HashMap<>();
        map.put("course_id", mCourseId);
        map.put("user_id", (String) SPUtils.getSpValues(SPKeyValuesUtils.SP_USER_ID, SPUtils.TYPE_STRING));
        map.put("type_id",mType);
        Log.e("NOte",map.toString());
        GloableConstant.getInstance().startProgressDialog(this);
        Xutil.GET(ApiUrl.COURSEDETAILCATALOGS,map,new MyCallBack<NewCourseCatalogResponse>(){
            @Override
            public void onSuccess(NewCourseCatalogResponse result) {
                super.onSuccess(result);
                GloableConstant.getInstance().stopProgressDialog();
                if(result.getData().getCourse_list() != null && result.getData().getCourse_list().size() > 0){
                    List<NewCourseCatalogResponse.mCourseList> courseList = result.getData().getCourse_list();
                    List<NewCourseCatalogResponse.mCatalog> catalogList = new ArrayList<>();
                    for (int i = 0,count = courseList.size(); i < count; i++) {
                        catalogList.addAll(courseList.get(i).getCatalog());
                    }
                    getAdapter(catalogList);

//                    List<NoteaWorkChapterResponse.mData> mChapterList = new ArrayList<>();
//                    for(int i=0;i< result.getData().getCourse_list().size();i++){
//                        NoteaWorkChapterResponse.mData response = new NoteaWorkChapterResponse.mData();
//                        response.setId(result.getData().getCourse_list().get(i).getId());
//                        response.setTitle(result.getData().getCourse_list().get(i).getName());
////                      response.setPractice(result.getData().getCourse_list().get(i).getId());
//                        mChapterList.add(response);
//                    }
//                    mNoteTypePopUpWindow = new AllPopupwindow(AddNoteActivity.this,mChapterList,"addnote");
                }else{
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

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        MobclickAgent.onPageStart("添加笔记");
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("添加笔记");
        MobclickAgent.onPause(this);
    }


    private PopupWindow mPopWindChoose;
    private CommSelectorRecyclerViewAdapter<NewCourseCatalogResponse.mCatalog> mAdapter;
    private void getAdapter(List<NewCourseCatalogResponse.mCatalog> datas){
        if(mAdapter == null){
            mAdapter = new CommSelectorRecyclerViewAdapter<NewCourseCatalogResponse.mCatalog>(this,R.layout.item_pop_add_note,datas, CommSelectorRecyclerViewAdapter.ESelectMode.SINGLE_MUST_ONE_SELECTED) {
                @Override
                protected void convert(ViewHolderZhy holder, NewCourseCatalogResponse.mCatalog item, final int position) {
                    holder
                            .setText(R.id.item_tv_title, item.getTitle())
                            .setOnClickListener(R.id.item_tv_title, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    doSelected(position);
                                    mChapterId = getItem(position).getId();
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
        View menuView = LayoutInflater.from(this).inflate(R.layout.pop_choose_course_add_note ,null);

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
                mPopWindChoose.dismiss();
            }
        });
        RecyclerView recyclerView = (RecyclerView) menuView.findViewById(R.id.pop_listView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mAdapter);
    }

    private void showPop(View view){
        if(mPopWindChoose != null){
            mPopWindChoose.showAsDropDown(view);
        }
    }
    private void dismiss(){
        if(mPopWindChoose != null){
            mPopWindChoose.dismiss();
        }
    }


}
