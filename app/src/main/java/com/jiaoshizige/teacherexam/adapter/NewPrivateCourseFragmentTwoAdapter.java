package com.jiaoshizige.teacherexam.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jiaoshizige.teacherexam.R;
import com.jiaoshizige.teacherexam.base.MBaseAdapter;
import com.jiaoshizige.teacherexam.model.NewCourseResponse;

/**
 * Created by Administrator on 2018/7/11.
 */

public class NewPrivateCourseFragmentTwoAdapter extends MBaseAdapter<NewCourseResponse.mNozero> {

    public NewPrivateCourseFragmentTwoAdapter(Context context) {
        super(context);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder mHolder;
        if (convertView == null) {
            mHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.item_new_course_list, null);
            mHolder.mTitle = convertView.findViewById(R.id.class_name);
            mHolder.mTeacher = convertView.findViewById(R.id.teacher_name);
            mHolder.mAssistant = convertView.findViewById(R.id.assistant_name);
            mHolder.mPrice = convertView.findViewById(R.id.price);
            mHolder.mMarketPrice = convertView.findViewById(R.id.market_price);
            mHolder.mType = convertView.findViewById(R.id.course_type);
            mHolder.mNum = convertView.findViewById(R.id.learn_num);
            mHolder.mImage = convertView.findViewById(R.id.image);
            convertView.setTag(mHolder);
        } else {
            mHolder = (ViewHolder) convertView.getTag();
        }

//        if(mList.get(position).getType().equals("1")){
//            mHolder.mType.setBackgroundResource(R.drawable.purple_bottom_shape5);
//        }else if(mList.get(position).getType().equals("2")){
//            mHolder.mType.setBackgroundResource(R.drawable.orange_bottom_shape5);
//        }else if(mList.get(position).getType().equals("3")){
//            mHolder.mType.setBackgroundResource(R.drawable.red_bottom_shape5);
//        }else{
//            mHolder.mType.setBackgroundResource(R.drawable.orange_bottom_shape5);
//        }
        if (mList.get(position).getCid().equals("11")) {
            mHolder.mType.setBackgroundResource(R.drawable.orange_bottom_shape5);
        } else if (mList.get(position).getCid().equals("12")) {
            mHolder.mType.setBackgroundResource(R.drawable.purple_bottom_shape5);
        } else {
            mHolder.mType.setBackgroundResource(R.drawable.orange_bottom_shape5);
        }
        mHolder.mTitle.setText(mList.get(position).getGroup_name());
        mHolder.mTeacher.setText(mList.get(position).getTeacher_name());
        mHolder.mAssistant.setText(mList.get(position).getAssistant_name());
        mHolder.mPrice.setText(mList.get(position).getPrice());
        mHolder.mMarketPrice.setText("￥" + mList.get(position).getMarket_price());
        mHolder.mMarketPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        mHolder.mType.setText(mList.get(position).getBq());
        mHolder.mNum.setText(mList.get(position).getSale_num());
        Glide.with(mContext).load(mList.get(position).getBq_img()).into(mHolder.mImage);
        return convertView;
    }

    class ViewHolder {
        TextView mTitle;
        TextView mTeacher;
        TextView mAssistant;
        TextView mPrice;
        TextView mMarketPrice;
        TextView mType;
        ImageView mImage;
        TextView mNum;
    }
}
