package com.jiaoshizige.teacherexam.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.easefun.polyvsdk.PolyvBitRate;
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
import com.umeng.analytics.MobclickAgent;
import com.jiaoshizige.teacherexam.R;
import com.jiaoshizige.teacherexam.base.BaseActivity;
import com.jiaoshizige.teacherexam.http.ApiUrl;
import com.jiaoshizige.teacherexam.http.MyCallBack;
import com.jiaoshizige.teacherexam.http.SupportResponse;
import com.jiaoshizige.teacherexam.http.Xutil;
import com.jiaoshizige.teacherexam.model.AddLearnRecordModel;
import com.jiaoshizige.teacherexam.palyer.PolyvDownloadInfo;
import com.jiaoshizige.teacherexam.palyer.PolyvDownloadSQLiteHelper;
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

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/1/3.
 * 录播
 */

public class RecordingActivity extends BaseActivity {
    private static final String TAG = RecordingActivity.class.getSimpleName();
    public static final String IS_VLMS_ONLINE = "isVlmsOnline";
    @BindView(R.id.view_layout)
    RelativeLayout mViewLayout;//播放器的parentView
    @BindView(R.id.polyv_video_view)
    PolyvVideoView mVideoView;//播放主视频播放器
    @BindView(R.id.srt)
    TextView mSrtTextView;//字幕
    @BindView(R.id.polyv_player_light_view)
    PolyvPlayerLightView mLightView;//手势出现的亮度界面
    @BindView(R.id.polyv_player_volume_view)
    PolyvPlayerVolumeView mVolumeView;//手势出现的音量界面
    @BindView(R.id.polyv_player_progress_view)
    PolyvPlayerProgressView mProgressView;//手势出现的进度界面
    @BindView(R.id.polyv_player_media_controller)
    PolyvPlayerMediaController mMediaController;//视频控制栏
    @BindView(R.id.loading_progress)
    ProgressBar loadingProgress;//视频加载缓冲视图
    //    @BindView(R.id.polyv_player_question_view)//普通问答界面
//    PolyvPlayerQuestionView questionView;
//PolyvPlayerAuditionView auditionView//语音问答界面
//PolyvAuxiliaryVideoView auxiliaryVideoView//用于播放广告片头的播放器
    @BindView(R.id.auxiliary_loading_progress)
    ProgressBar auxiliaryLoadingProgress;//视频广告，视频片头加载缓冲视图
    //    @BindView(R.id.polyv_player_auxiliary_view)
//    PolyvPlayerAuxiliaryView auxiliaryView//图片广告界面
    @BindView(R.id.polyv_player_first_start_view)
    PolyvPlayerPreviewView firstStartView;
    @BindView(R.id.iv_vlms_cover)
    ImageView mIvVlmsCover;
    private int fastForwardPos = 0;
    private boolean isPlay = false;
    private int isLearnTime = 0;
    private int isAdd = 0;
    private AddLearnRecordModel mModel = new AddLearnRecordModel();
    Handler handler = new Handler();
    String mVid = "";
    private String mNoCourseId = "";
    private String mTimePercentage = "";

    @Override
    protected int getLayoutId() {
        return R.layout.recording_layout;
    }

