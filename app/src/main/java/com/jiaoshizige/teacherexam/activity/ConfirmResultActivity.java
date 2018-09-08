package com.jiaoshizige.teacherexam.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jiaoshizige.teacherexam.R;
import com.jiaoshizige.teacherexam.base.BaseActivity;
import com.jiaoshizige.teacherexam.model.RecruitPostAnswerResponse;
import com.jiaoshizige.teacherexam.utils.Defaultcontent;
import com.jiaoshizige.teacherexam.utils.ShareUtils;
import com.jiaoshizige.teacherexam.utils.ToastUtil;
import com.jiaoshizige.teacherexam.widgets.OnSelectItemListener;
import com.jiaoshizige.teacherexam.widgets.SharePopWindow;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/8/10.
 */

public class ConfirmResultActivity extends BaseActivity implements View.OnClickListener{
    @BindView(R.id.top)
    LinearLayout mTop;
    @BindView(R.id.image)
    ImageView mImage;
    @BindView(R.id.start1)
    ImageView mStar1;
    @BindView(R.id.start2)
    ImageView mStar2;
    @BindView(R.id.start3)
    ImageView mStar3;
    @BindView(R.id.content)
    TextView mContent;
    @BindView(R.id.share)
    TextView mShare;
    @BindView(R.id.check)
    TextView mCheck;
    @BindView(R.id.repaly)
    TextView mRepaly;
    @BindView(R.id.next)
    TextView mNext;

    private RecruitPostAnswerResponse.mData mData;
    private Context context;
    int mNumber60;
    int mNumber80;
    int user_score;
    private String flag;
    private Intent intent;
    private String id;
    SharePopWindow mSharePopWindow;

    @Override
    protected int getLayoutId() {
        return R.layout.confirm_result_layout;
    }

