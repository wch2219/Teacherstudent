package com.jiaoshizige.teacherexam.mycourse.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.easefun.polyvsdk.PolyvBitRate;
import com.easefun.polyvsdk.PolyvDownloader;
import com.easefun.polyvsdk.PolyvDownloaderManager;
import com.easefun.polyvsdk.PolyvSDKUtil;
import com.easefun.polyvsdk.srt.PolyvSRTItemVO;
import com.easefun.polyvsdk.video.PolyvMediaInfoType;
import com.easefun.polyvsdk.video.PolyvPlayErrorReason;
import com.easefun.polyvsdk.video.PolyvVideoView;
import com.easefun.polyvsdk.video.listener.IPolyvOnAdvertisementCountDownListener;
import com.easefun.polyvsdk.video.listener.IPolyvOnAdvertisementEventListener2;
import com.easefun.polyvsdk.video.listener.IPolyvOnAdvertisementOutListener2;
import com.easefun.polyvsdk.video.listener.IPolyvOnCompletionListener2;
import com.easefun.polyvsdk.video.listener.IPolyvOnErrorListener2;
import com.easefun.polyvsdk.video.listener.IPolyvOnGestureClickListener;
import com.easefun.polyvsdk.video.listener.IPolyvOnGestureLeftDownListener;
import com.easefun.polyvsdk.video.listener.IPolyvOnGestureLeftUpListener;
import com.easefun.polyvsdk.video.listener.IPolyvOnGestureRightDownListener;
import com.easefun.polyvsdk.video.listener.IPolyvOnGestureRightUpListener;
import com.easefun.polyvsdk.video.listener.IPolyvOnGestureSwipeLeftListener;
import com.easefun.polyvsdk.video.listener.IPolyvOnGestureSwipeRightListener;
import com.easefun.polyvsdk.video.listener.IPolyvOnInfoListener2;
import com.easefun.polyvsdk.video.listener.IPolyvOnPreloadPlayListener;
import com.easefun.polyvsdk.video.listener.IPolyvOnPreparedListener2;
import com.easefun.polyvsdk.video.listener.IPolyvOnQuestionAnswerTipsListener;
import com.easefun.polyvsdk.video.listener.IPolyvOnQuestionOutListener2;
import com.easefun.polyvsdk.video.listener.IPolyvOnTeaserCountDownListener;
import com.easefun.polyvsdk.video.listener.IPolyvOnTeaserOutListener;
import com.easefun.polyvsdk.video.listener.IPolyvOnVideoPlayErrorListener2;
import com.easefun.polyvsdk.video.listener.IPolyvOnVideoSRTListener;
import com.easefun.polyvsdk.video.listener.IPolyvOnVideoStatusListener;
import com.easefun.polyvsdk.vo.PolyvADMatterVO;
import com.easefun.polyvsdk.vo.PolyvQuestionVO;
import com.jiaoshizige.teacherexam.R;
import com.jiaoshizige.teacherexam.activity.LoginActivity;
import com.jiaoshizige.teacherexam.activity.RecordingActivity;
import com.jiaoshizige.teacherexam.activity.StudyCalendarActivity;
import com.jiaoshizige.teacherexam.activity.aaa.xx.activity.CourseDownFileActivity;
import com.jiaoshizige.teacherexam.activity.aaa.xx.adapter.SanJiDeLeiBiaoAdapter;
import com.jiaoshizige.teacherexam.activity.aaa.xx.bean.NewCourseListBean;
import com.jiaoshizige.teacherexam.activity.aaa.xx.utils.WcHLogUtils;
import com.jiaoshizige.teacherexam.base.GloableConstant;
import com.jiaoshizige.teacherexam.fragment.SignalCourseCatalogFragmnet;
import com.jiaoshizige.teacherexam.http.ApiUrl;
import com.jiaoshizige.teacherexam.http.MyCallBack;
import com.jiaoshizige.teacherexam.http.Xutil;
import com.jiaoshizige.teacherexam.model.CourseDetialResponse;
import com.jiaoshizige.teacherexam.mycourse.utils.Util;
import com.jiaoshizige.teacherexam.palyer.PolyvPlayerLightView;
import com.jiaoshizige.teacherexam.palyer.PolyvPlayerMediaController;
import com.jiaoshizige.teacherexam.palyer.PolyvPlayerPreviewView;
import com.jiaoshizige.teacherexam.palyer.PolyvPlayerProgressView;
import com.jiaoshizige.teacherexam.palyer.PolyvPlayerVolumeView;
import com.jiaoshizige.teacherexam.utils.PolyvErrorMessageUtils;
import com.jiaoshizige.teacherexam.utils.PolyvScreenUtils;
import com.jiaoshizige.teacherexam.utils.SPKeyValuesUtils;
import com.jiaoshizige.teacherexam.utils.SPUtils;
import com.jiaoshizige.teacherexam.utils.ToastUtil;
import com.jiaoshizige.teacherexam.utils.ToolUtils;
import com.jiaoshizige.teacherexam.yy.fragment.CourseraNoteFragment;
import com.jiaoshizige.teacherexam.yy.fragment.HomeworkFragment;
import com.jiaoshizige.teacherexam.yy.fragment.QuestionAndAnswerFragment;
import com.umeng.analytics.MobclickAgent;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.zhouzhuo.zzhorizontalprogressbar.ZzHorizontalProgressBar;

