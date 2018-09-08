package com.jiaoshizige.teacherexam.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jiaoshizige.teacherexam.model.OnLineResponse;
import com.jiaoshizige.teacherexam.widgets.CustomDigitalClock;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.jiaoshizige.teacherexam.R;
import com.jiaoshizige.teacherexam.base.GloableConstant;
import com.jiaoshizige.teacherexam.http.ApiUrl;
import com.jiaoshizige.teacherexam.http.MyCallBack;
import com.jiaoshizige.teacherexam.http.Xutil;
import com.jiaoshizige.teacherexam.model.CreatResponse;
import com.jiaoshizige.teacherexam.model.OpenClassDetailResponse;
import com.jiaoshizige.teacherexam.utils.SPKeyValuesUtils;
import com.jiaoshizige.teacherexam.utils.SPUtils;
import com.jiaoshizige.teacherexam.utils.ShareUtils;
import com.jiaoshizige.teacherexam.utils.ToastUtil;
import com.jiaoshizige.teacherexam.utils.ToolUtils;
import com.jiaoshizige.teacherexam.widgets.OnSelectItemListener;
import com.jiaoshizige.teacherexam.widgets.SharePopWindow;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/12/a1 0011.
 */

public class OpenClassDetailActivity extends AppCompatActivity implements View.OnClickListener {
    @BindView(R.id.detail_image)
    ImageView mImage;
    @BindView(R.id.back)
    ImageView mBack;
    @BindView(R.id.share)
    ImageView mShare;
    @BindView(R.id.open_name)
    TextView mName;
    @BindView(R.id.open_data)
    TextView mData;
    @BindView(R.id.open_sale_num)
    TextView mSale;
    @BindView(R.id.recycle_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.open_introduce)
    TextView mIntroduce;
    @BindView(R.id.open_consult)
    RelativeLayout mConsult;
    @BindView(R.id.open_order)
    RelativeLayout mOrder;
    @BindView(R.id.open_order_text)
    TextView mOrderText;
    @BindView(R.id.load_time)
    CustomDigitalClock mLoadTime;
    @BindView(R.id.load_time_ly)
    LinearLayout mLoadTimeLy;
    private String _id, text, user_id;
    private Context mContext;
    private int tag;
    private String url;
    private String flag;
    private String lb_url;
    private String mToken;
    private String mRoomId;
    private String title = "";
    private String description = "";
    private String Image = "";
    private String mNamestr;
    private Long time = System.currentTimeMillis() / 1000;
    SharePopWindow mSharePopWindow;
    private String appName;
    private String start;

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("公开课详情");
        MobclickAgent.onResume(this);
        openClassDetail();
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("公开课详情");
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.open_class_detail_layout);
        ButterKnife.bind(this);
        mContext = this;
        user_id = (String) SPUtils.getSpValues(SPKeyValuesUtils.SP_USER_ID, SPUtils.TYPE_STRING);
        if (getIntent().getStringExtra("_id") != null) {
            _id = getIntent().getStringExtra("_id");
        } else {
            _id = "";
        }
        Log.e("*********_idxiang", _id + "/////////");
        if (getIntent().getStringExtra("lb_url") != null) {
            lb_url = getIntent().getStringExtra("lb_url");
        } else {
            lb_url = "";
        }
        if (getIntent().getStringExtra("recent") != null) {
            text = getIntent().getStringExtra("recent");
        } else {
            text = "";
        }
        if (getIntent().getExtras().get("live_url") != null) {
            url = (String) getIntent().getExtras().get("live_url");
        } else {
            url = "";
        }
        if (getIntent().getExtras().get("token") != null) {
            mToken = (String) getIntent().getExtras().get("token");
        } else {
            mToken = "";
        }

        Log.e("*********mToken", mToken + "/////////");
        if (getIntent().getExtras().get("chat_room_id") != null) {
            mRoomId = (String) getIntent().getExtras().get("chat_room_id");
        } else {
            mRoomId = "";
        }
        if (getIntent().getExtras().get("name") != null) {
            mNamestr = (String) getIntent().getExtras().get("name");
        }
        mBack.setOnClickListener(this);
        mShare.setOnClickListener(this);
        mConsult.setOnClickListener(this);
        mOrder.setOnClickListener(this);
