package com.jiaoshizige.teacherexam.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.jiaoshizige.teacherexam.R;
import com.jiaoshizige.teacherexam.base.GloableConstant;
import com.jiaoshizige.teacherexam.base.MBaseAdapter;
import com.jiaoshizige.teacherexam.model.EvaluationReportResponse;

/**
 * Created by Administrator on 2018/1/4 0004.
 */

public class ImageAdapter extends MBaseAdapter<EvaluationReportResponse.Other> {

    public ImageAdapter(Context context) {
        super(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView==null){
            holder=new ViewHolder();
            convertView=mInflater.inflate(R.layout.item_image,null);
            holder.mHeadImage= (ImageView) convertView.findViewById(R.id.head_img);
            convertView.setTag(holder);
        }else {
            holder= (ViewHolder) convertView.getTag();
        }
        Glide.with(mContext).load(mList.get(position).getAvatar()).apply(GloableConstant.getInstance().getOptions()).into(holder.mHeadImage);
        return convertView;
    }
    class ViewHolder{
        ImageView mHeadImage;
    }
}
