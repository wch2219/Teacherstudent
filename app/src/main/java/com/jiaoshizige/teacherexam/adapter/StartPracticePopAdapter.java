package com.jiaoshizige.teacherexam.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jiaoshizige.teacherexam.R;
import com.jiaoshizige.teacherexam.base.MBaseAdapter;

import java.util.List;

/**
 * Created by Administrator on 2018/8/7.
 */

public class StartPracticePopAdapter extends BaseAdapter {
    private List<String> mList;
    private Context mContext;
    private LayoutInflater mLayoutInflater;

    public StartPracticePopAdapter(List<String> mList, Context mContext) {
        this.mList = mList;
        this.mContext = mContext;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder mHolder;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.examtype, null);
            mHolder = new ViewHolder();
            mHolder.mText = (TextView) convertView.findViewById(R.id.tv_item);
            mHolder.child = (RadioButton) convertView.findViewById(R.id.rb_item);
            mHolder.mItem = convertView.findViewById(R.id.item);
            convertView.setTag(mHolder);
        } else {
            mHolder = (ViewHolder) convertView.getTag();
        }
        mHolder.mText.setText(mList.get(position));


        if (clickTemp == position) {
            mHolder.child.setChecked(true);
            mHolder.mText.setTextColor(ContextCompat.getColor(mContext, R.color.submitbtn_color));
        } else {
            mHolder.child.setChecked(false);
            mHolder.mText.setTextColor(ContextCompat.getColor(mContext, R.color.text_color3));
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
        RelativeLayout mItem;
    }
}
