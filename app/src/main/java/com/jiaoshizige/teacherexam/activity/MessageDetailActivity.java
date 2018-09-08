package com.jiaoshizige.teacherexam.activity;

import android.text.Html;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.jiaoshizige.teacherexam.R;
import com.jiaoshizige.teacherexam.base.BaseActivity;
import com.jiaoshizige.teacherexam.base.GloableConstant;
import com.jiaoshizige.teacherexam.http.ApiUrl;
import com.jiaoshizige.teacherexam.http.MyCallBack;
import com.jiaoshizige.teacherexam.http.SupportResponse;
import com.jiaoshizige.teacherexam.http.Xutil;
import com.jiaoshizige.teacherexam.model.PushDatailResponse;
import com.jiaoshizige.teacherexam.utils.SPKeyValuesUtils;
import com.jiaoshizige.teacherexam.utils.SPUtils;
import com.jiaoshizige.teacherexam.utils.ToastUtil;
import com.jiaoshizige.teacherexam.widgets.MyWebView;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/12/21 0021.
 */

public class MessageDetailActivity extends BaseActivity {
    @BindView(R.id.title)
    TextView mTitle;
    @BindView(R.id.detail)
    MyWebView mDatail;
    @BindView(R.id.number)
    TextView mNum;
    @BindView(R.id.zan)
    LinearLayout mZan;
    @BindView(R.id.zan_image)
    ImageView mImage;
    @BindView(R.id.zan_num)
    TextView mZanNum;

    private String id;
    private String user_id;
    private String detail;
    private int zanNum;
    private String mIsLike;
    private Html.ImageGetter imageGetter;

    @Override
    protected int getLayoutId() {
        return R.layout.message_datail_layout;
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("系统公告");
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("系统公告");
        MobclickAgent.onPause(this);
    }

    @Override
    protected void initView() {
        setSubTitle().setText("");
        setToolbarTitle().setText("系统公告");
        user_id = (String) SPUtils.getSpValues(SPKeyValuesUtils.SP_USER_ID, SPUtils.TYPE_STRING);
        if (getIntent().getStringExtra("_id") != null) {
            id = getIntent().getStringExtra("_id");
            deleteMessage(id);
        }

        if (getIntent().getStringExtra("detail")!=null){
            detail=getIntent().getStringExtra("detail");
        }
//        if (getIntent().getStringExtra("message_id")!=null){
//            deleteMessage(getIntent().getStringExtra("message_id"));
//        }
        mZan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                like();
            }
        });
//        WebSettings webSettings = mDatail.getSettings();
//        webSettings.setJavaScriptEnabled(true);
//        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        pushDetail();


//        WebSettings webSettings = holder.mImage.getSettings();

//        webSettings.setJavaScriptEnabled(true);
//        String test = "<style type='text/css'>p{margin: 0;padding: 0;}img {max-width:100%!important;height:auto!important;font-size:14px;}</style>"+mList.get(position).getDetail();
//        holder.mImage.loadDataWithBaseURL(null, test, "text/html", "utf-8", null);
    }

    /**
     * 推送详情
     */
    private void pushDetail() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("user_id", user_id);
        GloableConstant.getInstance().startProgressDialog(this);
        Xutil.Post(ApiUrl.PUSHDETAIL, map, new MyCallBack<PushDatailResponse>() {
            @Override
            public void onSuccess(PushDatailResponse result) {
                super.onSuccess(result);
                GloableConstant.getInstance().stopProgressDialog1();
                if (result.getStatus_code().equals("200")) {
                    mTitle.setText(result.getData().getTitle());
                    WebSettings webSettings = mDatail.getSettings();
                    webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
                    webSettings.setJavaScriptEnabled(true);
                    String test = "<style type='text/css'>p{margin: 0;padding: 0;}img {max-width:100%!important;height:auto!important;font-size:14px;}</style>"+detail;
                    mDatail.loadDataWithBaseURL(null, test, "text/html", "utf-8", null);
//                    String test = "<style type='text/css'>img {max-width:100%!important;height:auto!important;font-size:14px;}</style>" + result.getData().getDetail();
//                    mDatail.loadDataWithBaseURL(null, test, "text/html", "utf-8", null);
                    mNum.setText(result.getData().getCount_view() + "人已看");
                    mZanNum.setText(result.getData().getCount_zan() + "");
                    zanNum = result.getData().getCount_zan();
                    mIsLike = result.getData().getIs_like();
                    Log.e("***********mIsLike11111",mIsLike);
                    if (result.getData().getIs_like().equals("1")) {
                        mImage.setImageResource(R.mipmap.common_like_pre);
                    } else {
                        mImage.setImageResource(R.mipmap.common_like);
                    }
                }
            }
        });
    }


    /**
     * 点赞
     */
    private void like() {
        Map<String, Object> map = new HashMap<>();
        map.put("user_id", user_id);
        map.put("be_like_id", id);
        map.put("type", 6);
        Log.e("*********map",map.toString());
        GloableConstant.getInstance().startProgressDialog(this);
        Xutil.Post(ApiUrl.GETZAN, map, new MyCallBack<SupportResponse>() {
            @Override
            public void onSuccess(SupportResponse result) {
                super.onSuccess(result);
                Log.e("****result",result.getStatus_code());
                GloableConstant.getInstance().stopProgressDialog();
                Log.e("***********mIsLike",mIsLike);
                if (mIsLike.equals("1")) {
                    if (result.getStatus_code().equals("205")) {
                        ToastUtil.showShortToast(result.getMessage());
                        mZanNum.setText((zanNum - 1) + "");
                        mImage.setImageResource(R.mipmap.common_like);
                        mIsLike="0";
                        zanNum=zanNum - 1;
                    }
                } else {
                    if (result.getStatus_code().equals("204")) {
                        ToastUtil.showShortToast(result.getMessage());
                        mZanNum.setText((zanNum + 1) + "");
                        mImage.setImageResource(R.mipmap.common_like_pre);
                    }
                    if (result.getStatus_code().equals("205")) {
                        ToastUtil.showShortToast(result.getMessage());
                        mZanNum.setText((zanNum - 1 + 1) + "");
                        mImage.setImageResource(R.mipmap.common_like);
                    }
                }

            }
        });
    }
    /**
     * 查看推送消息
     *
     * @param
     */
    private void deleteMessage(String message_id) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", message_id);
        map.put("user_id", user_id);
        map.put("type", 1);
        Xutil.Post(ApiUrl.PUSHDELETE, map, new MyCallBack<SupportResponse>() {
            @Override
            public void onSuccess(SupportResponse result) {
                super.onSuccess(result);
                if (result.getStatus_code().equals("204")) {
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
            }
        });
    }
}
