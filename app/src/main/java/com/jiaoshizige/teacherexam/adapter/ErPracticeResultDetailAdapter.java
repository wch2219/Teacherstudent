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
import com.jiaoshizige.teacherexam.base.MBaseAdapter;
import com.jiaoshizige.teacherexam.model.TestResultDetailResponse;
import com.jiaoshizige.teacherexam.utils.URLImageParser;

/**
 * Created by Administrator on 2018/8/10.
 */

public class ErPracticeResultDetailAdapter extends MBaseAdapter<TestResultDetailResponse.mMetas> {
    private String answer;
    private String user_answer;
    private String[] letter = {"A", "B", "C", "D"};
    public ErPracticeResultDetailAdapter(Context context) {
        super(context);
    }

    public ErPracticeResultDetailAdapter(Context context, String answer, String user_answer) {
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
        Log.e("******title", title + "///////");
        holder.mAnswerContent.setText(Html.fromHtml(title, imageGetter, null));
        if (user_answer.equals(letter[position])) {
            if (user_answer.equals(answer)){
                holder.mAnswer.setBackground(ContextCompat.getDrawable(mContext, R.mipmap.tmxq_icon_correct));
                holder.mAnswer.setText("");
                holder.mLayout.setBackgroundResource(R.color.purple7);
            }else {
                holder.mAnswer.setBackground(ContextCompat.getDrawable(mContext, R.mipmap.tmxq_icon_mistake));
                holder.mAnswer.setText("");
                holder.mLayout.setBackgroundResource(R.color.red1);

            }
        } else {
            holder.mAnswer.setBackground(ContextCompat.getDrawable(mContext, R.mipmap.icon_option_circle));
            holder.mAnswer.setText(letter[position]);
            holder.mLayout.setBackgroundResource(R.color.white);
        }

        if (clickTemp == position) {
            holder.mAnswer.setBackground(ContextCompat.getDrawable(mContext, R.mipmap.tmxq_icon_correct));
            holder.mAnswer.setText("");
            holder.mLayout.setBackgroundResource(R.color.purple7);
        }
        return convertView;
    }
    private int clickTemp = -1;

    public void setSelection(int position) {
        clickTemp = position;
    }
    class ViewHolder {
        TextView mAnswer;
        TextView mAnswerContent;
        RelativeLayout mLayout;

    }
}
