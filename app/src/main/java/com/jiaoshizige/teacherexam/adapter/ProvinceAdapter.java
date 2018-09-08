package com.jiaoshizige.teacherexam.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.jiaoshizige.teacherexam.R;
import com.jiaoshizige.teacherexam.model.provincesResponse;

import java.util.List;

/**
 * Created by Administrator on 2017/7/31.
 */

public class ProvinceAdapter extends BaseAdapter implements SectionIndexer{
    private Context mContext;
    private List<provincesResponse.mProvinces> mList;
    private LayoutInflater inflater;
    public ProvinceAdapter(Context context, List<provincesResponse.mProvinces> list){
        this.mContext = context;
        this.mList = list;
        this.inflater = LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return mList.size();
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
        ViewHolder holder = null;
        final provincesResponse.mProvinces province = mList.get(position);
        if(convertView == null){
            holder = new ViewHolder();
            convertView =  inflater.inflate(R.layout.item_province, null);
            holder.mCataLog = (TextView) convertView.findViewById(R.id.catalog);
            holder.mTitle = (TextView) convertView.findViewById(R.id.title_letter);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        //根据position获取分类的首字母的char ascii值
        int section = getSectionForPosition(position);
        //如果当前位置等于该分类首字母的Char的位置，则认为是第一次出现
        if(position == getPositionForSection(section)){
            holder.mCataLog.setVisibility(View.VISIBLE);
            holder.mCataLog.setText(province.getSortLetters());
        }else{
            holder.mCataLog.setVisibility(View.GONE);
        }
        holder.mTitle.setText(mList.get(position).getProvinceName());
        return convertView;
    }

    @Override
    public Object[] getSections() {
        return null;
    }
    /**
     * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
     * @param
     * @return
     */
    @Override
    public int getPositionForSection(int sectionIndex) {
        for(int i=0;i<getCount();i++){
            String sortStr=mList.get(i).getSortLetters();
            char firstChar=sortStr.toUpperCase().charAt(0);
            
            if(firstChar==sectionIndex)
            {
                return i;
            }
        }
        return -1;
    }
    /**
     * 根据ListView的当前位置获取匪类的首字母的Char ascii值
     * @param position
     * @return
     */
    @Override
    public int getSectionForPosition(int position) {
        return mList.get(position).getSortLetters().charAt(0);
    }
    /**
     * 提取英文的首字母，非英文字母用#代替
     * @param str
     * @return
     */
    private String getAlpha(String str){
        String sortStr=str.trim().substring(0,1).toUpperCase();
        //正则表达式，判断首字母是否是英文字母
        if(sortStr.matches("[A-Z]"))
        {
            return sortStr;
        }
        else
        {
            return "#";
        }
    }
    final static class ViewHolder{
        TextView mCataLog;
        TextView mTitle;
    }
}
