package com.jiaoshizige.teacherexam.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jiaoshizige.teacherexam.R;

import java.util.List;

/**
 * Created by Administrator on 2017/12/29 0029.
 */

public class GridviewAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private List<String> mList;

    public GridviewAdapter(Context mContext, List<String> mList) {
        this.mContext = mContext;
        this.mList = mList;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int i) {
        return mList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View contentview, ViewGroup viewGroup) {
        ViewHolder holder;

        contentview = mLayoutInflater.inflate(R.layout.item, null);
//        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        layoutParams.gravity = Gravity.CENTER_HORIZONTAL;
//        contentview.setLayoutParams(layoutParams);
        holder = new ViewHolder();
        holder.textView = (TextView) contentview.findViewById(R.id.text);
        holder.vertical = (ImageView) contentview.findViewById(R.id.vertical);
        holder.horizontal = (ImageView) contentview.findViewById(R.id.horizontal);
        holder.textView.setText(mList.get(i));
        switch (i) {
            case 3:
                holder.horizontal.setVisibility(View.GONE);
                holder.vertical.setVisibility(View.VISIBLE);
                break;
            case 4:
                holder.horizontal.setVisibility(View.VISIBLE);
                holder.vertical.setVisibility(View.VISIBLE);
                break;
            case 7:
                holder.horizontal.setVisibility(View.GONE);
                break;
            case 11:
                holder.horizontal.setVisibility(View.GONE);
                holder.vertical.setVisibility(View.VISIBLE);
                break;
            case 12:
                holder.textView.setVisibility(View.INVISIBLE);
                holder.vertical.setVisibility(View.INVISIBLE);
                holder.horizontal.setVisibility(View.INVISIBLE);
                break;
            case 15:
                holder.horizontal.setVisibility(View.GONE);
                holder.vertical.setVisibility(View.GONE);
                break;
            default:
                holder.horizontal.setVisibility(View.VISIBLE);
                holder.vertical.setVisibility(View.GONE);
                break;
        }
        return contentview;
    }

    class ViewHolder {
        TextView textView;
        ImageView horizontal,vertical;
    }
}


