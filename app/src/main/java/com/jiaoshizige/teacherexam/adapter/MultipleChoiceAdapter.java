package com.jiaoshizige.teacherexam.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.jiaoshizige.teacherexam.R;
import com.jiaoshizige.teacherexam.base.MBaseAdapter;
import com.jiaoshizige.teacherexam.model.PracticeResponse;
import com.jiaoshizige.teacherexam.utils.URLImageParser;

import java.util.Collections;
import java.util.List;

/**
 * Created by Administrator on 2018/1/a1 0011.
 */

public class MultipleChoiceAdapter extends MBaseAdapter<PracticeResponse.mMetas> {
    private String[] letter = {"A", "B", "C", "D", "E", "F"};
    ListView lv;
    private List<String> mCorrectAnswer;
    public List<PracticeResponse.mMetas> options;//各个选项
    private List<String> myAnswer;
    private boolean myResult = false;

    public MultipleChoiceAdapter(Context context, List<PracticeResponse.mMetas> options, List<String> mCorrectAnswer, ListView lv) {
        super(context);
        this.options = options;
        this.mCorrectAnswer = mCorrectAnswer;
        this.lv = lv;
    }

//    public MultipleChoiceAdapter(Context context) {
//        super(context);
//    }

    public String getText(int position) {
        String text;
        text = letter[position];
        return text;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_subject_multiple_fragment_layout, parent, false);
            holder = new ViewHolder();
            holder.mAnswerContent = (TextView) convertView.findViewById(R.id.answer_content);
            holder.ctv = (RadioButton) convertView.findViewById(R.id.answer_decide);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.ctv.setText(letter[position]);
        URLImageParser imageGetter = new URLImageParser(holder.mAnswerContent);
        holder.mAnswerContent.setText(Html.fromHtml(mList.get(position).getChoose().getText(), imageGetter, null));

        if (myAnswer != null) {
            if (myAnswer.size() == 0) {
                //没有选择
                holder.ctv.setBackgroundResource(R.mipmap.icon_option_normal);
                holder.ctv.setText(letter[position]);
            } else {
                if (myResult) {
                    //查看结果
                    for (int i = 0; i < myAnswer.size(); i++) {
                        boolean has = false;
                        for (int k = 0; k < mCorrectAnswer.size(); k++) {
                            if (myAnswer.get(i).equals(mCorrectAnswer.get(k))) {
                                //选对
                                has = true;
                            }else{
                                //漏选
                                if (mCorrectAnswer.get(k).equals(letter[position])) {
                                    holder.ctv.setBackgroundResource(R.mipmap.icon_option_correct);
                                    holder.ctv.setText("");
                                }

                            }
                        }

                        if (has) {
                            if (myAnswer.get(i).equals(letter[position])) {
                                holder.ctv.setBackgroundResource(R.mipmap.icon_option_correct);
                                holder.ctv.setText("");
                            }
                        } else {
                            //选错
                            if (myAnswer.get(i).equals(letter[position])) {
                                holder.ctv.setBackgroundResource(R.mipmap.icon_option_error);
                                holder.ctv.setText("");
                            }

                        }

                    }
                } else {
                    //判断用户的点击选项
                    for (int i = 0; i < myAnswer.size(); i++) {
                        if (myAnswer.get(i).equals(letter[position])) {
                            holder.ctv.setBackgroundResource(R.mipmap.icon_option_correct);
                            holder.ctv.setText("");
                        }
                    }
                }

            }
        } else {
            //没有点击的选型有：正确选项和没有选择的选项
            holder.ctv.setBackgroundResource(R.mipmap.icon_option_normal);
            holder.ctv.setText(letter[position]);
        }

        //用户查看结果


        return convertView;
    }

    public void getStatus() {
        for (int i = 0; i < options.size(); i++) {
            options.get(i).getChoose().setStatus("1");
        }

        for (int i = 0; i < mCorrectAnswer.size(); i++) {
            options.get(i).getChoose().setConsequence("1");
        }
        notifyDataSetChanged();
    }

    public void getResult(int position) {
        options.get(position).getChoose().setConsequence("1");
        notifyDataSetChanged();
    }


    public void getResulterror(int position) {
        options.get(position).getChoose().setConsequence("0");
        notifyDataSetChanged();
    }

    public void updateBackground(int position, View view) {
        int backgroundId;
        int textcolor;
        if (lv.isItemChecked(position)) {
            backgroundId = R.mipmap.purseovar;

        } else {
            backgroundId = R.mipmap.common_choose;
        }
        Drawable background = mContext.getResources().getDrawable(backgroundId);
        view.setBackgroundDrawable(background);

    }

    class ViewHolder {
        RadioButton ctv;
        TextView mAnswerContent;
    }

    public void setPosition(List<String> myAnswer) {
        this.myAnswer = myAnswer;
    }

    public void setResult() {
        myResult = true;
    }

}
