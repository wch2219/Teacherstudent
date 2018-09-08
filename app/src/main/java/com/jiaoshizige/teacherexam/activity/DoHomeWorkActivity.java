package com.jiaoshizige.teacherexam.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jiaoshizige.teacherexam.constant.AppConstant;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.entity.LocalMedia;
import com.umeng.analytics.MobclickAgent;
import com.jiaoshizige.teacherexam.R;
import com.jiaoshizige.teacherexam.adapter.SelectPicGridViewAdapter;
import com.jiaoshizige.teacherexam.base.BaseActivity;
import com.jiaoshizige.teacherexam.base.GloableConstant;
import com.jiaoshizige.teacherexam.http.ApiUrl;
import com.jiaoshizige.teacherexam.http.MyCallBack;
import com.jiaoshizige.teacherexam.http.SupportResponse;
import com.jiaoshizige.teacherexam.http.Xutil;
import com.jiaoshizige.teacherexam.utils.DialogManager;
import com.jiaoshizige.teacherexam.utils.MainConstant;
import com.jiaoshizige.teacherexam.utils.PhotoUtils;
import com.jiaoshizige.teacherexam.utils.PictureSelectorConfig;
import com.jiaoshizige.teacherexam.utils.SPKeyValuesUtils;
import com.jiaoshizige.teacherexam.utils.SPUtils;
import com.jiaoshizige.teacherexam.utils.ToastUtil;
import com.jiaoshizige.teacherexam.utils.ToolUtils;
import com.jiaoshizige.teacherexam.widgets.AudioRecoderUtils;
import com.jiaoshizige.teacherexam.widgets.MediaManager;

import org.xutils.common.util.KeyValue;
import org.xutils.http.RequestParams;
import org.xutils.http.body.MultipartBody;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

import static com.jiaoshizige.teacherexam.utils.PhotoUtils.getSDPath;

/**
 * Created by Administrator on 2017/12/13.
 * 写作业
 */

public class DoHomeWorkActivity extends BaseActivity {
    @BindView(R.id.text_content)
    EditText mTextContent;
    @BindView(R.id.vioce_rt)
    RelativeLayout mVioceRtLy;
    @BindView(R.id.vioce_length)
    TextView mVioceLength;
    @BindView(R.id.add_photo)
    GridView mAddPhoto;
    private String mWorkId;
    @BindView(R.id.select_photo)
    ImageView mSelectPhoto;
    @BindView(R.id.select_vioce)
    ImageView mSelectVioce;
    @BindView(R.id.limit)
    CheckBox mLimit;
    @BindView(R.id.album)
    ImageView mAlbum;
    @BindView(R.id.camera)
    ImageView mCamera;
    @BindView(R.id.select_vioce_content)
    LinearLayout mSelectVioceContent;//添加音频
    @BindView(R.id.select_photo_content)
    LinearLayout mSelectPhotoContent;//图片添加
    @BindView(R.id.add_vioce)
    ImageView mAddVioce;
    View animView;
    private String mLitmitStr = "1";
    private ArrayList<String> mPicList = new ArrayList<>(); //上传的图片凭证的数据源
    private SelectPicGridViewAdapter mGridViewAddImgAdapter; //展示上传的图片的适配器
    DialogManager dialogManager = new DialogManager(DoHomeWorkActivity.this);
    private float mTime = 0;
    private static final int STATE_NORMAL = 1;// 默认的状态
    private static final int STATE_RECORDING = 2;// 正在录音
    private static final int STATE_WANT_TO_CANCEL = 3;// 希望取消
    private int mCurrentState = STATE_NORMAL; // 当前的状态
    private boolean isRecording = false;// 已经开始录音
    private boolean mReady;
    private AudioRecoderUtils mAudioRecoderUtils;
    String mViocePath = "";
    private static final int DISTANCE_Y_CANCEL = 50;
    static final int VOICE_REQUEST_CODE = 66;
    private static final int CODE_GALLERY_REQUEST = 0xa0;
    private static final int CODE_CAMERA_REQUEST = 0xa1;
    private static final int CODE_RESULT_REQUEST = 0xa2;
    private static final int CAMERA_PERMISSIONS_REQUEST_CODE = 0x03;
    private static final int STORAGE_PERMISSIONS_REQUEST_CODE = 0x04;
    private Uri imageUri;
    private File fileUri;
    private File mPhotoFile;
    private String mPhotoPath;
    public final static int CAMERA_RESULT = 1;
    private String mType;////1图文格式，2音频

