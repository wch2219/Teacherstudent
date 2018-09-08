package com.jiaoshizige.teacherexam.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.bumptech.glide.Glide;
import com.umeng.analytics.MobclickAgent;
import com.jiaoshizige.teacherexam.R;
import com.jiaoshizige.teacherexam.adapter.TabPagerAdapter;
import com.jiaoshizige.teacherexam.base.GloableConstant;
import com.jiaoshizige.teacherexam.yy.activity.MyHomeWorkActivity;
import com.jiaoshizige.teacherexam.yy.activity.MyNoteActivity;
import com.jiaoshizige.teacherexam.yy.fragment.CourseraNoteFragment;
import com.jiaoshizige.teacherexam.yy.fragment.QuestionAndAnswerFragment;
import com.jiaoshizige.teacherexam.fragment.SanJIdeLieBiaoFragment;
import com.jiaoshizige.teacherexam.fragment.SignalCourseCatalogFragmnet;
import com.jiaoshizige.teacherexam.http.ApiUrl;
import com.jiaoshizige.teacherexam.http.MyCallBack;
import com.jiaoshizige.teacherexam.http.Xutil;
import com.jiaoshizige.teacherexam.model.ClassDetailResponse;
import com.jiaoshizige.teacherexam.model.CourseDetialResponse;
import com.jiaoshizige.teacherexam.model.NewCourseCatalogResponse;
import com.jiaoshizige.teacherexam.model.NoteaWorkChapterResponse;
import com.jiaoshizige.teacherexam.utils.SPKeyValuesUtils;
import com.jiaoshizige.teacherexam.utils.SPUtils;
import com.jiaoshizige.teacherexam.utils.ToastUtil;
import com.jiaoshizige.teacherexam.widgets.AllPopupwindow;
import com.jiaoshizige.teacherexam.yy.fragment.HomeworkFragment;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.zhouzhuo.zzhorizontalprogressbar.ZzHorizontalProgressBar;

/**
 * 我的班级课程已购买的
 */

