package com.jiaoshizige.teacherexam.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jiaoshizige.teacherexam.constant.AppConstant;
import com.umeng.analytics.MobclickAgent;
import com.jiaoshizige.teacherexam.R;
import com.jiaoshizige.teacherexam.base.BaseActivity;
import com.jiaoshizige.teacherexam.base.GloableConstant;
import com.jiaoshizige.teacherexam.http.ApiUrl;
import com.jiaoshizige.teacherexam.http.MyCallBack;
import com.jiaoshizige.teacherexam.http.Xutil;
import com.jiaoshizige.teacherexam.model.AvarResponse;
import com.jiaoshizige.teacherexam.model.PersonMessageResponse;
import com.jiaoshizige.teacherexam.utils.PhotoUtils;
import com.jiaoshizige.teacherexam.utils.SPKeyValuesUtils;
import com.jiaoshizige.teacherexam.utils.SPUtils;
import com.jiaoshizige.teacherexam.utils.ToastUtil;
import com.jiaoshizige.teacherexam.widgets.OnSelectItemListener;
import com.jiaoshizige.teacherexam.widgets.PicturePopWindow;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/a1/6 0006.
 * 账号与安全
 */

public class AccountSecurityActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.avar)
    ImageView avar;
   @BindView(R.id.updata)
   RelativeLayout mUpdata;
    @BindView(R.id.amend_nickname)
    RelativeLayout amend_nickname;
    @BindView(R.id.amend_password)
    RelativeLayout amend_password;
    @BindView(R.id.login_num)
    RelativeLayout login_num;
    @BindView(R.id.nickname)
    TextView mNickName;

    PicturePopWindow mPicturePopWindow;
    private Context mContext;
    private Intent intent;
    private String user_id,phone;


    public static final int CODE_GALLERY_REQUEST = 0xa0;
    public static final int CODE_CAMERA_REQUEST = 0xa1;
    public static final int CODE_RESULT_REQUEST = 0xa2;
    public static final int CAMERA_PERMISSIONS_REQUEST_CODE = 0x03;
    public static final int STORAGE_PERMISSIONS_REQUEST_CODE = 0x04;
    public File fileUri = new File(Environment.getExternalStorageDirectory().getPath() + "/photo.jpg");
    public File fileCropUri = new File(Environment.getExternalStorageDirectory().getPath() + "/crop_photo.jpg");
    public Uri imageUri;
    public Uri cropImageUri;

       @Override
    protected void onStart() {
        super.onStart();
        GainMessage();
    }
    @Override
    protected int getLayoutId() {
        return R.layout.account_security_activity_layout;
    }

    @Override
    protected void initView() {
        mContext = this;
        setSubTitle().setText("");
        setToolbarTitle().setText("账号与安全");
        user_id= (String) SPUtils.getSpValues(SPKeyValuesUtils.SP_USER_ID,SPUtils.TYPE_STRING);
        avar.setOnClickListener(this);
        amend_nickname.setOnClickListener(this);
        amend_password.setOnClickListener(this);
        login_num.setOnClickListener(this);
        mUpdata.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.avar:
                break;
            case R.id.amend_nickname:
                intent = new Intent(AccountSecurityActivity.this, AmentNickNameActivity.class);
                startActivity(intent);
                break;
            case R.id.amend_password:
                intent = new Intent(AccountSecurityActivity.this, AmendPasswordActivity.class);
                startActivity(intent);
                break;
            case R.id.login_num:
                intent = new Intent(AccountSecurityActivity.this, LoginNumActivity.class);
                intent.putExtra("phone",phone);
                startActivity(intent);
                break;
            case R.id.updata:
                showPopWindow();
                break;
            default:
                break;

        }
    }
    /*获取信息*/
    private void GainMessage(){
        Map<String,String> map=new HashMap<>();
        map.put("user_id",user_id);
        GloableConstant.getInstance().showmeidialog(this);
        Xutil.GET(ApiUrl.USER,map,new MyCallBack<PersonMessageResponse>(){
            @Override
            public void onSuccess(PersonMessageResponse result) {
                super.onSuccess(result);
                GloableConstant.getInstance().stopProgressDialog();
                Log.e("*********result",result.getStatus_code());
                if (result.getStatus_code().equals("200")){
                    if (result.getData().getAvatar()!=null){
                        Glide.with(mContext).load(result.getData().getAvatar())
                                .apply(GloableConstant.getInstance().getOptions()).into(avar);

                    }else {

                    }

                    mNickName.setText(result.getData().getNick_name());
                    phone=result.getData().getMobile();
                }
            }

            @Override
            public void onFinished() {
                super.onFinished();
                GloableConstant.getInstance().stopProgressDialog();
            }
        });


    }





