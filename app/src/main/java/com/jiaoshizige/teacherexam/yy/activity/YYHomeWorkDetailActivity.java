package com.jiaoshizige.teacherexam.yy.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.UiThread;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jiaoshizige.teacherexam.activity.HomeWorkDetailActivity;
import com.jwenfeng.library.pulltorefresh.BaseRefreshListener;
import com.jwenfeng.library.pulltorefresh.ViewStatus;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.jiaoshizige.teacherexam.R;
import com.jiaoshizige.teacherexam.activity.DoHomeWorkActivity;
import com.jiaoshizige.teacherexam.activity.ReplayDetialActivity;
import com.jiaoshizige.teacherexam.base.BaseActivity;
import com.jiaoshizige.teacherexam.base.CommSelectorRecyclerViewAdapter;
import com.jiaoshizige.teacherexam.base.GloableConstant;
import com.jiaoshizige.teacherexam.base.ViewHolderZhy;
import com.jiaoshizige.teacherexam.http.ApiUrl;
import com.jiaoshizige.teacherexam.http.MyCallBack;
import com.jiaoshizige.teacherexam.http.SupportResponse;
import com.jiaoshizige.teacherexam.http.Xutil;
import com.jiaoshizige.teacherexam.model.GetZanReplyResponse;
import com.jiaoshizige.teacherexam.model.HomeWorkDetailResponse;
import com.jiaoshizige.teacherexam.utils.PullToRefreshLayoutRewrite;
import com.jiaoshizige.teacherexam.utils.SDViewUtil;
import com.jiaoshizige.teacherexam.utils.SPKeyValuesUtils;
import com.jiaoshizige.teacherexam.utils.SPUtils;
import com.jiaoshizige.teacherexam.utils.ShareUtils;
import com.jiaoshizige.teacherexam.utils.ToastUtil;
import com.jiaoshizige.teacherexam.utils.ToolUtils;
import com.jiaoshizige.teacherexam.widgets.MediaManager;
import com.jiaoshizige.teacherexam.widgets.OnSelectItemListener;
import com.jiaoshizige.teacherexam.widgets.SharePopWindow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/12/12.
 * 作业详情
 */
public class YYHomeWorkDetailActivity extends BaseActivity {
    @BindView(R.id.work_title)
    TextView mWorkTitle;
    @BindView(R.id.teacher_icon)
    ImageView mTeacherIcon;
    @BindView(R.id.teacher_name)
    TextView mTeacherName;
    @BindView(R.id.teacher_type)
    TextView mTeacherType;
    @BindView(R.id.creat_time)
    TextView mCreatTime;
    @BindView(R.id.work_content)
    WebView mWorkContent;
    @BindView(R.id.mine_photo)
    ImageView mMinePhoto;
    @BindView(R.id.mine_nickname)
    TextView mMineNickName;
    @BindView(R.id.answer_time)
    TextView mAnswerTime;
    @BindView(R.id.do_homework_ly)
    LinearLayout mDoHomeWorkLy;//去做作业
    @BindView(R.id.do_homework)
    TextView mDoHomeWork;
    @BindView(R.id.mine_honework)
    LinearLayout mMineHomeWork;//已经有作业了
    @BindView(R.id.vioce_rt)
    RelativeLayout mVioceRt;
    @BindView(R.id.vioce_length)
    TextView mVioceLength;
    @BindView(R.id.answer_content)
    TextView mAnswerContent;
    @BindView(R.id.add_pic)
    LinearLayout mMineAddPicLy;
    @BindView(R.id.del_answer)
    TextView mDelAnswer;
    @BindView(R.id.school_work)
    RecyclerView mSchoolWork;
    @BindView(R.id.teacher_reply_ly)
    LinearLayout mTeacherReplyLy;
    @BindView(R.id.reply_teacher_name)
    TextView mTeacherReplyName;
    @BindView(R.id.reply_teacher_type)
    ImageView mTeacherReplyType;
    @BindView(R.id.teacher_reply_content)
    TextView mTeacherReplyContent;
    @BindView(R.id.classmate_reply_ly)
    LinearLayout mClassMateReplayLy;
    @BindView(R.id.replay_content)
    EditText mReplayContentEd;
    @BindView(R.id.replay)
    Button mReplay;
    @BindView(R.id.work_detial_ly)
    NestedScrollView mWorkDetialLy;
    @BindView(R.id.pulltorefresh)
    PullToRefreshLayoutRewrite mPullToRefresh;
    @BindView(R.id.check_answer)
    TextView mCheck;
    View mAanimView;
    //    View otherAnimView;
    private String mWorkId;
    private String mIsdone;
    private String mAnswerId;
    private String mPid;
    private String mZanId;
    private int mPage = 1;
    private String mPageSize = "15";
    private String mPageNum = "1";
    private float downX;    //按下时 的X坐标
    private float downY;    //按下时 的Y坐标
    String action = "";
    private int buttonFlag = 0;
    SharePopWindow mSharePopWindow;
    private String title;
    private String mImage;
    private String content;
    WindowManager wm;
    private CommSelectorRecyclerViewAdapter<HomeWorkDetailResponse.mOtherHomeWork> mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.home_work_detail_activity;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPage = 1;
        mPageNum = "1";
        requestHomeWorkDetail();
        MobclickAgent.onPageStart("作业详情");
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("作业详情");
        MobclickAgent.onPause(this);
        MediaManager.pause();
    }

    @Override
    protected void initView() {
        setSubTitle().setText("");
        setToolbarTitle().setText("作业详情");
        setSubTitle().setBackground(ContextCompat.getDrawable(this, R.mipmap.share_icon));
        wm = (WindowManager) this
                .getSystemService(Context.WINDOW_SERVICE);
        if (getIntent().getExtras().get("work_id") != null) {
            mWorkId = (String) getIntent().getExtras().get("work_id");
        } else {
            mWorkId = "";
        }

        Log.e("********mWorkId*", mWorkId);
        if (getIntent().getExtras().get("is_done") != null) {
            mIsdone = (String) getIntent().getExtras().get("is_done");
        } else {
            mIsdone = "";
        }
        if (getIntent().getStringExtra("message_id") != null) {
            deleteMessage(getIntent().getStringExtra("message_id"));
        }
        mPullToRefresh.setRefreshListener(new BaseRefreshListener() {
            @Override
            public void refresh() {
                mPage = 1;
                mPageNum = "1";
                requestHomeWorkDetail();
            }

            @Override
            public void loadMore() {
                mPage++;
                requestHomeWorkDetail();
                Log.e("TAG", "mPage" + mPage);
            }
        });
        mSchoolWork.setNestedScrollingEnabled(false);
        mWorkDetialLy.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

                if (scrollY == (v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight())) {
                    //底部加载
                    buttonFlag = 1;
                } else {
                    buttonFlag = 0;
                }
            }
        });
        mWorkDetialLy.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //在触发时回去到起始坐标
                float x = event.getX();
                float y = event.getY();
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        downX = x;
                        downY = y;
