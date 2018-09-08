package com.jiaoshizige.teacherexam.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.guideview.GuideView;
import com.guideview.GuideViewHelper;
import com.guideview.LightType;
import com.jiaoshizige.teacherexam.MainActivity;
import com.jiaoshizige.teacherexam.R;
import com.jiaoshizige.teacherexam.activity.BookDetailActivity;
import com.jiaoshizige.teacherexam.activity.CoursesDetialActivity;
import com.jiaoshizige.teacherexam.activity.InformationDetialActivity;
import com.jiaoshizige.teacherexam.activity.LinkWebActivity;
import com.jiaoshizige.teacherexam.activity.LiveActivity;
import com.jiaoshizige.teacherexam.activity.LoginActivity;
import com.jiaoshizige.teacherexam.activity.MineCourseActivityOne;
import com.jiaoshizige.teacherexam.activity.MyQuestionBankActivity;
import com.jiaoshizige.teacherexam.activity.OpenClassDetailActivity;
import com.jiaoshizige.teacherexam.activity.QuestionBankDetailActivity;
import com.jiaoshizige.teacherexam.adapter.ExamTypeSelectAdapter;
import com.jiaoshizige.teacherexam.base.GloableConstant;
import com.jiaoshizige.teacherexam.base.MBaseFragment;
import com.jiaoshizige.teacherexam.http.ApiUrl;
import com.jiaoshizige.teacherexam.http.MyCallBack;
import com.jiaoshizige.teacherexam.http.SupportResponse;
import com.jiaoshizige.teacherexam.http.Xutil;
import com.jiaoshizige.teacherexam.model.AdvertSementResponse;
import com.jiaoshizige.teacherexam.model.ExamTypeResponse;
import com.jiaoshizige.teacherexam.model.InformationResponse;
import com.jiaoshizige.teacherexam.model.MessageEvent;
import com.jiaoshizige.teacherexam.model.MineStudyResponse;
import com.jiaoshizige.teacherexam.model.OnLineResponse;
import com.jiaoshizige.teacherexam.model.PersonMessageResponse;
import com.jiaoshizige.teacherexam.model.QusstionBankResponse;
import com.jiaoshizige.teacherexam.model.SignDayResponse;
import com.jiaoshizige.teacherexam.mycourse.activity.MyNewCoursesActivity;
import com.jiaoshizige.teacherexam.utils.ADTextView;
import com.jiaoshizige.teacherexam.utils.GifView;
import com.jiaoshizige.teacherexam.utils.SDOtherUtil;
import com.jiaoshizige.teacherexam.utils.SPKeyValuesUtils;
import com.jiaoshizige.teacherexam.utils.SPUtils;
import com.jiaoshizige.teacherexam.utils.Sphelper;
import com.jiaoshizige.teacherexam.utils.ToastUtil;
import com.jiaoshizige.teacherexam.utils.XCRoundImageView;
import com.jiaoshizige.teacherexam.widgets.AdEntity;
import com.jiaoshizige.teacherexam.widgets.CenterCenterStyle;
import com.jiaoshizige.teacherexam.widgets.HorizontalListView;
import com.jiaoshizige.teacherexam.widgets.ProgressBarView;
import com.jiaoshizige.teacherexam.yy.activity.InformationListActivity;
import com.jiaoshizige.teacherexam.yy.activity.MessageCenterNewActivity;
import com.jiaoshizige.teacherexam.zz.bean.HomeAdsBean;
import com.jiaoshizige.teacherexam.zz.bean.ZhiBoBean;
import com.jiaoshizige.teacherexam.zz.database.QuestionBankList;
import com.jiaoshizige.teacherexam.zz.database.QuestionBankListDao;
import com.jiaoshizige.teacherexam.zz.utils.NetworkUtils;
import com.jiaoshizige.teacherexam.zz.widget.CornerTransform;
import com.jiaoshizige.teacherexam.zz.widget.OpenCustomDigitalClock;
import com.umeng.analytics.MobclickAgent;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;
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
import butterknife.Unbinder;
import q.rorbin.badgeview.QBadgeView;

/**
 * Created by Administrator on 2017/8/2 0002.
 * 学习
 */
public class StudyFragment extends MBaseFragment implements View.OnClickListener {

    @BindView(R.id.message1)
    ImageView mMessage1;
    @BindView(R.id.photo1)
    XCRoundImageView mPhoto1;
    @BindView(R.id.main_study1)
    RelativeLayout mMainStudy1;
    @BindView(R.id.nick_name1)
    TextView mNickName1;
    @BindView(R.id.sign1)
    Button mSign1;
    @BindView(R.id.learn_days1)
    TextView mLearnDays1;
    @BindView(R.id.textView1)
    TextView mTextView1;
    @BindView(R.id.task_days1)
    TextView mTaskDays1;
    @BindView(R.id.main_text_score1)
    TextView mMainTextScore1;
    @BindView(R.id.change)
    TextView mChange;
    @BindView(R.id.message)
    ImageView mMessage;
    @BindView(R.id.dot)
    TextView mDot;
    @BindView(R.id.photo)
    XCRoundImageView mPhoto;
    @BindView(R.id.main_study)
    RelativeLayout mMainStudy;
    @BindView(R.id.nick_name)
    TextView mNickName;
    @BindView(R.id.sign)
    Button mSign;
    @BindView(R.id.sign_rt)
    RelativeLayout mSignRt;
    @BindView(R.id.learn_days)
    TextView mLearnDays;
    @BindView(R.id.textView)
    TextView mTextView;
    @BindView(R.id.task_days)
    TextView mTaskDays;
    @BindView(R.id.main_text_score)
    TextView mMainTextScore;
    @BindView(R.id.notice)
    ADTextView mNotice;
    @BindView(R.id.to_info_list)
    TextView mToInfoList;
    @BindView(R.id.more_infomation)
    LinearLayout mMoreInfomation;
    @BindView(R.id.info_list_ly)
    LinearLayout mInfoListLy;
    @BindView(R.id.qusetion_look)
    TextView mQusetionLook;
    @BindView(R.id.tiku)
    RelativeLayout mTiku;
    @BindView(R.id.listView)
    HorizontalListView mListView;
    @BindView(R.id.main_view)
    View mMainView;
    @BindView(R.id.look_class)
    TextView mLookClass;
    @BindView(R.id.tv_class_note)
    TextView mTvClassNote;
    @BindView(R.id.add_class)
    TextView mAddClass;
    @BindView(R.id.class_no_data)
    LinearLayout mClassNoData;
    @BindView(R.id.class_list)
    RecyclerView mClassList;
    @BindView(R.id.live_type)
    TextView mLiveType;
    @BindView(R.id.live_more)
    TextView mLiveMore;
    @BindView(R.id.zhibo)
    LinearLayout mZhibo;
    @BindView(R.id.zhibolistview)
    HorizontalListView mZhibolistview;
    @BindView(R.id.main_now_live)
    LinearLayout mMainNowLive;
    @BindView(R.id.bannner)
    ImageView mBannner;
    @BindView(R.id.close_banner)
    TextView mCloseBanner;
    @BindView(R.id.banner_rl)
    RelativeLayout mBannerRl;
    Unbinder unbinder;

