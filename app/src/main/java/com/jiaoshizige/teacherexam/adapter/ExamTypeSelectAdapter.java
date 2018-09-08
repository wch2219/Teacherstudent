package com.jiaoshizige.teacherexam.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import com.jiaoshizige.teacherexam.R;
import com.jiaoshizige.teacherexam.base.MBaseAdapter;
import com.jiaoshizige.teacherexam.model.ExamTypeResponse;

/**
 * Created by Administrator on 2018/7/20.
 */

public class ExamTypeSelectAdapter extends MBaseAdapter<ExamTypeResponse.mData> {
    public ExamTypeSelectAdapter(Context context) {
        super(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
       ViewHolder mHolder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_updata_message_pop, null);
            mHolder = new ViewHolder();
            mHolder.mPrimary = (TextView) convertView.findViewById(R.id.primary);
            convertView.setTag(mHolder);
        } else {
            mHolder = (ViewHolder) convertView.getTag();
        }
        mHolder.mPrimary.setText(mList.get(position).getName());
        if (position==clickTemp){
            mHolder.mPrimary.setBackgroundResource(R.drawable.exam_background);
            mHolder.mPrimary.setTextColor(ContextCompat.getColor(mContext,R.color.white));
        }else {
            mHolder.mPrimary.setBackgroundResource(R.drawable.exam_background_purse);
            mHolder.mPrimary.setTextColor(ContextCompat.getColor(mContext,R.color.purple4));
        }
        return convertView;
    }
    private int clickTemp = -1;

    public void setSelection(int position) {
        clickTemp = position;
    }
    public static class ViewHolder {
        TextView mPrimary;

    }
}
