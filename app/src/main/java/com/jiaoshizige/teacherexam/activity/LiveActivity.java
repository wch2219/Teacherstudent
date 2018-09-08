package com.jiaoshizige.teacherexam.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alivc.player.AliVcMediaPlayer;
import com.alivc.player.MediaPlayer;
import com.bumptech.glide.Glide;
import com.jiaoshizige.teacherexam.R;
import com.jiaoshizige.teacherexam.adapter.ChatListAdapter;
import com.jiaoshizige.teacherexam.base.BaseActivity;
import com.jiaoshizige.teacherexam.base.GloableConstant;
import com.jiaoshizige.teacherexam.constant.AppConstant;
import com.jiaoshizige.teacherexam.http.ApiUrl;
import com.jiaoshizige.teacherexam.http.MyCallBack;
import com.jiaoshizige.teacherexam.http.SupportResponse;
import com.jiaoshizige.teacherexam.http.Xutil;
import com.jiaoshizige.teacherexam.model.AddLearnRecordModel;
import com.jiaoshizige.teacherexam.model.GiftListResponse;
import com.jiaoshizige.teacherexam.model.OnLineResponse;
import com.jiaoshizige.teacherexam.model.OpenClassDetailResponse;
import com.jiaoshizige.teacherexam.model.SendGiftResponse;
import com.jiaoshizige.teacherexam.utils.SPKeyValuesUtils;
import com.jiaoshizige.teacherexam.utils.SPUtils;
import com.jiaoshizige.teacherexam.utils.ShareUtils;
import com.jiaoshizige.teacherexam.utils.Sphelper;
import com.jiaoshizige.teacherexam.utils.ToastUtil;
import com.jiaoshizige.teacherexam.utils.ToolUtils;
import com.jiaoshizige.teacherexam.widgets.ChatListView;
import com.jiaoshizige.teacherexam.widgets.EmojiBoard;
import com.jiaoshizige.teacherexam.widgets.GiftMessage;
import com.jiaoshizige.teacherexam.widgets.LiveKit;
import com.jiaoshizige.teacherexam.widgets.OnSelectItemListener;
import com.jiaoshizige.teacherexam.widgets.SharePopWindow;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.bean.SHARE_MEDIA;

import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.MessageContent;
import io.rong.imlib.model.UserInfo;
import io.rong.message.TextMessage;

/**
 * Created by Administrator on 2017/12/21.
 * 直播
 */
public class LiveActivity extends BaseActivity implements Handler.Callback {
    @BindView(R.id.live_back)
    ImageView mLiveBack;
    @BindView(R.id.live_gift_switch)
    ImageView mLiveGiftSwitch;
    @BindView(R.id.live_share)
    ImageView mLiveShare;
    //    @BindView(R.id.live_switch)
//    TextView mLiveSwitch;
    @BindView(R.id.live_screen)
    TextView mLiveScreen;
    @BindView(R.id.chat_content)
    EditText mChatContent;
    @BindView(R.id.chat_send)
    TextView mChartSend;
    @BindView(R.id.send_gift)
    TextView mSendGift;
    @BindView(R.id.cross_live_switch)
    TextView mCrossLiveSwitch;//横屏切换开关
    @BindView(R.id.show_chat_switch)
    TextView mShowChatSwitch;//横屏开关聊天
    @BindView(R.id.surfaceView)
    SurfaceView mSurfaceView;
    @BindView(R.id.surfaceView_content)
    LinearLayout mSurfaseViewContentLy;
    @BindView(R.id.bottom_chat)
    LinearLayout mBottonChat;
    @BindView(R.id.gift_ly)//送礼的layout
            LinearLayout mGiftLy;
    @BindView(R.id.gift_empty)//中间的空白ly
            LinearLayout mGiftEmpty;
    @BindView(R.id.recharge)
    TextView mRecharge;
    @BindView(R.id.icon_num)
    TextView mCionNum;
    @BindView(R.id.send)
    TextView mSendBtn;
    @BindView(R.id.gift_recycle)
    RecyclerView mGiftRecycle;
    @BindView(R.id.chat_listview)
    ChatListView mCharListView;
    @BindView(R.id.input_emoji_btn)
    ImageView mInputEmojiBtn;
    @BindView(R.id.input_bar)
    LinearLayout mInputBar;
    @BindView(R.id.input_emoji_board)
    EmojiBoard mEmojiBoard;
    @BindView(R.id.ly_ceng)
    LinearLayout mLyCeng;

