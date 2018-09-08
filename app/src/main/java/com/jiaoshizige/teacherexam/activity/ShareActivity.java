package com.jiaoshizige.teacherexam.activity;

/**
 * Created by Administrator on 2017/a1/8 0008.
 */


import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.jiaoshizige.teacherexam.http.ApiUrl;
import com.jiaoshizige.teacherexam.http.MyCallBack;
import com.jiaoshizige.teacherexam.http.Xutil;
import com.jiaoshizige.teacherexam.model.ShareResponse;
import com.jiaoshizige.teacherexam.utils.SPKeyValuesUtils;
import com.jiaoshizige.teacherexam.utils.SPUtils;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.jiaoshizige.teacherexam.R;
import com.jiaoshizige.teacherexam.base.BaseActivity;
import com.jiaoshizige.teacherexam.utils.Defaultcontent;
import com.jiaoshizige.teacherexam.utils.ShareUtils;
import com.jiaoshizige.teacherexam.utils.ToastUtil;
import com.jiaoshizige.teacherexam.widgets.OnSelectItemListener;
import com.jiaoshizige.teacherexam.widgets.SharePopWindow;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;

/**
 * 分享应用
 */

public class ShareActivity extends BaseActivity implements View.OnClickListener {

    SharePopWindow mSharePopWindow;

    @BindView(R.id.share)
    Button mShare;
    @BindView(R.id.image_share)
    ImageView mImage;

    private Activity mContext;
    @Override
    protected int getLayoutId() {
        return R.layout.share_activity;
    }

    @Override
    protected void initView() {
        setSubTitle().setText("");
        setToolbarTitle().setText("分享");
        mContext=this;
        mShare.setOnClickListener(this);
        shareImage();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.share:
                showPopWindow();
                break;
            default:
                break;
        }
    }


    private void  shareImage(){
        Map<String,Object> map=new HashMap<>();
        map.put("user_id", (String) SPUtils.getSpValues(SPKeyValuesUtils.SP_USER_ID, SPUtils.TYPE_STRING));
        Xutil.Post(ApiUrl.SHARE,map,new MyCallBack<ShareResponse>(){
            @Override
            public void onSuccess(ShareResponse result) {
                super.onSuccess(result);
                Log.e("********getStatus_code",result.getStatus_code());
                if (result.getStatus_code().equals("200")){
                    if (result.getData().getAz()!=null){

                        Log.e("********image",result.getData().getAz());
                        Glide.with(mContext).load(result.getData().getAz()).into(mImage);
                    }
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
                Log.e("********ex",ex.getMessage());
            }
        });

    }

    public void showPopWindow() {
        mSharePopWindow = new SharePopWindow(this);
        mSharePopWindow.isShowing();
        mSharePopWindow.setOnSelectItemListener(new OnSelectItemListener() {
            @Override
            public void selectItem(String name, int type) {
                if (mSharePopWindow != null && mSharePopWindow.isShowing()) {
                    mSharePopWindow.dismiss();

                    switch (type) {
                        case SharePopWindow.POP_WINDOW_ITEM_1:
//                            ToastUtil.showShortToast("微信");
                            ShareUtils.shareWeb(mContext, Defaultcontent.url, Defaultcontent.title
                                    , Defaultcontent.text, Defaultcontent.imageurl, R.mipmap.ic_launcher, SHARE_MEDIA.WEIXIN
                            );
                            break;
                        case SharePopWindow.POP_WINDOW_ITEM_2:
//                            ToastUtil.showShortToast("朋友圈");
                            ShareUtils.shareWeb(mContext, Defaultcontent.url, Defaultcontent.title
                                    , Defaultcontent.text, Defaultcontent.imageurl, R.mipmap.ic_launcher, SHARE_MEDIA.WEIXIN_CIRCLE
                            );
//                            ToastUtil.showShortToast("朋友圈");
                            break;
                        case SharePopWindow.POP_WINDOW_ITEM_3:
//                            ToastUtil.showShortToast("QQ");
                            ShareUtils.shareWeb(mContext, Defaultcontent.url, Defaultcontent.title
                                    , Defaultcontent.text, Defaultcontent.imageurl, R.mipmap.ic_launcher, SHARE_MEDIA.QQ
                            );
                            break;
                        case SharePopWindow.POP_WINDOW_ITEM_4:
                            ToastUtil.showShortToast("QQ空间");
                            ShareUtils.shareWeb(mContext, Defaultcontent.url, Defaultcontent.title
                                    , Defaultcontent.text, Defaultcontent.imageurl, R.mipmap.ic_launcher, SHARE_MEDIA.QZONE
                            );
                            break;
                        case SharePopWindow.POP_WINDOW_ITEM_5:
                            ShareUtils.shareWeb(mContext, Defaultcontent.url, Defaultcontent.title
                                    , Defaultcontent.text, Defaultcontent.imageurl, R.mipmap.ic_launcher, SHARE_MEDIA.SMS
                            );
                            ToastUtil.showShortToast("短信");
                            break;
                        case SharePopWindow.POP_WINDOW_ITEM_6:
//                            ToastUtil.showShortToast("新浪微博");
                            ShareUtils.shareWeibo(mContext, Defaultcontent.url, Defaultcontent.title
                                    , Defaultcontent.text, Defaultcontent.imageurl, R.mipmap.ic_launcher, SHARE_MEDIA.SINA
                            );

                            break;
                        default:
                            break;
                    }
                }
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

}
