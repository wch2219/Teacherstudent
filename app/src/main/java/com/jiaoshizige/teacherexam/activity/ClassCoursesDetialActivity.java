package com.jiaoshizige.teacherexam.activity;

import android.content.Intent;
import android.graphics.Paint;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.bumptech.glide.Glide;
import com.jiaoshizige.teacherexam.R;
import com.jiaoshizige.teacherexam.adapter.TabPagerAdapter;
import com.jiaoshizige.teacherexam.base.BaseActivity;
import com.jiaoshizige.teacherexam.base.GloableConstant;
import com.jiaoshizige.teacherexam.fragment.CourseCatalogFragmnet;
import com.jiaoshizige.teacherexam.fragment.CourseraNoteFragment;
import com.jiaoshizige.teacherexam.yy.fragment.QuestionAndAnswerFragment;
import com.jiaoshizige.teacherexam.http.ApiUrl;
import com.jiaoshizige.teacherexam.http.MyCallBack;
import com.jiaoshizige.teacherexam.http.Xutil;
import com.jiaoshizige.teacherexam.model.CourseDetialResponse;
import com.jiaoshizige.teacherexam.model.NoteaWorkChapterResponse;
import com.jiaoshizige.teacherexam.utils.SPKeyValuesUtils;
import com.jiaoshizige.teacherexam.utils.SPUtils;
import com.jiaoshizige.teacherexam.utils.ToastUtil;
import com.jiaoshizige.teacherexam.utils.ToolUtils;
import com.jiaoshizige.teacherexam.widgets.AllPopupwindow;
import com.jiaoshizige.teacherexam.yy.activity.MyHomeWorkActivity;
import com.jiaoshizige.teacherexam.yy.activity.MyNoteActivity;
import com.jiaoshizige.teacherexam.yy.fragment.HomeworkFragment;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/a1/13.
 * 课程详情 已经拥有的班级里面的
 * //////
 */

