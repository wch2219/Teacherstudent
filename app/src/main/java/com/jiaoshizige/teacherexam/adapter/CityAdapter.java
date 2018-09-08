package com.jiaoshizige.teacherexam.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jiaoshizige.teacherexam.R;
import com.jiaoshizige.teacherexam.base.MBaseAdapter;
import com.jiaoshizige.teacherexam.model.provincesResponse;

/**
 * Created by Administrator on 2017/a1/2.
 */

public class CityAdapter extends MBaseAdapter<provincesResponse.mCitys> {
    public CityAdapter(Context context) {
        super(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        viewHolder = new ViewHolder();
      /*  if(convertView == null){
            viewHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.item_provinces_city,null);
            viewHolder.mCity = (TextView) convertView.findViewById(R.id.city);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }*/
        convertView = mInflater.inflate(R.layout.item_provinces_city,null);
        viewHolder.mCity = (TextView) convertView.findViewById(R.id.city);
        viewHolder.mCity.setText(mList.get(position).getCitysName());
        return convertView;
    }
    public static class ViewHolder{
        TextView mCity;
    }
}
