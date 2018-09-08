package com.jiaoshizige.teacherexam.activity.aaa;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.jiaoshizige.teacherexam.R;
import com.jiaoshizige.teacherexam.activity.RecruitProblemActivity;
import com.jiaoshizige.teacherexam.model.CheckPointResponse;
import com.jiaoshizige.teacherexam.widgets.CardHandler;
import com.jiaoshizige.teacherexam.widgets.CardViewPager;
import com.jiaoshizige.teacherexam.widgets.ElasticCardView;

/**
 * description
 * <p>
 * Created by sunjian on 2017/6/24.
 */

public class MyCardHandler implements CardHandler<MyBean.mData> {
    private String tag;

    @Override
    public View onBind(final Context context, final MyBean.mData data, final int position, int mode) {
        View view = View.inflate(context, R.layout.item_card_view, null);
        TextView mQid = view.findViewById(R.id.qid);
        TextView mTitle = view.findViewById(R.id.title);
        TextView mIntroduce = view.findViewById(R.id.introduce);
        ImageView mImageView1 = view.findViewById(R.id.crown1);
        ImageView mImageView2 = view.findViewById(R.id.crown2);
        ImageView mImageView3 = view.findViewById(R.id.crown3);
        TextView mButton = view.findViewById(R.id.button);
        TextView mText = view.findViewById(R.id.text);

        if (data.getIs_open().equals("1")) {
            mText.setText("");
            Log.d("**********data", data.getIs_pass() + "/data.getIs_pass()////");
            if (data.getIs_pass().equals("1")) {
                mButton.setText("再次闯关");
                mButton.setBackgroundResource(R.drawable.exam_background);
                mButton.setTextColor(ContextCompat.getColor(context, R.color.white));
                tag = "1";
            } else {
                mButton.setText("开始闯关");
                mButton.setBackgroundResource(R.drawable.exam_background);
                mButton.setTextColor(ContextCompat.getColor(context, R.color.white));
                tag = "";
            }
        } else {
            mText.setText("闯过前面的关卡才能解锁哦");
            mButton.setText("未解锁");
            mButton.setBackgroundResource(R.drawable.exam_background_normal);
            mButton.setTextColor(ContextCompat.getColor(context, R.color.text_color9));
        }
        if (data.getHuangguan_have_totle_num().equals("1")) {
            mImageView1.setImageResource(R.mipmap.crown_select);
            mImageView2.setImageResource(R.mipmap.crown_normal);
            mImageView3.setImageResource(R.mipmap.crown_normal);
        } else if (data.getHuangguan_have_totle_num().equals("2")) {
            mImageView1.setImageResource(R.mipmap.crown_select);
            mImageView2.setImageResource(R.mipmap.crown_select);
            mImageView3.setImageResource(R.mipmap.crown_normal);
        } else if (data.getHuangguan_have_totle_num().equals("3")) {
            mImageView1.setImageResource(R.mipmap.crown_select);
            mImageView2.setImageResource(R.mipmap.crown_select);
            mImageView3.setImageResource(R.mipmap.crown_select);
        } else {
            mImageView1.setImageResource(R.mipmap.crown_normal);
            mImageView2.setImageResource(R.mipmap.crown_normal);
            mImageView3.setImageResource(R.mipmap.crown_normal);
        }
        mQid.setText((position + 1) + "");
        mTitle.setText(data.getName());
        mIntroduce.setText(data.getSmalltext());
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (data.getIs_open().equals("1")) {
                    Intent intent = new Intent();
                    intent.setClass(context, RecruitProblemActivity.class);
                    intent.putExtra("id", data.getId());
                    intent.putExtra("name", data.getName());
                    if (data.getIs_pass().equals("1")) {
                        tag = "1";
                    } else {
                        tag = "";
                    }
                    intent.putExtra("tag", tag);
                    context.startActivity(intent);
                }

            }
        });
        return view;
    }

}