    private int mSwitch = 0;
    private int mHvFlag = 0;//横竖屏标记操作
    private List<String> logStrs = new ArrayList<>();
    private SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss.SS");
    private AliVcMediaPlayer mPlayer;//播放
    private boolean isCompleted = false;
    private int mClickPosition = -1;
    private int mIcon = 0;//暂时默认为0
    private int mGiftPrice;
    private String mGiftName;
    private String mGiftImg;
    private String mGiftId;
    //    private String mUrl = "rtmp://live.hkstv.hk.lxdns.com/live/hks";
    private String mUrl;
    private String mToken;
    private String mRoomId;
    private Handler handler = new Handler(this);
    //    private static Handler mLiveHandler = new Handler();
    private int isLearnTime = 0;
    private int isAdd = 0;
    Handler handlerTime=new Handler();
    private AddLearnRecordModel mModel = new AddLearnRecordModel();
    private ChatListAdapter mChatListAdapter;
    private String _id;
    private String mPullUrl;
    private String mLiveName;
    private String mLiveContent;
    private String mLiveImage;
    private String chatFalg = "0";
    private String nLiveName;
    private List<String> mGiftIdList = new ArrayList();
    private List<String> mGiftNameList = new ArrayList();
    LiveOnLineThred onLineThred = new LiveOnLineThred();
    private int handlerFlag = 0;
    @Override
    protected int getLayoutId() {
        return R.layout.live_activity;
    }

    @Override
    protected void initView() {
        if (getIntent().getStringExtra("live_url")!=null){
            mUrl=getIntent().getStringExtra("live_url");
        }
        if (getIntent().getStringExtra("url")!=null){
            mUrl=getIntent().getStringExtra("url");
        }
        if(getIntent().getExtras().get("model") !=null){
            mModel = (AddLearnRecordModel) getIntent().getExtras().get("model");
            isLearnTime = Integer.valueOf(mModel.getLearn_time()) * 60 * 1000;

            Log.e("**********isLearnTime",isLearnTime+"///////");
        }
        if (getIntent().getStringExtra("token")!=null){
            mToken=getIntent().getStringExtra("token");
        }else{
            mToken = "";
        }
        if (getIntent().getStringExtra("chat_room_id")!=null){
            mRoomId=getIntent().getStringExtra("chat_room_id");
        }else{
            mRoomId = "";
        }
        Log.e("**********chat_room_id",mRoomId+"**************"+getIntent().getStringExtra("chat_room_id")+"////");
        if (getIntent().getStringExtra("_id")!=null){
            _id=getIntent().getStringExtra("_id");
            openClassDetail();
        }
        if(getIntent().getExtras().get("name") != null){
            nLiveName = (String) getIntent().getExtras().get("name");
        }
        GloableConstant.getInstance().startProgressDialog(this);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        mSurfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            public void surfaceCreated(SurfaceHolder holder) {
                holder.setType(SurfaceHolder.SURFACE_TYPE_GPU);
                holder.setKeepScreenOn(true);
                Log.d("TAG", "AlivcPlayer onSurfaceCreated." + mPlayer);

                // Important: surfaceView changed from background to front, we need reset surface to mediaplayer.
                // 对于从后台切换到前台,需要重设surface;部分手机锁屏也会做前后台切换的处理
                if (mPlayer != null) {
                    mPlayer.setVideoSurface(mSurfaceView.getHolder().getSurface());
                    mCrossLiveSwitch.setVisibility(View.GONE);
                }
                Log.d("TAG", "AlivcPlayeron SurfaceCreated over.");
            }

            public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
                Log.d("TAG", "onSurfaceChanged is valid ? " + holder.getSurface().isValid());
                if (mPlayer != null)
                    mPlayer.setSurfaceChanged();
            }

            public void surfaceDestroyed(SurfaceHolder holder) {
                Log.d("TAG", "onSurfaceDestroy.");
            }
        });
        /*int height = ToolUtils.getHeight(this);
        mCharListView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,ToolUtils.px2dip(this,(height-300))));*/
        mChatContent.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
//                mInputBar.setSelected(hasFocus);
                mEmojiBoard.setVisibility(View.GONE);
                mInputEmojiBtn.setSelected(mEmojiBoard.getVisibility() == View.VISIBLE);
                if(mGiftLy.getVisibility() == View.VISIBLE){
                    mGiftLy.setVisibility(View.GONE);
                }
                Log.e("PPPPO","Focus");
            }
        });
        mChatContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mEmojiBoard.getVisibility() == View.VISIBLE){
                    mEmojiBoard.setVisibility(View.GONE);
                    mInputEmojiBtn.setSelected(false);
                    if(mGiftLy.getVisibility() == View.VISIBLE){
                        mGiftLy.setVisibility(View.GONE);
                    }
                    Log.e("PPPPO","ckick");
                }
            }
        });
        mEmojiBoard.setItemClickListener(new EmojiBoard.OnEmojiItemClickListener() {
            @Override
            public void onClick(String code) {
                if (code.equals("/DEL")) {
                    mChatContent.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL));
                } else {
                    mChatContent.getText().insert(mChatContent.getSelectionStart(), code);
                }
            }
        });
        mChatListAdapter = new ChatListAdapter(this);
        mCharListView.setAdapter(mChatListAdapter);
        LiveKit.addEventHandler(handler);
        initVodPlayer();
        reuqestGiftList();
        login();
        replay();
       if (getIntent().getExtras().get("model") !=null){
           handler.postDelayed(runnable,isLearnTime);//直播在看到规定时间的时候 调用线程 添加学习日历
       }
