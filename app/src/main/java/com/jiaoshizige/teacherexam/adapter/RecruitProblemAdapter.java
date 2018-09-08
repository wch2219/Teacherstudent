package com.jiaoshizige.teacherexam.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jiaoshizige.teacherexam.R;
import com.jiaoshizige.teacherexam.base.GloableConstant;
import com.jiaoshizige.teacherexam.base.MBaseAdapter;
import com.jiaoshizige.teacherexam.model.MessageEvent;
import com.jiaoshizige.teacherexam.model.RecruitProblemResponse;
import com.jiaoshizige.teacherexam.utils.ToastUtil;
import com.jiaoshizige.teacherexam.utils.URLImageParser;
import com.jiaoshizige.teacherexam.widgets.CheckableLayout;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by Administrator on 2018/7/25.
 */

public class RecruitProblemAdapter extends MBaseAdapter<RecruitProblemResponse.mMetas> {
    private String answer;
    private String[] letter = {"A", "B", "C", "D"};
    private int num = 0;

    public RecruitProblemAdapter(Context context, String answer) {
        super(context);
        this.answer = answer;
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

        if (clickTemp == position) {
            if (answer.equals(letter[position])) {
                holder.mAnswer.setBackground(ContextCompat.getDrawable(mContext, R.mipmap.tmxq_icon_correct));
                holder.mAnswer.setText("");
                holder.mLayout.setBackgroundResource(R.color.purple7);
                if (GloableConstant.getInstance().getCorrectNum() != null) {
                    num = Integer.valueOf(GloableConstant.getInstance().getCorrectNum()) + 1;
                } else {
                    num = num + 1;
                }
                GloableConstant.getInstance().setCorrectNum(String.valueOf(num));
                EventBus.getDefault().post(new MessageEvent("ZUIHOU"));
            } else {
                holder.mAnswer.setBackground(ContextCompat.getDrawable(mContext, R.mipmap.tmxq_icon_mistake));
                holder.mAnswer.setText("");
                holder.mLayout.setBackgroundResource(R.color.red1);
                if (GloableConstant.getInstance().getCorrectNum() != null) {
                    num = Integer.valueOf(GloableConstant.getInstance().getCorrectNum());
                    GloableConstant.getInstance().setCorrectNum(String.valueOf(num));
                } else {
                    GloableConstant.getInstance().setCorrectNum(null);
                }
                notifyDataSetChanged();

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
        RelativeLayout mLayout;

    }
}