    @Override
    protected void initView() {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);//强制横屏
        PolyvScreenUtils.setLandscape(this);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
//        Settings.System.putInt(this.getContentResolver(), Settings.System.ACCELEROMETER_ROTATION, 0);
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
        mMediaController.changeToLandscape();
//////////////////////////////////////////////////////
        mVid = getIntent().getStringExtra("value");
        int bitrate = getIntent().getIntExtra("bitrate", PolyvBitRate.ziDong.getNum());
        boolean startNow = getIntent().getBooleanExtra("startNow", false);
        boolean isMustFromLocal = getIntent().getBooleanExtra("isMustFromLocal", false);
//        play(vid, bitrate, startNow, isMustFromLocal);
//        play("c538856dde2600e0096215c16592d4d3_c", 1, false, true);
        if (getIntent().getExtras().get("no_course_id") != null) {
            mNoCourseId = (String) getIntent().getExtras().get("no_course_id");
            if (mNoCourseId.equals("1")) {
                mMediaController.setmAddNoteGone();
            }
        }
        if (getIntent().getExtras().get("model") != null) {

            mModel = (AddLearnRecordModel) getIntent().getExtras().get("model");
            isLearnTime = Integer.valueOf(mModel.getLearn_time()) * 60 * 1000;//时间
//            isLearnTime = 0;//有时间用时间
            Log.e("TTT", mModel.getCourse_name() + isLearnTime);
        } else {
            if (getIntent().getExtras().get("live_url") == null) {
                isLearnTime = Integer.valueOf(PolyvDownloadSQLiteHelper.getInstance(this).getInfo(mVid).getLearn_time());
            }
//            isLearnTime = 0;//下载处进入没有model 用数据库中的数据
        }
        if (getIntent().getExtras().get("live_url") != null) {
            mVid = (String) getIntent().getExtras().get("live_url");
            Log.e("TAGs", (String) getIntent().getExtras().get("live_url"));
        }
        if (getIntent().getExtras().get("live_url") != null) {
            play(mVid, 1, false, false);
        } else {
            play(mVid, 1, false, true);
        }
        if (getIntent().getExtras().get("flag") != null) {
            if (getIntent().getExtras().get("flag").equals("1")) {
                mMediaController.setbitVisible();
                play(mVid, 1, true, false);
            }
        }
        Log.e("**********3333vid", mVid + "////////");
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
                AlertDialog.Builder builder = new AlertDialog.Builder(RecordingActivity.this);
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
                Toast.makeText(RecordingActivity.this, "当前视频无法播放，请向管理员反馈(error code " + PolyvPlayErrorReason.VIDEO_ERROR + ")", Toast.LENGTH_SHORT).show();
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
                Log.d(TAG, String.format("LeftUp %b %b brightness %d", start, end, mVideoView.getBrightness(RecordingActivity.this)));
                int brightness = mVideoView.getBrightness(RecordingActivity.this) + 5;
                if (brightness > 100) {
                    brightness = 100;
                }