//        mLiveHandler.postDelayed(Liverunnable,5000);//计时线程 五秒请求下视频是否在线 (时间可自定义)
        onLineThred.start();
    }
    @OnClick({R.id.live_back,R.id.live_gift_switch,R.id.live_share,R.id.live_screen,R.id.chat_send,R.id.send_gift,R.id.gift_empty,R.id.recharge,R.id.send,R.id.input_emoji_btn,R.id.hide_gif_ly,R.id.ly_ceng})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.live_back:
                if(mHvFlag == 1){
                    updatePlayerViewMode();
                }else{
                    finish();
                }
                break;
            case R.id.live_gift_switch:
                if(mSwitch == 1){
                    mLiveGiftSwitch.setBackground(ContextCompat.getDrawable(LiveActivity.this,R.mipmap.live_iocn_gift_default));
                    mSwitch = 0;
                }else{
                    mLiveGiftSwitch.setBackground(ContextCompat.getDrawable(LiveActivity.this,R.mipmap.live_iocn_gift_disabled));
                    mSwitch = 1;
                }
                break;
            case R.id.live_share:
                showSharePopWindow();
                break;
            case R.id.live_screen:
                updatePlayerViewMode();
                break;
            case R.id.chat_send://发送消息
                Log.e("***********",mChatContent+"//////////"+chatFalg);
                if(!mChatContent.getText().toString().trim().equals("") ){
                    if(!chatFalg.equals("1")){
                        TextMessage content = TextMessage.obtain(mChatContent.getText().toString().trim());
                        LiveKit.sendMessage(content);
                        if(mEmojiBoard.getVisibility() == View.VISIBLE){
                            mEmojiBoard.setVisibility(View.GONE);
                            mInputEmojiBtn.setSelected(false);
                        }
                        Log.e("TAG","发送送");

                    }else{
                        ToastUtil.showShortToast("登陆聊天室失败，请重新登录");
                    }
                }
                break;
            case R.id.send_gift:
                mGiftLy.setVisibility(View.VISIBLE);
                break;
            case R.id.gift_empty:
                mGiftLy.setVisibility(View.GONE);
                mEmojiBoard.setVisibility(View.GONE);
                mInputEmojiBtn.setSelected(false);
                break;
            case R.id.recharge:
                Intent intent = new Intent();
                intent.setClass(LiveActivity.this,TeacherCoinActivity.class);
                startActivityForResult(intent,3);
                break;
            case R.id.send://发送礼物
                if(mIcon >= mGiftPrice){
                    if(mGiftId !=null){
                        requestSendGift(mGiftId,"1",mRoomId);
                    }else{
                        ToastUtil.showShortToast("请选择礼物");
                    }
                }else{
                    ToastUtil.showShortToast("教师币余额不足");
                }
                break;
            case R.id.input_emoji_btn:
                mEmojiBoard.setVisibility(mEmojiBoard.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
                mInputEmojiBtn.setSelected(mEmojiBoard.getVisibility() == View.VISIBLE);
                hideKeyboard(getCurrentFocus().getWindowToken());
                break;
            case R.id.hide_gif_ly:
                mGiftLy.setVisibility(View.GONE);
                break;
            case R.id.ly_ceng:
                if(mGiftLy.getVisibility() == View.VISIBLE){
                    mGiftLy.setVisibility(View.GONE);
                }
                break;

        }
    }

    /**
     * 初始化一下播放SDK的操作 (参考阿里demo)
     */
    private void initVodPlayer() {
        mPlayer = new AliVcMediaPlayer(this, mSurfaceView);

        mPlayer.setPreparedListener(new MediaPlayer.MediaPlayerPreparedListener() {
            @Override
            public void onPrepared() {
//                Toast.makeText(LiveActivity.this.getApplicationContext(), "Prepared successfully<", Toast.LENGTH_SHORT).show();
                logStrs.add(format.format(new Date()) + "prepared successfully");

//                showVideoSizeInfo();
            }
        });
        mPlayer.setFrameInfoListener(new MediaPlayer.MediaPlayerFrameInfoListener() {
            @Override
            public void onFrameInfoListener() {
                Map<String, String> debugInfo = mPlayer.getAllDebugInfo();
                long createPts = 0;
                if (debugInfo.get("create_player") != null) {
                    String time = debugInfo.get("create_player");
                    createPts = (long) Double.parseDouble(time);
                    logStrs.add(format.format(new Date(createPts)) + "create a player successfully");
                }
                if (debugInfo.get("open-url") != null) {
                    String time = debugInfo.get("open-url");
                    long openPts = (long) Double.parseDouble(time) + createPts;
                    logStrs.add(format.format(new Date(openPts)) + "open an url successfully");
                }
                if (debugInfo.get("find-stream") != null) {
                    String time = debugInfo.get("find-stream");
                    Log.d("Tag" + "lfj0914", "find-Stream time =" + time + " , createpts = " + createPts);
                    long findPts = (long) Double.parseDouble(time) + createPts;
                    logStrs.add(format.format(new Date(findPts)) + " get a streaming successfully");
                }
                if (debugInfo.get("open-stream") != null) {
                    String time = debugInfo.get("open-stream");
                    Log.d("Tag" + "lfj0914", "open-Stream time =" + time + " , createpts = " + createPts);
                    long openPts = (long) Double.parseDouble(time) + createPts;
                    logStrs.add(format.format(new Date(openPts)) + "start to open a streaming");
                }
                logStrs.add(format.format(new Date()) + "the first frame showed");
            }
        });
        mPlayer.setErrorListener(new MediaPlayer.MediaPlayerErrorListener() {
            @Override
            public void onError(int i, String msg) {
                Toast.makeText(LiveActivity.this.getApplicationContext(), "Fail!! Reason:" + msg, Toast.LENGTH_SHORT).show();
            }
        });
        mPlayer.setCompletedListener(new MediaPlayer.MediaPlayerCompletedListener() {
            @Override
            public void onCompleted() {
                Log.d("TAG", "onCompleted--- ");
                isCompleted = true;
            }
        });
        mPlayer.setSeekCompleteListener(new MediaPlayer.MediaPlayerSeekCompleteListener() {
            @Override
            public void onSeekCompleted() {
                logStrs.add(format.format(new Date()) + "seek operation completed");
            }
        });
        mPlayer.setStoppedListener(new MediaPlayer.MediaPlayerStoppedListener() {
            @Override
            public void onStopped() {
                logStrs.add(format.format(new Date()) + "play operation stopped");
            }
        });
        mPlayer.setBufferingUpdateListener(new MediaPlayer.MediaPlayerBufferingUpdateListener() {
            @Override
            public void onBufferingUpdateListener(int percent) {
                Log.d("TAG", "onBufferingUpdateListener--- " + percent);
            }
        });
        mPlayer.enableNativeLog();

    }
    private void start() {

        if (mPlayer != null) {
            mPlayer.prepareAndPlay(mUrl);
            Log.e("TAG2","start");
            GloableConstant.getInstance().stopProgressDialog();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e("**********onPause","///////onPause");
        if (mPlayer != null) {
            mPlayer.pause();
        }
        MobclickAgent.onPageEnd("直播");
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mPlayer != null){
            mPlayer.resume();
        }
        MobclickAgent.onPageStart("直播");
        MobclickAgent.onResume(this);
    }
    private void stop() {
        if (mPlayer != null) {
            mPlayer.stop();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 3){
            if(resultCode == RESULT_OK){
                if(data.getExtras().get("icon") != null){
                    int nowIcon = Integer.valueOf(mCionNum.getText().toString());
                    int total = nowIcon + Integer.valueOf((String) data.getExtras().get("icon"));
                    mCionNum.setText(String.valueOf(total));
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runnable);
//        mLiveHandler.removeCallbacks(Liverunnable);
        if (mPlayer != null) {
            mPlayer.stop();
            mPlayer.destroy();
        }
        LiveKit.quitChatRoom(new RongIMClient.OperationCallback() {
            @Override
            public void onSuccess() {
                GloableConstant.getInstance().stopProgressDialog();
                LiveKit.removeEventHandler(handler);
                LiveKit.logout();
                Toast.makeText(LiveActivity.this, "退出聊天室成功", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                GloableConstant.getInstance().stopProgressDialog();
                LiveKit.removeEventHandler(handler);
                LiveKit.logout();
                Toast.makeText(LiveActivity.this, "退出聊天室失败! errorCode = " + errorCode, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void finish() {
        super.finish();
        handler.removeCallbacks(runnable);
//        mLiveHandler.removeCallbacks(Liverunnable);
    }

    private void replay() {
        stop();
        start();
    }
    /**
     * 横竖屏 判断 如果横屏则转为竖屏 反之亦然
     */
    private void updatePlayerViewMode() {
        if(mSurfaceView != null){
            int orientation = getResources().getConfiguration().orientation;
            if(orientation == Configuration.ORIENTATION_PORTRAIT){  //竖专横。
                Toast.makeText(LiveActivity.this,"横屏",Toast.LENGTH_SHORT).show();
                mHvFlag = 1;
                mBottonChat.setVisibility(View.GONE);
                mCrossLiveSwitch.setVisibility(View.GONE);
                if(mPlayer != null){
                    mPlayer.setVideoScalingMode(MediaPlayer.VideoScalingMode.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING);
                }
                this.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
//                mSurfaceView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);//显示状态栏，Activity不全屏显示(恢复到有状态的正常情况)。
                mSurfaceView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);//Activity全屏显示，且状态栏被隐藏覆盖掉
                /**
                 * View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN：Activity全屏显示，但状态栏不会被隐藏覆盖，状态栏依然可见，Activity顶端布局部分会被状态遮住。
                 * View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION：效果同View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                 * . View.SYSTEM_UI_LAYOUT_FLAGS：效果同View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                 *  View.SYSTEM_UI_FLAG_HIDE_NAVIGATION：隐藏虚拟按键(导航栏)。有些手机会用虚拟按键来代替物理按键。
                 *  View.SYSTEM_UI_FLAG_LOW_PROFILE：状态栏显示处于低能显示状态(low profile模式)，状态栏上一些图标显示会被隐藏。
                 */
                //设置view的布局，宽高之类
                mSurfaseViewContentLy.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                RelativeLayout.LayoutParams aliVcVideoViewLayoutParams = (RelativeLayout.LayoutParams) mSurfaceView.getLayoutParams();
                aliVcVideoViewLayoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
                aliVcVideoViewLayoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
                mSurfaceView.setLayoutParams(aliVcVideoViewLayoutParams);
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            }else if(orientation == Configuration.ORIENTATION_LANDSCAPE){
                Toast.makeText(LiveActivity.this,"竖屏",Toast.LENGTH_SHORT).show();
                mHvFlag = 0;
                mCrossLiveSwitch.setVisibility(View.GONE);
                mShowChatSwitch.setVisibility(View.GONE);
                mBottonChat.setVisibility(View.VISIBLE);
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                //设置view的布局，宽高
                RelativeLayout.LayoutParams aliVcVideoViewLayoutParams = (RelativeLayout.LayoutParams) mSurfaceView.getLayoutParams();
                aliVcVideoViewLayoutParams.height = ToolUtils.dip2px(LiveActivity.this,250);
                aliVcVideoViewLayoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
                mSurfaceView.setLayoutParams(aliVcVideoViewLayoutParams);
                mSurfaseViewContentLy.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ToolUtils.dip2px(LiveActivity.this,250)));
                showNavigationBar();
            }
        }
    }

    /**
     * 登录  融云登录  后台返回的token 和房间ID
     * 登录的时候设置头像和昵称   没有昵称则为匿名
     */
    private void login(){
//        getWebsiteDatetime("http://www.baidu.com");
        Log.e("TAG","denglu"+mToken+"mRoomId"+mRoomId);
        //"Y2Qt//PPKKiL246SvMi4WuYgiUo62D2ZKEMUDl3/KpVbWcN3AFIjz/ZntlVwMiBNWx5HmUscZoAUyL6JAc0MTQ=="//融云的token 替换为用户的
        LiveKit.connect(mToken, new RongIMClient.ConnectCallback() {
            @Override
            public void onTokenIncorrect() {
                Log.e("TAH", "connect onTokenIncorrect");
                // 检查appKey 与token是否匹配.
            }

            @Override
            public void onSuccess(String userId) {
                Log.e("TAG", "connect onSuccess");
               /* LiveKit.setCurrentUser(user);
                Intent intent = new Intent(LoginActivity.this, LiveShowActivity.class);
                intent.putExtra(LiveShowActivity.LIVE_URL, url);
                startActivity(intent);*/
//                joinChatRoom("ChatRoom01");
//             UserInfo mUser  = new  UserInfo(userId,"融云", Uri.parse("http://rongcloud-web.qiniudn.com/b4612816d27ce158661df0c8b320ea64"));
                String userName;
                String avatar;
                if(Sphelper.getString(LiveActivity.this,"NICKNAME","nick_name") !=null && !Sphelper.getString(LiveActivity.this,"NICKNAME","nick_name").equals("")){
                    userName = Sphelper.getString(LiveActivity.this,"NICKNAME","nick_name");
                }else{
                    userName = "匿名";
                }
                if(Sphelper.getString(LiveActivity.this,"AVATAR","avatar") != null && !Sphelper.getString(LiveActivity.this,"AVATAR","avatar").equals("")){
                    avatar = Sphelper.getString(LiveActivity.this,"AVATAR","avatar");
                }else{
                    avatar="";
                }
                Log.e("YYY",userName+"s"+avatar);
//                Uri.parse("http://rongcloud-web.qiniudn.com/b4612816d27ce158661df0c8b320ea64")
                UserInfo mUser  = new  UserInfo(userId,userName,Uri.parse(avatar));
                LiveKit.setCurrentUser(mUser);
//                joinChatRoom("64654");//直播房间聊天的房间号

                joinChatRoom(mRoomId);
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                Log.e("TAG", "connect onError = " + errorCode);
                ToastUtil.showShortToast("加入聊天室失败");
                chatFalg = "1";
                // 根据errorCode 检查原因.
            }
        });
    }
    /**
     * 加入聊天室
     * @param roomId
     */
    private void joinChatRoom(final String roomId) {
        Log.e("********roomId",roomId);
        LiveKit.joinChatRoom(roomId, 5, new RongIMClient.OperationCallback() {
            @Override
            public void onSuccess() {
                TextMessage content = TextMessage.obtain("加入直播间");
                chatFalg = "0";
                LiveKit.sendMessage(content);
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {

                Toast.makeText(LiveActivity.this, "聊天室加入失败! errorCode = " + errorCode, Toast.LENGTH_SHORT).show();
                Log.e("errorCode",errorCode+"");
            }
        });
    }
    @Override
    public boolean handleMessage(android.os.Message msg) {
        switch (msg.what) {
            case LiveKit.MESSAGE_ARRIVED: {
                MessageContent content = (MessageContent) msg.obj;
          /*      GiftMessage giftMessage = null;
                if(content instanceof GiftMessage){
                    giftMessage = (GiftMessage) content;
                    for (int i = 0; i < mGiftIdList.size();i++){
                        if(giftMessage.getType().equals(mGiftIdList.get(i))){
                            giftMessage.setContent("赠送了礼物"+"“"+mGiftNameList.get(i)+"”");
                        }
                    }
                    Log.e("sd",giftMessage.getContent()+giftMessage.getType());
                    mChatListAdapter.addMessage(giftMessage);
                }else{
                    mChatListAdapter.addMessage(content);
                }*/
                mChatListAdapter.addMessage(content);

                break;
            }
            case LiveKit.MESSAGE_SENT: {
                MessageContent content = (MessageContent) msg.obj;
                mChatListAdapter.addMessage(content);
                if(!mChatContent.getText().toString().trim().equals("")){
                    mChatContent.setText("");
                }
                Log.e("TAG","maosizoule");
                break;
            }
            case LiveKit.MESSAGE_SEND_ERROR: {
                break;
            }
            default:
        }
        mChatListAdapter.notifyDataSetChanged();
        return false;
    }
    /**
     * 礼物列表
     */
    private void reuqestGiftList(){
        Map<String ,Object> map = new HashMap<>();
//        map.put("user_id","1");
        map.put("user_id", (String) SPUtils.getSpValues(SPKeyValuesUtils.SP_USER_ID, SPUtils.TYPE_STRING));
        Xutil.Post(ApiUrl.GIFTLIST,map,new MyCallBack<GiftListResponse>(){
            @Override
            public void onSuccess(final GiftListResponse result) {
                if(result.getData().getGift_list() != null && result.getData().getGift_list().size() > 0){
                    for (int i = 0; i< result.getData().getGift_list().size();i++){
                        mGiftIdList.add(result.getData().getGift_list().get(i).getId());
                        mGiftNameList.add(result.getData().getGift_list().get(i).getGift_name());
                    }
                }
                Log.e("*******result",result.getStatus_code());
                super.onSuccess(result);
                GiftListResponse.mData mData = result.getData();
                mIcon = mData.getIntegral();
                for (int i= 0;i< result.getData().getGift_list().size();i++){
                    mData.getGift_list().get(i).setState("0");
                }
                mCionNum.setText(mData.getIntegral()+"");
                LinearLayoutManager manager = new LinearLayoutManager(LiveActivity.this);
                manager.setOrientation(LinearLayoutManager.HORIZONTAL);
                mGiftRecycle.setLayoutManager(manager);
                mGiftRecycle.setAdapter(new CommonAdapter<GiftListResponse.mGift>(LiveActivity.this,R.layout.item_gift,mData.getGift_list()) {
                    @Override
                    protected void convert(final ViewHolder holder, final GiftListResponse.mGift mGift, final int position) {
                        holder.setText(R.id.gift_name,mGift.getGift_name());
                        holder.setText(R.id.gift_price,mGift.getIntegral()+"教师币");
                        ImageView mGiftImg = (ImageView) holder.getConvertView().findViewById(R.id.gift_img);

                        if(mGift.getState().equals("1")){
                            holder.setTextColor(R.id.gift_name,ContextCompat.getColor(LiveActivity.this,R.color.gif_color));
                            holder.setTextColor(R.id.gift_price,ContextCompat.getColor(LiveActivity.this,R.color.gif_color));
                            holder.setBackgroundRes(R.id.gif_bg,R.mipmap.gif_bg);
                        }else{
                            holder.setTextColor(R.id.gift_name,ContextCompat.getColor(LiveActivity.this,R.color.white));
                            holder.setTextColor(R.id.gift_price,ContextCompat.getColor(LiveActivity.this,R.color.white));
                            holder.setBackgroundRes(R.id.gif_bg,R.mipmap.gif_bg_t);
                        }
                        Log.e("***********iamge",ApiUrl.BASEIMAGE+mGift.getImage());
                        Glide.with(LiveActivity.this).load(ApiUrl.BASEIMAGE+mGift.getImage()).apply(GloableConstant.getInstance().getDefaultOptionssmall()).into(mGiftImg);
                        mGiftImg.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if(mClickPosition != -1)
                                    result.getData().getGift_list().get(mClickPosition).setState("0");
                                mGift.setState("1");
                                mGiftPrice = Integer.valueOf(mGift.getIntegral());
                                mGiftName = mGift.getGift_name();
                                mGiftId = mGift.getId();
                                mClickPosition = position;
//                                holder.setBackgroundRes(R.id.gif_bg,R.mipmap.gif_bg);

                                notifyDataSetChanged();
                            }

                        });
                    }
                });
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
            }
        });
    }
    public List<String> getmGiftIdList(){
        return mGiftIdList;
    }
    public List<String> getmGiftNameList(){
        return mGiftNameList;
    }
    /**
     * 赠送礼物
     */
    private void requestSendGift(final String gift_id, String num, String live_id){
        Map<String,Object> map = new HashMap<>();
//        map.put("user_id","1");
        map.put("user_id", (String) SPUtils.getSpValues(SPKeyValuesUtils.SP_USER_ID, SPUtils.TYPE_STRING));
        map.put("gift_id",gift_id);
        map.put("num",num);
        map.put("live_id",live_id);
        Xutil.Post(ApiUrl.SENDFIFT,map,new MyCallBack<SendGiftResponse>(){
            @Override
            public void onSuccess(SendGiftResponse result) {
                super.onSuccess(result);
                if(result.getStatus_code().equals("204")){
                    GiftMessage msg = new GiftMessage(gift_id,"赠送了礼物"+"“"+ mGiftName+"”");
//                    TextMessage msg = new TextMessage("赠送了礼物"+"“"+ mGiftName+"”");
                    LiveKit.sendMessage(msg);
                    mGiftLy.setVisibility(View.GONE);
                    if(result.getData() != null){
                        if(result.getData().getIntegral() != null)
                            mCionNum.setText(result.getData().getIntegral());
                    }
                }else{
                    ToastUtil.showShortToast(result.getMessage());
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
                ToastUtil.showShortToast(ex.getMessage());
            }
        });
    }
    Runnable runnable=new Runnable() {
        @Override
        public void run() {
            // TODO Auto-generated method stub
            //要做的事情
            try{
                addLearnRecord();
                handler.postDelayed(this, 5000);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    };
    Runnable Liverunnable=new Runnable() {
        @Override
        public void run() {
            // TODO Auto-generated method stub
            //要做的事情
            try{
                isOnLine();
                handler.postDelayed(this, 5000);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    };
    public class LiveOnLineThred extends Thread {
        @Override
        public void run() {
            super.run();
            do {
                try {
                    Thread.sleep(5000);
                    Message msg = new Message();
                    if(handlerFlag == 0){
                        msg.what = 1;
                    }else{
                        msg.what = 2;
                    }

                    onLinehandler.sendMessage(msg);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } while (true);
        }
    }
    Handler onLinehandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 1){
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        isOnLine();
                    }
                }).start();
            }
        }
    };

    public void addLearnRecord(){
        Map<String,Object> map = new HashMap<>();
//        map.put("user_id","1");
        map.put("user_id", SPUtils.getSpValues(SPKeyValuesUtils.SP_USER_ID, SPUtils.TYPE_STRING));
        map.put("type","1");
        map.put("group_id",mModel.getGroup_id());
        map.put("course_id",mModel.getCourse_id());
        map.put("course_name",mModel.getCourse_name());
        map.put("chapter_id",mModel.getChapter_id());//章id
        map.put("chapter_name",mModel.getChapter_name());//章名称
        map.put("section_id",mModel.getSection_id());//节id
        map.put("section_name",mModel.getSection_name());//节名称
        map.put("type_id",mModel.getType());
        Log.e("MAO",map.toString());
        map.put("ltime", "1");
//        map.put("learn_time",mModel.getLearn_time());////学习完成时间：分钟


        Xutil.Post(ApiUrl.AddLEARNRECORD,map,new MyCallBack<SupportResponse>(){
            @Override
            public void onSuccess(SupportResponse result) {
                super.onSuccess(result);
                handler.removeCallbacks(runnable);
                if(result.getStatus_code().equals("204")){
                    //成功
                    isAdd = 0;
                }else if(result.getStatus_code().equals("205")){
                    //重复提交
                    isAdd = 0;
                }else if(result.getStatus_code().equals("206")){
                    //提交失败
                    isAdd = 1;
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
            }
        });
    }
    /**
     * 隐藏显示状态栏
     */
    private void hideNavigationBar() {
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
    }

    private void showNavigationBar() {
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_VISIBLE;
        decorView.setSystemUiVisibility(uiOptions);
    }
    private void openClassDetail() {
        Map<String, String> map = new HashMap<>();
        map.put("open_course_id", _id);
//        map.put("user_id", user_id);
        map.put("user_id", (String) SPUtils.getSpValues(SPKeyValuesUtils.SP_USER_ID, SPUtils.TYPE_STRING));
        Log.e("TTTTG",map.toString());
        GloableConstant.getInstance().showmeidialog(this);
        Xutil.GET(ApiUrl.OPENDETAIL, map, new MyCallBack<OpenClassDetailResponse>() {
            @Override
            public void onSuccess(OpenClassDetailResponse result) {

                super.onSuccess(result);
                GloableConstant.getInstance().stopProgressDialog1();
                if (result.getStatus_code().equals("200")){
//                    mPullUrl=result.getData().getLive_info().getPull_url();
                    mPullUrl = String.format("%s%s", AppConstant.share_url_head,result.getData().getId()+"&user_id="+(String) SPUtils.getSpValues(SPKeyValuesUtils.SP_USER_ID, SPUtils.TYPE_STRING));
                    mLiveName=result.getData().getName();
                    mLiveContent=result.getData().getDescription();
                    mLiveImage=result.getData().getImage();
//                    mLiveImage="https://wxapi.zgclass.com/uploads/open_course/20180331144839205.jpg";
                    Log.e("TTTG",mPullUrl+"ss"+mLiveName+"sdd"+mLiveContent+mLiveImage);
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
                GloableConstant.getInstance().stopProgressDialog1();
                Log.e("**********ex",ex.getMessage()+"******");
            }
        });
    }
    SharePopWindow mSharePopWindow;
    public void showSharePopWindow() {
        mSharePopWindow = new SharePopWindow(this);
        mSharePopWindow.isShowing();
        mSharePopWindow.setOnSelectItemListener(new OnSelectItemListener() {
            @Override
            public void selectItem(String name, int type) {
                if (mSharePopWindow != null && mSharePopWindow.isShowing()) {
                    mSharePopWindow.dismiss();

                    switch (type) {
                        case SharePopWindow.POP_WINDOW_ITEM_1:
                            ShareUtils.shareWeb(LiveActivity.this, mPullUrl, mLiveName
                                    , mLiveContent, mLiveImage, R.mipmap.ic_launcher, SHARE_MEDIA.WEIXIN
                            );
                            break;
                        case SharePopWindow.POP_WINDOW_ITEM_2:
                            ShareUtils.shareWeb(LiveActivity.this, mPullUrl, mLiveName
                                    , mLiveContent, mLiveImage, R.mipmap.ic_launcher, SHARE_MEDIA.WEIXIN_CIRCLE
                            );
                            break;
                        case SharePopWindow.POP_WINDOW_ITEM_3:
                            ShareUtils.shareWeb(LiveActivity.this, mPullUrl, mLiveName
                                    , mLiveContent, mLiveImage, R.mipmap.ic_launcher, SHARE_MEDIA.QQ
                            );
                            break;
                        case SharePopWindow.POP_WINDOW_ITEM_4:

                            ShareUtils.shareWeb(LiveActivity.this, mPullUrl, mLiveName
                                    , mLiveContent, mLiveImage, R.mipmap.ic_launcher, SHARE_MEDIA.QZONE
                            );
                            break;
                        case SharePopWindow.POP_WINDOW_ITEM_5:
                            ShareUtils.shareWeb(LiveActivity.this,mPullUrl, mLiveName
                                    , mLiveContent, mLiveImage, R.mipmap.ic_launcher, SHARE_MEDIA.SMS
                            );
                            break;
                        case SharePopWindow.POP_WINDOW_ITEM_6:

                            ShareUtils.shareWeibo(LiveActivity.this,mPullUrl, mLiveName
                                    , mLiveContent, mLiveImage, R.mipmap.ic_launcher, SHARE_MEDIA.SINA
                            );
                            break;
                        default:
                            break;
                    }
                }
            }
        });
    }
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (ToolUtils.isShouldHideKeyboard(v, ev)) {
                hideKeyboard(v.getWindowToken());
            }
            View view = findViewById(R.id.bottom_chat);
            if(ToolUtils.isShouldHideEmojiBoard(view,ev)){
                if(mEmojiBoard.getVisibility() == View.VISIBLE){
                    mEmojiBoard.setVisibility(View.GONE);
                    mInputEmojiBtn.setSelected(false);
                }
            }

        }
        return super.dispatchTouchEvent(ev);
    }
    private void hideKeyboard(IBinder token) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);

        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode==KeyEvent.KEYCODE_BACK){
            if(mHvFlag == 1){
                updatePlayerViewMode();
            }else{
                finish();
            }

            return false;
        }
        return super.onKeyDown(keyCode, event);
    }
    private void isOnLine(){
        Map<String,String> map=new HashMap<>();
        map.put("app_name",nLiveName);
        Log.e("OPOP",nLiveName+"///////");
        Xutil.GET(ApiUrl.ONLINE,map,new MyCallBack<OnLineResponse>(){
            @Override
            public void onSuccess(OnLineResponse result) {
                super.onSuccess(result);
                if (result.getStatus_code().equals("200")){
                    if (result.getData().getIs_online()!=null){
                        // 备注：0直播不在线 1正在直播
                        if(result.getData().getIs_online().equals("0")){
                            handlerFlag = 1;
                            ToastUtil.showShortToast("请检查网络链接");
//                            mLiveHandler.removeCallbacks(Liverunnable);//wuyong
                            onLineThred.interrupt();//wuyong
//                            finish();
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
}
