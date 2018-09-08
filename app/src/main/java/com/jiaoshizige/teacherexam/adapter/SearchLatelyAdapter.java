package com.jiaoshizige.teacherexam.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jiaoshizige.teacherexam.R;
import com.jiaoshizige.teacherexam.base.MBaseAdapter;
import com.jiaoshizige.teacherexam.model.SearchResponse;

/**
 * Created by Administrator on 2017/8/5.
 * 最近搜索
 */

public class SearchLatelyAdapter extends MBaseAdapter<SearchResponse.mMyTag> {
    public SearchLatelyAdapter(Context context) {
        super(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if(convertView == null){
            viewHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.item_grid_search,null);
            viewHolder.mItemSearch = (TextView) convertView.findViewById(R.id.item_search);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

            if(mList != null && mList.size() >0){
                viewHolder.mItemSearch.setText(mList.get(position).getKeyword());

            }


        return convertView;
    }
    public static class ViewHolder{
        TextView mItemSearch;
    }
}
