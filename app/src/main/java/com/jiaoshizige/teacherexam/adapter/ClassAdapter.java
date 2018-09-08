package com.jiaoshizige.teacherexam.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jiaoshizige.teacherexam.R;
import com.jiaoshizige.teacherexam.base.MBaseAdapter;

/**
 * Created by Administrator on 2017/a1/8 0008.
 */

public class ClassAdapter extends MBaseAdapter {
    private LayoutInflater mLayoutInflater;

    public ClassAdapter(Context context) {
        super(context);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder mHolder;
        if (convertView==null){
            convertView=mInflater.inflate(R.layout.item_class_fragment,null);
            mHolder=new ViewHolder();
            convertView.setTag(mHolder);
        }else {
            mHolder= (ViewHolder) convertView.getTag();
        }

        return convertView;
    }
     class ViewHolder{

     }
}