//        openClassDetail();
        mLoadTime.setClockListener(new CustomDigitalClock.ClockListener() {
            @Override
            public void timeEnd() {
                mLoadTimeLy.setVisibility(View.GONE);
                mOrderText.setText(View.VISIBLE);
                mOrderText.setText("立即观看");
                mOrder.setBackgroundColor(ContextCompat.getColor(OpenClassDetailActivity.this, R.color.buy_color));
            }

            @Override
            public void remainFiveMinutes() {

            }
        });
    }

    private long timeNow = 0;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.share:
                showSharePopWindow();
                break;
            case R.id.open_consult:
                if (ToolUtils.checkApkExist(this, "com.tencent.mobileqq")) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("mqqwpa://im/chat?chat_type=wpa&uin=" + qq)));
                } else {
                    ToastUtil.showShortToast("本机未安装QQ应用");
                }
                break;
            case R.id.open_order:
                Log.e("********flag", flag + "********tag**" + tag);
                if (System.currentTimeMillis() - timeNow > 2000) {
                    if (!flag.equals("")) {
                        if (flag.equals("1")) {

                            if (text.equals("recent")) {
                                ToastUtil.showShortToast("直播还未开始");
                            } else {
                                if (lb_url.equals("")) {
//                                  intent.setClass(OpenClassDetailActivity.this, LiveActivity.class);
//                                  intent.putExtra("live_url", url);
//                                  intent.putExtra("chat_room_id", mRoomId);
//                                  intent.putExtra("token", mToken);
//                                  intent.putExtra("_id", _id);
//                                  intent.putExtra("name", mNamestr);
                                    if (appName != null) {
                                        onLine(appName);
                                    }
                                } else {
                                    Intent intent = new Intent();
                                    intent = RecordingActivity.newIntent(mContext, RecordingActivity.PlayMode.portrait, lb_url, 1, true, true);
                                    intent.setClass(OpenClassDetailActivity.this, RecordingActivity.class);
                                    Log.e("TYY", lb_url);
                                    intent.putExtra("live_url", lb_url);
                                    intent.putExtra("no_course_id", "1");
                                    startActivity(intent);
                                }

                            }
                        } else {
                            Log.e("*********tag", tag + "");
                            switch (tag) {
                                case 0:
                                    orderOpenClass();
                                    break;
                                case 1:
                                    Intent intent = new Intent();
                                    intent.setClass(OpenClassDetailActivity.this, OpenClassOrderActivity.class);
                                    intent.putExtra("_id", _id);
                                    startActivity(intent);
                                    break;
                            }
//                }
                        }
                    }
                    timeNow = System.currentTimeMillis();
                }


                break;
            default:
                break;
        }

    }

    private String qq;

    private void openClassDetail() {
        Map<String, String> map = new HashMap<>();
        map.put("open_course_id", _id);
//        map.put("user_id", user_id);
        map.put("user_id", (String) SPUtils.getSpValues(SPKeyValuesUtils.SP_USER_ID, SPUtils.TYPE_STRING));
        Log.e("YYY", map.toString());
        GloableConstant.getInstance().showmeidialog(this);
        Xutil.GET(ApiUrl.OPENDETAIL, map, new MyCallBack<OpenClassDetailResponse>() {
            @Override
            public void onSuccess(OpenClassDetailResponse result) {
                super.onSuccess(result);
                GloableConstant.getInstance().stopProgressDialog();
                if (result.getStatus_code().equals("200")) {
                    Glide.with(mContext).load(result.getData().getImage()).apply(GloableConstant.getInstance().getDefaultOptionssmall()).into(mImage);
                    mName.setText(result.getData().getName());
                    title = result.getData().getName();
                    description = result.getData().getDescription();
                    appName = result.getData().getLive_info().getApp_name();
                    Image = result.getData().getImage();
                    if (result.getData().getQq() != null) {
                        qq = result.getData().getQq();
                    }
                    mData.setText(result.getData().getStart_time() + "-" + result.getData().getEnd_time());
                    mSale.setText(result.getData().getSale_num() + "人已预约");
                    mIntroduce.setText(result.getData().getDescription());
                    //使用push_url为rtmp的直播数据流  pull_url为浏览器播放地址  或者可做为分享地址
                /*    if (result.getData().getLive_info().getPull_url() != null) {
                        url = result.getData().getLive_info().getPull_url();
                    } else {
                        url = "";
                    }*/
//                    if (result.getData().getLive_info().getToken()!=null){
//                        mToken=result.getData().getLive_info().getToken();
//                    }else {
//                        mToken="";
//                    }
                    if (result.getData().getIs_buy() != null) {
                        Log.e("TAG", time + "");
//                        String end = result.getData().getEnd_times()+"000";
//                        String start = result.getData().getStart_times() + "000";
                        String end = result.getData().getEnd_times();
                        start = result.getData().getStart_times();
                        if (result.getData().getIs_buy().equals("1")) {
                            if (getIntent().getExtras().get("recent") != null) {
                                Log.e("*******getExtras", getIntent().getExtras().get("recent") + "/////");
                                if (getIntent().getExtras().get("recent").equals("history")) {
                                    mOrderText.setText("已结束，点击回看");
                                    mOrderText.setTextColor(getResources().getColor(R.color.white));
                                    mOrder.setBackgroundResource(R.color.buy_color);
                                    flag = "1";
                                } else if (getIntent().getExtras().get("recent").equals("now")) {
                                    if (ToolUtils.getTime(Long.valueOf(start), time).equals("2")) {
                                        mOrderText.setVisibility(View.GONE);
                                        mLoadTimeLy.setVisibility(View.VISIBLE);
                                        mLoadTime.setEndTime(Long.valueOf(start + "000"));
                                        mOrder.setBackgroundResource(R.color.line_color);
                                        mLoadTime.setClockListener(new CustomDigitalClock.ClockListener() {
                                            @Override
                                            public void timeEnd() {
                                                openClassDetail();
//                                                    Intent intent=new Intent();
//                                                    if (lb_url.equals("")) {
//                                                        intent.setClass(OpenClassDetailActivity.this, LiveActivity.class);
//                                                        intent.putExtra("live_url", url);
//                                                        intent.putExtra("chat_room_id", mRoomId);
//                                                        intent.putExtra("token", mToken);
//                                                        intent.putExtra("_id", _id);
//                                                        intent.putExtra("name", mNamestr);
//                                                    } else {
//                                                        intent = RecordingActivity.newIntent(mContext, RecordingActivity.PlayMode.portrait, lb_url, 1, true, true);
//                                                        intent.setClass(OpenClassDetailActivity.this, RecordingActivity.class);
//                                                        Log.e("TYY", lb_url);
//                                                        intent.putExtra("live_url", lb_url);
//                                                        intent.putExtra("no_course_id", "1");
//                                                    }
//                                                    startActivity(intent);
                                            }

                                            @Override
                                            public void remainFiveMinutes() {

                                            }
                                        });
                                        flag = "1";
                                    } else {
                                        mOrderText.setText("立即观看");
                                        mOrderText.setTextColor(getResources().getColor(R.color.upen_class));
                                        mOrder.setBackgroundResource(R.color.line_color);
                                        flag = "1";
                                    }

                                } else if (getIntent().getExtras().get("recent").equals("recent")) {

//                                        Log.e("TAGSSS", "wocao" + "ddd" + ToolUtils.getTime(time, Long.valueOf(end)).equals("1"));
//                                        Log.e("TAGSSS", "wocao" + ToolUtils.getTime(Long.valueOf(start), time).equals("2") + "ddd");
                                    if (ToolUtils.getTime(Long.valueOf(start), time).equals("2")) {
                                        Log.e("TAGSSS", "weisha2222");
//                                        mLoadTime.setEndTime(Long.valueOf(start));
                                        mOrderText.setText("已预约");
                                        mOrderText.setTextColor(getResources().getColor(R.color.upen_class));
                                        mOrder.setBackgroundResource(R.color.line_color);
                                        flag = "1";
                                        Log.e("TAGSSS", "weisha");
                                    }
                                }
                            }

                        } else {
                            flag = "2";
                            Log.e("*********price", result.getData().getPrice());
                            if ((result.getData().getPrice()).equals("0.00")) {
                                mOrderText.setText("立即预约（免费）");
                                mOrderText.setTextColor(getResources().getColor(R.color.white));
                                mOrder.setBackgroundResource(R.color.buy_color);
                                tag = 0;
                            } else {
                                mOrderText.setTextColor(getResources().getColor(R.color.white));
                                mOrder.setBackgroundResource(R.color.buy_color);
                                mOrderText.setText("立即预约" + (result.getData().getPrice()));
                                tag = 1;
                            }
                        }
                    }
                    LinearLayoutManager manager = new LinearLayoutManager(mContext);
                    manager.setOrientation(LinearLayoutManager.HORIZONTAL);
                    mRecyclerView.setLayoutManager(manager);
                    if (result.getData().getTags().size() > 0) {
                        mRecyclerView.setAdapter(new CommonAdapter<OpenClassDetailResponse.mTags>(mContext, R.layout.item_open_class_detail_layout, result.getData().getTags()) {

                            @Override
                            protected void convert(ViewHolder holder, OpenClassDetailResponse.mTags mTags, int position) {
                                holder.setText(R.id.open_introduce, mTags.getTag_name());
                            }
                        });
                    }

                } else if (result.getStatus_code().equals("401")) {
                    GloableConstant.getInstance().clearAll();
                    ToastUtil.showShortToast("请重新登录");
                    startActivity(new Intent(OpenClassDetailActivity.this, LoginActivity.class));
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
     * 免费课程预约
     */
    private void orderOpenClass() {
        Map<String, Object> map = new HashMap<>();
        map.put("user_id", SPUtils.getSpValues(SPKeyValuesUtils.SP_USER_ID, SPUtils.TYPE_STRING));
        map.put("id", _id);
        map.put("integral", "0");
        map.put("pay_type", "1");
        map.put("type", "4");
        GloableConstant.getInstance().showmeidialog(this);
        Xutil.Post(ApiUrl.CREAT, map, new MyCallBack<CreatResponse>() {
            @Override
            public void onSuccess(CreatResponse result) {
                super.onSuccess(result);
                GloableConstant.getInstance().stopProgressDialog();
                Log.e("*********getStatus_code", result.getStatus_code());
                if (result.getStatus_code().equals("200")) {
                    if (text.equals("now")) {
//                        mOrderText.setText("立即观看");

                        if (ToolUtils.getTime(Long.valueOf(start), time).equals("2")) {
                            mOrderText.setVisibility(View.GONE);
                            mLoadTimeLy.setVisibility(View.VISIBLE);
                            Log.e("*******start", start + "*****");
                            if (start != null) {
                                mLoadTime.setEndTime(Long.valueOf(start));
                            } else {
                                mOrderText.setText("已预约");
                            }
                            mOrder.setBackgroundResource(R.color.line_color);
                            mLoadTime.setClockListener(new CustomDigitalClock.ClockListener() {
                                @Override
                                public void timeEnd() {
                                    openClassDetail();
//                                                    Intent intent=new Intent();
//                                                    if (lb_url.equals("")) {
//                                                        intent.setClass(OpenClassDetailActivity.this, LiveActivity.class);
//                                                        intent.putExtra("live_url", url);
//                                                        intent.putExtra("chat_room_id", mRoomId);
//                                                        intent.putExtra("token", mToken);
//                                                        intent.putExtra("_id", _id);
//                                                        intent.putExtra("name", mNamestr);
//                                                    } else {
//                                                        intent = RecordingActivity.newIntent(mContext, RecordingActivity.PlayMode.portrait, lb_url, 1, true, true);
//                                                        intent.setClass(OpenClassDetailActivity.this, RecordingActivity.class);
//                                                        Log.e("TYY", lb_url);
//                                                        intent.putExtra("live_url", lb_url);
//                                                        intent.putExtra("no_course_id", "1");
//                                                    }
//                                                    startActivity(intent);
                                }

                                @Override
                                public void remainFiveMinutes() {

                                }
                            });
                            flag = "1";
                        } else {
                            mOrderText.setText("立即观看");
                            mOrderText.setTextColor(getResources().getColor(R.color.upen_class));
                            mOrder.setBackgroundResource(R.color.line_color);
                            flag = "1";
                        }
                    } else {
                        mOrderText.setText("已预约");
                    }

                    mOrderText.setTextColor(getResources().getColor(R.color.text_color9));
                    mOrder.setBackgroundResource(R.color.normal_bg);
                    flag = "1";
                } else if (result.getStatus_code().equals("401")) {
                    GloableConstant.getInstance().clearAll();
                    ToastUtil.showShortToast("请重新登录");
                    startActivity(new Intent(OpenClassDetailActivity.this, LoginActivity.class));
                } else {
                    ToastUtil.showShortToast("预约失败，请重新预约");
                }
            }

            @Override
            public void onFinished() {
                super.onFinished();
                GloableConstant.getInstance().stopProgressDialog();
            }
        });
    }

    public void showSharePopWindow() {
        mSharePopWindow = new SharePopWindow(this);
        mSharePopWindow.isShowing();
        final String url = ApiUrl.BASEIMAGE + "api/v1/open_share?open_course_id=" + _id + "&user_id=" + user_id;

        mSharePopWindow.setOnSelectItemListener(new OnSelectItemListener() {
            @Override
            public void selectItem(String name, int type) {
                if (mSharePopWindow != null && mSharePopWindow.isShowing()) {
                    mSharePopWindow.dismiss();

                    switch (type) {
                        case SharePopWindow.POP_WINDOW_ITEM_1:
                            ShareUtils.shareWeb(OpenClassDetailActivity.this, url, title
                                    , description, Image, R.mipmap.ic_launcher, SHARE_MEDIA.WEIXIN
                            );
                            break;
                        case SharePopWindow.POP_WINDOW_ITEM_2:
                            ShareUtils.shareWeb(OpenClassDetailActivity.this, url, title
                                    , description, Image, R.mipmap.ic_launcher, SHARE_MEDIA.WEIXIN_CIRCLE
                            );
                            break;
                        case SharePopWindow.POP_WINDOW_ITEM_3:
                            ShareUtils.shareWeb(OpenClassDetailActivity.this, url, title
                                    , description, Image, R.mipmap.ic_launcher, SHARE_MEDIA.QQ
                            );
                            break;
                        case SharePopWindow.POP_WINDOW_ITEM_4:

                            ShareUtils.shareWeb(OpenClassDetailActivity.this, url, title
                                    , description, Image, R.mipmap.ic_launcher, SHARE_MEDIA.QZONE
                            );
                            break;
                        case SharePopWindow.POP_WINDOW_ITEM_5:
                            ShareUtils.shareWeb(OpenClassDetailActivity.this, url, title
                                    , description, Image, R.mipmap.ic_launcher, SHARE_MEDIA.SMS
                            );
                            break;
                        case SharePopWindow.POP_WINDOW_ITEM_6:

                            ShareUtils.shareWeibo(OpenClassDetailActivity.this, url, title
                                    , description, Image, R.mipmap.ic_launcher, SHARE_MEDIA.SINA
                            );
                            break;
                        default:
                            break;
                    }
                }
            }
        });
    }

    private void onLine(String appname) {
        Map<String, String> map = new HashMap<>();
        map.put("app_name", appname);
        Log.e("OP*********OP", appname);
        Xutil.GET(ApiUrl.ONLINE, map, new MyCallBack<OnLineResponse>() {
            @Override
            public void onSuccess(OnLineResponse result) {
                super.onSuccess(result);
                Log.e("OP*********OP", result.getStatus_code());
                if (result.getStatus_code().equals("200")) {
                    if (result.getData().getIs_online() != null) {
                        // 备注：0直播不在线 1正在直播
                        if (result.getData().getIs_online().equals("0")) {
                            ToastUtil.showShortToast("直播不在线");
                        } else {
                            Intent intent1 = new Intent();
//                            intent.setClass(OpenClassDetailActivity.this, LiveActivity.class);
//                            ToastUtil.showShortToast("88888888");
//                                                        intent.putExtra("url", result.getData().getNow().get(position).getLive_info().getPush_url());
//                            intent.putExtra("chat_room_id",Now.getLive_info().getChat_room_id());
//                            intent.putExtra("token",Now.getLive_info().getToken());
//                            intent.putExtra("live_url",Now.getLive_info().getPush_url());
//                            intent.putExtra("_id", _id);
//                            intent.putExtra("name", Now.getLive_info().getName());
                            intent1.setClass(OpenClassDetailActivity.this, LiveActivity.class);
                            intent1.putExtra("live_url", url);
                            intent1.putExtra("chat_room_id", mRoomId);
                            intent1.putExtra("token", mToken);
                            intent1.putExtra("_id", _id);
                            intent1.putExtra("name", mNamestr);
                            startActivity(intent1);
                        }
                    }
                } else if (result.getStatus_code().equals("401")) {
                    GloableConstant.getInstance().clearAll();
                    ToastUtil.showShortToast("请重新登录");
                    startActivity(new Intent(OpenClassDetailActivity.this, LoginActivity.class));
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
                Log.e("*******mapex", ex.getMessage() + "///////");
            }

            @Override
            public void onFinished() {
                super.onFinished();
            }
        });

    }


}
