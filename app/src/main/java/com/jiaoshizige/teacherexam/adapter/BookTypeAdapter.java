package com.jiaoshizige.teacherexam.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import com.jiaoshizige.teacherexam.R;
import com.jiaoshizige.teacherexam.base.MBaseAdapter;
import com.jiaoshizige.teacherexam.model.NewCatBookResponse;

/**
 * Created by Administrator on 2018/3/30 0030.
 */

public class BookTypeAdapter extends MBaseAdapter<NewCatBookResponse.mData> {
    private LayoutInflater mLayoutInflater;

    public BookTypeAdapter(Context context) {
        super(context);
        mLayoutInflater = LayoutInflater.from(context);
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
        mHolder.mText.setText(mList.get(position).getCat_name());
        if (clickTemp==position){
            mHolder.child.setChecked(true);
            mHolder.mText.setTextColor(ContextCompat.getColor(mContext,R.color.purple4));
        }else {
            mHolder.child.setChecked(false);
            mHolder.mText.setTextColor(ContextCompat.getColor(mContext,R.color.text_color3));
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