    @Override
    protected void initView() {
        context =this;
        setSubTitle().setText("");
        intent=new Intent();
        if (getIntent().getStringExtra("id")!=null){
            id=getIntent().getStringExtra("id");
        }else {
            id="";
        }
        if (getIntent().getExtras().get("data") != null) {
            mData = (RecruitPostAnswerResponse.mData) getIntent().getExtras().get("data");
            setToolbarTitle().setText(mData.getName());
            mNumber60= (int) (Integer.valueOf(mData.getTotal_score())*0.6);
            mNumber80= (int) (Integer.valueOf(mData.getTotal_score())*0.8);
            Log.d("**************number", mNumber60+"------------"+mNumber80+"--------------"+mData.getPass_score());
            user_score=Integer.valueOf(mData.getPass_score());
            if (mData.getIs_pass().equals("1")){
                flag="1";//闯关成功
                mShare.setText("炫耀一下");

                mTop.setBackgroundResource(R.mipmap.shunlitongguan_png_bg);
                mImage.setImageResource(R.mipmap.img_victory);
                mContent.setText("共"+mData.getTotal_score()+"道题,答对"+mData.getPass_score()+"道题,答错"+mData.getError_score()+"道题");
                Log.d("*********", "initView: ");
                if (Integer.valueOf(mData.getPass_score())>=mNumber60&&Integer.valueOf(mData.getPass_score())<mNumber80){
                    Log.d("*********比较", "initView: chengg");
                    mStar1.setImageResource(R.mipmap.shunlitongguani_png_crow_middle);
                    mStar2.setImageResource(R.mipmap.chuangguanshibai_png_crown_middle);
                    mStar3.setImageResource(R.mipmap.chuangguanshibai_png_crown_middle);
                }else {
                    Log.d("*********比较", "initView: shibai");
                }

                if (Integer.valueOf(mData.getPass_score())>=mNumber80&&Integer.valueOf(mData.getPass_score())<Integer.valueOf(mData.getTotal_score())){
                    mStar1.setImageResource(R.mipmap.shunlitongguani_png_crow_middle);
                    mStar2.setImageResource(R.mipmap.shunlitongguani_png_crow_middle);
                    mStar3.setImageResource(R.mipmap.chuangguanshibai_png_crown_middle);
                }
//
                if (mData.getPass_score().equals(mData.getTotal_score())){
                    mStar1.setImageResource(R.mipmap.shunlitongguani_png_crow_middle);
                    mStar2.setImageResource(R.mipmap.shunlitongguani_png_crow_middle);
                    mStar3.setImageResource(R.mipmap.shunlitongguani_png_crow_middle);
                }
            }else {
                flag="0";//闯关失败
                mShare.setText("求安慰");
                mNext.setTextColor(ContextCompat.getColor(context,R.color.text_color10));
                mTop.setBackgroundResource(R.mipmap.chuangguanshibai_png_bg);
                mImage.setImageResource(R.mipmap.img_defeat);
                mContent.setText("共"+mData.getTotal_score()+"道题,答对"+mData.getPass_score()+"道题,答错"+mData.getError_score()+"道题");
                mStar1.setImageResource(R.mipmap.chuangguanshibai_png_crown_middle);
                mStar2.setImageResource(R.mipmap.chuangguanshibai_png_crown_middle);
                mStar3.setImageResource(R.mipmap.chuangguanshibai_png_crown_middle);

            }
            mShare.setOnClickListener(this);
            mCheck.setOnClickListener(this);
            mRepaly.setOnClickListener(this);
            mNext.setOnClickListener(this);


        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.share:
                showPopWindow();
                break;
            case R.id.check:
                intent.setClass(ConfirmResultActivity.this,MyPracticeNoteDetailActivity.class);
                intent.putExtra("id",mData.getId());
                intent.putExtra("name",mData.getName());
                startActivity(intent);
                finish();
                break;
            case R.id.repaly:
                intent.setClass(context,RecruitProblemActivity.class);
                intent.putExtra("id",id);
                intent.putExtra("name",mData.getName());
                intent.putExtra("tag","1");
                startActivity(intent);
                finish();
                break;
            case R.id.next:
                if (flag.equals("1")){
                    intent.setClass(context,RecruitProblemActivity.class);
                    intent.putExtra("id",mData.getGuanqia_next_id());
                    intent.putExtra("name",mData.getName());
                    startActivity(intent);
                    finish();
                }else {
                    ToastUtil.showShortToast("您还未通关");
                }

                break;
        }
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
                            ShareUtils.shareWeb((Activity) context, Defaultcontent.url, Defaultcontent.title
                                    , Defaultcontent.text, Defaultcontent.imageurl, R.mipmap.ic_launcher, SHARE_MEDIA.WEIXIN
                            );
                            break;
                        case SharePopWindow.POP_WINDOW_ITEM_2:
//                            ToastUtil.showShortToast("朋友圈");
                            ShareUtils.shareWeb((Activity) context, Defaultcontent.url, Defaultcontent.title
                                    , Defaultcontent.text, Defaultcontent.imageurl, R.mipmap.ic_launcher, SHARE_MEDIA.WEIXIN_CIRCLE
                            );
//                            ToastUtil.showShortToast("朋友圈");
                            break;
                        case SharePopWindow.POP_WINDOW_ITEM_3:
//                            ToastUtil.showShortToast("QQ");
                            ShareUtils.shareWeb((Activity) context, Defaultcontent.url, Defaultcontent.title
                                    , Defaultcontent.text, Defaultcontent.imageurl, R.mipmap.ic_launcher, SHARE_MEDIA.QQ
                            );
                            break;
                        case SharePopWindow.POP_WINDOW_ITEM_4:
                            ToastUtil.showShortToast("QQ空间");
                            ShareUtils.shareWeb((Activity) context, Defaultcontent.url, Defaultcontent.title
                                    , Defaultcontent.text, Defaultcontent.imageurl, R.mipmap.ic_launcher, SHARE_MEDIA.QZONE
                            );
                            break;
                        case SharePopWindow.POP_WINDOW_ITEM_5:
                            ShareUtils.shareWeb((Activity) context, Defaultcontent.url, Defaultcontent.title
                                    , Defaultcontent.text, Defaultcontent.imageurl, R.mipmap.ic_launcher, SHARE_MEDIA.SMS
                            );
                            ToastUtil.showShortToast("短信");
                            break;
                        case SharePopWindow.POP_WINDOW_ITEM_6:
//                            ToastUtil.showShortToast("新浪微博");
                            ShareUtils.shareWeibo((Activity) context, Defaultcontent.url, Defaultcontent.title
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
