package com.jiaoshizige.teacherexam.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.jiaoshizige.teacherexam.R;
import com.jiaoshizige.teacherexam.base.GloableConstant;
import com.jiaoshizige.teacherexam.http.ApiUrl;
import com.jiaoshizige.teacherexam.http.MyCallBack;
import com.jiaoshizige.teacherexam.http.SupportResponse;
import com.jiaoshizige.teacherexam.http.Xutil;
import com.jiaoshizige.teacherexam.model.InformationDetialResponse;
import com.jiaoshizige.teacherexam.utils.SPKeyValuesUtils;
import com.jiaoshizige.teacherexam.utils.SPUtils;
import com.jiaoshizige.teacherexam.utils.ShareUtils;
import com.jiaoshizige.teacherexam.utils.ToastUtil;
import com.jiaoshizige.teacherexam.widgets.OnSelectItemListener;
import com.jiaoshizige.teacherexam.widgets.SharePopWindow;
import com.jiaoshizige.teacherexam.yy.activity.InformationListActivity;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/a1/22.
 * 资讯详情
 */
public class InformationDetialActivity extends Activity {
    @BindView(R.id.title)
    TextView mTitle;
    @BindView(R.id.author)
    TextView mAuthor;
    @BindView(R.id.read_num)
    TextView mReadNum;
    @BindView(R.id.time)
    TextView mTime;
    @BindView(R.id.detail_webview)
    WebView mDetialWebView;
    @BindView(R.id.detail_collect)
    ImageView mDetialCollect;
    @BindView(R.id.detail_share)
    LinearLayout mDetialShare;
    @BindView(R.id.back_im)
    TextView mBack;
    @BindView(R.id.toolbar_title)
    TextView mTitleName;
    private String mInfoId;
    private String collectType;
    private String user_id;
    SharePopWindow mSharePopWindow;
    private String title;
    private String content;
    private String mImage;

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("资讯详情");
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("资讯详情");
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.information_detial_activity);
        ButterKnife.bind(this);
        user_id = (String) SPUtils.getSpValues(SPKeyValuesUtils.SP_USER_ID, SPUtils.TYPE_STRING);
        if (getIntent().getExtras().get("info_id") != null) {
            mInfoId = (String) getIntent().getExtras().get("info_id");
        } else {
            mInfoId = "";
        }
        if (getIntent().getStringExtra("title") != null) {
            title = getIntent().getStringExtra("title");
        }
        if (getIntent().getStringExtra("content") != null) {
            content = getIntent().getStringExtra("content");
        }
        if (getIntent().getStringExtra("image") != null) {
            mImage = getIntent().getStringExtra("image");
        }
        mTitleName.setText("资讯详情");
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(InformationDetialActivity.this, InformationListActivity.class));
                finish();
            }
        });
        WebSettings webSettings = mDetialWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        requestInfoDetial();

    }