public class NewMyClassDetialActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {
    @BindView(R.id.back_im)
    TextView mBack;
    @BindView(R.id.toolbar_title)
    TextView mToolbar_title;
    @BindView(R.id.toolbar_subtitle)
    TextView mToolbar_subtitle;
    @BindView(R.id.study_prodress)
    ZzHorizontalProgressBar mProgress_bar;
    @BindView(R.id.completion_degree)
    TextView mCompletionDegree;
    @BindView(R.id.headmaster_photo)
    ImageView mHeadMaster;
    @BindView(R.id.headmaster_nickname)
    TextView mHeadMasterNickName;
    @BindView(R.id.assistant_photo)
    RecyclerView mAssistant;
    @BindView(R.id.add_group)
    TextView mAddGroup;
    @BindView(R.id.tablayout)
    TabLayout mTabLayout;
    @BindView(R.id.viewpager)
    ViewPager mViewPager;
    @BindView(R.id.float_action_btn)
    FloatingActionButton mFloatActionBtn;
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
    @BindView(R.id.appbarlayout)
    AppBarLayout mAppbarLayout;
    private String user_id, qq;
    private String mProgress;
    private Context mContext;
    private String mClassId;//等于mCourseId
    private List<Fragment> mFragments;
    private List<String> tabTitles;
    private TabPagerAdapter pagerAdapter;
    GridLayoutManager gridLayoutManager;
    private AllPopupwindow mAllPopUpWindowHomeWork;
    private AllPopupwindow mAllPopUpWindowNote;
    private HomeworkFragment homeWorkFragment;
    private CourseraNoteFragment courseraNoteFragment;
    private String mCourseId;
    private String mCourseNameStr = "name";
    private String mGroupId;
    private String isBuy;
    private int mPosition;
    private String mHomeWorkChapterTitle = "";
    private String mNoteChapterTitle = "";
    private int hasFilter = 0;
    private String mType;
    private int hasdata = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_class_datail_layout);
        ButterKnife.bind(this);
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("我的班级课程已购买的");
        MobclickAgent.onResume(this);
        courseDetial();
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("我的班级课程已购买的");
        MobclickAgent.onPause(this);
    }

    private void initView() {
        mContext = this;
        user_id = (String) SPUtils.getSpValues(SPKeyValuesUtils.SP_USER_ID, SPUtils.TYPE_STRING);
        if (getIntent().getExtras().get("progress") != null) {
            mProgress = (String) getIntent().getExtras().get("progress");
        } else {
            mProgress = "0";
        }

        Log.e("*********mProgress", mProgress);
        mCompletionDegree.setText(mProgress);
        if (mProgress != null && !mProgress.equals("")) {
            mProgress_bar.setProgress(Integer.parseInt(mProgress));
        } else {
            mProgress_bar.setProgress(Integer.parseInt("0"));
            mCompletionDegree.setText("0");
        }
        if (getIntent().getStringExtra("id") != null) {
            mClassId = getIntent().getStringExtra("id");
        } else {
            mClassId = "";
        }
        if (getIntent().getStringExtra("is_buy") != null) {
            isBuy = getIntent().getStringExtra("is_buy");
        } else {
            isBuy = "";
        }
        Log.d("*****isBuy", isBuy + "**********");
        if (getIntent().getStringExtra("id") != null) {
            mCourseId = getIntent().getStringExtra("id");
        } else {
            mCourseId = "";
        }
        Log.d("*****mCourseId", mCourseId + "**********");
        if (getIntent().getExtras().get("name") != null) {
            mToolbar_title.setText((String) getIntent().getExtras().get("name"));
        } else {

            mToolbar_title.setText("");
        }
        if (getIntent().getExtras().get("type") != null) {
            mType = (String) getIntent().getExtras().get("type");
        } else {
            mType = "";
        }
        mToolbar_subtitle.setText("学习日历");
        mFragments = new ArrayList<>();
        tabTitles = new ArrayList<>();
        tabTitles.add("目录");
        tabTitles.add("作业");
        tabTitles.add("笔记");
        tabTitles.add("问答");
        homeWorkFragment = new HomeworkFragment(mCourseId);
        courseraNoteFragment = new CourseraNoteFragment(mCourseId);
//        mFragments.add(new CourseCatalogFragmnet(mGroupId,mCourseId,isBuy,mCourseNameStr));

        Log.e("******mType", mType + "///////");
        if (mType.equals("1")) {
//            mFragments.add(new NewCourseCatalogFragmnet(mClassId,mType));
            mFragments.add(new SanJIdeLieBiaoFragment(mClassId, mType, isBuy));
        } else {
            mFragments.add(new SignalCourseCatalogFragmnet(mCourseId, mType, isBuy,false));
        }

        mFragments.add(homeWorkFragment);
        mFragments.add(courseraNoteFragment);
        mFragments.add(new QuestionAndAnswerFragment(mCourseId));
        for (int i = 0; i < tabTitles.size(); i++) {
            mTabLayout.addTab(mTabLayout.newTab().setText(tabTitles.get(i)));
        }
        pagerAdapter = new TabPagerAdapter(getSupportFragmentManager(), mFragments, tabTitles);
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.setAdapter(pagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
        mViewPager.addOnPageChangeListener(this);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        gridLayoutManager = new GridLayoutManager(this, 4);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return 1;
            }
        });