    private TextView mType, mmChange, unChange;
    private ListView mRecyclerView;
    private LinearLayout mButton, mName;
    private RelativeLayout mCancel;
    private TextView mTitle, mDescribe;
    private List<ExamTypeResponse.mData> mDatas;
    private int mIsSign = 0;
    public static final int REUEST_CODDE = 1;
    private List<AdEntity> mList = new ArrayList<AdEntity>();
    private PopupWindow mPopupWindow;
    private View mContentView;
    private String exam_type;
    private GuideViewHelper helper;
    private String message;
    SetExamTypeDialogFragment dialog = new SetExamTypeDialogFragment(getActivity());
    private String user_id;
    private List<ExamTypeResponse.mData> mExamData = new ArrayList<>();

    private int MESSAGERESULT = 11;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_study_activity1, container, false);
        return view;
    }

    public void dialogSign() {
        SignDialogFragmnet dialog = new SignDialogFragmnet(getActivity());
        dialog.setTargetFragment(StudyFragment.this, REUEST_CODDE);
        dialog.setCancelable(false);
        dialog.show(getFragmentManager(), "dialogsign");
    }

    public void dialogToSet() {
        dialog.setTargetFragment(StudyFragment.this, REUEST_CODDE);
        dialog.setCancelable(false);
        dialog.show(getFragmentManager(), "dialogtoset");
    }

    public void dialogToSetDismiss() {
        dialog.dialogDismiss();
    }

    @Override
    public void onResume() {
        super.onResume();

//        requestInfo();
//        requestMineClass();
//        requestAdvert();
//        requestUserInfo();
//        showOnCreate();
        //新手指引
        if (!Sphelper.getString(getActivity(), "ISFIRST", "isfirst").equals("1")) {
            stop();
        } else {
            if (popupWindow == null) {
                //广告弹窗
                if (TextUtils.isEmpty(String.valueOf(SPUtils.getSpValues("HomeAdsIsClose", 1)))) {
                    requestAdvert2();
                }
            }

        }


//        stop();
        // MobclickAgent.onPageStart("学习");
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
//        if (!hidden) {
//            mNowLive();
//            requestInfo();
//            requestUserInfo();
//            requestMineClass();
//        }
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("学习");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe()
    public void onMessageEvent(MessageEvent event) {
        if (event.getMessage().equals("消息中心")) {
            Log.e("TAG", "wwwwwjieshpu1111" + event.getMessage());
            // requestUserInfo();
        }
    }

    @Override
    protected void onInitViews(Bundle savedInstanceState) {

        user_id = (String) SPUtils.getSpValues(SPKeyValuesUtils.SP_USER_ID, SPUtils.TYPE_STRING);

        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }

        exam_type = String.valueOf(SPUtils.getSpValues("examType", SPUtils.TYPE_STRING));
        if (exam_type.equals("1")) {
            mChange.setText("幼儿");
        } else if (exam_type.equals("2")) {
            mChange.setText("中学");
        } else if (exam_type.equals("3")) {
            mChange.setText("小学");
        }

        if (NetworkUtils.isNetworkConnected(getActivity())) {
            requestUserInfo();
            requestInfo();
            requestMineClass();
            requestAdvert();
            mNowLive();
            getQusetionBank();
        } else {
            mNickName.setText(String.valueOf(SPUtils.getSpValues("userName", 1)));
            getLocalData();
        }

        mNotice.setBackColor(ContextCompat.getColor(getActivity(), R.color.text_color3));
        mNotice.setFrontColor(ContextCompat.getColor(getActivity(), R.color.red));
        mNotice.setmDuration(1000);
        mNotice.setInterval(1000);
        mNotice.setOnItemClickListener(new ADTextView.OnItemClickListener() {
            @Override
            public void onClick(InformationResponse.mData mUrl) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), InformationDetialActivity.class);
                intent.putExtra("info_id", mUrl.getId());
                startActivity(intent);
            }
        });


    }

    @OnClick({R.id.add_class, R.id.message, R.id.change, R.id.close_banner, R.id.live_more, R.id.look_class, R.id.to_info_list, R.id.qusetion_look, R.id.sign})
    public void Onclick(View view) {
        switch (view.getId()) {
            case R.id.to_info_list:
                startActivity(new Intent(getActivity(), InformationListActivity.class));
                break;
            case R.id.add_class:
                if (mAddClass.getText().toString().equals("立即学习")) {
                    Intent intent = new Intent(getActivity(), MineCourseActivityOne.class);
                    startActivity(intent);
                } else if (mAddClass.getText().toString().equals("+添加课程")) {
                    MainActivity activity = (MainActivity) getActivity();
                    activity.changeSchoolTab("0");
                    //sendBroadcast("classSchool");
                }
                break;

            case R.id.message:
                startActivityForResult(new Intent(getActivity(), MessageCenterNewActivity.class), MESSAGERESULT);
                break;
            case R.id.change:
                Drawable drawable = ContextCompat.getDrawable(getActivity(), R.mipmap.switch_type);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                mChange.setCompoundDrawables(null, null, drawable, null);
                showPopWindow();
                break;
            case R.id.close_banner:
                mBannerRl.setVisibility(View.GONE);
                break;
            case R.id.live_more:
                MainActivity activityLive = (MainActivity) getActivity();
                activityLive.changeLiveTab();
                break;
            case R.id.today_live:
//                ToastUtil.showShortToast("今日直播");
                break;
            case R.id.look_class:
//                startActivity(new Intent(getActivity(), MineCourseActivity.class));
                startActivity(new Intent(getActivity(), MineCourseActivityOne.class));
                break;
            case R.id.qusetion_look:
                startActivity(new Intent(getActivity(), MyQuestionBankActivity.class));
                break;

            case R.id.sign:
                if (mIsSign != 1) {//签到
                    requestSign();
                } else {
                    dialogSign();
                }
            default:
                break;
        }

    }

    private void sendBroadcast(String classSchool) {
        Intent intent = new Intent();
        intent.setAction(classSchool);
        getActivity().sendBroadcast(intent);
    }

    /**
     * 我的题库列表
     */

    private String mPageNum = "1", mPageSize = "10";
    private int mPage = 1;
    private List<QusstionBankResponse.mData> mListQuestion = new ArrayList<>();

    private void getQusetionBank() {
        Map<String, String> map = new HashMap<>();
        map.put("user_id", (String) SPUtils.getSpValues(SPKeyValuesUtils.SP_USER_ID, SPUtils.TYPE_STRING));
        map.put("page", mPageNum);
        map.put("page_size", mPageSize);
        Log.d("**************map", map.toString());
        GloableConstant.getInstance().startProgressDialog(getActivity());
        Xutil.GET(ApiUrl.QUESTIONBANK, map, new MyCallBack<QusstionBankResponse>() {
            @Override
            public void onSuccess(QusstionBankResponse result) {
                super.onSuccess(result);
                GloableConstant.getInstance().stopProgressDialog();
                if (result.getStatus_code().equals("200")) {

                    if (result.getData() != null && result.getData().size() > 0) {
                        //把题库数据保存到本地数据库中
                        List<QuestionBankList> tmplist = new QuestionBankListDao(getActivity()).queryByColumnNameAndColumnName("user_id", user_id, "examtype_id", exam_type);
                        if (tmplist != null && tmplist.size() > 0) {
                            //表中数据先判断有没有，若存在就更新，不存在就插入
                            for (int i = 0; i < result.getData().size(); i++) {
                                List<QuestionBankList> queryList = new QuestionBankListDao(getActivity()).queryByColumnNameas("user_id", user_id, "examtype_id", exam_type, "qbid", result.getData().get(i).getId());
                                if (queryList.size() > 0) {
                                    QusstionBankResponse.mData qusstionBankResponse = result.getData().get(i);
                                    QuestionBankList questionBankListItem = queryList.get(0);

                                    questionBankListItem.setName(qusstionBankResponse.getName());
                                    questionBankListItem.setExam_name(qusstionBankResponse.getExam_name());
                                    questionBankListItem.setHuangguan_totle_num(qusstionBankResponse.getHuangguan_totle_num());
                                    questionBankListItem.setHuangguan_have_totle_num(qusstionBankResponse.getHuangguan_have_totle_num());
                                    questionBankListItem.setGuanqia_totle_pass_num(qusstionBankResponse.getGuanqia_totle_pass_num());
                                    questionBankListItem.setGuanqia_totle_num(qusstionBankResponse.getGuanqia_totle_num());

                                    new QuestionBankListDao(getActivity()).update(questionBankListItem);
                                } else {
                                    QusstionBankResponse.mData qusstionBankResponse = result.getData().get(i);

                                    QuestionBankList questionBankList = new QuestionBankList(qusstionBankResponse.getId(), qusstionBankResponse.getName(), qusstionBankResponse.getExam_name(), qusstionBankResponse.getGuanqia_totle_num()
                                            , qusstionBankResponse.getGuanqia_totle_pass_num(), qusstionBankResponse.getHuangguan_totle_num(), qusstionBankResponse.getHuangguan_have_totle_num(), exam_type, user_id);
                                    new QuestionBankListDao(getActivity()).insert(questionBankList);
                                }

                            }

                        } else {
                            //表中没有数据，直接insert
                            for (int i = 0; i < result.getData().size(); i++) {
                                QusstionBankResponse.mData qusstionBankResponse = result.getData().get(i);
                                QuestionBankList questionBankList = new QuestionBankList(qusstionBankResponse.getId(), qusstionBankResponse.getName(), qusstionBankResponse.getExam_name(), qusstionBankResponse.getGuanqia_totle_num()
                                        , qusstionBankResponse.getGuanqia_totle_pass_num(), qusstionBankResponse.getHuangguan_totle_num(), qusstionBankResponse.getHuangguan_have_totle_num(), exam_type, user_id);
                                new QuestionBankListDao(getActivity()).insert(questionBankList);
                            }

                        }


                        mTiku.setVisibility(View.VISIBLE);
                        mListView.setVisibility(View.VISIBLE);
                        if (mPageNum.equals("1")) {
                            mListQuestion.clear();
                            mListQuestion = result.getData();
                        } else {
                            if (null == mListQuestion) {
                                mListQuestion.clear();
                                mListQuestion = result.getData();
                            } else {
                                mListQuestion.clear();
                                mListQuestion.addAll(result.getData());
                            }
                        }
                        GloableConstant.getInstance().setTiku_id(null);
                        mListView.setAdapter(new com.zhy.adapter.abslistview.CommonAdapter<QusstionBankResponse.mData>(getActivity(), R.layout.item_quetion_bank1, mListQuestion) {


                            @Override
                            protected void convert(com.zhy.adapter.abslistview.ViewHolder viewHolder, final QusstionBankResponse.mData item, int position) {
                                viewHolder.setText(R.id.title, item.getName());
                                Log.d("***********tatus_code", item.getExam_name());
                                viewHolder.setText(R.id.type, item.getExam_name());
                                viewHolder.setText(R.id.to_confirm_progress, item.getGuanqia_totle_pass_num());
                                viewHolder.setText(R.id.all_to_confirm, "/" + item.getGuanqia_totle_num());
                                viewHolder.setText(R.id.crown_progress, item.getHuangguan_have_totle_num());
                                viewHolder.setText(R.id.all_crown, "/" + item.getHuangguan_totle_num());
                                viewHolder.setOnClickListener(R.id.item, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        GloableConstant.getInstance().setTiku_id(item.getId());
                                        Intent intent = new Intent();
                                        intent.setClass(mContext, QuestionBankDetailActivity.class);
                                        intent.putExtra("name", item.getName());
                                        intent.putExtra("id", item.getId());
                                        startActivity(intent);

                                    }
                                });

                            }

                        });
                    } else {
                        mTiku.setVisibility(View.GONE);
                        mListView.setVisibility(View.GONE);

                    }
                }
            }

            @Override
            public void onFinished() {
                super.onFinished();
                GloableConstant.getInstance().stopProgressDialog();

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
                Log.e("/////////ex", ex.getMessage() + "///////");
                GloableConstant.getInstance().stopProgressDialog();

            }
        });
    }


    /**
     * 我的课程
     */
    private void requestMineClass() {
        Map<String, Object> map = new HashMap<>();
        map.put("user_id", SPUtils.getSpValues(SPKeyValuesUtils.SP_USER_ID, SPUtils.TYPE_STRING));
        GloableConstant.getInstance().startProgressDialog(getActivity());
        Xutil.Post(ApiUrl.MYGROUP, map, new MyCallBack<MineStudyResponse>() {
            @Override
            public void onSuccess(MineStudyResponse result) {
                super.onSuccess(result);
                if (result.getStatus_code().equals("200")) {
                    if (result.getData() != null && result.getData().size() > 0) {
                        mClassNoData.setVisibility(View.GONE);
                        mClassList.setVisibility(View.VISIBLE);
                        mLookClass.setVisibility(View.VISIBLE);

                        mClassList.setLayoutManager(new LinearLayoutManager(getActivity()));
                        List<MineStudyResponse.mData> mDatas = new ArrayList<>();
                        mDatas.add(result.getData().get(0));
                        mClassList.setAdapter(new CommonAdapter<MineStudyResponse.mData>(getActivity(), R.layout.item_class_study, mDatas) {
                            @Override
                            protected void convert(ViewHolder holder, final MineStudyResponse.mData mData, int position) {
                                String section_name = mData.getSection_name();
                                final int progress = SDOtherUtil.strToInt(mData.getLearn_percent());
                                ProgressBarView mViewBar = holder.getView(R.id.view_bar);
                                mViewBar.setProgress((int) (progress * 3.6));
                                mViewBar.setReachedBarColor(ContextCompat.getColor(getActivity(), R.color.buy_color));
                                mViewBar.SetTextColor(ContextCompat.getColor(getActivity(), R.color.buy_color));
                                mViewBar.setText(progress + "");
                                Log.e("*********getGroup_name", mData.getGroup_name() + "////////" +
                                        mData.getSection_name());
                                holder
                                        .setText(R.id.class_name, mData.getGroup_name())
                                        .setTextColor(R.id.section_name, TextUtils.isEmpty(section_name) ? getResources().getColor(R.color.text_color9) : getResources().getColor(R.color.purple4))
                                        .setText(R.id.section_name, TextUtils.isEmpty(section_name) ? "未开始学习" : section_name)
                                        .setBackgroundRes(R.id.class_type, TextUtils.isEmpty(section_name) ? R.mipmap.picture :
                                                "1".equals(mData.getType()) ? R.mipmap.live_pre :
                                                        "2".equals(mData.getType()) ? R.mipmap.exercise_pre :
                                                                "3".equals(mData.getType()) ? R.mipmap.image_text_pre :
                                                                        R.mipmap.picture)
                                        .setVisible(R.id.day1, "1".equals(mData.getIs_buy()))
                                        .setText(R.id.section_days, "1".equals(mData.getIs_buy()) ? mData.getEnd_time() : "课程已到期，请前往续费")
                                        .setVisible(R.id.day2, "1".equals(mData.getIs_buy()))
                                        .setOnClickListener(R.id.item_mine_class, new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                if ("1".equals(mData.getIs_buy())) {
                                                    Intent intent = new Intent();
                                                    intent.putExtra("id", mData.getId());
                                                    intent.putExtra("progress", progress + "");
                                                    intent.putExtra("name", mData.getGroup_name());
                                                    intent.putExtra("type", mData.getCourse_type());
//                                                    intent.setClass(getActivity(), NewMyClassDetialActivity.class);
                                                    intent.setClass(getActivity(), MyNewCoursesActivity.class);
                                                    startActivity(intent);
                                                } else {
                                                    Intent intent = new Intent();
                                                    intent.putExtra("course_id", mData.getId());
                                                    intent.putExtra("type", mData.getCourse_type());//以前传的是1 2 3 4 视频 图文等 现在type变成1班级2课堂
                                                    intent.setClass(getActivity(), CoursesDetialActivity.class);
                                                    startActivity(intent);
                                                    Log.e("******id", mData.getCourse_id());
                                                }
                                            }
                                        }).setOnClickListener(R.id.look_class, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        ToastUtil.showShortToast("查看全部课程");
                                        startActivity(new Intent(getActivity(), MineCourseActivityOne.class));
                                    }
                                });

                            }
                        });
                    } else {
                        mClassNoData.setVisibility(View.VISIBLE);
                        mClassList.setVisibility(View.GONE);
                        mLookClass.setVisibility(View.GONE);
                        if (Integer.parseInt(result.getIs_course()) > 0) {
                            mTvClassNote.setText("当前暂无学习记录，开始你的学习之旅吧");
                            mAddClass.setText("立即学习");
                        } else {
                            mTvClassNote.setText("你现在还没有课程哦，快点击添加吧");
                            mAddClass.setText("+添加课程");
                        }
                    }
                } else if (result.getStatus_code().equals("203")) {
                    mClassNoData.setVisibility(View.VISIBLE);
                    mClassList.setVisibility(View.GONE);
                    mLookClass.setVisibility(View.GONE);

                    if (Integer.parseInt(result.getIs_course()) > 0) {
                        mTvClassNote.setText("当前暂无学习记录，开始你的学习之旅吧");
                        mAddClass.setText("立即学习");
                    } else {
                        mTvClassNote.setText("你现在还没有课程哦，快点击添加吧");
                        mAddClass.setText("+添加课程");
                    }
                } else if (result.getStatus_code().equals("401")) {
                    GloableConstant.getInstance().clearAll();
                    ToastUtil.showShortToast("请重新登录");
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                } else {
                    ToastUtil.showShortToast("数据错误");
                }

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
                GloableConstant.getInstance().stopProgressDialog1();
                mClassNoData.setVisibility(View.VISIBLE);
                mClassList.setVisibility(View.GONE);
            }
        });
    }

    /**
     * 首页资讯
     */
    private void requestInfo() {
        Map<String, Object> map = new HashMap<>();
        map.put("page", 1);
        map.put("page_size", 10);
        map.put("user_id", (String) SPUtils.getSpValues(SPKeyValuesUtils.SP_USER_ID, SPUtils.TYPE_STRING));
        Xutil.Post(ApiUrl.ARTICLELIST, map, new MyCallBack<InformationResponse>() {
            @Override
            public void onSuccess(InformationResponse result) {
                super.onSuccess(result);

                if (result.getStatus_code().equals("200")) {
                    if (result.getData() != null && result.getData().size() > 0) {
                        if (mList.size() > 1) {
                            mList.clear();
                        }
                        String textString = null;
                        String textdescription = null;
                        for (int i = 0; i < result.getData().size(); i++) {
                            if (result.getData().get(i).getTitle().length() > 18) {
                                textString = result.getData().get(i).getTitle().substring(0, 18) + "...";
                                textdescription = result.getData().get(i).getDescription();
                            } else {
                                textString = result.getData().get(i).getTitle();
                                textdescription = result.getData().get(i).getDescription();
                            }
//                      mList.add(new AdEntity("【公告】"+textString, "", result.getData().get(i)));

                            if (!result.getData().get(i).equals("") && result.getData().get(i) != null) {
                                mList.add(new AdEntity("最新", textString, result.getData().get(i)));
                            }


                        }
                    }
                } else if (result.getStatus_code().equals("203")) {
                    mInfoListLy.setVisibility(View.GONE);
                } else if (result.getStatus_code().equals("401")) {
                    GloableConstant.getInstance().clearAll();
                    ToastUtil.showShortToast("请重新登录");
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                }
                if (mList.size() > 0) {
                    mNotice.setmTexts(mList);
                    mNotice.invalidate();
//                    mList.remove(0);
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
            }
        });
    }

    /**
     * 用户信息
     */
    public void requestUserInfo() {
        Map<String, String> map = new HashMap<>();
//        map.put("user_id","1");
        map.put("user_id", (String) SPUtils.getSpValues(SPKeyValuesUtils.SP_USER_ID, SPUtils.TYPE_STRING));
        Log.e("*******************map", "requestUserInfo: +++++++++");
        Xutil.GET(ApiUrl.USER, map, new MyCallBack<PersonMessageResponse>() {
            @Override
            public void onSuccess(PersonMessageResponse result) {
                super.onSuccess(result);
                if (result.getStatus_code().equals("200")) {
                    if (result.getData() != null) {
                        Glide.with(getActivity()).load(result.getData().getAvatar()).apply(GloableConstant.getInstance().getOptions()).into(mPhoto);
                        mLearnDays.setText(result.getData().getSign_num());
                        mTaskDays.setText(result.getData().getTask_num());
                        mMainTextScore.setText(result.getData().getCredit());
                        mChange.setText(result.getData().getExam_type());
                        message = result.getData().getExam_type();
                        mNickName.setText(result.getData().getNick_name());
                        SPUtils.setSPValues(SPUtils.TYPE_STRING, "userName", result.getData().getNick_name());
                        if (result.getData().getIs_read_num().equals("0")) {
                            mDot.setVisibility(View.GONE);
                        } else {
                            mDot.setVisibility(View.VISIBLE);
                            new QBadgeView(getActivity()).bindTarget(mDot).setBadgeNumber(Integer.valueOf(result.getData().getIs_read_num())).setBadgeTextSize(10, true);

                        }
                        if (result.getData().getIs_sign().equals("1")) {
                            mSign.setText("已签到");
                            mSign.setTextColor(getResources().getColor(R.color.yellow));
                            mSign.setBackgroundResource(R.drawable.purse_yellow);
                            mIsSign = 1;
                        }
                        exam_type = result.getData().getExam_type_id();
                        SPUtils.setSPValues(1, "examType", exam_type);
                        if (result.getData().getExam_type().equals("")) {
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    dialogToSet();
                                }
                            }, 3000);

                        } else {
                            dialogToSetDismiss();
                        }

                    }
                } else if (result.getStatus_code().equals("401")) {
                    GloableConstant.getInstance().clearAll();
                    ToastUtil.showShortToast("请重新登录");
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MESSAGERESULT) {
            requestUserInfo();
        }
    }

    /**
     * 更换考试类型
     */
    ExamTypeSelectAdapter mExamTypeSelectAdapter;

    private void showPopWindow() {
//        mchange.setBackground(getResources().getDrawable(R.mipmap.classfy_check));
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mContentView = inflater.inflate(R.layout.item_updata_message, null);
        mPopupWindow = new PopupWindow(mContentView, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        mType = (TextView) mContentView.findViewById(R.id.type);
        mRecyclerView = (ListView) mContentView.findViewById(R.id.recycle_view);
        mExamTypeSelectAdapter = new ExamTypeSelectAdapter(getActivity());
        mRecyclerView.setAdapter(mExamTypeSelectAdapter);
        mmChange = (TextView) mContentView.findViewById(R.id.sure_change);
        unChange = (TextView) mContentView.findViewById(R.id.un_change);
        mButton = (LinearLayout) mContentView.findViewById(R.id.button);
        mCancel = (RelativeLayout) mContentView.findViewById(R.id.cancel);
        mTitle = (TextView) mContentView.findViewById(R.id.title);
        mName = (LinearLayout) mContentView.findViewById(R.id.name);
        mDescribe = (TextView) mContentView.findViewById(R.id.describe);
        mRecyclerView.setVisibility(View.GONE);
        mCancel.setVisibility(View.GONE);
        mType.setText(message);
        mPopupWindow.setFocusable(true);// 取得焦点
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        //点击外部消失
        mPopupWindow.setOutsideTouchable(false);
        //设置可以点击
        mPopupWindow.setTouchable(true);
        setBackgroundAlpha(0.5f);//设置屏幕透明度
        mPopupWindow.showAtLocation(mContentView, Gravity.CENTER, 0, 0);
        mPopupWindow.setOnDismissListener(new poponDismissListener());
        mRecyclerView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        mRecyclerView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                mExamTypeSelectAdapter.setSelection(position);
                mExamTypeSelectAdapter.notifyDataSetChanged();
                ExamTypeResponse.mData mData = (ExamTypeResponse.mData) parent.getAdapter().getItem(position);
                exam_type = mData.getId();
                mChange.setText(mData.getName());
                message = mData.getName();
                if (mPopupWindow.isShowing()) {
                    mPopupWindow.dismiss();
                }
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        exam_type = mExamData.get(position).getId();
                        if (NetworkUtils.isNetworkConnected(getActivity())) {
                            UpDataMessage();
                        } else {
                            getLocalData();
                        }

                    }
                }, 300);
            }
        });
        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                mchange.setBackground(getResources().getDrawable(R.mipmap.classfy));
                if (mPopupWindow.isShowing()) {
                    mPopupWindow.dismiss();
                }
            }
        });
        unChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                mchange.setBackground(getResources().getDrawable(R.mipmap.classfy));
                if (mPopupWindow.isShowing()) {
                    mPopupWindow.dismiss();
                }
            }
        });
        mmChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTitle.setText("您想更换的阶段是？");
                mCancel.setVisibility(View.VISIBLE);
                mName.setVisibility(View.GONE);
                mDescribe.setVisibility(View.GONE);
                mButton.setVisibility(View.GONE);
                mRecyclerView.setVisibility(View.VISIBLE);
                // examType();


                mExamData = new ArrayList<>();
                mExamData.add(new ExamTypeResponse.mData("1", "幼儿"));
                mExamData.add(new ExamTypeResponse.mData("2", "中学"));
                mExamData.add(new ExamTypeResponse.mData("3", "小学"));

                mExamTypeSelectAdapter.setmList(mExamData);
                for (int i = 0; i < mExamData.size(); i++) {
                    if (exam_type.equals(mExamData.get(i).getId())) {
                        mExamTypeSelectAdapter.setSelection(i);
                    }
                }
            }
        });


    }

    /**
     * 去设置考试类型
     */
    private void setExamTypePopWindow() {
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View mExamContentView = inflater.inflate(R.layout.select_exam_type_popwindow, null);
        PopupWindow mExamPopupWindow = new PopupWindow(mExamContentView, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        LinearLayout mButton = (LinearLayout) mContentView.findViewById(R.id.button);
        mExamPopupWindow.setFocusable(true);// 取得焦点
        mExamPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        //点击外部消失
        mExamPopupWindow.setOutsideTouchable(false);
        //设置可以点击
        mExamPopupWindow.setTouchable(true);
        setBackgroundAlpha(0.5f);//设置屏幕透明度
        mExamPopupWindow.showAtLocation(mContentView, Gravity.CENTER, 0, 0);
        mExamPopupWindow.setOnDismissListener(new poponDismissListener());
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 /* if(result.getData().getExam_type() == null){
                            startActivity(new Intent(getActivity(),ExamSubjectSelect.class));
                        }*/
            }
        });
    }

    /**
     * 获取考试类型
     */

    private int mSelectedPos = -1;