//    @Override
//    protected void initView() {
//        setSubTitle().setText("");
//        setToolbarTitle().setText("资讯详情");
//
//    }

    @OnClick({R.id.product_detail_collect, R.id.detail_share})
    public void onClick(View v) {
        if (v.getId() == R.id.product_detail_collect) {
//            if(collectType == 0){
//                requestAddCollect();
//            }else{
//                rquestDeleCollect();
//            }
            if (collectType.equals("0")) {
                requestAddCollect();
            } else {
                rquestDeleCollect();
            }
        } else if (v.getId() == R.id.detail_share) {
            showPopWindow();
        }

    }

    private void requestInfoDetial() {
        Map<String, String> map = new HashMap<>();
        map.put("article_id", mInfoId);
//        map.put("user_id","1");
        map.put("user_id", (String) SPUtils.getSpValues(SPKeyValuesUtils.SP_USER_ID, SPUtils.TYPE_STRING));
        GloableConstant.getInstance().showmeidialog(this);
        Xutil.GET(ApiUrl.DETIALDETIAL, map, new MyCallBack<InformationDetialResponse>() {
            @Override
            public void onSuccess(InformationDetialResponse result) {
                super.onSuccess(result);
                GloableConstant.getInstance().stopProgressDialog();
                if (result.getStatus_code().equals("200") && result.getData() != null) {
                    if (TextUtils.isEmpty(title)) {
                        title = result.getData().getTitle();
                    }
                    if (TextUtils.isEmpty(content)) {
                        content = result.getData().getDescription();
                    }
                    if (TextUtils.isEmpty(mImage)) {
                        mImage = result.getData().getImages();
                    }
                    mTitle.setText(result.getData().getTitle());
                    mAuthor.setText(result.getData().getAuthor());
                    mReadNum.setText(result.getData().getView_count() + "人已阅读");
                    mTime.setText(result.getData().getCreated_at());
                    String test = "<style type='text/css'>img {max-width:100%!important;height:auto!important;font-size:14px;}</style>" + result.getData().getContent();
                    mDetialWebView.loadDataWithBaseURL(null, test, "text/html", "utf-8", null);

                    Log.e("********collect", result.getData().getCollect_status());
                    if (result.getData().getCollect_status().equals("yes")) {
//                        mDetialCollect.setBackground(ContextCompat.getDrawable(InformationDetialActivity.this, R.mipmap.tab_collection_pre));
                        mDetialCollect.setImageResource(R.mipmap.tab_collection_pre);
//                        collectType = "1";
                        GloableConstant.getInstance().setCollectType("1");
                    } else {
//                        mDetialCollect.setBackground(ContextCompat.getDrawable(InformationDetialActivity.this, R.mipmap.tab_collection));
//                        mCollect = 0;
                        mDetialCollect.setImageResource(R.mipmap.tab_collection);
                        GloableConstant.getInstance().setCollectType("0");
                    }
                    collectType = GloableConstant.getInstance().getCollectType();
                } else {
                    ToastUtil.showShortToast("请求出错");
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
            }
        });
    }

    private void requestAddCollect() {
        Map<String, Object> map = new HashMap<>();
        map.put("goods_id", mInfoId);
        map.put("user_id", user_id);
        map.put("type", "4");
        GloableConstant.getInstance().startProgressDialog(this);
        Xutil.Post(ApiUrl.ADDCOLLECT, map, new MyCallBack<SupportResponse>() {
            @Override
            public void onSuccess(SupportResponse result) {
                super.onSuccess(result);
                GloableConstant.getInstance().stopProgressDialog1();
                if (result.getStatus_code().equals("204")) {
                    collectType = "1";
                    GloableConstant.getInstance().setCollectType("1");
                    ToastUtil.showShortToast("收藏成功");
//                    mDetialCollect.setBackground(ContextCompat.getDrawable(InformationDetialActivity.this, R.mipmap.tab_collection_pre));
                    mDetialCollect.setImageResource(R.mipmap.tab_collection_pre);
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

    private void rquestDeleCollect() {
        Map<String, Object> map = new HashMap<>();
        map.put("goods_id", mInfoId);
        map.put("user_id", user_id);
        map.put("type", "4");
        GloableConstant.getInstance().startProgressDialog(this);
        Xutil.Post(ApiUrl.DELCOLLECT, map, new MyCallBack<SupportResponse>() {
            @Override
            public void onSuccess(SupportResponse result) {
                super.onSuccess(result);
                GloableConstant.getInstance().stopProgressDialog1();
                if (result.getStatus_code().equals("204")) {
                    ToastUtil.showShortToast("取消收藏成功");
//                    mDetialCollect.setBackground(ContextCompat.getDrawable(InformationDetialActivity.this, R.mipmap.tab_collection));
                    mDetialCollect.setImageResource(R.mipmap.tab_collection);
                    collectType = "0";
                    EventBus.getDefault().post("咨询取消收藏");
                    GloableConstant.getInstance().setCollectType("0");
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

    public void showPopWindow() {
        mSharePopWindow = new SharePopWindow(this);
        mSharePopWindow.isShowing();
        final String url = ApiUrl.BASEIMAGE + "api/v1/article_share?article_id=" + mInfoId + "&user_id=" + user_id;
        mSharePopWindow.setOnSelectItemListener(new OnSelectItemListener() {
            @Override
            public void selectItem(String name, int type) {
                if (mSharePopWindow != null && mSharePopWindow.isShowing()) {
                    mSharePopWindow.dismiss();

                    switch (type) {
                        case SharePopWindow.POP_WINDOW_ITEM_1:
//
                            ShareUtils.shareWeb(InformationDetialActivity.this, url, title
                                    , content, mImage, R.mipmap.ic_launcher, SHARE_MEDIA.WEIXIN
                            );
                            break;
                        case SharePopWindow.POP_WINDOW_ITEM_2:
//
                            ShareUtils.shareWeb(InformationDetialActivity.this, url, title
                                    , content, mImage, R.mipmap.ic_launcher, SHARE_MEDIA.WEIXIN_CIRCLE
                            );
//
                            break;
                        case SharePopWindow.POP_WINDOW_ITEM_3:
                            ShareUtils.shareWeb(InformationDetialActivity.this, url, title
                                    , content, mImage, R.mipmap.ic_launcher, SHARE_MEDIA.QQ
                            );
                            break;
                        case SharePopWindow.POP_WINDOW_ITEM_4:

                            ShareUtils.shareWeb(InformationDetialActivity.this, url, title
                                    , content, mImage, R.mipmap.ic_launcher, SHARE_MEDIA.QZONE
                            );
                            break;
                        case SharePopWindow.POP_WINDOW_ITEM_5:
                            ShareUtils.shareWeb(InformationDetialActivity.this, url, title
                                    , content, mImage, R.mipmap.ic_launcher, SHARE_MEDIA.SMS
                            );
//
                            break;
                        case SharePopWindow.POP_WINDOW_ITEM_6:
                            ShareUtils.shareWeibo(InformationDetialActivity.this, url, title
                                    , content, mImage, R.mipmap.ic_launcher, SHARE_MEDIA.SINA
                            );
//
                            break;
                        default:
                            break;
                    }
                }
            }
        });
    }

}