public class ClassCoursesDetialActivity extends BaseActivity implements ViewPager.OnPageChangeListener {
    @BindView(R.id.cover_photo)
    ImageView mCoverPhoto;
    @BindView(R.id.course_name)
    TextView mCourseName;
    @BindView(R.id.course_price)
    TextView mCoursePrice;
    @BindView(R.id.student_num)
    TextView mStudentNum;
    @BindView(R.id.original_price)
    TextView mOriginalPrice;
    @BindView(R.id.give_book)
    TextView mGIveBook;
    @BindView(R.id.courses_tag_list)
    RecyclerView mCoursesTagList;
    @BindView(R.id.activity_first)
    TextView mActivityFirst;
    @BindView(R.id.first_icon)
    ImageView mFirstIcon;
    @BindView(R.id.activity_num)
    TextView mActivityNum;
    @BindView(R.id.activity_switch)
    ToggleButton mActivitySwitch;
//    @BindView(R.id.activity_second)
//    TextView mActivitySecond;
   /* @BindView(R.id.second_icon)
    ImageView mSecondIcon;*/
    @BindView(R.id.tag_ly)
    LinearLayout mTagLy;
//    @BindView(R.id.hide_activity)
//    RecyclerView mHideActivity;
    @BindView(R.id.tablayout)
    TabLayout mTabLayout;
    @BindView(R.id.viewpager)
    ViewPager mViewPager;
    @BindView(R.id.note_classify)
    LinearLayout mNoteClassify;
    @BindView(R.id.classify_type)
    TextView mClassifyType;
    @BindView(R.id.note_icon)
    ImageView mNoteIcon;
    @BindView(R.id.classify_type_name)
    TextView mClassifyTypeName;
    @BindView(R.id.classify_switch)
    ToggleButton mClassifySwitch;
    @BindView(R.id.float_action_btn)
    FloatingActionButton mFloatActionBtn;
    @BindView(R.id.buy_bottom)
    LinearLayout mBuyBottom;
    @BindView(R.id.activity_ly)
    RelativeLayout mActivityLy;
    @BindView(R.id.consultation)
    LinearLayout mConsultation;
    @BindView(R.id.collection)
    LinearLayout mCollection;
    @BindView(R.id.all_add_buy)
    TextView mAddBuy;
    private List<Fragment> mFragments;
    private List<String> tabTitles;
    private TabPagerAdapter pagerAdapter;
    private int mPosition;
    private String mType;
    private String mCourseId;
    private String mCourseNameStr;
    private String mGroupId;
    private AllPopupwindow mAllPopUpWindowHomeWork;
    private AllPopupwindow mAllPopUpWindowNote;
    GridLayoutManager gridLayoutManager;
    private HomeworkFragment homeWorkFragment;
    private  CourseraNoteFragment courseraNoteFragment;
    private int collectType;
    private int hasFilter = 0;
    private int isBuy;
    private String mHomeWorkChapterTitle = "";
    private String mNoteChapterTitle = "";
    @Override
    protected int getLayoutId() {
        return R.layout.course_detial_activity;
    }
    protected void onStart() {
        super.onStart();
        ToolUtils.setIndicator(mTabLayout,25,25);
    }
    @Override
    protected void initView() {
        if(getIntent().getExtras().get("type") != null){
            mType = (String) getIntent().getExtras().get("type");
            if(mType.equals("1")){
                isBuy = 1;
                mBuyBottom.setVisibility(View.GONE);
                if (getIntent().getStringExtra("flag").equals("1")){
                    mTagLy.setVisibility(View.GONE);
                }else {
                    mTagLy.setVisibility(View.VISIBLE);
                }
            }else{
                isBuy = 0;
                mBuyBottom.setVisibility(View.VISIBLE);
                mAddBuy.setText("购买课程");
            }
        }
        if(getIntent().getExtras().get("course_id") != null){
            mCourseId = (String) getIntent().getExtras().get("course_id");
        }else{
            mCourseId = "";
        }
        if(getIntent().getExtras().get("group_id") != null){
            mGroupId = (String) getIntent().getExtras().get("group_id");
        }else{
            mGroupId = "";
        }
        if(getIntent().getExtras().get("course_name")!= null){
            mCourseNameStr = (String) getIntent().getExtras().get("course_name");
        }else{
            mCourseNameStr = "";
        }

        setSubTitle().setText("");
        setToolbarTitle().setText("课程");
        mFragments = new ArrayList<>();
        tabTitles = new ArrayList<>();
        tabTitles.add("目录");
        tabTitles.add("作业");
        tabTitles.add("笔记");
        tabTitles.add("问答");
        homeWorkFragment =  new HomeworkFragment(mCourseId);
        courseraNoteFragment =  new CourseraNoteFragment(mCourseId);
        mFragments.add(new CourseCatalogFragmnet(mGroupId,mCourseId,isBuy,mCourseNameStr));
        mFragments.add(homeWorkFragment);
        mFragments.add(courseraNoteFragment);
        mFragments.add(new QuestionAndAnswerFragment(mCourseId));
        for(int i=0;i<tabTitles.size();i++){
            mTabLayout.addTab(mTabLayout.newTab().setText(tabTitles.get(i)));
        }
        pagerAdapter = new TabPagerAdapter(getSupportFragmentManager(),mFragments,tabTitles);
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.setAdapter(pagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
        mViewPager.addOnPageChangeListener(this);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        gridLayoutManager = new GridLayoutManager(this, 4);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override public int getSpanSize(int position) {
                return 1;
            }
        });
//        mHideActivity.setLayoutManager(new LinearLayoutManager(this));
//        mAllPopUpWindowHomeWork = new AllPopupwindow(this,list,"homework");
//        mAllPopUpWindowNote = new AllPopupwindow(this,list,"note");
        mBuyBottom.setVisibility(View.GONE);
        requestCourseDetial();
    }
    @OnClick({R.id.all_add_buy,R.id.consultation,R.id.collection,R.id.float_action_btn})
    public void onClick(View v){
        switch (v.getId()){
            case R.id.all_add_buy:
                startActivity(new Intent(ClassCoursesDetialActivity.this,ConfirmOrderActivity.class));
                break;
            case R.id.consultation:
                ToastUtil.showShortToast("1");
                break;
            case R.id.collection:
                ToastUtil.showShortToast("1");
                break;
            case R.id.float_action_btn:
                if(mPosition == 3){
                    Intent intent = new Intent();
                    intent.putExtra("course_id",mCourseId);
                    intent.setClass(this, PutQuestionsActivity.class);
                    startActivity(intent);
                }else{
                    homeWorkFragment.backToTop();
                }
                break;
        }
    }



    @OnClick(R.id.activity_switch)
    public void onToggleButtonClick(){
        if(mActivitySwitch.isChecked()){
//            mHideActivity.setVisibility(View.VISIBLE);
        }else{
//            mHideActivity.setVisibility(View.GONE);
        }
    }
    @OnClick(R.id.classify_switch)
    public void ClassifyToggleSwitch(){
        if(hasFilter != 1){
            if(mClassifySwitch.isChecked()){
                if(mPosition == 1){
                    mAllPopUpWindowHomeWork.setOnDismissListener(new ClassCoursesDetialActivity.poponDismissListenermDepartment());
                    mAllPopUpWindowHomeWork.showFilterPopup(mNoteClassify);
                }else{
                    mAllPopUpWindowNote.setOnDismissListener(new ClassCoursesDetialActivity.poponDismissListenermDepartment());
                    mAllPopUpWindowNote.showFilterPopup(mNoteClassify);
                }
            }else{
                mClassifySwitch.setChecked(false);
                if(mPosition == 1){
                    mAllPopUpWindowHomeWork.dismiss();
                }else{
                    mAllPopUpWindowNote.dismiss();
                }
            }
        }else{
            ToastUtil.showShortToast("暂无分类");
        }

    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        mPosition = position;
        if(position == 3){
            mFloatActionBtn.setVisibility(View.VISIBLE);
            mFloatActionBtn.setImageResource(R.mipmap.home_png_askquestions_default);
        }else{
            mFloatActionBtn.setVisibility(View.GONE);
        }
    if(position == 2){
        mNoteClassify.setVisibility(View.VISIBLE);
        mNoteIcon.setVisibility(View.VISIBLE);
        mClassifyType.setText("我的笔记");
       if(!mNoteChapterTitle.equals("")){
           mClassifyTypeName.setText(mNoteChapterTitle);
       }else{
           mClassifyTypeName.setText("全部课时");
       }

        mNoteIcon.setBackground(ContextCompat.getDrawable(this,R.mipmap.note_icon));
        mClassifyType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("course_id",mCourseId);
                intent.setClass(ClassCoursesDetialActivity.this,MyNoteActivity.class);
                startActivity(intent);
            }
        });
        requestNoteShapter("2");
    }else if(position == 1){
        mClassifyType.setText("我的作业");
        if(!mHomeWorkChapterTitle.equals("")){
            mClassifyTypeName.setText(mHomeWorkChapterTitle);
        }else{
            mClassifyTypeName.setText("全部课时");
        }
        mNoteIcon.setVisibility(View.VISIBLE);
        mNoteIcon.setBackground(ContextCompat.getDrawable(this,R.mipmap.home_icon_operation_default));
        mNoteClassify.setVisibility(View.VISIBLE);
        mFloatActionBtn.setVisibility(View.VISIBLE);
        mFloatActionBtn.setImageResource(R.mipmap.home_icon_return_default);
        mClassifyType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("course_id",mCourseId);
                intent.setClass(ClassCoursesDetialActivity.this,MyHomeWorkActivity.class);
                startActivity(intent);
            }
        });
        requestNoteShapter("2");
    }else{
        mNoteClassify.setVisibility(View.GONE);
    }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
    class poponDismissListenermDepartment implements PopupWindow.OnDismissListener {
        @Override
        public void onDismiss() {
            setBackgroundAlpha(1f);
            mClassifySwitch.setChecked(false);
            if(mPosition == 1){
                if(GloableConstant.getInstance().getmHomeWorkChapterId() != null){
                    homeWorkFragment.changeChapter(GloableConstant.getInstance().getmHomeWorkChapterId());
                    mHomeWorkChapterTitle = GloableConstant.getInstance().getmHomeWorkChapterTitle();
                    mClassifyTypeName.setText(mHomeWorkChapterTitle);
                }
            }else{
                if(GloableConstant.getInstance().getmListNoteChapterId() != null) {
                    courseraNoteFragment.changeChapter(GloableConstant.getInstance().getmListNoteChapterId());
                    mNoteChapterTitle = GloableConstant.getInstance().getmListNoteChapterTitle();
                    mClassifyTypeName.setText(mNoteChapterTitle);
                }
            }

        }
    }
    public void setBackgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha;
        getWindow().setAttributes(lp);
    }
    /**
     * 课程详情
     */
    private void requestCourseDetial(){
        Map<String,String> map = new HashMap<>();
//        map.put("user_id","1");
        map.put("user_id", (String) SPUtils.getSpValues(SPKeyValuesUtils.SP_USER_ID, SPUtils.TYPE_STRING));
        map.put("course_id",mGroupId);
//        map.put("course_id","8");
        Log.e("TGGs***********",map.toString());
        Xutil.GET(ApiUrl.COURSERDETAIL,map,new MyCallBack<CourseDetialResponse>(){
            @Override
            public void onSuccess(CourseDetialResponse result) {
                super.onSuccess(result);
                if(result.getStatus_code().equals("200")){
                    if(result.getData() != null){
                        Glide.with(ClassCoursesDetialActivity.this).load(result.getData().getImage()).apply(GloableConstant.getInstance().getDefaultOptionssmall()).into(mCoverPhoto);
                        mCourseName.setText(result.getData().getName());
                        mCoursePrice.setText("￥"+result.getData().getPrice());
                        mStudentNum.setText(result.getData().getSale_num()+"人在学习");
                        mOriginalPrice.setText("￥"+result.getData().getMarket_price());
                        mOriginalPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                        if(!result.getData().getBook_id().equals("")){
                            mGIveBook.setVisibility(View.VISIBLE);
                        }else{
                            mGIveBook.setVisibility(View.GONE);
                        }
                        mCoursesTagList.setVisibility(View.VISIBLE);
                        mCoursesTagList.setLayoutManager(gridLayoutManager);
                        if(result.getData().getTag_list() != null && result.getData().getTag_list().size() > 0){
                            mCoursesTagList.setAdapter(new CommonAdapter<CourseDetialResponse.mTagList>(ClassCoursesDetialActivity.this,R.layout.item_courses_detial_tag,result.getData().getTag_list()) {
                                @Override
                                protected void convert(ViewHolder holder, CourseDetialResponse.mTagList mTagList, int position) {
                                    holder.setText(R.id.tag_name,mTagList.getTag_name());
                                    ImageView mTagImg = (ImageView)holder.getConvertView().findViewById(R.id.tag_img);
                                    Glide.with(ClassCoursesDetialActivity.this).load(mTagList.getImages()).into(mTagImg);
                                }
                            });
                        }else{
                            mTagLy.setVisibility(View.GONE);
                        }
                        if(result.getData().getActivity() != null && result.getData().getActivity().size() > 0){
                            mActivityNum.setText(result.getData().getActivity().size()+"个活动");
                            if(result.getData().getActivity().size() > 0 && result.getData().getActivity().size() < 2){
                                mActivityFirst.setText(result.getData().getActivity().get(0).getActivity_name());
                                Glide.with(ClassCoursesDetialActivity.this).load(result.getData().getActivity().get(0).getIcon()).into(mFirstIcon);
                                mActivitySwitch.setVisibility(View.INVISIBLE);
                            }/*else if(result.getData().getActivity().size() == 2){
                                mActivitySwitch.setVisibility(View.INVISIBLE);
                                mActivityFirst.setText(result.getData().getActivity().get(0).getActivity_name());
                                Glide.with(ClassCoursesDetialActivity.this).load(result.getData().getActivity().get(0).getIcon()).into(mFirstIcon);
//                                mActivitySecond.setText(result.getData().getActivity().get(1).getActivity_name());
//                                Glide.with(ClassCoursesDetialActivity.this).load(result.getData().getActivity().get(1).getIcon()).into(mSecondIcon);
                            }else{
                                mActivitySwitch.setVisibility(View.VISIBLE);
                                mHideActivity.setAdapter(new CommonAdapter<CourseDetialResponse.mActivity>(ClassCoursesDetialActivity.this,R.layout.item_courses_detial_activity,result.getData().getActivity()) {

                                    @Override
                                    protected void convert(ViewHolder holder, CourseDetialResponse.mActivity mActivity, int position) {
                                        holder.setText(R.id.tag_name,mActivity.getActivity_name());
                                        ImageView mIconImg =  (ImageView) holder.getConvertView().findViewById(R.id.tag_img);
                                        Glide.with(ClassCoursesDetialActivity.this).load(mActivity.getIcon()).into(mIconImg);
                                    }
                                });
                            }*/
                        }else{
                            mActivityLy.setVisibility(View.GONE);
                        }

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
     * 课时列表 作业 笔记
     * @param type
     */
    private void requestNoteShapter(final String type){
        Map<String,String> map = new HashMap<>();
        map.put("course_id",mGroupId);
//        map.put("course_id","8");
        map.put("type",type);//1考试，2笔记,作业
        Log.e("TAGmap",map.toString());
//        GloableConstant.getInstance().showmeidialog(this);
        Xutil.GET(ApiUrl.CHAPTER,map,new MyCallBack<NoteaWorkChapterResponse>(){
            @Override
            public void onSuccess(NoteaWorkChapterResponse result) {
                super.onSuccess(result);
//                GloableConstant.getInstance().stopProgressDialog();
                if(result.getStatus_code().equals("200")){
                    if(result.getData() != null){
                       /* if(type.equals("1")){
                            mAllPopUpWindowHomeWork = new AllPopupwindow(ClassCoursesDetialActivity.this,result.getData(),"homework");
                        }else{
                            mAllPopUpWindowNote = new AllPopupwindow(ClassCoursesDetialActivity.this,result.getData(),"note");
                        }*/
                        mAllPopUpWindowHomeWork = new AllPopupwindow(ClassCoursesDetialActivity.this,result.getData(),"homework");
                        mAllPopUpWindowNote = new AllPopupwindow(ClassCoursesDetialActivity.this,result.getData(),"note");
                    }else{
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
}
