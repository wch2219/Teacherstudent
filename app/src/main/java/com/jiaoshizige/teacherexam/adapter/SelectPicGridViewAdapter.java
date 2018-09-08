package com.jiaoshizige.teacherexam.adapter;

/**
 * Created by Administrator on 2017/a1/16 0016.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.jiaoshizige.teacherexam.R;
import com.jiaoshizige.teacherexam.utils.MainContent;

import java.util.ArrayList;
import java.util.List;

/**
 * 选择图片
 */
public class SelectPicGridViewAdapter extends BaseAdapter{
    private Context mContext;
    private List<String> mList=new ArrayList<>();
    private LayoutInflater mLayoutInflater;

    public SelectPicGridViewAdapter(Context mContext, List<String> mList) {
        this.mContext = mContext;
        this.mList = mList;
        mLayoutInflater=LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        int count=mList==null ? 1:mList.size()+1;
        if (count> MainContent.MAX_SELECT_PIC_NUM){
            return mList.size();
        }else {
            return count;
        }
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView=mLayoutInflater.inflate(R.layout.item_gridview_select_picture,parent,false);
        ImageView iv= (ImageView) convertView.findViewById(R.id.select_iv);
        if (position<mList.size()){
            String url=mList.get(position);
            Glide.with(mContext).load(url).into(iv);
        }else {
            iv.setImageResource(R.mipmap.common_addpic);
        }

        return convertView;
    }
}
