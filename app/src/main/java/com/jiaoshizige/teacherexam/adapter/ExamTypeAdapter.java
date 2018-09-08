package com.jiaoshizige.teacherexam.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import com.jiaoshizige.teacherexam.R;
import com.jiaoshizige.teacherexam.base.GloableConstant;
import com.jiaoshizige.teacherexam.base.MBaseAdapter;
import com.jiaoshizige.teacherexam.model.ExamTypeResponse;

/**
 * Created by Administrator on 2017/a1/21 0021.
 */

public class ExamTypeAdapter extends MBaseAdapter<ExamTypeResponse.mData> {
    private LayoutInflater mLayoutInflater;


    public ExamTypeAdapter(Context context) {
        super(context);
        mLayoutInflater=LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder mHolder;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.examtype, null);
            mHolder = new ViewHolder();
            mHolder.mText = (TextView) convertView.findViewById(R.id.tv_item);
            mHolder.child = (RadioButton) convertView.findViewById(R.id.rb_item);
            convertView.setTag(mHolder);
        } else {
            mHolder = (ViewHolder) convertView.getTag();
        }
        mHolder.mText.setText(mList.get(position).getName());



        if (clickTemp==position){
            mHolder.child.setChecked(true);
        }else {
            mHolder.child.setChecked(false);
        }

        return convertView;
    }

    private int clickTemp = -1;

    public void setSelection(int position) {
        clickTemp = position;
    }

    public static class ViewHolder {
        TextView mText;
        RadioButton child;
    }

}