//                        Log.e("Tag","=======按下时X："+x);
//                        Log.e("Tag","=======按下时Y："+y);
                        break;
                    case MotionEvent.ACTION_UP:
//                        Log.e("Tag","=======抬起时X："+x);
//                        Log.e("Tag","=======抬起时Y："+y);

                        //获取到距离差
                        float dx = x - downX;
                        float dy = y - downY;
                        if (Math.abs(dx) > 8 && Math.abs(dy) > 8) {
                            //通过距离差判断方向
                            int orientation = getOrientation(dx, dy);
                            switch (orientation) {
                                case 'r':
                                    action = "右";
                                    break;
                                case 'l':
                                    action = "左";
                                    break;
                                case 't':
                                    if (buttonFlag == 1) {
                                        action = "上";
                                        mSchoolWork.smoothScrollToPosition(0);
                                        if (buttonFlag == 1) {
                                            mPage++;
                                            mPageNum = String.valueOf(mPage);
                                            requestHomeWorkDetail();
                                            Log.e("TAG", buttonFlag + "daodibule" + mPage);
                                        }

                                    }
                                    break;
                                case 'b':
                                    action = "下";
                                    break;
                            }

                        }
                        break;
                }
                return false;
            }
        });

    }

    /**
     * 根据距离差判断 滑动方向
     *
     * @param dx X轴的距离差
     * @param dy Y轴的距离差
     * @return 滑动的方向
     */
    private int getOrientation(float dx, float dy) {
        Log.e("Tag", "========X轴距离差：" + dx);
        Log.e("Tag", "========Y轴距离差：" + dy);
        if (Math.abs(dx) > Math.abs(dy)) {
            //X轴移动
            return dx > 0 ? 'r' : 'l';
        } else {
            //Y轴移动
            return dy > 0 ? 'b' : 't';
        }
    }

    @OnClick({R.id.replay, R.id.do_homework, R.id.toolbar_subtitle})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.replay:
                if (!mReplayContentEd.getText().toString().trim().equals("")) {
                    requestReply(mAnswerId, mReplayContentEd.getText().toString().trim(), mPid);
                } else {
                    ToastUtil.showShortToast("请输入回复内容");
                }
                break;
            case R.id.do_homework:
                Intent intent = new Intent();
                intent.putExtra("work_id", mWorkId);//answer_id
                intent.setClass(YYHomeWorkDetailActivity.this, DoHomeWorkActivity.class);
                startActivity(intent);
                break;
            case R.id.toolbar_subtitle:
                showSharePopWindow();
                break;


        }
    }


    /**
     * 作业详情
     */
    private void requestHomeWorkDetail() {
        Map<String, String> map = new HashMap<>();
//        map.put("user_id","35");
        map.put("user_id", (String) SPUtils.getSpValues(SPKeyValuesUtils.SP_USER_ID, SPUtils.TYPE_STRING));
        map.put("work_id", mWorkId);
        map.put("page", mPageNum);
        map.put("page_size", mPageSize);
//        map.put("work_id","1");
        Log.e("TAg", map.toString());
        GloableConstant.getInstance().startProgressDialog(this);
        Xutil.GET(ApiUrl.HOMEWORKDEATIL, map, new MyCallBack<HomeWorkDetailResponse>() {
            @Override
            public void onSuccess(final HomeWorkDetailResponse result) {
                super.onSuccess(result);
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        if (mPageNum.equals("1")) {
                            mWorkDetialLy.fullScroll(mWorkDetialLy.FOCUS_UP);
                        } else {
                            mWorkDetialLy.fullScroll(mWorkDetialLy.FOCUS_DOWN);
                        }
                    }
                });
                GloableConstant.getInstance().stopProgressDialog1();
                mPullToRefresh.finishRefresh();
                mPullToRefresh.finishLoadMore();
                mPullToRefresh.showView(ViewStatus.CONTENT_STATUS);
                if (result.getStatus_code().equals("200")) {
                    if (result.getData() != null) {
                        Log.e("TAG1", "jkjknull");
                        if (result.getData().getHomework() != null) {
                            Log.e("TAG1", "jkjkhomweee");
                            mAnswerId = result.getData().getHomework().getId();//作业id
                            mWorkTitle.setText(result.getData().getHomework().getTitle());
                            mTeacherName.setText(result.getData().getHomework().getName());
                            title = result.getData().getHomework().getTitle();
                            content = result.getData().getHomework().getContent();
                            mImage = result.getData().getHomework().getHeadimg();
                            mCreatTime.setText(result.getData().getHomework().getCreated_at());
                            if (result.getData().getHomework().getType().equals("1")) {
                                mTeacherType.setBackground(ContextCompat.getDrawable(YYHomeWorkDetailActivity.this, R.mipmap.ban));
                            } else {
                                mTeacherType.setBackground(ContextCompat.getDrawable(YYHomeWorkDetailActivity.this, R.mipmap.zhu));
                            }
                            Glide.with(YYHomeWorkDetailActivity.this).load(result.getData().getHomework().getHeadimg()).apply(GloableConstant.getInstance().getOptions()).into(mTeacherIcon);
                            WebSettings webSettings = mWorkContent.getSettings();
                            webSettings.setJavaScriptEnabled(true);
                            webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
                            webSettings.setLoadWithOverviewMode(true);
                            String test = "<style type='text/css'>p{margin-left:15px;margin-right:15px},img {width:100%!important;height:auto!important;font-size:14px;text-align:center}</style>" + result.getData().getHomework().getContent();
                            mWorkContent.loadDataWithBaseURL(null, test, "text/html", "utf-8", null);
                        } else {
                            //应该不会出现吧
                        }
                        /**
                         * 我的作业
                         */

                        if (!ToolUtils.isNullOrEmpty(result.getData().getMy_homework()) && result.getData().getMy_homework().getId() != null) {

                            if (mIsdone.equals("1")) {
                                mDoHomeWorkLy.setVisibility(View.GONE);
                                mMineHomeWork.setVisibility(View.VISIBLE);
                                mAnswerTime.setVisibility(View.VISIBLE);
                                mAnswerTime.setText(result.getData().getMy_homework().getCreated_at());
                                mCheck.setText(result.getData().getMy_homework().getCount_reply() + "人回复");
                                mCheck.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent();
                                        intent.setClass(YYHomeWorkDetailActivity.this, ReplayDetialActivity.class);
                                        intent.putExtra("answer_id", result.getData().getMy_homework().getId());
                                        startActivity(intent);
                                    }
                                });
                                mDelAnswer.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        ToastUtil.showShortToast("删除");
                                        requestDeleHomeWoek(result.getData().getMy_homework().getId());
                                    }
                                });
                            } else {
                                mDoHomeWorkLy.setVisibility(View.VISIBLE);
                                mMineHomeWork.setVisibility(View.GONE);
                            }
                            if (result.getData().getMy_homework().getIs_done().equals("1")) {
                                mDoHomeWorkLy.setVisibility(View.GONE);
                                mMineHomeWork.setVisibility(View.VISIBLE);
                                mAnswerTime.setVisibility(View.VISIBLE);
                                mAnswerTime.setText(result.getData().getMy_homework().getCreated_at());
                                mDelAnswer.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        ToastUtil.showShortToast("删除");
                                        requestDeleHomeWoek(result.getData().getMy_homework().getId());
                                    }
                                });
                            } else {
                                mDoHomeWorkLy.setVisibility(View.VISIBLE);
                                mMineHomeWork.setVisibility(View.GONE);
                            }
                            mMineNickName.setText(result.getData().getMy_homework().getNick_name());
                            Glide.with(YYHomeWorkDetailActivity.this).load(result.getData().getMy_homework().getAvatar()).apply(GloableConstant.getInstance().getOptions()).into(mMinePhoto);
                            if (!result.getData().getMy_homework().getContent().equals("")) {
                                mAnswerContent.setText(result.getData().getMy_homework().getContent());
                            } else {
                                mAnswerContent.setVisibility(View.GONE);
                            }
                            if (result.getData().getMy_homework().getImages() != null && result.getData().getMy_homework().getImages().size() > 0) {
                                //操作图片展示
                                mMineAddPicLy.removeAllViews();

                                int width = wm.getDefaultDisplay().getWidth();
                                for (int i = 0; i < result.getData().getMy_homework().getImages().size(); i++) {
                                    ImageView imageView = new ImageView(YYHomeWorkDetailActivity.this);
                                    imageView.setLayoutParams(new ViewGroup.LayoutParams((width - ToolUtils.dip2px(YYHomeWorkDetailActivity.this, 30)), (width - ToolUtils.dip2px(YYHomeWorkDetailActivity.this, 30))));
                                    Glide.with(YYHomeWorkDetailActivity.this).load(result.getData().getMy_homework().getImages().get(i).getImg()).apply(GloableConstant.getInstance().getDefaultOptionssmall()).into(imageView);
                                    imageView.setPadding(10, 0, 10, 0);
                                    mMineAddPicLy.addView(imageView);
                                }
                            } else {
                                mMineAddPicLy.removeAllViews();
                                mMineAddPicLy.setVisibility(View.GONE);
                            }
                            if (result.getData().getMy_homework().getType().equals("1")) {
                                mVioceRt.setVisibility(View.GONE);
                            } else {
                                if (!result.getData().getMy_homework().getAudio_url().equals("")) {
                                    if (!result.getData().getMy_homework().getDuration().equals("")) {
                                        mVioceRt.setVisibility(View.VISIBLE);
                                        mVioceLength.setText(result.getData().getMy_homework().getDuration() + "s");
                                    }
                                    mVioceRt.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            //操作下载音频数据 不取数据
                                            if (mAanimView != null) {
                                                mAanimView.setBackground(ContextCompat.getDrawable(YYHomeWorkDetailActivity.this, R.mipmap.home_iocn_play_default));
                                                mAanimView = null;
                                            }

                                            mAanimView = (View) findViewById(R.id.bo_vioce);
                                            mAanimView.setBackgroundResource(R.drawable.play_anim);
                                            AnimationDrawable animation = (AnimationDrawable) mAanimView.getBackground();
                                            animation.start();
                                            Log.e("OOOww", result.getData().getMy_homework().getAudio_url());
//                                        String s = "http://www.w3school.com.cn/i/horse.mp3";
                                            String aa = ApiUrl.BASEIMAGE + "uploads/homework_audio/20180514111738149.mp3";
                                            MediaManager.playSound(YYHomeWorkDetailActivity.this, Uri.parse(result.getData().getMy_homework().getAudio_url()), new MediaPlayer.OnCompletionListener() {
                                                public void onCompletion(MediaPlayer mp) {
                                                    mAanimView.setBackground(ContextCompat.getDrawable(YYHomeWorkDetailActivity.this, R.mipmap.home_iocn_play_default));
                                                }
                                            });
                                        }
                                    });

                                } else {
                                    mVioceRt.setVisibility(View.GONE);
                                }
                            }
                            if (result.getData().getMy_homework().getTeacher_reply() != null && result.getData().getMy_homework().getTeacher_reply().getType() != null) {
                                mTeacherReplyLy.setVisibility(View.VISIBLE);
                                mTeacherReplyName.setText(result.getData().getMy_homework().getTeacher_reply().getName());
                                mTeacherReplyContent.setText(result.getData().getMy_homework().getTeacher_reply().getContent());
                                if (result.getData().getMy_homework().getTeacher_reply().getType().equals("1")) {
                                    mTeacherReplyType.setBackground(ContextCompat.getDrawable(YYHomeWorkDetailActivity.this, R.mipmap.ban));
                                } else {
                                    mTeacherReplyType.setBackground(ContextCompat.getDrawable(YYHomeWorkDetailActivity.this, R.mipmap.zhu));
                                }
                            } else {
                                mTeacherReplyLy.setVisibility(View.GONE);
                            }
                        } else {
                            //正常是不会空的
                            Log.e("TAG1", "56565656565");
                            mDoHomeWorkLy.setVisibility(View.VISIBLE);
                            mMineHomeWork.setVisibility(View.GONE);

                        }
                        /**
                         * 同学作业
                         */
                        if (result.getData().getOther_homework() != null && result.getData().getOther_homework().size() > 0) {
                            initListView();
                            if ("1".equals(mPageNum)) {
                                mAdapter.updateData(result.getData().getOther_homework());
                            } else {
                                mAdapter.appendData(result.getData().getOther_homework());
                            }
                        } else {
                            if (mPage > 1) {
                                ToastUtil.showShortToast("没有更多数据了");
                            }
                            //没有人回答问题
                            Log.e("TAG1", "jkjk" + "ssss");
                        }
                    } else {
                        //接口返回数据为空
                    }
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
                Log.e("ATG1", ex.getMessage() + " ");
                mPullToRefresh.finishRefresh();
                mPullToRefresh.finishLoadMore();
                mPullToRefresh.showView(ViewStatus.CONTENT_STATUS);
                GloableConstant.getInstance().stopProgressDialog1();
            }
        });
    }

    Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 1:

                    SDViewUtil.hideInputMethod(getToolbar(), getApplication());
                    break;
            }
            return false;
        }
    });

    private void hideKeyboard(IBinder token) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);

        }
    }

    private PopupWindow mPopWindReplayEdit;

    /**
     * 回复输入框
     *
     * @param view
     * @param mOtherHomeWork
     */
    public void showPopReplayEdit(View view, final HomeWorkDetailResponse.mOtherHomeWork mOtherHomeWork) {
        String nickname = mOtherHomeWork.getNick_name() + "";

        backgroundAlpha(0.6f);
        if (mPopWindReplayEdit != null) {
            EditText pop_input = (EditText) mPopWindReplayEdit.getContentView().findViewById(R.id.pop_ed_input_content);
            pop_input.setHint("@" + nickname);
//            SDViewUtil.showInputMethod(pop_input, 300);
            mPopWindReplayEdit.showAtLocation(view, Gravity.BOTTOM, 0, 0);
            return;
        }
        View menuView = LayoutInflater.from(this).inflate(R.layout.pop_relpay_ed_input, null);
        final EditText pop_ed_input_content = (EditText) menuView.findViewById(R.id.pop_ed_input_content);
        Button pop_btn_send_replay = (Button) menuView.findViewById(R.id.pop_btn_send_replay);
        pop_ed_input_content.setHint("@" + nickname);
        mPopWindReplayEdit = new PopupWindow(menuView, -1, -2);
        mPopWindReplayEdit.setTouchable(true);
        mPopWindReplayEdit.setFocusable(true);
        mPopWindReplayEdit.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        mPopWindReplayEdit.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        mPopWindReplayEdit.setBackgroundDrawable(new BitmapDrawable());
        mPopWindReplayEdit.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                SDViewUtil.hideInputMethod(getToolbar());
//                hideKeyboard(getCurrentFocus().getWindowToken());
                backgroundAlpha(1.0f);
            }
        });