/*上传头像*/
    private void UpdataAvar(){
        Map<String,Object> map=new HashMap<>();
        map.put("user_id",user_id);
        map.put("avatar",fileCropUri);
        Log.e("*****map",map.toString());
        GloableConstant.getInstance().showmeidialog(this);
        Xutil.Post(ApiUrl.UPDATA,map,new MyCallBack<AvarResponse>(){
            @Override
            public void onSuccess(AvarResponse result) {
                super.onSuccess(result);
                GloableConstant.getInstance().stopProgressDialog();
                Log.e("*-*****result",result.getStatus_code());
                if (result.getStatus_code().equals("204")){
                    ToastUtil.showShortToast(result.getMessage());
                    Glide.with(mContext).load(result.getData().getHead_img())
                            .apply(GloableConstant.getInstance().getOptions())
                            .into(avar);
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
                Log.e("************ex",ex.getMessage()+"-----------");
            }

            @Override
            public void onFinished() {
                super.onFinished();
                GloableConstant.getInstance().stopProgressDialog();
            }
        });

    }


    public void showPopWindow() {
        mPicturePopWindow = new PicturePopWindow(this);
        mPicturePopWindow.isShowing();
        mPicturePopWindow.setOnSelectItemListener(new OnSelectItemListener() {
            @Override
            public void selectItem(String name, int type) {
                if (mPicturePopWindow != null && mPicturePopWindow.isShowing()) {
                    mPicturePopWindow.dismiss();

                    switch (type) {
                        case PicturePopWindow.POP_WINDOW_ITEM_1:
                            autoObtainCameraPermission();
                            break;
                        case PicturePopWindow.POP_WINDOW_ITEM_2:
                            autoObtainStoragePermission();
                            break;
                        case PicturePopWindow.POP_WINDOW_ITEM_3:
                            break;
                        default:
                            break;
                    }
                }
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
                ToastUtil.showShortToast("您已经拒绝过一次");
            }
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE}, CAMERA_PERMISSIONS_REQUEST_CODE);
        } else {//有权限直接调用系统相机拍照
            if (hasSdcard()) {
                imageUri = Uri.fromFile(fileUri);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
//                    imageUri = FileProvider.getUriForFile(AccountSecurityActivity.this, "com.zz.fileprovider", fileUri);//通过FileProvider创建一个content类型的Uri
                    imageUri = FileProvider.getUriForFile(AccountSecurityActivity.this, AppConstant.fileprovider, fileUri);//通过FileProvider创建一个content类型的Uri
                PhotoUtils.takePicture(this, imageUri, CODE_CAMERA_REQUEST);
            } else {
                ToastUtil.showShortToast("设备没有SD卡！");
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case CAMERA_PERMISSIONS_REQUEST_CODE: {//调用系统相机申请拍照权限回调
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (hasSdcard()) {
                        imageUri = Uri.fromFile(fileUri);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                            imageUri = FileProvider.getUriForFile(AccountSecurityActivity.this, AppConstant.fileprovider, fileUri);//通过FileProvider创建一个content类型的Uri
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

    private static final int output_X = 480;
    private static final int output_Y = 480;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case CODE_CAMERA_REQUEST://拍照完成回调
                    cropImageUri = Uri.fromFile(fileCropUri);
                    PhotoUtils.cropImageUri(this, imageUri, cropImageUri, 1, 1, output_X, output_Y, CODE_RESULT_REQUEST);
                    break;
                case CODE_GALLERY_REQUEST://访问相册完成回调
                    if (hasSdcard()) {
                        cropImageUri = Uri.fromFile(fileCropUri);
                        Uri newUri = Uri.parse(PhotoUtils.getPath(this, data.getData()));
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
//                            newUri = FileProvider.getUriForFile(this, "com.zz.fileprovider", new File(newUri.getPath()));
                            newUri = FileProvider.getUriForFile(this, AppConstant.fileprovider, new File(newUri.getPath()));
                        PhotoUtils.cropImageUri(this, newUri, cropImageUri, 1, 1, output_X, output_Y, CODE_RESULT_REQUEST);
                    } else {
                        ToastUtil.showShortToast("设备没有SD卡！");
                    }
                    break;
                case CODE_RESULT_REQUEST:
                    Bitmap bitmap = PhotoUtils.getBitmapFromUri(cropImageUri, this);
                    if (bitmap != null) {
                        showImages(bitmap);
                    }
                    break;
            }
        }
    }


    /**
     * 自动获取sdk权限
     */

    private void autoObtainStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSIONS_REQUEST_CODE);
        } else {
            PhotoUtils.openPic(this, CODE_GALLERY_REQUEST);
        }

    }

    private void showImages(Bitmap bitmap) {
        UpdataAvar();
        avar.setImageBitmap(bitmap);

    }

    /**
     * 检查设备是否存在SDCard的工具方法
     */
    public static boolean hasSdcard() {
        String state = Environment.getExternalStorageState();
        return state.equals(Environment.MEDIA_MOUNTED);
    }
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("账号与安全"); //手动统计页面("SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(this); //统计时长
        GainMessage();
    }
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("账号与安全"); //手动统计页面("SplashScreen"为页面名称，可自定义)，必须保证 onPageEnd 在 onPause 之前调用，因为SDK会在 onPause 中保存onPageEnd统计到的页面数据。
        MobclickAgent.onPause(this);
    }

}