    @Override
    protected int getLayoutId() {
        return R.layout.do_home_work_activity;
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("写作业");
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("写作业");
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public DialogManager getDialog() {
        return dialogManager;
    }

    @Override
    protected void initView() {
        setSubTitle().setText("发布");
        setSubTitle().setTextColor(ContextCompat.getColor(this, R.color.purple4));
        setToolbarTitle().setText("写作业");
        if (getIntent().getExtras().get("work_id") != null) {
            mWorkId = (String) getIntent().getExtras().get("work_id");
        } else {
            mWorkId = "";
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date = new Date(System.currentTimeMillis());
        fileUri = new File(Environment.getExternalStorageDirectory().getPath() + format.format(date) + ".jpg");
        mAudioRecoderUtils = new AudioRecoderUtils();
        mAudioRecoderUtils.setOnAudioStatusUpdateListener(new AudioRecoderUtils.OnAudioStatusUpdateListener() {
            @Override
            public void onUpdate(double db, long time) {
                mTime = time / 1000;
                dialogManager.updateVoiceLevel((int) db / 10);
            }

            @Override
            public void onStop(String filePath) {
                Log.e("sss", filePath + "");
                mViocePath = filePath;

            }
        });
        if (!mViocePath.equals("")) {
            if (ToolUtils.fileIsExists(mViocePath) && ToolUtils.fileIsNull(mAudioRecoderUtils.getFilr())) {
                mVioceRtLy.setVisibility(View.VISIBLE);
            } else {
                mVioceRtLy.setVisibility(View.GONE);
            }
        }
        requestPermissions();
        mGridViewAddImgAdapter = new SelectPicGridViewAdapter(this, mPicList);
        mAddPhoto.setAdapter(mGridViewAddImgAdapter);
        mAddPhoto.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (position == parent.getChildCount() - 1) {
                    //如果“增加按钮形状的”图片的位置是最后一张，且添加了的图片的数量不超过4张，才能点击
                    if (mPicList.size() == MainConstant.MAX_SELECT_PIC_NUM) {
                        //最多添加4张图片
                        viewPluImg(position);
                    } else {
                        //添加凭证图片
                        selectPic(MainConstant.MAX_SELECT_PIC_NUM - mPicList.size());
                    }
                } else {
                    viewPluImg(position);
                }
            }
        });
        setSubTitle().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestDoHomeWork();
            }
        });
    }

    private void selectPic(int maxTotal) {
        PictureSelectorConfig.initMultiConfig(this, maxTotal);
    }

    private void refreshAdapter(List<LocalMedia> picList) {
        for (LocalMedia localMedia : picList) {
            //被压缩后的图片路径
            if (localMedia.isCompressed()) {
                String compressPath = localMedia.getCompressPath(); //压缩后的图片路径
                mPicList.add(compressPath); //把图片添加到将要上传的图片数组中
                mGridViewAddImgAdapter.notifyDataSetChanged();
            }
        }
    }

    //查看大图
    private void viewPluImg(int position) {
        Intent intent = new Intent(this, PlusImageActivity.class);
        intent.putStringArrayListExtra(MainConstant.IMG_LIST, mPicList);
        intent.putExtra(MainConstant.POSITION, position);
        startActivityForResult(intent, MainConstant.REQUEST_CODE_MAIN);
    }


    @OnCheckedChanged(R.id.limit)
    public void onCheckedChanged(CheckBox buttonView, boolean isChecked) {
        if (isChecked) {
            mLitmitStr = "2";

        } else {
            mLitmitStr = "1";
        }
        Log.e("YYS", "ss" + mLitmitStr);
    }

    @OnClick({R.id.select_photo, R.id.select_vioce, R.id.camera, R.id.album, R.id.text_content, R.id.vioce_rt})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.select_photo:
                if(mTime > 0){
                    ToastUtil.showShortToast("音频跟图文不能同时使用");
                    mType = "2";
                }else{
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(mSelectPhoto.getWindowToken(), 0);
                    mSelectPhoto.setBackground(ContextCompat.getDrawable(DoHomeWorkActivity.this, R.mipmap.picture_pressed));
                    mSelectVioce.setBackground(ContextCompat.getDrawable(DoHomeWorkActivity.this, R.mipmap.voice_default));
                    mSelectVioceContent.setVisibility(View.GONE);
                    mSelectPhotoContent.setVisibility(View.VISIBLE);
                    mTextContent.setVisibility(View.VISIBLE);
                }

                break;
            case R.id.select_vioce:
                if(!mTextContent.getText().toString().trim().equals("") || mPicList.size() > 1){
                    ToastUtil.showShortToast("图文跟音频不能同时使用");
                    mType = "1";
                }else{
                    InputMethodManager imma = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                imma.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                    imma.hideSoftInputFromWindow(mSelectVioce.getWindowToken(), 0);
                    mSelectVioce.setBackground(ContextCompat.getDrawable(DoHomeWorkActivity.this, R.mipmap.voice_pressed));
                    mSelectPhoto.setBackground(ContextCompat.getDrawable(DoHomeWorkActivity.this, R.mipmap.picture_default));
                    mSelectVioceContent.setVisibility(View.VISIBLE);
                    mSelectPhotoContent.setVisibility(View.GONE);
                    mTextContent.setVisibility(View.GONE);

                }
                break;
            case R.id.camera:
                autoObtainCameraPermission();
                break;
            case R.id.album:
                selectPic(MainConstant.MAX_SELECT_PIC_NUM - mPicList.size());
                break;
            case R.id.text_content:
                mSelectVioceContent.setVisibility(View.GONE);
                mSelectPhotoContent.setVisibility(View.GONE);
                mSelectVioce.setBackground(ContextCompat.getDrawable(DoHomeWorkActivity.this, R.mipmap.voice_default));
                mSelectPhoto.setBackground(ContextCompat.getDrawable(DoHomeWorkActivity.this, R.mipmap.picture_default));
                break;
            case R.id.vioce_rt:
                // 播放动画（帧动画）
                if (animView != null) {
                    animView.setBackground(ContextCompat.getDrawable(DoHomeWorkActivity.this, R.mipmap.home_iocn_play_default));
                    animView = null;
                }
                animView = (View) findViewById(R.id.bo_vioce);
                animView.setBackgroundResource(R.drawable.play_anim);
                AnimationDrawable animation = (AnimationDrawable) animView.getBackground();
                animation.start();
                String s = "http://www.w3school.com.cn/i/horse.mp3";
                Uri sd = Uri.parse("http://www.w3school.com.cn/i/horse.mp3");
                ToastUtil.showShortToast("dianji");
                MediaManager.playSound(DoHomeWorkActivity.this, Uri.parse(mViocePath), new MediaPlayer.OnCompletionListener() {
                    public void onCompletion(MediaPlayer mp) {
//                        animView.setBackgroundResource(R.mipmap.home_iocn_play_default);
                        animView.setBackground(ContextCompat.getDrawable(DoHomeWorkActivity.this, R.mipmap.home_iocn_play_default));
                    }
                });
                break;
        }
    }

    //////////////////////////////////////////////////////////
    private boolean wantToCancle(int x, int y) {
        if (x < 0 || x > mAddVioce.getWidth()) { // 超过按钮的宽度 先设置 50
            return true;
        }
        // 超过按钮的高度
        if (y < -DISTANCE_Y_CANCEL || y > mAddVioce.getHeight() + DISTANCE_Y_CANCEL) {
            return true;
        }

        return false;
    }

    public void reset() {
        isRecording = false;
        mTime = 0;
        mReady = false;
        mAddVioce.setBackground(ContextCompat.getDrawable(DoHomeWorkActivity.this, R.mipmap.home_png_voice_default));
    }

    /**
     * 开启扫描之前判断权限是否打开
     */
    private void requestPermissions() {
        //判断是否开启摄像头权限
        if ((ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) &&
                (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED)
                ) {
            StartListener();
            //判断是否开启语音权限
        } else {
            //请求获取摄像头权限
            ActivityCompat.requestPermissions((Activity) this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO}, VOICE_REQUEST_CODE);
        }

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            mPicList.add(mPhotoPath);
            mGridViewAddImgAdapter.notifyDataSetChanged();
        }
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    refreshAdapter(PictureSelector.obtainMultipleResult(data));
                    break;

            }

        }
        if (requestCode == MainConstant.REQUEST_CODE_MAIN && resultCode == MainConstant.RESULT_CODE_VIEW_IMG) {
            //查看大图页面删除了图片
            ArrayList<String> toDeletePicList = data.getStringArrayListExtra(MainConstant.IMG_LIST); //要删除的图片的集合
            mPicList.clear();
            mPicList.addAll(toDeletePicList);
            mGridViewAddImgAdapter.notifyDataSetChanged();
        }
    }

    public void StartListener() {
        /**
         * touch时间
         */
        mAddVioce.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int x = (int) event.getX();// 获得x轴坐标
                int y = (int) event.getY();// 获得y轴坐标
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        //按住事件发生后执行代码的区域
                        ToolUtils.deleteFile(mAudioRecoderUtils.getFilr());
                        mCurrentState = STATE_NORMAL;
//                        mAudioRecoderUtils.setTime(0);
//                        if(mAudioRecoderUtils.getTime() >= 3){
//                            //大于60s 自动完成
//                            Log.e("GGH", "SSSS"+mAudioRecoderUtils.getFlag());
//                            isRecording = false;
//                            mAudioRecoderUtils.stopRecord();
//                            dialogManager.dimissDialog();
//                            if(ToolUtils.fileIsExists(mViocePath) && ToolUtils.fileIsNull(mAudioRecoderUtils.getFilr())){
//                                mVioceRtLy.setVisibility(View.VISIBLE);
//                                mVioceLength.setText((int)mTime+"s");
//                            }else{
//                                mVioceRtLy.setVisibility(View.GONE);
//                            }
//                        }else{
                        isRecording = true;
                        mAudioRecoderUtils.startRecord();
                        dialogManager.showRecordingDialog();
                        mAddVioce.setBackground(ContextCompat.getDrawable(DoHomeWorkActivity.this, R.mipmap.home_png_voice_pressed));
//                        }
                        break;
                    }
                    case MotionEvent.ACTION_UP: {
                        //松开事件发生后执行代码的区域
                        if (mTime < 1) {
                            dialogManager.tooShort();
                            mAudioRecoderUtils.cancelRecord();
                            Thread t = new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        Thread.sleep(1000);//让他显示10秒后，取消ProgressDialog
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    dialogManager.dimissDialog();
                                }

                            });
                            t.start();
                        } else if (mCurrentState == STATE_WANT_TO_CANCEL) {
                            mAudioRecoderUtils.cancelRecord();
                            dialogManager.dimissDialog();
                        } else {
                            mAudioRecoderUtils.stopRecord();
                            dialogManager.dimissDialog();
                            if (ToolUtils.fileIsExists(mViocePath) && ToolUtils.fileIsNull(mAudioRecoderUtils.getFilr())) {
                                mVioceRtLy.setVisibility(View.VISIBLE);
                                mVioceLength.setText((int) mTime + "s");
                                WindowManager wm = (WindowManager) DoHomeWorkActivity.this.getSystemService(Context.WINDOW_SERVICE);
                                DisplayMetrics outMetrics = new DisplayMetrics();
                                wm.getDefaultDisplay().getMetrics(outMetrics);
                                int mMaxItemWidth = (int) (outMetrics.widthPixels * 0.7f);
                                int mMinItemWidth = (int) (outMetrics.widthPixels * 0.15f);
                                ViewGroup.LayoutParams lp = mVioceRtLy.getLayoutParams();
                                lp.width = (int) (mMinItemWidth + (mMaxItemWidth / 60f) * mTime);
                                mVioceRtLy.setLayoutParams(lp);
                            } else {
                                mVioceRtLy.setVisibility(View.GONE);
                            }
                        }
                        break;
                    }
                    case MotionEvent.ACTION_MOVE: {
                        Log.e("GGH", isRecording + "SSSSaadddmove" + mAudioRecoderUtils.getTime());
                        if (isRecording) {
                            // 如果想要取消，根据x,y的坐标看是否需要取消
                            if (wantToCancle(x, y)) {
                                mCurrentState = STATE_WANT_TO_CANCEL;
                                dialogManager.wantToCancel();
                            } else {
                                mCurrentState = STATE_RECORDING;
                                Log.e("GGH", "SSSSaadddmdsdsve");
                            }
                        }
                    }
                }
                return true;
            }
        });
    }

    /**
     * 自动获取相机权限
     */
    private void autoObtainCameraPermission() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE}, CAMERA_PERMISSIONS_REQUEST_CODE);
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE}, CAMERA_PERMISSIONS_REQUEST_CODE);

            }
        } else {//有权限直接调用系统相机拍照
            if (ToolUtils.hasSdcard()) {
                imageUri = Uri.fromFile(fileUri);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
//                    imageUri = FileProvider.getUriForFile(this, "com.zz.fileprovider", fileUri);//通过FileProvider创建一个content类型的Uri
                    imageUri = FileProvider.getUriForFile(DoHomeWorkActivity.this, AppConstant.fileprovider, fileUri);//通过FileProvider创建一个content类型的Uri
                PhotoUtils.takePicture(this, imageUri, CODE_CAMERA_REQUEST);
                tackPic(DoHomeWorkActivity.this);
            } else {
                ToastUtil.showShortToast("设备没有SD卡！");
            }
        }
    }

    /**
     * 拍照创建文件夹
     *
     * @param activity
     */
    public void tackPic(Activity activity) {
        try {
            Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");//开始拍照
            mPhotoPath = getSDPath() + "/zgwkpic" + PhotoUtils.getPhotoFileName();//设置图片文件路径，getSDPath()和getPhotoFileName()具体实现在下面
            mPhotoFile = new File(mPhotoPath);
            if (!mPhotoFile.exists()) {
                mPhotoFile.createNewFile();//创建新文件
            }
            intent.putExtra(MediaStore.EXTRA_OUTPUT,//Intent有了图片的信息
                    Uri.fromFile(mPhotoFile));
            activity.startActivityForResult(intent, CAMERA_RESULT);//跳转界面传回拍照所得数据
        } catch (Exception e) {
        }
    }

    /**
     * 请求权限回调
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case CAMERA_PERMISSIONS_REQUEST_CODE: {//调用系统相机申请拍照权限回调
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ToolUtils.hasSdcard()) {
                        imageUri = Uri.fromFile(fileUri);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                            imageUri = FileProvider.getUriForFile(this, AppConstant.fileprovider, fileUri);//通过FileProvider创建一个content类型的Uri
                        PhotoUtils.takePicture(this, imageUri, CODE_CAMERA_REQUEST);
                    } else {
                        ToastUtil.showShortToast("设备没有SD卡！");
                    }
                } else {
                    ToastUtil.showShortToast("请允许打开相机！！");
                }
                break;
            }
            case STORAGE_PERMISSIONS_REQUEST_CODE://调用系统相册申请Sdcard权限回调
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    PhotoUtils.openPic(this, CODE_GALLERY_REQUEST);
                } else {
                    ToastUtil.showShortToast("请允许打操作SDCard！！");
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ToolUtils.deleteFile(mAudioRecoderUtils.getFilr());
    }

    ////////////////////////////////////////
    private void requestDoHomeWork() {
        //构建RequestParams对象，传入请求的服务器地址URL
        RequestParams params = new RequestParams(ApiUrl.DOHOMEWORK);
//        params.setAsJsonContent(true);
        List<KeyValue> list = new ArrayList<>();
        if(mPicList.size() > 0){
            for (int i = 0; i < mPicList.size(); i++) {
                File file = new File(mPicList.get(i));
                list.add(new KeyValue("images"+"["+i+"]", file));
            }
        }else{
            list.add(new KeyValue("images", ""));
        }
        if(mTime > 1){
            File voice = new File(mViocePath);//语音文件
            list.add(new KeyValue("audio_url", voice));
        }else{
            list.add(new KeyValue("audio_url", ""));
        }
//        list.add(new KeyValue("user_id", "4"));
//          map.put("user_id",  SPUtils.getSpValues(SPKeyValuesUtils.SP_USER_ID, SPUtils.TYPE_STRING));
        list.add(new KeyValue("user_id", SPUtils.getSpValues(SPKeyValuesUtils.SP_USER_ID, SPUtils.TYPE_STRING)));
        list.add(new KeyValue("work_id", mWorkId));
        list.add(new KeyValue("content", mTextContent.getText().toString().trim()));
        if(!mTextContent.getText().toString().trim().equals("")){
            mType = "1";
        }
        if(mTime > 0){
            mType = "2";
        }
        list.add(new KeyValue("is_seen", mLitmitStr));//1任何人可见，2仅对楼主可见	未加密
        list.add(new KeyValue("type", mType));//1图文格式，2音频
        list.add(new KeyValue("duration",(int)mTime));
        MultipartBody body = new MultipartBody(list, "UTF-8");
        params.setRequestBody(body);
        GloableConstant.getInstance().startProgressDialog(this);
        Xutil.PostImageFiles(params, new MyCallBack<SupportResponse>() {
            @Override
            public void onSuccess(SupportResponse result) {
                super.onSuccess(result);
                GloableConstant.getInstance().stopProgressDialog1();
                if(result.getStatus_code().equals("204")){
                    ToastUtil.showShortToast("作业提交成功");
                    finish();
                }else{
                    ToastUtil.showShortToast(result.getMessage());
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
                Log.e("TAG",ex.getMessage()+"");
                GloableConstant.getInstance().stopProgressDialog1();
            }
        });
    }
}