//    private void examType() {
//        Map<String, String> map = new HashMap<>();
//        GloableConstant.getInstance().startProgressDialog(getContext());
//        Xutil.GET(ApiUrl.EXAMTYPE, map, new MyCallBack<ExamTypeResponse>() {
//            @Override
//            public void onSuccess(ExamTypeResponse result) {
//                super.onSuccess(result);
//                GloableConstant.getInstance().stopProgressDialog1();
//                if (result.getStatus_code().equals("200")) {
//                    if (result.getData().size() > 0) {
//                        mExamData = result.getData();
//                        mExamTypeSelectAdapter.setmList(mExamData);
//                        for (int i = 0; i < mExamData.size(); i++) {
//                            if (message.equals(mExamData.get(i).getName())) {
//                                mExamTypeSelectAdapter.setSelection(i);
//                            }
//                        }
//
////                        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
////                        mRecyclerView.setAdapter(new CommonAdapter<ExamTypeResponse.mData>(getActivity(), R.layout.item_updata_message_pop, result.getData()) {
////
////                            @Override
////                            protected void convert(final ViewHolder holder, final ExamTypeResponse.mData mData, final int position) {
////                                holder.setText(R.id.primary, mData.getName());
////
////                                if (message.equals(mData.getName())) {
////                                    Log.d("********position", position + "*///////" + message);
////                                    holder.setBackgroundRes(R.id.primary, R.drawable.exam_background);
////                                    holder.setTextColorRes(R.id.primary, R.color.white);
////                                    mExamData.get(position).setSelected(true);
////                                    mSelectedPos=position;
////                                }
////                                holder.setOnClickListener(R.id.primary, new View.OnClickListener() {
////                                    @Override
////                                    public void onClick(View v) {
////                                        Log.e("*********position", mDatas.get(position).getName() + "");
////                                        mchange.setText(mDatas.get(position).getName());
////                                        mExamTypeId = mDatas.get(position).getId();
////
////                                        if (!message.equals(mDatas.get(position).getName())) {
////                                            Log.d("********position", position + "*///////" + message);
////                                            holder.setBackgroundRes(R.id.primary, R.drawable.exam_background_purse);
////                                            holder.setTextColorRes(R.id.primary, R.color.purple4);
////                                            message = mDatas.get(position).getName();
////                                        }
////                                        holder.setBackgroundRes(R.id.primary, R.drawable.exam_background);
////                                        holder.setTextColorRes(R.id.primary, R.color.white);
////                                        new Handler().postDelayed(new Runnable(){
////                                            public void run() {
////                                                //execute the task
////                                                UpDataMessage();
////                                            }
////                                        }, 2000);
////
////                                    }
////                                });
////                            }
////                        });
//                    }
//                }
//            }
//        });
//    }

    /**
     * 更新类型
     */

    private void UpDataMessage() {
        Map<String, Object> map = new HashMap<>();
        map.put("user_id", SPUtils.getSpValues(SPKeyValuesUtils.SP_USER_ID, SPUtils.TYPE_STRING));
        map.put("exam_type", exam_type);
        Log.e("********map", map.toString());
        Xutil.Post(ApiUrl.UPDATA, map, new MyCallBack<SupportResponse>() {
            @Override
            public void onSuccess(SupportResponse result) {
                super.onSuccess(result);
                if (result.getStatus_code().equals("204")) {
                    ToastUtil.showShortToast(result.getMessage());
                    Drawable drawable = ContextCompat.getDrawable(getActivity(), R.mipmap.switch_type);
                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                    mChange.setCompoundDrawables(null, null, drawable, null);
//                    mchange.setBackground(getResources().getDrawable(R.mipmap.classfy));
                    if (mPopupWindow.isShowing()) {
                        mPopupWindow.dismiss();
                    }
                    EventBus.getDefault().post(new MessageEvent("更换考试类型"));
                    SPUtils.setSPValues(1, "examType", exam_type);

                    getQusetionBank();
                } else {
                    ToastUtil.showShortToast(result.getMessage());
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
            }
        });
    }

    /*获取信息*/
    private void GainMessage() {
        Map<String, String> map = new HashMap<>();
//        map.put("user_id", "1");
        map.put("user_id", (String) SPUtils.getSpValues(SPKeyValuesUtils.SP_USER_ID, SPUtils.TYPE_STRING));
        GloableConstant.getInstance().startProgressDialog(getActivity());
        Xutil.GET(ApiUrl.USER, map, new MyCallBack<PersonMessageResponse>() {
            @Override
            public void onSuccess(PersonMessageResponse result) {
                super.onSuccess(result);
                GloableConstant.getInstance().stopProgressDialog1();
                Log.e("*********result", result.getStatus_code());
                if (result.getStatus_code().equals("200")) {
                    exam_type = result.getData().getExam_type();
                    mType.setText(exam_type);
                    Log.e("*********exam_type", exam_type);
                }
            }

            @Override
            public void onFinished() {
                super.onFinished();
                GloableConstant.getInstance().stopProgressDialog();
            }
        });


    }


    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha 屏幕透明度0.0-1.0 1表示完全不透明
     */
    public void setBackgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.alpha = bgAlpha;
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        getActivity().getWindow().setAttributes(lp);
    }

    @Override
    public void onClick(View v) {
        helper.nextLight();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    class poponDismissListener implements PopupWindow.OnDismissListener {

        @Override
        public void onDismiss() {

            setBackgroundAlpha(1f);
        }
    }

    /**
     * 签到
     */
    public void requestSign() {
        Map<String, Object> map = new HashMap<>();
//        map.put("user_id","1");
        map.put("user_id", SPUtils.getSpValues(SPKeyValuesUtils.SP_USER_ID, SPUtils.TYPE_STRING));
        Xutil.Post(ApiUrl.USERSIGN, map, new MyCallBack<SignDayResponse>() {
            @Override
            public void onSuccess(SignDayResponse result) {
                super.onSuccess(result);
                if (result.getStatus_code().equals("204")) {
                    ToastUtil.showShortToast("签到成功");
                    mSign.setText("已签到");
                    mSign.setTextColor(getResources().getColor(R.color.yellow));
                    mSign.setBackgroundResource(R.drawable.purse_yellow);
                    mIsSign = 1;
                    dialogSign();
                    /*if(Integer.valueOf(result.getData().getSign_num()) % 7 == 0){
                        mRemainDays.setText("领取教师币成功");
                    }else{
                        mRemainDays.setText("签到成功");
                    }
                    mList.get(Integer.valueOf(result.getData().getSign_num()) - 1).setIs_sign("1");
                    mSignAdapter.notifyDataSetChanged();
                    mTotalCoin.setText("我的教师币 "+(mCionNum + 1));
                    mSignedDays.setText("已经连续签到"+(mSignNum + 1)+"天");
                    mRemainDays.setTextColor(ContextCompat.getColor(mContext,R.color.text_color6));
                    mRemainDays.setBackground(ContextCompat.getDrawable(mContext,R.drawable.gray_shap_5));*/
                } else {
                    ToastUtil.showShortToast(result.getMessage());
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
            }
        });
    }


    private void showOnCreate() {

        View deco_view1 = LayoutInflater.from(getActivity()).inflate(R.layout.newpersonguide_layout_one, (ViewGroup) getActivity().getWindow().getDecorView(), false);
        View deco_view2 = LayoutInflater.from(getActivity()).inflate(R.layout.newpersonguide_layout_two, (ViewGroup) getActivity().getWindow().getDecorView(), false);
        helper = new GuideViewHelper(getActivity())
                .addView(mChange, new CenterCenterStyle(deco_view1, -40))

//                .type(LightType.Circle)
                .type(LightType.Rectangle)
                .autoNext()
                .addView(mAddClass, new CenterCenterStyle(deco_view2, -40))
                .onDismiss(new GuideView.OnDismissListener() {
                    @Override
                    public void dismiss() {
//                        ToastUtil.showShortToast("5656");
//                        MainActivity activity = (MainActivity) getActivity();
//                        activity.changeSchoolTab();
                        if (TextUtils.isEmpty(String.valueOf(SPUtils.getSpValues("HomeAdsIsClose", 1)))) {
                            requestAdvert2();
                        }

                    }
                });
        helper.show();
        Sphelper.save(getActivity(), "ISFIRST", "isfirst", "1");


    }

    private PopupWindow popupWindow;

    private void showAds(final List<HomeAdsBean.DataBean.ImgArrBean> imgList, final String title) {
        View deco_view1 = LayoutInflater.from(getActivity()).inflate(R.layout.view_home_ads, null, false);
        Banner banner = deco_view1.findViewById(R.id.ads_banner);
        ImageView close = deco_view1.findViewById(R.id.iv_close);

        popupWindow = new PopupWindow(getActivity());
        popupWindow.setContentView(deco_view1);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#55000000")));
        popupWindow.setOutsideTouchable(false);
        popupWindow.setTouchable(true);
        popupWindow.showAtLocation(mChange, 0, 0, 0);

        //设置图片加载器
        banner.setImageLoader(new GlideImageLoader());
        //设置图片集合
        banner.setImages(imgList);
        //banner设置方法全部调用完毕时最后调用s
        banner.start();

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
                SPUtils.setSPValues(1, "HomeAdsIsClose", "1");
            }
        });
        banner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                if (imgList.size() > 0) {
                    if (imgList.get(position) != null) {
                        adsIntentTo(imgList.get(position).getType(), imgList.get(position).getType_id(), title);
                    }
                }

            }
        });


    }

    private class GlideImageLoader extends ImageLoader {
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            /**
             注意：
             1.图片加载器由自己选择，这里不限制，只是提供几种使用方法
             2.返回的图片路径为Object类型，由于不能确定你到底使用的那种图片加载器，
             传输的到的是什么格式，那么这种就使用Object接收和返回，你只需要强转成你传输的类型就行，
             切记不要胡乱强转！
             */
            //Glide 加载图片简单用法
            HomeAdsBean.DataBean.ImgArrBean homeSlideBean = (HomeAdsBean.DataBean.ImgArrBean) path;
            Glide.with(context).load(homeSlideBean.getImages()).into(imageView);
        }

    }


    public void stop() {
        new Thread(new Runnable() {

            @Override
            public void run() {
                try {

                    Thread.sleep(2000);
                    handler.sendEmptyMessage(1);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }
        }).start();
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int what = msg.what;
            if (what == 1) {
                showOnCreate();
            }
        }
    };

    public void requestAdvert() {
        Map<String, Object> map = new HashMap<>();
        map.put("user_id", SPUtils.getSpValues(SPKeyValuesUtils.SP_USER_ID, SPUtils.TYPE_STRING));
        map.put("ad_type", "1");
        Xutil.Post(ApiUrl.ADVERTISEMENT, map, new MyCallBack<AdvertSementResponse>() {
            @Override
            public void onSuccess(final AdvertSementResponse result) {
                super.onSuccess(result);
                if (result.getStatus_code().equals("200")) {
                    //普通ads
                    mBannerRl.setVisibility(View.VISIBLE);
                    Glide.with(getActivity()).load(result.getData().getImages()).apply(GloableConstant.getInstance().getDefaultOption()).into(mBannner);
                    mBannerRl.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            adsIntentTo(result.getData().getType(), result.getData().getType_id(), result.getData().getTitle());
                        }
                    });

                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
            }
        });
    }

    public void requestAdvert2() {
        Map<String, Object> map = new HashMap<>();
        map.put("user_id", SPUtils.getSpValues(SPKeyValuesUtils.SP_USER_ID, SPUtils.TYPE_STRING));
        map.put("ad_type", "2");
        Xutil.Post(ApiUrl.ADVERTISEMENT, map, new MyCallBack<HomeAdsBean>() {
            @Override
            public void onSuccess(final HomeAdsBean result) {
                super.onSuccess(result);
                if (result.getStatus_code().equals("200")) {
                    //弹窗ads
                    if (result.getData().getImg_arr().size() > 0) {
                        showAds(result.getData().getImg_arr(), result.getData().getTitle());
                    }

                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
            }
        });
    }


    private void adsIntentTo(String type, String typeid, String title) {
        switch (Integer.valueOf(type)) {
            case 1:
//                                    ToastUtil.showShortToast("班级");
                Intent classIntent = new Intent();
                classIntent.setClass(getActivity(), CoursesDetialActivity.class);
                classIntent.putExtra("type", type);
                classIntent.putExtra("course_id", typeid);//type_id为课程id
                startActivity(classIntent);
                break;
            case 2:
//                                    ToastUtil.showShortToast("课程");
                Intent courseIntent = new Intent();
                courseIntent.setClass(getActivity(), CoursesDetialActivity.class);
                courseIntent.putExtra("type", type);
                courseIntent.putExtra("course_id", typeid);//type_id为课s程id
                startActivity(courseIntent);
                break;
            case 3:
//                                    ToastUtil.showShortToast("图书");
                Intent bookIntent = new Intent();
                bookIntent.setClass(getActivity(), BookDetailActivity.class);
                bookIntent.putExtra("book_id", typeid);
                startActivity(bookIntent);
                break;
            case 4:
//                                    ToastUtil.showShortToast("图文");
                Intent ItIntent = new Intent();
                ItIntent.setClass(getActivity(), InformationDetialActivity.class);
                ItIntent.putExtra("info_id", typeid);
                startActivity(ItIntent);
                break;
            case 5:
//                                    ToastUtil.showShortToast("外链");
                Intent linkIntent = new Intent();
                linkIntent.setClass(getActivity(), LinkWebActivity.class);
                linkIntent.putExtra("url", typeid);
                linkIntent.putExtra("title", title);
                startActivity(linkIntent);
                break;
            case 6:
//                                    ToastUtil.showShortToast("课程列表");
                MainActivity activity = (MainActivity) getActivity();
                activity.changeSchoolTab("0");
                //sendBroadcast("classSchool");
                break;
            case 7:
//                                    ToastUtil.showShortToast("图书列表");
                MainActivity activity1 = (MainActivity) getActivity();
                activity1.changeSchoolTab("1");
                //sendBroadcast("classSchool");
                break;
            case 8:
//                                    ToastUtil.showShortToast("公开课列表");
                MainActivity activity2 = (MainActivity) getActivity();
                activity2.changeLiveTab();
                //sendBroadcast("");
                break;
        }
    }

    /**
     * 直播推荐
     */
//    private long time;
//    private long time1;
//    private long time2;
//    private String _id;
//    private Intent intent;

    //    private long timeNow = 0;
    private void mNowLive() {
        Map<String, String> map = new HashMap<>();
        map.put("user_id", (String) SPUtils.getSpValues(SPKeyValuesUtils.SP_USER_ID, SPUtils.TYPE_STRING));
        Xutil.GET(ApiUrl.HOMEOPEN, map, new MyCallBack<ZhiBoBean>() {
            @Override
            public void onSuccess(final ZhiBoBean result) {
                super.onSuccess(result);
                if (result.getStatus_code().equals("200")) {
                    if (result.getData() != null && result.getData().size() > 0) {
                        mMainNowLive.setVisibility(View.VISIBLE);
                        mZhibolistview.setAdapter(new com.zhy.adapter.abslistview.CommonAdapter<ZhiBoBean.DataBean>(getActivity(), R.layout.item_home_zhibo, result.getData()) {

                            @Override
                            protected void convert(final com.zhy.adapter.abslistview.ViewHolder viewHolder, final ZhiBoBean.DataBean item, int position) {
                                viewHolder.setText(R.id.open_time, item.getHome_time());
                                viewHolder.setText(R.id.open_title, item.getName());
                                ImageView imageView = viewHolder.getView(R.id.open_img);
                                CornerTransform transform = new CornerTransform(getActivity(), dip2px(getActivity(), 10));
                                //只是绘制左上角和右上角圆角
                                transform.setExceptCorner(false, false, true, true);
                                RequestOptions requestOptions = new RequestOptions();
                                requestOptions.transform(transform);
                                Glide.with(getActivity()).load(item.getImage()).apply(requestOptions).into(imageView);

                                if (item.getIs_live() == 1) {//即将直播
                                    viewHolder.setVisible(R.id.open_yugao, false);
                                    viewHolder.setVisible(R.id.view_mask, true);
                                    long startTime = Long.parseLong(item.getStart_times());
                                    long endTime = Long.parseLong(item.getEnd_times());
                                    long currTime = System.currentTimeMillis();

                                    if (currTime >= startTime & currTime <= endTime) {
                                        //直播中
                                        viewHolder.setVisible(R.id.ll_livein, true);
                                        viewHolder.setVisible(R.id.open_countdown, false);
                                        GifView gif = viewHolder.getView(R.id.open_gif);
                                        gif.setMovieResource(R.raw.picture);

                                        viewHolder.setOnClickListener(R.id.open_item, new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                onLine(item.getLive_info().getApp_name(), item);
                                            }
                                        });
                                    } else {
                                        viewHolder.setVisible(R.id.ll_livein, false);
                                        viewHolder.setVisible(R.id.open_countdown, true);
                                        OpenCustomDigitalClock countdown = viewHolder.getView(R.id.open_countdown);
                                        if (currTime < startTime) {
                                            //未开始
                                            countdown.setEndTime(startTime);
                                            countdown.setClockListener(new OpenCustomDigitalClock.ClockListener() {
                                                @Override
                                                public void timeEnd() {
                                                    //倒计时结束
                                                    viewHolder.setVisible(R.id.ll_livein, true);
                                                    viewHolder.setVisible(R.id.open_countdown, false);
                                                    GifView gif = viewHolder.getView(R.id.open_gif);
                                                    gif.setMovieResource(R.raw.picture);
                                                }

                                                @Override
                                                public void remainFiveMinutes() {

                                                }
                                            });

                                            viewHolder.setOnClickListener(R.id.open_item, new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    Intent intent = new Intent();
                                                    intent.setClass(getActivity(), OpenClassDetailActivity.class);
                                                    intent.putExtra("_id", item.getId());
                                                    intent.putExtra("recent", "recent");
                                                    startActivity(intent);
                                                }

                                            });
                                        } else if (currTime > endTime) {
                                            //已结束
                                            countdown.setText("直播已结束");

                                            viewHolder.setOnClickListener(R.id.open_item, new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    Intent intent = new Intent();
                                                    intent.setClass(getActivity(), OpenClassDetailActivity.class);
                                                    intent.putExtra("_id", item.getId());
                                                    intent.putExtra("lb_url", item.getLb_url());
                                                    intent.putExtra("recent", "history");
                                                    startActivity(intent);
                                                }

                                            });
                                        }

                                    }

                                } else {//预告
                                    viewHolder.setVisible(R.id.open_yugao, true);
                                    viewHolder.setVisible(R.id.view_mask, false);
                                    viewHolder.setVisible(R.id.ll_livein, false);
                                    viewHolder.setVisible(R.id.open_countdown, false);
                                    viewHolder.setOnClickListener(R.id.open_item, new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            Intent intent = new Intent();
                                            intent.setClass(getActivity(), OpenClassDetailActivity.class);
                                            intent.putExtra("_id", item.getId());
                                            intent.putExtra("recent", "recent");
                                            startActivity(intent);
                                        }
                                    });

                                }
                            }

                        });

                    } else {
                        mMainNowLive.setVisibility(View.GONE);

                    }
                } else if (result.getStatus_code().equals("401")) {
                    GloableConstant.getInstance().clearAll();
                    ToastUtil.showShortToast("请重新登录");
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                } else {
                    mMainNowLive.setVisibility(View.GONE);
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
            }
        });
    }

    private void onLine(String appname, final ZhiBoBean.DataBean bean) {
        Map<String, String> map = new HashMap<>();
        map.put("app_name", appname);
        Log.e("OPOP", appname);
        Xutil.GET(ApiUrl.ONLINE, map, new MyCallBack<OnLineResponse>() {
            @Override
            public void onSuccess(OnLineResponse result) {
                super.onSuccess(result);
                if (result.getStatus_code().equals("200")) {
                    if (result.getData().getIs_online() != null) {
                        // 备注：0直播不在线 1正在直播
                        if (result.getData().getIs_online().equals("0")) {
                            ToastUtil.showShortToast("直播不在线");
                        } else {
                            Intent intent = new Intent();
                            intent.setClass(getActivity(), LiveActivity.class);
                            intent.putExtra("_id", bean.getId());
                            intent.putExtra("chat_room_id", bean.getLive_info().getChat_room_id());
                            intent.putExtra("token", bean.getLive_info().getToken());
                            intent.putExtra("live_url", bean.getLive_info().getPush_url());
                            intent.putExtra("name", bean.getLive_info().getName());
                            startActivity(intent);
                        }
                    }
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
            }

            @Override
            public void onFinished() {
                super.onFinished();
            }
        });

    }

    @Override
    protected void refresh() {
        super.refresh();
        requestMineClass();
    }

    private void getLocalData() {

        List<QuestionBankList> tmplist = new QuestionBankListDao(getActivity()).queryByColumnNameAndColumnName("user_id", user_id, "examtype_id", exam_type);
        if (tmplist != null && tmplist.size() > 0) {
            //表中有数据
            mTiku.setVisibility(View.VISIBLE);
            mListView.setVisibility(View.VISIBLE);
            GloableConstant.getInstance().setTiku_id(null);
            mListView.setAdapter(new com.zhy.adapter.abslistview.CommonAdapter<QuestionBankList>(getActivity(), R.layout.item_quetion_bank1, tmplist) {

                @Override
                protected void convert(com.zhy.adapter.abslistview.ViewHolder viewHolder, final QuestionBankList item, int position) {
                    viewHolder.setText(R.id.title, item.getName());
                    Log.d("***********tatus_code", item.getExam_name());
                    viewHolder.setText(R.id.type, item.getExam_name());
                    viewHolder.setText(R.id.to_confirm_progress, item.getGuanqia_totle_pass_num());
                    viewHolder.setText(R.id.all_to_confirm, "/" + item.getGuanqia_totle_num());
                    viewHolder.setText(R.id.crown_progress, item.getHuangguan_have_totle_num());
                    viewHolder.setText(R.id.all_crown, "/" + item.getHuangguan_totle_num());
                    viewHolder.setOnClickListener(R.id.item, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            GloableConstant.getInstance().setTiku_id(item.getQbid());
                            Intent intent = new Intent();
                            intent.setClass(mContext, QuestionBankDetailActivity.class);
                            intent.putExtra("name", item.getName());
                            intent.putExtra("id", item.getId());
                            startActivity(intent);

                        }
                    });

                }

            });


        } else {
            //表中没有数据
            mTiku.setVisibility(View.GONE);
            mListView.setVisibility(View.GONE);
        }
    }

    public int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }


}
