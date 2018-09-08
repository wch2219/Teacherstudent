package com.jiaoshizige.teacherexam.activity;


import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.jiaoshizige.teacherexam.R;
import com.jiaoshizige.teacherexam.model.QuestionBankDetailResponse;
import com.jiaoshizige.teacherexam.utils.ShareUtils;
import com.jiaoshizige.teacherexam.widgets.ArcProgress;
import com.jiaoshizige.teacherexam.widgets.OnSelectItemListener;
import com.jiaoshizige.teacherexam.widgets.SharePopWindow;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by Administrator on 2018/7/18.
 */

public class TestDataActivity extends AppCompatActivity {
    @BindView(R.id.myProgress02)
    ArcProgress mProgress02;
    @BindView(R.id.back_im)
    TextView mBackIn;
    @BindView(R.id.toolbar_subtitle)
    ImageView mShare;
    @BindView(R.id.toolbar_title)
    TextView mTitle;
    @BindView(R.id.title)
    TextView mName;
    @BindView(R.id.breakthrough_progress)
    TextView mBreakthroughProgress;
    @BindView(R.id.more_than)
    TextView mMoreThan;
    @BindView(R.id.schedule_ranking)
    TextView mRanking;
    @BindView(R.id.get_crown)
    TextView mCrown;
    @BindView(R.id.full_crown)
    TextView mFullCrown;
    @BindView(R.id.recruit_num)
    TextView mRecruitNum;
    @BindView(R.id.quantity)
    TextView mQuantity;
    @BindView(R.id.correct)
    TextView mCorrect;

    private QuestionBankDetailResponse.mTotle mTotle;
    private String name;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_data_layout);
        ButterKnife.bind(this);
        mTitle.setText("题库数据");
        if (getIntent().getStringExtra("name") != null) {
            name = getIntent().getStringExtra("name");
        } else {
            name = "";
        }
        if (!name.equals("")) {
            mName.setText(name);
        }
        if (getIntent().getExtras().get("mTotle") != null) {
            mTotle = (QuestionBankDetailResponse.mTotle) getIntent().getExtras().get("mTotle");
            mBreakthroughProgress.setText(mTotle.getGuanqia_totle_pass_num() + "/" + mTotle.getGuanqia_totle_num());
            mMoreThan.setText("超过" + mTotle.getRank_percent() + "%的学霸");
            mRanking.setText(mTotle.getPaiming() + "名/" + mTotle.getTotle_user());
            mCrown.setText(mTotle.getHuangguan_have_totle_num() + "/" + mTotle.getHuangguan_totle_num());
            mFullCrown.setText("满星通过" + mTotle.getFull_huangguan() + "个关卡");
            mQuantity.setText(mTotle.getDatiliang());
            mCorrect.setText(mTotle.getAccuracy() + "%");
            mProgress02.setOnCenterDraw(new ArcProgress.OnCenterDraw() {
                @Override
                public void draw(Canvas canvas, RectF rectF, float x, float y, float storkeWidth, int progress) {
                    Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
                    textPaint.setStrokeWidth(35);
                    textPaint.setTextSize(25);
                    textPaint.setColor(getResources().getColor(R.color.white));
                    String progressStr = mTotle.getPercent() + "%";
                    float textX = x - (textPaint.measureText(progressStr) / 2);
                    float textY = y - ((textPaint.descent() + textPaint.ascent()) / 2);
                    canvas.drawText(progressStr, textX, textY, textPaint);
                }
            });
            mProgress02.setProgress(Integer.valueOf(mTotle.getPercent()));
        }
        Log.d("/////////mTotle", mTotle.getFull_huangguan() + "/////////");

        mBackIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha 屏幕透明度0.0-1.0 1表示完全不透明
     */
    public void setBackgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha;
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        getWindow().setAttributes(lp);
    }


    class poponDismissListener implements PopupWindow.OnDismissListener {

        @Override
        public void onDismiss() {
            setBackgroundAlpha(1f);
        }
    }

    SharePopWindow mSharePopWindow;
//    public void showSharePopWindow() {
//        mSharePopWindow = new SharePopWindow(this);
//        mSharePopWindow.isShowing();
//        final String url=ApiUrl.BASEIMAGE +"api/v1/course_share?type_id="+mType+"&course_id="+mCourseId+"&user_id="+user_id;
//        mSharePopWindow.setOnSelectItemListener(new OnSelectItemListener() {
//            @Override
//            public void selectItem(String name, int type) {
//                if (mSharePopWindow != null && mSharePopWindow.isShowing()) {
//                    mSharePopWindow.dismiss();
//
//                    switch (type) {
//                        case SharePopWindow.POP_WINDOW_ITEM_1:
//                            ShareUtils.shareWeb(TestDataActivity.this, url, title
//                                    , title, mImage, R.mipmap.ic_launcher, SHARE_MEDIA.WEIXIN
//                            );
//                            break;
//                        case SharePopWindow.POP_WINDOW_ITEM_2:
//                            ShareUtils.shareWeb(TestDataActivity.this, url,title
//                                    , title, mImage, R.mipmap.ic_launcher, SHARE_MEDIA.WEIXIN_CIRCLE
//                            );
//                            break;
//                        case SharePopWindow.POP_WINDOW_ITEM_3:
//                            ShareUtils.shareWeb(TestDataActivity.this, url, title
//                                    , title, mImage, R.mipmap.ic_launcher, SHARE_MEDIA.QQ
//                            );
//                            break;
//                        case SharePopWindow.POP_WINDOW_ITEM_4:
//
//                            ShareUtils.shareWeb(TestDataActivity.this, url,title
//                                    , title, mImage, R.mipmap.ic_launcher, SHARE_MEDIA.QZONE
//                            );
//                            break;
//                        case SharePopWindow.POP_WINDOW_ITEM_5:
//                            ShareUtils.shareWeb(TestDataActivity.this, url,title
//                                    , title, mImage, R.mipmap.ic_launcher, SHARE_MEDIA.SMS
//                            );
//                            break;
//                        case SharePopWindow.POP_WINDOW_ITEM_6:
//                            Log.e("wwwww","www1");
//                            ShareUtils.shareWeibo(TestDataActivity.this, url, title
//                                    , title, mImage, R.mipmap.ic_launcher, SHARE_MEDIA.SINA
//                            );
//                            break;
//                        default:
//                            break;
//                    }
//                }
//            }
//        });
//    }
}
