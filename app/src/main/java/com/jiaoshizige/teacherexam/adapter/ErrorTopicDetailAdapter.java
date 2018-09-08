package com.jiaoshizige.teacherexam.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jiaoshizige.teacherexam.R;
import com.jiaoshizige.teacherexam.base.GloableConstant;
import com.jiaoshizige.teacherexam.base.MBaseAdapter;
import com.jiaoshizige.teacherexam.model.ErrrorTopicDetailResponse;
import com.jiaoshizige.teacherexam.model.MessageEvent;
import com.jiaoshizige.teacherexam.utils.URLImageParser;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by Administrator on 2018/8/1.
 */

public class ErrorTopicDetailAdapter extends MBaseAdapter<ErrrorTopicDetailResponse.mMates> {
    private String[] letter = {"A", "B", "C", "D"};
    private String answer;
    private String user_answer;

    public ErrorTopicDetailAdapter(Context context) {
        super(context);
    }

    public ErrorTopicDetailAdapter(Context context, String answer, String user_answer) {
        super(context);
        this.answer = answer;
        this.user_answer = user_answer;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_recruit_proliem_layout, null);
            holder = new ViewHolder();
            holder.mAnswer = (TextView) convertView.findViewById(R.id.answer);
            holder.mAnswerContent = (TextView) convertView.findViewById(R.id.answer_content);
            holder.mLayout = (RelativeLayout) convertView.findViewById(R.id.layout);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.mAnswer.setText(letter[position]);
        URLImageParser imageGetter = new URLImageParser(holder.mAnswerContent);
        String title = mList.get(position).getChoose().getText();
        Log.e("******clickTemp", clickTemp + "///////");
        holder.mAnswerContent.setText(Html.fromHtml(title, imageGetter, null));
        if (clickTemp == position) {
            user_answer=letter[position];
            if (user_answer != null) {
                if (user_answer.equals(letter[position])) {
                    if (user_answer.equals(answer)) {
                        holder.mAnswer.setBackground(ContextCompat.getDrawable(mContext, R.mipmap.tmxq_icon_correct));
                        holder.mAnswer.setText("");
                        holder.mLayout.setBackgroundResource(R.color.purple7);
                    } else {
                        holder.mAnswer.setBackground(ContextCompat.getDrawable(mContext, R.mipmap.tmxq_icon_mistake));
                        holder.mAnswer.setText("");
                        holder.mLayout.setBackgroundResource(R.color.red1);

                    }
                } else {
                    holder.mAnswer.setBackground(ContextCompat.getDrawable(mContext, R.mipmap.icon_option_circle));
                    holder.mAnswer.setText(letter[position]);
                    holder.mLayout.setBackgroundResource(R.color.white);
                }
            } else {
                holder.mAnswer.setBackground(ContextCompat.getDrawable(mContext, R.mipmap.icon_option_circle));
                holder.mAnswer.setText(letter[position]);
                holder.mLayout.setBackgroundResource(R.color.white);
            }



        }


        if (click == position) {
            holder.mAnswer.setBackground(ContextCompat.getDrawable(mContext, R.mipmap.tmxq_icon_correct));
            holder.mAnswer.setText("");
            holder.mLayout.setBackgroundResource(R.color.purple7);
        }
        return convertView;
    }

    private int clickTemp = -1;
    private int click=-1;

    public void setSelection(int position) {
        clickTemp = position;

    }
    public void selection(int position) {
        click = position;

    }

    class ViewHolder {
        TextView mAnswer;
        TextView mAnswerContent;
        RelativeLayout mLayout;

    }
}