//        SDViewUtil.showInputMethod(pop_input_content,300);
//        SDViewUtil.showInputMethod(pop_ed_input_content, 300);
        mPopWindReplayEdit.showAtLocation(view, Gravity.BOTTOM, 0, 0);
        pop_btn_send_replay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String contentStr = pop_ed_input_content.getText().toString().trim();
                if (TextUtils.isEmpty(contentStr)) {
                    ToastUtil.showShortToast("请输入回复内容");
                    return;
                }
                requestReply(mAnswerId, contentStr, mPid);
                pop_ed_input_content.setText("");
                mPopWindReplayEdit.dismiss();
            }
        });
    }
//    protected InputMethodManager mInputMethodManager;
//        protected void hideSoftKeyboard() {
//            mInputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//            if (getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN && getCurrentFocus() != null) {
//                mInputMethodManager.hideSoftInputFromWindow(
//                        getCurrentFocus().getWindowToken(),
//                        InputMethodManager.HIDE_NOT_ALWAYS);
//            }
//        }


    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     */
    protected void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getWindow().setAttributes(lp);
    }

    /**
     * 删除作业
     *
     * @param work_id
     */
    private void requestDeleHomeWoek(String work_id) {
        Map<String, Object> map = new HashMap<>();
//        map.put("user_id","1");
        map.put("user_id", SPUtils.getSpValues(SPKeyValuesUtils.SP_USER_ID, SPUtils.TYPE_STRING));
        map.put("work_id", work_id);
        Xutil.Post(ApiUrl.DELETHOMEWORK, map, new MyCallBack<SupportResponse>() {
            @Override
            public void onSuccess(SupportResponse result) {
                super.onSuccess(result);
                if (result.getStatus_code().equals("204")) {
                    ToastUtil.showShortToast("作业删除成功");
                    mDoHomeWorkLy.setVisibility(View.VISIBLE);
                    mMineHomeWork.setVisibility(View.GONE);
                    mAnswerTime.setVisibility(View.GONE);
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

    /**
     * 回复作业
     */
    private void requestReply(String answer_id, final String content, String pid) {
        Map<String, Object> map = new HashMap<>();
//        map.put("user_id","1");
        map.put("user_id", SPUtils.getSpValues(SPKeyValuesUtils.SP_USER_ID, SPUtils.TYPE_STRING));
        map.put("answer_id", answer_id);
        map.put("content", content);
        map.put("pid", pid);
        Log.e("huifuzuoye", map.toString());
        Xutil.Post(ApiUrl.REPLAYHOMEWORK, map, new MyCallBack<SupportResponse>() {
            @Override
            public void onSuccess(SupportResponse result) {
                super.onSuccess(result);
                if (result.getStatus_code().equals("204")) {
                    ToastUtil.showShortToast("回复成功");
                    SDViewUtil.hideInputMethod(getToolbar());
                    String count = mAdapter.getSelected().getCount_reply();

                    mAdapter.getSelected().setCount_reply(strToInt(count) + 1 + "");
                    mAdapter.notifyDataSetChanged();
//                    requestHomeWorkDetail();
                    mClassMateReplayLy.setVisibility(View.GONE);
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

    private int strToInt(String intStr) {
        try {
            return Integer.valueOf(intStr);
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * 点赞
     *
     * @param be_like_id
     * @param mOtherHomeWork
     */
    private void requestGetZan(String be_like_id, final HomeWorkDetailResponse.mOtherHomeWork mOtherHomeWork, final TextView mZanText) {
        Map<String, Object> map = new HashMap<>();
//        map.put("user_id","1");
        map.put("user_id", SPUtils.getSpValues(SPKeyValuesUtils.SP_USER_ID, SPUtils.TYPE_STRING));
        map.put("be_like_id", be_like_id);
        map.put("type", "4");//1评论，2笔记, 3问答回复，4作业，5作业回复
        map.put("", "");
        Xutil.Post(ApiUrl.GETZAN, map, new MyCallBack<GetZanReplyResponse>() {
            @Override
            public void onSuccess(GetZanReplyResponse result) {
                super.onSuccess(result);
                if (result.getStatus_code().equals("204")) {
                    ToastUtil.showShortToast("点赞成功");
//                    mOtherHomeWork.setIs_like("1");
//                    mOtherHomeWork.setCount_zan(String.valueOf(Integer.valueOf(mOtherHomeWork.getCount_zan())+1));
                    Log.e("TAG", mOtherHomeWork.getCount_zan() + "cheng");
                    Drawable drawable = ContextCompat.getDrawable(YYHomeWorkDetailActivity.this, R.mipmap.common_like_pre);
                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                    mZanText.setCompoundDrawables(drawable, null, null, null);
                    mZanText.setText(result.getData().getCount());
                } else if (result.getStatus_code().equals("205")) {
                    ToastUtil.showShortToast("取消点赞成功");
//                    mOtherHomeWork.setIs_like("0");
//                    mOtherHomeWork.setCount_zan(String.valueOf(Integer.valueOf(mOtherHomeWork.getCount_zan())-1));
                    Log.e("TAG", mOtherHomeWork.getCount_zan() + "bai");
                    Drawable drawable = ContextCompat.getDrawable(YYHomeWorkDetailActivity.this, R.mipmap.common_like);
                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                    mZanText.setCompoundDrawables(drawable, null, null, null);
                    mZanText.setText(result.getData().getCount());
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

    public void showSharePopWindow() {
        mSharePopWindow = new SharePopWindow(this);
        mSharePopWindow.isShowing();
        String user_id = (String) SPUtils.getSpValues(SPKeyValuesUtils.SP_USER_ID, SPUtils.TYPE_STRING);
        final String url = ApiUrl.BASEIMAGE + "api/v1/work_share?user_id=" + user_id + "&work_id=" + mWorkId + "&user_id=" + user_id;
        mSharePopWindow.setOnSelectItemListener(new OnSelectItemListener() {
            @Override
            public void selectItem(String name, int type) {
                if (mSharePopWindow != null && mSharePopWindow.isShowing()) {
                    mSharePopWindow.dismiss();

                    switch (type) {
                        case SharePopWindow.POP_WINDOW_ITEM_1:
                            ShareUtils.shareWeb(YYHomeWorkDetailActivity.this, url, title
                                    , content, mImage, R.mipmap.ic_launcher, SHARE_MEDIA.WEIXIN
                            );
                            break;
                        case SharePopWindow.POP_WINDOW_ITEM_2:
                            ShareUtils.shareWeb(YYHomeWorkDetailActivity.this, url, title
                                    , content, mImage, R.mipmap.ic_launcher, SHARE_MEDIA.WEIXIN_CIRCLE
                            );
                            break;
                        case SharePopWindow.POP_WINDOW_ITEM_3:
                            ShareUtils.shareWeb(YYHomeWorkDetailActivity.this, url, title
                                    , content, mImage, R.mipmap.ic_launcher, SHARE_MEDIA.QQ
                            );
                            break;
                        case SharePopWindow.POP_WINDOW_ITEM_4:

                            ShareUtils.shareWeb(YYHomeWorkDetailActivity.this, url, title
                                    , content, mImage, R.mipmap.ic_launcher, SHARE_MEDIA.QZONE
                            );
                            break;
                        case SharePopWindow.POP_WINDOW_ITEM_5:
                            ShareUtils.shareWeb(YYHomeWorkDetailActivity.this, url, title
                                    , content, mImage, R.mipmap.ic_launcher, SHARE_MEDIA.SMS
                            );
                            break;
                        case SharePopWindow.POP_WINDOW_ITEM_6:

                            ShareUtils.shareWeibo(YYHomeWorkDetailActivity.this, url, title
                                    , content, mImage, R.mipmap.ic_launcher, SHARE_MEDIA.SINA
                            );
                            break;
                        default:
                            break;
                    }
                }
            }
        });
    }

    /**
     * 查看推送消息
     */
    private void deleteMessage(String message_id) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", message_id);
        map.put("user_id", SPUtils.getSpValues(SPKeyValuesUtils.SP_USER_ID, SPUtils.TYPE_STRING));
        map.put("type", 1);
        Xutil.Post(ApiUrl.DELETE, map, new MyCallBack<SupportResponse>() {
            @Override
            public void onSuccess(SupportResponse result) {
                super.onSuccess(result);
                if (result.getStatus_code().equals("204")) {
//                ToastUtil.showShortToast("删除成功");
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MediaManager.release();
    }


    private void initListView() {
        if (mAdapter == null) {
            mAdapter = new CommSelectorRecyclerViewAdapter<HomeWorkDetailResponse.mOtherHomeWork>(this, R.layout.item_other_answer, null
                    , CommSelectorRecyclerViewAdapter.ESelectMode.SINGLE_MUST_ONE_SELECTED) {
                @Override
                protected void convert(final ViewHolderZhy holder, final HomeWorkDetailResponse.mOtherHomeWork mOtherHomeWork, final int position) {
                    holder.setText(R.id.other_name, mOtherHomeWork.getNick_name());
                    holder.setText(R.id.other_answer_time, mOtherHomeWork.getCreated_at());
                    ImageView mOtherPhote = (ImageView) holder.getConvertView().findViewById(R.id.other_photo);
                    Glide.with(YYHomeWorkDetailActivity.this).load(mOtherHomeWork.getAvatar()).apply(GloableConstant.getInstance().getOptions()).into(mOtherPhote);
                    if (!mOtherHomeWork.getContent().equals("")) {
                        holder.setText(R.id.other_answer_content, mOtherHomeWork.getContent());
                    } else {
                        holder.setVisible(R.id.other_answer_content, false);
                    }
                    if (mOtherHomeWork.getIs_seen().equals("2")) {
                        holder.setText(R.id.other_answer_content, "此回答仅楼主可见");
                        holder.setTextColor(R.id.other_answer_content, ContextCompat.getColor(YYHomeWorkDetailActivity.this, R.color.purple4));
                        holder.setVisible(R.id.vioce_rt, false);
                        holder.setVisible(R.id.answer_pic, false);
                    }
                    if (!mOtherHomeWork.getAudio_url().equals("")) {
                        holder.setVisible(R.id.vioce_rt, true);
                        if (mOtherHomeWork.getDuration() != null && !mOtherHomeWork.getDuration().equals("")) {
                            holder.setText(R.id.vioce_length, mOtherHomeWork.getDuration());

                        }
                                       /* View otherAnimView = (View)holder.getConvertView().findViewById(R.id.bo_vioce);
                                        if (otherAnimView != null) {
                                            otherAnimView.setBackground(ContextCompat.getDrawable(HomeWorkDetailActivity.this, R.mipmap.home_iocn_play_default));
                                            otherAnimView = null;
                                        }*/
                        ///操作下载录音
//                                        final View finalOtherAnimView = otherAnimView;
                        holder.setOnClickListener(R.id.vioce_rt, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                final View finalOtherAnimView = (View) holder.getConvertView().findViewById(R.id.bo_vioce);
                                finalOtherAnimView.setBackgroundResource(R.drawable.play_anim);
                                AnimationDrawable animation = (AnimationDrawable) finalOtherAnimView.getBackground();
                                animation.start();
                                String s = mOtherHomeWork.getAudio_url();
                                Log.e("OOO", mOtherHomeWork.getAudio_url());
//                                                String s = "http://www.w3school.com.cn/i/horse.mp3";
//                                                Uri sd = Uri.parse("http://www.w3school.com.cn/i/horse.mp3");
                                MediaManager.playSound(YYHomeWorkDetailActivity.this, Uri.parse(s), new MediaPlayer.OnCompletionListener() {
                                    public void onCompletion(MediaPlayer mp) {
                                        finalOtherAnimView.setBackground(ContextCompat.getDrawable(YYHomeWorkDetailActivity.this, R.mipmap.home_iocn_play_default));
                                    }
                                });
                            }
                        });
                    } else {
                        holder.setVisible(R.id.vioce_rt, false);
                    }
                    LinearLayout mOtherAddViewLy = (LinearLayout) holder.getConvertView().findViewById(R.id.answer_pic);
                    mOtherAddViewLy.setOrientation(LinearLayout.HORIZONTAL);
                    if (mOtherHomeWork.getImages() != null && mOtherHomeWork.getImages().size() > 0) {
                        //操作图片展示
                        mOtherAddViewLy.removeAllViews();
                        WindowManager wm = (WindowManager) mContext
                                .getSystemService(mContext.WINDOW_SERVICE);
                        int width = wm.getDefaultDisplay().getWidth();

                        for (int i = 0; i < mOtherHomeWork.getImages().size(); i++) {
                            ImageView imageView = new ImageView(mContext);
                            imageView.setLayoutParams(new ViewGroup.LayoutParams((width / 5), (width / 5)));

                            Glide.with(mContext).load(mOtherHomeWork.getImages().get(i).getImg()).apply(GloableConstant.getInstance().getDefaultOptionssmall()).into(imageView);
                            imageView.setPadding(10, 0, 10, 0);
                            mOtherAddViewLy.addView(imageView);
                        }
//                                        Log.e("HHHj",mOtherAddViewLy.get)

                    } else {
                        mOtherAddViewLy.removeAllViews();
                        holder.setVisible(R.id.answer_pic, false);
                    }

                    Log.e("*********greply()", mOtherHomeWork.getCount_reply() + "********");

                    holder.setText(R.id.classmate_replay_num, "查看" + mOtherHomeWork.getCount_reply() + "人回答");
                    if (!mOtherHomeWork.getCount_reply().equals("0")) {
                        holder.setOnClickListener(R.id.classmate_replay_num, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent();
                                intent.setClass(YYHomeWorkDetailActivity.this, ReplayDetialActivity.class);
                                intent.putExtra("answer_id", mAnswerId);
                                // TODO: 2018/6/9
                                intent.putExtra("pid", mOtherHomeWork.getId());
                                startActivity(intent);
                            }
                        });
                    }
//                                    holder.setText(R.id.zan,mOtherHomeWork.getCount_zan());
                    final TextView mZanText = (TextView) holder.getConvertView().findViewById(R.id.zan);
                    mZanText.setText(mOtherHomeWork.getCount_zan());
                    Log.e("TAG", mOtherHomeWork.getCount_zan());
                    if (mOtherHomeWork.getIs_like().equals("1")) {//0未点赞，1已点赞
                        Drawable drawable = ContextCompat.getDrawable(mContext, R.mipmap.common_like_pre);
                        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                        mZanText.setCompoundDrawables(drawable, null, null, null);
                    } else {
                        Drawable drawable = ContextCompat.getDrawable(mContext, R.mipmap.common_like);
                        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                        mZanText.setCompoundDrawables(drawable, null, null, null);
                    }
                    holder.setOnClickListener(R.id.zan, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            requestGetZan(mOtherHomeWork.getId(), mOtherHomeWork, mZanText);
//                                            notifyDataSetChanged();
                            Log.e("TAG", mOtherHomeWork.getCount_zan() + "kankan");
                        }
                    });
                    holder.setOnClickListener(R.id.reply, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mPid = mOtherHomeWork.getId();
                            showPopReplayEdit(v, getDatas().get(position));
                            doSelected(mOtherHomeWork);
//                                            mClassMateReplayLy.setVisibility(View.VISIBLE);
//                                            FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) mWorkDetialLy.getLayoutParams();
//                                            lp.setMargins(0, 0, 0, ToolUtils.dip2px(HomeWorkDetailActivity.this, 50));
//                                            mWorkDetialLy.setLayoutParams(lp);
                        }
                    });
                    if (mOtherHomeWork.getTeacher_reply() != null && mOtherHomeWork.getTeacher_reply().getType() != null) {
                        Log.e("TAG1", "tongxue" + mOtherHomeWork.getTeacher_reply());
                        holder.setText(R.id.reply_teacher_name, mOtherHomeWork.getTeacher_reply().getName());
                        holder.setText(R.id.teacher_reply_content, mOtherHomeWork.getTeacher_reply().getContent());
                        holder.setText(R.id.teacher_reply_time, mOtherHomeWork.getTeacher_reply().getCreated_at());

                        if (mOtherHomeWork.getTeacher_reply().getType().equals("1")) {
                            holder.setImageDrawable(R.id.reply_teacher_type, ContextCompat.getDrawable(YYHomeWorkDetailActivity.this, R.mipmap.ban));
                        } else {
                            holder.setImageDrawable(R.id.reply_teacher_type, ContextCompat.getDrawable(YYHomeWorkDetailActivity.this, R.mipmap.zhu));
                        }
                    } else {
                        holder.setVisible(R.id.teacher_reply_ly, false);
                    }
                }

            };
            mSchoolWork.setLayoutManager(new LinearLayoutManager(this));
            mSchoolWork.setAdapter(mAdapter);
        }
    }
}