/**
 * 课程目录播放页
 */

public class MyNewCoursesActivity extends AppCompatActivity implements SanJiDeLeiBiaoAdapter.OnItemVideoPlayListener,
SignalCourseCatalogFragmnet.LastLeanBackListener{
    private static final String TAG = RecordingActivity.class.getSimpleName();
    @BindView(R.id.polyv_video_view)
    PolyvVideoView mVideoView;
    @BindView(R.id.logo)
    ImageView logo;
    @BindView(R.id.fl_danmu)
    FrameLayout flDanmu;
    @BindView(R.id.srt)
    TextView mSrtTextView;
    @BindView(R.id.polyv_player_light_view)
    PolyvPlayerLightView mLightView;
    @BindView(R.id.polyv_player_volume_view)
    PolyvPlayerVolumeView mVolumeView;
    @BindView(R.id.polyv_player_progress_view)
    PolyvPlayerProgressView mProgressView;
    @BindView(R.id.polyv_player_media_controller)
    PolyvPlayerMediaController mMediaController;
    @BindView(R.id.loading_progress)
    ProgressBar loadingProgress;
    @BindView(R.id.auxiliary_loading_progress)
    ProgressBar auxiliaryLoadingProgress;
    @BindView(R.id.count_down)
    TextView countDown;
    @BindView(R.id.polyv_player_first_start_view)
    PolyvPlayerPreviewView firstStartView;
    @BindView(R.id.iv_vlms_cover)
    ImageView mIvVlmsCover;

    @BindView(R.id.iv_videopic)
    ImageView ivVideopic;
    @BindView(R.id.tv_video_title)
    TextView tvVideoTitle;
    @BindView(R.id.tool_bar)
    Toolbar toolBar;
    @BindView(R.id.view_layout)
    RelativeLayout mViewLayout;
    @BindView(R.id.completion_degree)
    TextView completionDegree;
    @BindView(R.id.study_prodress)
    ZzHorizontalProgressBar studyProdress;
    @BindView(R.id.tv_download)
    ImageView tvDownload;
    @BindView(R.id.ll_download)
    LinearLayout llDownload;
    @BindView(R.id.tv_ecalendar)
    ImageView tvEcalendar;
    @BindView(R.id.ll_ecalendar)
    LinearLayout llEcalendar;
    @BindView(R.id.sheet)
    ImageView sheet;
    @BindView(R.id.ll_classgroup)
    LinearLayout llClassgroup;
    @BindView(R.id.tablayout)
    TabLayout tablayout;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    @BindView(R.id.ll_play)
    LinearLayout llPlay;
    @BindView(R.id.rl_pic)
    RelativeLayout rlPic;
    private Context ctx;
    private String[] mTitles = new String[]{"目录", "作业", "问答", "问答"};
    private List<Fragment> listFragment;
    private String mType;
    private String mCourseId;
    private String isBuy;
    private String mClassId;
    private String qq;
    private int fastForwardPos = 0;
    private int isLearnTime = 0;
    private String last_learnVid;
    private String covermap;
    private String mName;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_mycourse);
        ButterKnife.bind(this);
        ctx = this;
        initView();
        initTabBar();
    }

    public void initTabBar() {

        toolBar.setTitle("");
        setSupportActionBar(toolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.polyv_btn_back_s);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                boolean landscape = PolyvScreenUtils.isLandscape(ctx);
                if (landscape) {
                mMediaController.changeToPortrait();
                }else {
                    finish();
                }
                break;
        }

        return true;
    }

    @Override
    public void backPlay(NewCourseListBean.DataBean.LastLearnBean last_learn) {
           tvVideoTitle.setText(last_learn.getSection_name());
        last_learnVid = last_learn.getVid();
    }

    protected void initView() {
        if (Build.VERSION.SDK_INT >= 23) {
            String[] mPermissionList = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CALL_PHONE, Manifest.permission.READ_LOGS, Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.SET_DEBUG_APP, Manifest.permission.SYSTEM_ALERT_WINDOW, Manifest.permission.GET_ACCOUNTS, Manifest.permission.WRITE_APN_SETTINGS};
            ActivityCompat.requestPermissions(this, mPermissionList, 123);
        }
        if (getIntent().getExtras().get("type") != null) {
            mType = (String) getIntent().getExtras().get("type");
            Log.e("*******type", mType);

        } else {
            mType = "";
        }
        if (getIntent().getExtras().get("course_id") != null) {
            mCourseId = (String) getIntent().getExtras().get("course_id");
        } else {
            mCourseId = "";
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
        if (getIntent().getStringExtra("name") != null) {
            mName = getIntent().getStringExtra("name");
        }else{
            mName = "";
        }
        covermap = getIntent().getStringExtra("covermap");
        Glide.with(ctx).load(covermap).into(ivVideopic);
        listFragment = new ArrayList<Fragment>();
//        listFragment.add(new CourseCatalogFragment());//目录
        WcHLogUtils.I("mCourseId"+mCourseId+";mClassId:"+mClassId+";mType:"+mType+";isBuy:"+isBuy+";user_id:"+(String) SPUtils.getSpValues(SPKeyValuesUtils.SP_USER_ID, SPUtils.TYPE_STRING));
        if (mType.equals("1")) {
//            mFragments.add(new NewCourseCatalogFragmnet(mClassId,mType));
            listFragment.add(new SignalCourseCatalogFragmnet(mClassId, mType, isBuy,true));
        } else {
            listFragment.add(new SignalCourseCatalogFragmnet(mCourseId, mType, isBuy, true));//目录
        }
        SignalCourseCatalogFragmnet.setLastLeanBackListener(this);
        listFragment.add(new HomeworkFragment(mCourseId));//作业
        listFragment.add(new CourseraNoteFragment(mCourseId));//笔记
        listFragment.add(new QuestionAndAnswerFragment(mCourseId));//问答


        viewpager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return listFragment.get(position);
            }

            @Override
            public int getCount() {
                return mTitles.length;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return mTitles[position];
            }
        });
        Log.i("wch","mCourseId:"+mCourseId+"\r"+"mType:"+mType+"\r");
        tablayout.post(new Runnable() {
            @Override
            public void run() {
                Util.setIndicator(tablayout, 15, 15);
            }
        });
        tablayout.setupWithViewPager(viewpager);
        tablayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();

                // 设置ViewPager的页面切换
                viewpager.setCurrentItem(position);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        mMediaController.initConfig(mViewLayout);
