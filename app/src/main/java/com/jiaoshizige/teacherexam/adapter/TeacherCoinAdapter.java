package com.jiaoshizige.teacherexam.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jiaoshizige.teacherexam.R;
import com.jiaoshizige.teacherexam.base.MBaseAdapter;
import com.jiaoshizige.teacherexam.model.TeacherCoinResponse;

/**
 * Created by Administrator on 2017/a1/22 0022.
 */

public class TeacherCoinAdapter extends MBaseAdapter<TeacherCoinResponse.mData> {
    private LayoutInflater mLayoutInflater;

    public TeacherCoinAdapter(Context context) {
        super(context);
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder mHolder;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.item_teacher_coin_activity, null);
            mHolder = new ViewHolder();
            mHolder.mCoin = (TextView) convertView.findViewById(R.id.teacher_coin);
            mHolder.mMoney= (TextView) convertView.findViewById(R.id.money);
            mHolder.mLayout= (RelativeLayout) convertView.findViewById(R.id.layout);
            convertView.setTag(mHolder);
        }else {
            mHolder= (ViewHolder) convertView.getTag();
        }
        mHolder.mMoney.setText("￥ "+mList.get(position).getPay_price());
        mHolder.mCoin.setText(mList.get(position).getTeacher_coin());
        if (clickTemp == position) {
            mHolder.mLayout.setBackgroundResource(R.drawable.yellow_select);

        } else {
            mHolder.mLayout.setBackgroundResource(R.color.white);

        }

        return convertView;
    }

    private int clickTemp=-1 ;
    public void setSelection(int position){
        clickTemp = position;
    }

    class ViewHolder {
        TextView mCoin;
        TextView mMoney;
        RelativeLayout mLayout;
    }
}