                mVideoView.setBrightness(RecordingActivity.this, brightness);
                mLightView.setViewLightValue(brightness, end);
            }
        });

        mVideoView.setOnGestureLeftDownListener(new IPolyvOnGestureLeftDownListener() {

            @Override
            public void callback(boolean start, boolean end) {
                Log.d(TAG, String.format("LeftDown %b %b brightness %d", start, end, mVideoView.getBrightness(RecordingActivity.this)));
                int brightness = mVideoView.getBrightness(RecordingActivity.this) - 5;
                if (brightness < 0) {
                    brightness = 0;
                }

                mVideoView.setBrightness(RecordingActivity.this, brightness);
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

    /**
     * 播放视频
     *
     * @param vid             视频id
     * @param bitrate         码率（清晰度）
     * @param startNow        是否现在开始播放视频
     * @param isMustFromLocal 是否必须从本地（本地缓存的视频）播放
     */
    public void play(final String vid, final int bitrate, boolean startNow, final boolean isMustFromLocal) {
        PolyvScreenUtils.setLandscape(this);
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (viewPagerFragment != null)
//            viewPagerFragment.getTalkFragment().onActivityResult(requestCode, resultCode, data);
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
        addLearnRecord(mTimePercentage);
    }

    @Override
    protected void onPause() {
        super.onPause();
        clearGestureInfo();
        mMediaController.pause();
        MobclickAgent.onPageEnd("录播");
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        //弹出去暂停
        isPlay = mVideoView.onActivityStop();
//        danmuFragment.pause();
    }

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

    public static Intent newIntent(Context context, PlayMode playMode, String vid) {
        return newIntent(context, playMode, vid, PolyvBitRate.ziDong.getNum());
    }

    public static Intent newIntent(Context context, PlayMode playMode, String vid, int bitrate) {
        return newIntent(context, playMode, vid, bitrate, false);
    }

    public static Intent newIntent(Context context, PlayMode playMode, String vid, int bitrate, boolean startNow) {
        return newIntent(context, playMode, vid, bitrate, startNow, false);
    }

    public static Intent newIntent(Context context, PlayMode playMode, String vid, int bitrate, boolean startNow,
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

    public static void intentTo(Context context, PlayMode playMode, String vid) {
        intentTo(context, playMode, vid, PolyvBitRate.ziDong.getNum());
    }

    public static void intentTo(Context context, PlayMode playMode, String vid, int bitrate) {
        intentTo(context, playMode, vid, bitrate, false);
    }

    public static void intentTo(Context context, PlayMode playMode, String vid, int bitrate, boolean startNow) {
        intentTo(context, playMode, vid, bitrate, startNow, false);
    }

    public static void intentTo(Context context, PlayMode playMode, String vid, int bitrate, boolean startNow,
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
                    return landScape;//无论 3 或者4 都设置横屏
//                    return portrait;
            }

            return null;
        }
    }

    /**
     *
     */
    public void addLearnRecord(String time) {
        Map<String, Object> map = new HashMap<>();
//        map.put("user_id","1");
        PolyvDownloadInfo model = new PolyvDownloadInfo();
        model = PolyvDownloadSQLiteHelper.getInstance(this).getInfo(mVid);

        if (model != null) {
            map.put("user_id", SPUtils.getSpValues(SPKeyValuesUtils.SP_USER_ID, SPUtils.TYPE_STRING));
            map.put("type", "1");
            if (model.getType().equals("2")) {
                map.put("group_id", model.getCourse_id());
            } else {
                map.put("group_id", model.getClass_id());
            }
            map.put("course_id", model.getCourse_id());
            map.put("course_name", model.getClassname());
            map.put("chapter_id", model.getChapter_id());//章id
            map.put("chapter_name", model.getChapter());//章名称
            map.put("section_id", model.getSection_id());//节id
            map.put("section_name", model.getSection_name());//节名称
            map.put("type_id", model.getType());//1班级2课程
            map.put("ltime", time);//0-1之间百分比
            Log.e("Tag1", map.toString());
        } else {
            if (mModel != null) {
                map.put("user_id", SPUtils.getSpValues(SPKeyValuesUtils.SP_USER_ID, SPUtils.TYPE_STRING));
                map.put("type", "1");
                map.put("group_id", mModel.getClass_id());
                map.put("course_id", mModel.getCourse_id());
                map.put("course_name", mModel.getClassname());
                map.put("chapter_id", mModel.getChapter_id());//章id
                map.put("chapter_name", mModel.getChapter_name());//章名称
                map.put("section_id", mModel.getSection_id());//节id
                map.put("section_name", mModel.getSection_name());//节名称
                map.put("type_id", mModel.getType());//1班级2课程
                map.put("ltime", time);//0-1之间百分比
                Log.e("Tag2", map.toString());
            } else {
                Log.e("III", "GG");
            }

        }


//        map.put("learn_time",mModel.getLearn_time());////学习完成时间：分钟
        Xutil.Post(ApiUrl.AddLEARNRECORD, map, new MyCallBack<SupportResponse>() {
            @Override
            public void onSuccess(SupportResponse result) {
                super.onSuccess(result);
//                handler.removeCallbacks(runnable);
                if (result.getStatus_code().equals("204")) {
                    //成功
                    isAdd = 0;
                } else if (result.getStatus_code().equals("205")) {
                    //重复提交
                    isAdd = 0;
                } else if (result.getStatus_code().equals("206")) {
                    //提交失败
                    isAdd = 1;
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
                handler.removeCallbacks(runnable);
            }
        });
    }

    public void toAddNote() {
        ToastUtil.showShortToast("笔记");
        Intent intent = new Intent();
        intent.setClass(RecordingActivity.this, AddNoteActivity.class);
        intent.putExtra("course_id", mModel.getGroup_id());
//        intent.putExtra("section_id",mModel.getSection_id());
        intent.putExtra("section_id", "1");
        intent.putExtra("type", mModel.getType());
        intent.putExtra("vid", mVid);
        startActivity(intent);
    }
}