//        mMediaController.setDanmuFragment(danmuFragment);
//        questionView.setPolyvVideoView(mVideoView);
//        auditionView.setPolyvVideoView(mVideoView);
//        auxiliaryVideoView.setPlayerBufferingIndicator(auxiliaryLoadingProgress);
//        auxiliaryView.setPolyvVideoView(mVideoView);
//        auxiliaryView.setDanmakuFragment(danmuFragment);
        mVideoView.setMediaController(mMediaController);
//        mVideoView.setAuxiliaryVideoView(auxiliaryVideoView);
        mVideoView.setPlayerBufferingIndicator(loadingProgress);

        mVideoView.setOpenAd(true);
        mVideoView.setOpenTeaser(true);
        mVideoView.setOpenQuestion(true);
        mVideoView.setOpenSRT(true);
        mVideoView.setOpenPreload(true, 2);
        mVideoView.setAutoContinue(true);
        mVideoView.setNeedGestureDetector(true);
//        mMediaController.changeToPortrait();


        courseDetial();
        handler.postDelayed(runnable, isLearnTime);
        //////////////////////////////////////////////
        mVideoView.setOnPreparedListener(new IPolyvOnPreparedListener2() {
            @Override
            public void onPrepared() {
                mMediaController.preparedView();
                // 没开预加载在这里开始弹幕
                // danmuFragment.start();
            }
        });

        mVideoView.setOnPreloadPlayListener(new IPolyvOnPreloadPlayListener() {
            @Override
            public void onPlay() {
                // 开启预加载在这里开始弹幕
//                danmuFragment.start();
            }
        });

        mVideoView.setOnInfoListener(new IPolyvOnInfoListener2() {
            @Override
            public boolean onInfo(int what, int extra) {
                switch (what) {
                    case PolyvMediaInfoType.MEDIA_INFO_BUFFERING_START:
//                        danmuFragment.pause(false);
                        break;
                    case PolyvMediaInfoType.MEDIA_INFO_BUFFERING_END:
//                        danmuFragment.resume(false);
                        break;
                }

                return true;
            }
        });

        mVideoView.setOnVideoStatusListener(new IPolyvOnVideoStatusListener() {
            @Override
            public void onStatus(int status) {
                if (status < 60) {
                    ToastUtil.showShortToast("状态错误 " + status);
                } else {
                    Log.d(TAG, String.format("状态正常 %d", status));
                }
            }
        });

        mVideoView.setOnVideoPlayErrorListener(new IPolyvOnVideoPlayErrorListener2() {
            @Override
            public boolean onVideoPlayError(@PolyvPlayErrorReason.PlayErrorReason int playErrorReason) {
                String message = PolyvErrorMessageUtils.getPlayErrorMessage(playErrorReason);
                message += "(error code " + playErrorReason + ")";

//                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
                builder.setTitle("错误");
                builder.setMessage(message);
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                    }
                });

                builder.show();
                return true;
            }
        });

        mVideoView.setOnErrorListener(new IPolyvOnErrorListener2() {
            @Override
            public boolean onError() {
                Toast.makeText(ctx, "当前视频无法播放，请向管理员反馈(error code " + PolyvPlayErrorReason.VIDEO_ERROR + ")", Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        mVideoView.setOnAdvertisementOutListener(new IPolyvOnAdvertisementOutListener2() {
            @Override
            public void onOut(@NonNull PolyvADMatterVO adMatter) {
//                auxiliaryView.show(adMatter);
            }
        });

        mVideoView.setOnAdvertisementCountDownListener(new IPolyvOnAdvertisementCountDownListener() {
            @Override
            public void onCountDown(int num) {
//                advertCountDown.setText("广告也精彩：" + num + "秒");
//                advertCountDown.setVisibility(View.VISIBLE);
            }

            @Override
            public void onEnd() {
//                advertCountDown.setVisibility(View.GONE);
//                auxiliaryView.hide();
            }
        });

        mVideoView.setOnAdvertisementEventListener(new IPolyvOnAdvertisementEventListener2() {
            @Override
            public void onShow(PolyvADMatterVO adMatter) {
                Log.i(TAG, "开始播放视频广告");
            }

            @Override
            public void onClick(PolyvADMatterVO adMatter) {
                if (!TextUtils.isEmpty(adMatter.getAddrUrl())) {
                    try {
                        new URL(adMatter.getAddrUrl());
                    } catch (MalformedURLException e1) {
                        Log.e(TAG, PolyvSDKUtil.getExceptionFullMessage(e1, -1));
                        return;
                    }

                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(adMatter.getAddrUrl()));
                    startActivity(intent);
                }
            }
        });

        mVideoView.setOnQuestionOutListener(new IPolyvOnQuestionOutListener2() {
            @Override
            public void onOut(@NonNull PolyvQuestionVO questionVO) {
                switch (questionVO.getType()) {
                    case PolyvQuestionVO.TYPE_QUESTION:
//                        questionView.show(questionVO);
                        break;

                    case PolyvQuestionVO.TYPE_AUDITION:
//                        auditionView.show(questionVO);
                        break;
                }
            }
        });

        mVideoView.setOnTeaserOutListener(new IPolyvOnTeaserOutListener() {
            @Override
            public void onOut(@NonNull String url) {
//                auxiliaryView.show(url);
            }
        });

        mVideoView.setOnTeaserCountDownListener(new IPolyvOnTeaserCountDownListener() {
            @Override
            public void onEnd() {
//                auxiliaryView.hide();
            }
        });

        mVideoView.setOnQuestionAnswerTipsListener(new IPolyvOnQuestionAnswerTipsListener() {

            @Override
            public void onTips(@NonNull String msg) {
//                questionView.showAnswerTips(msg);
            }
        });

        mVideoView.setOnCompletionListener(new IPolyvOnCompletionListener2() {
            @Override
            public void onCompletion() {
//                danmuFragment.pause();
            }
        });

        mVideoView.setOnVideoSRTListener(new IPolyvOnVideoSRTListener() {
            @Override
            public void onVideoSRT(@Nullable PolyvSRTItemVO subTitleItem) {
                if (subTitleItem == null) {
                    mSrtTextView.setText("");
                } else {
                    mSrtTextView.setText(subTitleItem.getSubTitle());
                }

                mSrtTextView.setVisibility(View.VISIBLE);
            }
        });

        mVideoView.setOnGestureLeftUpListener(new IPolyvOnGestureLeftUpListener() {

            @Override
            public void callback(boolean start, boolean end) {
                Log.d(TAG, String.format("LeftUp %b %b brightness %d", start, end, mVideoView.getBrightness((Activity) ctx)));
                int brightness = mVideoView.getBrightness((Activity) ctx) + 5;
                if (brightness > 100) {
                    brightness = 100;
                }

                mVideoView.setBrightness((Activity) ctx, brightness);
                mLightView.setViewLightValue(brightness, end);
            }
        });

        mVideoView.setOnGestureLeftDownListener(new IPolyvOnGestureLeftDownListener() {

            @Override
            public void callback(boolean start, boolean end) {
                Log.d(TAG, String.format("LeftDown %b %b brightness %d", start, end, mVideoView.getBrightness((Activity) ctx)));
                int brightness = mVideoView.getBrightness((Activity) ctx) - 5;
                if (brightness < 0) {
                    brightness = 0;
                }

                mVideoView.setBrightness((Activity) ctx, brightness);
                mLightView.setViewLightValue(brightness, end);
            }
        });

        mVideoView.setOnGestureRightUpListener(new IPolyvOnGestureRightUpListener() {

            @Override
            public void callback(boolean start, boolean end) {
                Log.d(TAG, String.format("RightUp %b %b volume %d", start, end, mVideoView.getVolume()));
                // 加减单位最小为10，否则无效果
                int volume = mVideoView.getVolume() + 10;
                if (volume > 100) {
                    volume = 100;
                }

                mVideoView.setVolume(volume);
                mVolumeView.setViewVolumeValue(volume, end);
            }
        });

        mVideoView.setOnGestureRightDownListener(new IPolyvOnGestureRightDownListener() {

            @Override
            public void callback(boolean start, boolean end) {
                Log.d(TAG, String.format("RightDown %b %b volume %d", start, end, mVideoView.getVolume()));
                // 加减单位最小为10，否则无效果
                int volume = mVideoView.getVolume() - 10;
                if (volume < 0) {
                    volume = 0;
                }

                mVideoView.setVolume(volume);
                mVolumeView.setViewVolumeValue(volume, end);
            }
        });

        mVideoView.setOnGestureSwipeLeftListener(new IPolyvOnGestureSwipeLeftListener() {

            @Override
            public void callback(boolean start, boolean end) {
                // 左滑事件
                Log.d(TAG, String.format("SwipeLeft %b %b", start, end));
                if (fastForwardPos == 0) {
                    fastForwardPos = mVideoView.getCurrentPosition();
                }

                if (end) {
                    if (fastForwardPos < 0)
                        fastForwardPos = 0;
                    mVideoView.seekTo(fastForwardPos);
//                    danmuFragment.seekTo();
                    if (mVideoView.isCompletedState()) {
                        mVideoView.start();
//                        danmuFragment.resume();
                    }
                    fastForwardPos = 0;
                } else {
                    fastForwardPos -= 10000;
                    if (fastForwardPos <= 0)
                        fastForwardPos = -1;
                }
                mProgressView.setViewProgressValue(fastForwardPos, mVideoView.getDuration(), end, false);
            }
        });

        mVideoView.setOnGestureSwipeRightListener(new IPolyvOnGestureSwipeRightListener() {

            @Override
            public void callback(boolean start, boolean end) {
                // 右滑事件
                Log.d(TAG, String.format("SwipeRight %b %b", start, end));
                if (fastForwardPos == 0) {
                    fastForwardPos = mVideoView.getCurrentPosition();
                }

                if (end) {
                    if (fastForwardPos > mVideoView.getDuration())
                        fastForwardPos = mVideoView.getDuration();
                    mVideoView.seekTo(fastForwardPos);
//                    danmuFragment.seekTo();
                    if (mVideoView.isCompletedState()) {
                        mVideoView.start();
//                        danmuFragment.resume();
                    }
                    fastForwardPos = 0;
                } else {
                    fastForwardPos += 10000;
                    if (fastForwardPos > mVideoView.getDuration())
                        fastForwardPos = mVideoView.getDuration();
                }
                mProgressView.setViewProgressValue(fastForwardPos, mVideoView.getDuration(), end, true);
            }
        });

        mVideoView.setOnGestureClickListener(new IPolyvOnGestureClickListener() {
            @Override
            public void callback(boolean start, boolean end) {
                if (mVideoView.isInPlaybackState() && mMediaController != null)
                    if (mMediaController.isShowing())
                        mMediaController.hide();
                    else
                        mMediaController.show();
            }
        });
        handler.postDelayed(runnable, 2000);
        Log.e("OPWWWW111", mMediaController.getRecordTime() + mMediaController.getTotalTime());
        mMediaController.changeToPortrait();
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            // TODO Auto-generated method stub
            //要做的事情
            try {
                int position = mVideoView.getCurrentPosition();
                int totalTime = mVideoView.getDuration() / 1000 * 1000;
                if (totalTime != 0) {
                    mTimePercentage = ToolUtils.FormatTwoPoint(String.valueOf((Float.valueOf(position) / Float.valueOf(totalTime))));
                }

                handler.postDelayed(this, 2000);


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    public void courseDetial() {
        SanJiDeLeiBiaoAdapter.setOnItemVideoPlayListener(this);
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

                } else if (result.getStatus_code().equals("401")) {
                    GloableConstant.getInstance().clearAll();
                    ToastUtil.showShortToast("请重新登录");
                    startActivity(new Intent(ctx, LoginActivity.class));
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

    @OnClick({R.id.ll_download, R.id.ll_ecalendar, R.id.ll_classgroup})
    public void onViewClicked(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.ll_download://下载
                intent = new Intent(ctx, CourseDownFileActivity.class);
                intent.putExtra("type",mType);
                intent.putExtra("covermap",covermap);
                intent.putExtra("course_id",mCourseId);
                intent.putExtra("name",mName);
                startActivity(intent);
                break;
            case R.id.ll_ecalendar://学习日历
                intent = new Intent();
                intent.putExtra("class_id", mClassId);
                intent.putExtra("type", mType);
                intent.setClass(ctx, StudyCalendarActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_classgroup://班级群
                joinQQGroup(qq);
                break;
        }
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
    public void startPlay(String path,String title) {
        Log.i("wch", path);
        toolBar.setTitle(title);
//        play(path, 1, true, true);
        rlPic.setVisibility(View.GONE);
        mVideoView.setVid(path);
    }
    @OnClick(R.id.ll_play)
    public void onViewClicked() {
        rlPic.setVisibility(View.GONE);

//        play("1ff1d468997ddc9e28e738ee802ba5bc_1", 2, true, true);

        mVideoView.setVid(last_learnVid);

    }
    /**
     * 播放视频
     *
     * @param vid             视频id
     * @param bitrate         码率（清晰度）
     * @param startNow        是否现在开始播放视频
     * @param isMustFromLocal 是否必须从本地（本地缓存的视频）播放
     */
    public void play(final String vid, final int bitrate, boolean startNow, final boolean isMustFromLocal) {
        PolyvDownloader downloader = PolyvDownloaderManager.getPolyvDownloader(vid, bitrate);

        if (TextUtils.isEmpty(vid)) return;
        if (mIvVlmsCover != null && mIvVlmsCover.getVisibility() == View.VISIBLE)
            mIvVlmsCover.setVisibility(View.GONE);

        mVideoView.release();
        mSrtTextView.setVisibility(View.GONE);
        mMediaController.hide();
        loadingProgress.setVisibility(View.GONE);
//        questionView.hide();
//        auditionView.hide();
//        auxiliaryVideoView.hide();
        auxiliaryLoadingProgress.setVisibility(View.GONE);
//        auxiliaryView.hide();
//        advertCountDown.setVisibility(View.GONE);
        firstStartView.hide();

//        danmuFragment.setVid(vid,mVideoView);
        if (startNow) {
            //调用setVid方法视频会自动播放
            mVideoView.setVid(vid, bitrate, isMustFromLocal);
        } else {
            //视频不播放，先显示一张缩略图
            firstStartView.setCallback(new PolyvPlayerPreviewView.Callback() {

                @Override
                public void onClickStart() {
                    //调用setVid方法视频会自动播放
                    mVideoView.setVid(vid, bitrate, isMustFromLocal);
                }
            });

            firstStartView.show(vid);
        }
    }

    private void clearGestureInfo() {
        mVideoView.clearGestureInfo();
        mProgressView.hide();
        mVolumeView.hide();
        mLightView.hide();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //回来后继续播放
        if (isPlay) {
            mVideoView.onActivityResume();
//            danmuFragment.resume();
//            if (auxiliaryView.isPauseAdvert()) {
//                auxiliaryView.hide();
//            }
        }
        mMediaController.resume();
        MobclickAgent.onPageStart("录播");
        MobclickAgent.onResume(this);
    }

    @Override
    public void finish() {
        super.finish();
//        addLearnRecord(mTimePercentage);
    }

    @Override
    protected void onPause() {
        super.onPause();
        clearGestureInfo();
        mMediaController.pause();
        MobclickAgent.onPageEnd("录播");
        MobclickAgent.onPause(this);
    }

    private boolean isPlay = false;

    @Override
    protected void onStop() {
        super.onStop();
        //弹出去暂停
        isPlay = mVideoView.onActivityStop();
//        danmuFragment.pause();
    }

    Handler handler = new Handler();
    private String mTimePercentage = "";

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mVideoView.destroy();
//        questionView.hide();
//        auditionView.hide();
//        auxiliaryView.hide();
        firstStartView.hide();
        mMediaController.disable();
        handler.removeCallbacks(runnable);
        Log.e("IIIIII", "OPOPOPOwww");
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            //判断横屏变竖屏
          /*  if (PolyvScreenUtils.isLandscape(this) && mMediaController != null) {
                mMediaController.changeToPortrait();
                return true;
            }*/
           /* if (viewPagerFragment != null && PolyvScreenUtils.isPortrait(this) && viewPagerFragment.isSideIconVisible()) {
                viewPagerFragment.setSideIconVisible(false);
                return true;
            }*/
        }

        return super.onKeyDown(keyCode, event);
    }

    public static Intent newIntent(Context context, RecordingActivity.PlayMode playMode, String vid) {
        return newIntent(context, playMode, vid, PolyvBitRate.ziDong.getNum());
    }

    public static Intent newIntent(Context context, RecordingActivity.PlayMode playMode, String vid, int bitrate) {
        return newIntent(context, playMode, vid, bitrate, false);
    }

    public static Intent newIntent(Context context, RecordingActivity.PlayMode playMode, String vid, int bitrate, boolean startNow) {
        return newIntent(context, playMode, vid, bitrate, startNow, false);
    }

    public static Intent newIntent(Context context, RecordingActivity.PlayMode playMode, String vid, int bitrate, boolean startNow,
                                   boolean isMustFromLocal) {
        Intent intent = new Intent(context, RecordingActivity.class);
        intent.putExtra("playMode", playMode.getCode());
        intent.putExtra("value", vid);
        intent.putExtra("bitrate", bitrate);
        intent.putExtra("startNow", startNow);
        Log.e("********1111vid", vid);
        intent.putExtra("isMustFromLocal", isMustFromLocal);
        return intent;
    }

    public static void intentTo(Context context, RecordingActivity.PlayMode playMode, String vid) {
        intentTo(context, playMode, vid, PolyvBitRate.ziDong.getNum());
    }

    public static void intentTo(Context context, RecordingActivity.PlayMode playMode, String vid, int bitrate) {
        intentTo(context, playMode, vid, bitrate, false);
    }

    public static void intentTo(Context context, RecordingActivity.PlayMode playMode, String vid, int bitrate, boolean startNow) {
        intentTo(context, playMode, vid, bitrate, startNow, false);
    }

    public static void intentTo(Context context, RecordingActivity.PlayMode playMode, String vid, int bitrate, boolean startNow,
                                boolean isMustFromLocal) {
        context.startActivity(newIntent(context, playMode, vid, bitrate, startNow, isMustFromLocal));
    }



    /**
     * 播放模式
     *
     * @author TanQu
     */
    public enum PlayMode {
        /**
         * 横屏
         */
        landScape(3),
        /**
         * 竖屏
         */
        portrait(4);

        private final int code;

        private PlayMode(int code) {
            this.code = code;
        }

        /**
         * 取得类型对应的code
         *
         * @return
         */
        public int getCode() {
            return code;
        }

        public static PlayMode getPlayMode(int code) {
            switch (code) {
                case 3:
                    return landScape;
                case 4:
                    return landScape;
                //无论 3 或者4 都设置横屏
//                    return portrait;
            }

            return null;
        }
    }

}