//        classDatail();
        courseDetial();

    }

    @OnClick({R.id.classify_switch, R.id.float_action_btn, R.id.back_im, R.id.toolbar_subtitle, R.id.add_group})
    public void ClassifyToggleSwitch(View view) {
        switch (view.getId()) {
            case R.id.classify_switch:
                if (hasFilter != 1) {
                    if (mClassifySwitch.isChecked()) {
                        if (mPosition == 1) {
                            if (hasdata == 1) {
                                mAllPopUpWindowHomeWork.setOnDismissListener(new NewMyClassDetialActivity.poponDismissListenermDepartment());
                                mAllPopUpWindowHomeWork.showFilterPopup(mNoteClassify);
                            }
                        } else {
                            if (hasdata == 1) {
                                mAllPopUpWindowNote.setOnDismissListener(new NewMyClassDetialActivity.poponDismissListenermDepartment());
                                mAllPopUpWindowNote.showFilterPopup(mNoteClassify);
                            }

                        }
                    } else {
                        mClassifySwitch.setChecked(false);
                        if (mAllPopUpWindowHomeWork == null) {
                            if (mPosition == 1) {
                                mAllPopUpWindowHomeWork.dismiss();
                            } else {
                                mAllPopUpWindowNote.dismiss();
                            }
                        }
                    }
                } else {
                    ToastUtil.showShortToast("暂无分类");
                }
                break;
            case R.id.float_action_btn:
                if (mPosition == 3) {
                    Intent intent = new Intent();
                    intent.putExtra("course_id", mCourseId);
                    intent.setClass(this, PutQuestionsActivity.class);
                    startActivity(intent);
                } else {
                    homeWorkFragment.backToTop();
                }
                break;
            case R.id.back_im:
                finish();
                break;
            case R.id.toolbar_subtitle:
                Intent intent = new Intent();
                intent.putExtra("class_id", mClassId);
                intent.putExtra("type", mType);
                intent.setClass(NewMyClassDetialActivity.this, StudyCalendarActivity.class);
                startActivity(intent);
                break;
            case R.id.add_group:
                joinQQGroup(qq);
                break;
        }


    }

    class poponDismissListenermDepartment implements PopupWindow.OnDismissListener {
        @Override
        public void onDismiss() {
            setBackgroundAlpha(1f);
            mClassifySwitch.setChecked(false);
            if (mPosition == 1) {
                if (GloableConstant.getInstance().getmHomeWorkChapterId() != null) {
                    homeWorkFragment.changeChapter(GloableConstant.getInstance().getmHomeWorkChapterId());
                    mHomeWorkChapterTitle = GloableConstant.getInstance().getmHomeWorkChapterTitle();
                    mClassifyTypeName.setText(mHomeWorkChapterTitle);
                }
            } else {
                if (GloableConstant.getInstance().getmListNoteChapterId() != null) {
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

    public void courseDetial() {
        Map<String, String> map = new HashMap<>();
        map.put("course_id", mClassId);
        map.put("user_id", (String) SPUtils.getSpValues(SPKeyValuesUtils.SP_USER_ID, SPUtils.TYPE_STRING));
        map.put("type_id", mType);
        Log.e("*********", map.toString());
//        GloableConstant.getInstance().startProgressDialog(this);
        Xutil.GET(ApiUrl.COURSERDETAIL, map, new MyCallBack<CourseDetialResponse>() {
            @Override
            public void onSuccess(CourseDetialResponse result) {
                super.onSuccess(result);
//                GloableConstant.getInstance().stopProgressDialog();
                if (result.getStatus_code().equals("200")) {
                    if (result.getData().getQq() != "") {
                        qq = result.getData().getQq();
                    } else {
                        ToastUtil.showShortToast("该班级尚未建群");
                    }
                    mToolbar_title.setText(result.getData().getName());
//                    mCompletionDegree.setText(result.getData().getLearn_percent());
//                    mProgress_bar.setProgress(Integer.valueOf(result.getData().getLearn_percent()));
                    Log.e("***********percent", result.getData().getLearn_percent() + "//////");
                    Glide.with(mContext).load(ApiUrl.BASEIMAGE + result.getData().getTeacher().getHeadImg())
                            .apply(GloableConstant.getInstance().getOptions()).into(mHeadMaster);
                    mHeadMasterNickName.setText(result.getData().getTeacher().getName());
                    LinearLayoutManager manager = new LinearLayoutManager(mContext);
                    manager.setOrientation(LinearLayoutManager.HORIZONTAL);
                    mAssistant.setLayoutManager(manager);
                    Log.e("*********", "eeeeeeeeee");
                    mAssistant.setAdapter(new CommonAdapter<ClassDetailResponse.mAssistant>(mContext, R.layout.item_small_phpto, result.getData().getAssistant()) {
                        @Override
                        protected void convert(ViewHolder holder, ClassDetailResponse.mAssistant mAssistant, int position) {
                            ImageView imageView = (ImageView) holder.getConvertView().findViewById(R.id.small_phpto);
                            Glide.with(mContext).load(ApiUrl.BASEIMAGE + mAssistant.getHeadImg()).apply(GloableConstant.getInstance().getOptions()).into(imageView);
                        }
                    });
                } else if (result.getStatus_code().equals("401")) {
                    GloableConstant.getInstance().clearAll();
                    ToastUtil.showShortToast("请重新登录");
                    startActivity(new Intent(NewMyClassDetialActivity.this, LoginActivity.class));
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
                Log.e("TAGs", "eee" + ex.getMessage());
//                GloableConstant.getInstance().stopProgressDialog();
            }
        });

    }

    /**
     * feiqi
     */
    private void classDatail() {
        Map<String, Object> map = new HashMap<>();
        map.put("group_id", mClassId);
//        map.put("group_id", "16");
        map.put("user_id", SPUtils.getSpValues(SPKeyValuesUtils.SP_USER_ID, SPUtils.TYPE_STRING));
        Log.e("*********", map.toString());
//        GloableConstant.getInstance().startProgressDialog(this);
        Xutil.Post(ApiUrl.CLASSSDETAIL, map, new MyCallBack<ClassDetailResponse>() {
            @Override
            public void onSuccess(ClassDetailResponse result) {
                super.onSuccess(result);
//                GloableConstant.getInstance().stopProgressDialog1();
                if (result.getStatus_code().equals("200")) {
                    if (result.getData().getQq() != "") {
                        qq = result.getData().getQq();
                    } else {
                        ToastUtil.showShortToast("该班级尚未建群");
                    }
                    mToolbar_title.setText(result.getData().getGroup_name());
                    Glide.with(mContext).load(result.getData().getTeacher().getHeadImg())
                            .apply(GloableConstant.getInstance().getOptions()).into(mHeadMaster);
                    LinearLayoutManager manager = new LinearLayoutManager(mContext);
                    manager.setOrientation(LinearLayoutManager.HORIZONTAL);
                    mAssistant.setLayoutManager(manager);

                    mAssistant.setAdapter(new CommonAdapter<ClassDetailResponse.mAssistant>(mContext, R.layout.item_small_phpto, result.getData().getAssistant()) {
                        @Override
                        protected void convert(ViewHolder holder, ClassDetailResponse.mAssistant mAssistant, int position) {
                            ImageView imageView = (ImageView) holder.getConvertView().findViewById(R.id.small_phpto);
                            Glide.with(mContext).load(mAssistant.getHeadImg()).apply(GloableConstant.getInstance().getOptions()).into(imageView);
                        }
                    });
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
//                GloableConstant.getInstance().stopProgressDialog1();

            }
        });

    }

    /****************
     * 发起添加群流程。群号：(121683601) 的 key 为： 5GuJf0LcgV33u88ZyEh_GnyCyAYKFSJD
     * 调用 joinQQGroup(5GuJf0LcgV33u88ZyEh_GnyCyAYKFSJD) 即可发起手Q客户端申请加群 2013级梦翔会员交流群(121683601)
     *
     * @param key 由官网生成的key
     * @return 返回true表示呼起手Q成功，返回fals表示呼起失败
     ******************/
    public boolean joinQQGroup(String key) {
        Intent intent = new Intent();
        intent.setData(Uri.parse("mqqopensdkapi://bizAgent/qm/qr?url=http%3A%2F%2Fqm.qq.com%2Fcgi-bin%2Fqm%2Fqr%3Ffrom%3Dapp%26p%3Dandroid%26k%3D" + key));
        // 此Flag可根据具体产品需要自定义，如设置，则在加群界面按返回，返回手Q主界面，不设置，按返回会返回到呼起产品界面    //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        try {
            startActivity(intent);
            return true;
        } catch (Exception e) {
            // 未安装手Q或安装的版本不支持
            ToastUtil.showShortToast("未安装手Q或安装的版本不支持");
            return false;
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        mPosition = position;
        if (position == 3) {
            mFloatActionBtn.setVisibility(View.VISIBLE);
            mFloatActionBtn.setImageResource(R.mipmap.home_png_askquestions_default);
        } else {
            mFloatActionBtn.setVisibility(View.GONE);
        }
        if (position == 2) {
            mNoteClassify.setVisibility(View.VISIBLE);
            mNoteIcon.setVisibility(View.VISIBLE);
            mClassifyType.setText("我的笔记");
            if (!mNoteChapterTitle.equals("")) {
                mClassifyTypeName.setText(mNoteChapterTitle);
            } else {
                mClassifyTypeName.setText("全部课时");
            }

            mNoteIcon.setBackground(ContextCompat.getDrawable(this, R.mipmap.note_icon));
            mClassifyType.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.putExtra("course_id", mCourseId);
                    intent.putExtra("type", mType);
                    intent.setClass(NewMyClassDetialActivity.this, MyNoteActivity.class);
                    startActivity(intent);
                }
            });
//            requestNoteShapter("2");
            requestShapter();
        } else if (position == 1) {
            mClassifyType.setText("我的作业");
            if (!mHomeWorkChapterTitle.equals("")) {
                mClassifyTypeName.setText(mHomeWorkChapterTitle);
            } else {
                mClassifyTypeName.setText("全部课时");
            }
            mNoteIcon.setVisibility(View.VISIBLE);
            mNoteIcon.setBackground(ContextCompat.getDrawable(this, R.mipmap.home_icon_operation_default));
            mNoteClassify.setVisibility(View.VISIBLE);
            mFloatActionBtn.setVisibility(View.VISIBLE);
            mFloatActionBtn.setImageResource(R.mipmap.home_icon_return_default);
            mClassifyType.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.putExtra("course_id", mCourseId);
                    intent.setClass(NewMyClassDetialActivity.this, MyHomeWorkActivity.class);
                    startActivity(intent);
                }
            });
//            requestNoteShapter("2");
            requestShapter();
        } else {
            mNoteClassify.setVisibility(View.GONE);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    /**
     * 课时列表 作业 笔记
     *
     * @param type 废弃
     */
    private void requestNoteShapter(final String type) {
        Map<String, String> map = new HashMap<>();
        map.put("course_id", mGroupId);
//        map.put("course_id","8");
        map.put("type", type);//1考试，2笔记,作业
        Log.e("TAGmap", map.toString());
//        GloableConstant.getInstance().showmeidialog(this);
        Xutil.GET(ApiUrl.CHAPTER, map, new MyCallBack<NoteaWorkChapterResponse>() {
            @Override
            public void onSuccess(NoteaWorkChapterResponse result) {
                super.onSuccess(result);
//                GloableConstant.getInstance().stopProgressDialog();
                if (result.getStatus_code().equals("200")) {
                    if (result.getData() != null) {
                       /* if(type.equals("1")){
                            mAllPopUpWindowHomeWork = new AllPopupwindow(ClassCoursesDetialActivity.this,result.getData(),"homework");
                        }else{
                            mAllPopUpWindowNote = new AllPopupwindow(ClassCoursesDetialActivity.this,result.getData(),"note");
                        }*/
                        mAllPopUpWindowHomeWork = new AllPopupwindow(NewMyClassDetialActivity.this, result.getData(), "homework");
                        mAllPopUpWindowNote = new AllPopupwindow(NewMyClassDetialActivity.this, result.getData(), "note");
                    } else {
                        hasFilter = 1;
                    }
                } else if (result.getStatus_code().equals("203")) {
                    hasFilter = 1;
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
        map.put("course_id", mClassId);
        map.put("user_id", (String) SPUtils.getSpValues(SPKeyValuesUtils.SP_USER_ID, SPUtils.TYPE_STRING));
        map.put("type_id", mType);
        Log.e("IOIO", map.toString());
        Xutil.GET(ApiUrl.COURSEDETAILCATALOGS, map, new MyCallBack<NewCourseCatalogResponse>() {
            @Override
            public void onSuccess(NewCourseCatalogResponse result) {
                super.onSuccess(result);
                if (result.getData().getCourse_list() != null && result.getData().getCourse_list().size() > 0) {
                    List<NoteaWorkChapterResponse.mData> mChapterList = new ArrayList<>();
                 /*   for(int i=0;i< result.getData().getCourse_list().size();i++){
                      NoteaWorkChapterResponse.mData response = new NoteaWorkChapterResponse.mData();
                      response.setId(result.getData().getCourse_list().get(i).getId());
                      response.setTitle(result.getData().getCourse_list().get(i).getName());
//                      response.setPractice(result.getData().getCourse_list().get(i).getId());
                        mChapterList.add(response);
                    }*/
                    NoteaWorkChapterResponse.mData responsenull = new NoteaWorkChapterResponse.mData();
                    responsenull.setId("");
                    responsenull.setTitle("全部课时");
                    mChapterList.add(responsenull);
                    for (int i = 0; i < result.getData().getCourse_list().size(); i++) {
                        for (int j = 0; j < result.getData().getCourse_list().get(i).getCatalog().size(); j++) {
                            NoteaWorkChapterResponse.mData response = new NoteaWorkChapterResponse.mData();
                            response.setId(result.getData().getCourse_list().get(i).getCatalog().get(j).getId());
                            response.setTitle(result.getData().getCourse_list().get(i).getCatalog().get(j).getTitle());
                            mChapterList.add(response);
                            Log.e("YUUU", mChapterList.size() + "sss22");
                        }
                    }
                    if (mChapterList.size() > 0) {
                        hasdata = 1;
                    }
                    mAllPopUpWindowHomeWork = new AllPopupwindow(NewMyClassDetialActivity.this, mChapterList, "homework");
                    mAllPopUpWindowNote = new AllPopupwindow(NewMyClassDetialActivity.this, mChapterList, "note");
                } else {
                    hasFilter = 1;
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
            }
        });
    }
}
