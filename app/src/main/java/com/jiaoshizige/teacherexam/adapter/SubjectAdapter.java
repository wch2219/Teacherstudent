package com.jiaoshizige.teacherexam.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jiaoshizige.teacherexam.R;
import com.jiaoshizige.teacherexam.base.GloableConstant;
import com.jiaoshizige.teacherexam.base.MBaseAdapter;
import com.jiaoshizige.teacherexam.model.PracticeResponse;
import com.jiaoshizige.teacherexam.utils.URLImageParser;
import com.jiaoshizige.teacherexam.widgets.CheckableLayout;

/**
 * Created by Administrator on 2018/1/10 0010.
 */

public class SubjectAdapter extends MBaseAdapter<PracticeResponse.mMetas> {

    private String[] letter = {"A", "B", "C", "D", "E", "F"};
    private String answer;

    public SubjectAdapter(Context context, String answer) {
        super(context);
        this.answer = answer;
        Log.e("*******answer", answer);
    }

    public SubjectAdapter(Context context) {
        super(context);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_subject_fragment_layout, null);
            holder = new ViewHolder();
            holder.mAnswer = (TextView) convertView.findViewById(R.id.answer);
            holder.mAnswerContent = (TextView) convertView.findViewById(R.id.answer_content);
            holder.mLayout = (CheckableLayout) convertView.findViewById(R.id.layout);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.mAnswer.setText(letter[position]);
        URLImageParser imageGetter = new URLImageParser(holder.mAnswerContent);
        holder.mAnswerContent.setText(Html.fromHtml(mList.get(position).getChoose().getText(), imageGetter, null));

        if (clickTemp == -1) {
            //没有选择
            holder.mAnswer.setBackground(ContextCompat.getDrawable(mContext, R.mipmap.icon_option_normal));
            holder.mAnswer.setText(letter[position]);
        } else {
            if (clickTemp == position) {
                //判断用户的点击选项
                if (answer.equals(letter[position])) {
                    holder.mAnswer.setBackground(ContextCompat.getDrawable(mContext, R.mipmap.icon_option_correct));
                    holder.mAnswer.setText("");
                } else {
                    holder.mAnswer.setBackground(ContextCompat.getDrawable(mContext, R.mipmap.icon_option_error));
                    holder.mAnswer.setText("");
                }
            } else {
                if (answer.equals(letter[position])) {
                    holder.mAnswer.setBackground(ContextCompat.getDrawable(mContext, R.mipmap.icon_option_correct));
                    holder.mAnswer.setText("");
                }else{
                    holder.mAnswer.setBackground(ContextCompat.getDrawable(mContext, R.mipmap.icon_option_normal));
                    holder.mAnswer.setText(letter[position]);
                }
            }


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
        CheckableLayout mLayout;


    }

}
