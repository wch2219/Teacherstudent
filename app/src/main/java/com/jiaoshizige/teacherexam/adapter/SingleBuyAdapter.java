package com.jiaoshizige.teacherexam.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jiaoshizige.teacherexam.R;
import com.jiaoshizige.teacherexam.model.SingleBuyResponse;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * Created by Administrator on 2018/3/15.
 */

public class SingleBuyAdapter extends BaseExpandableListAdapter {
    private SingleBuyResponse.Data mData;
    private List<List<SingleBuyResponse.Child>> mChilds;
    private Context mContext;
    public SingleBuyAdapter(Context context,SingleBuyResponse.Data datas,List<List<SingleBuyResponse.Child>> childs) {
        this.mContext = context;
        this.mChilds = childs;
        this.mData = datas;
    }
    public SingleBuyResponse.Data setData(SingleBuyResponse.Data datas){
        mData = datas;
        return mData;
    }
    /*public List<SingleBuyResponse.Child> setChili(List<SingleBuyResponse.Child> childs){
        mChilds = childs;
        Log.e("TAGfff",mChilds.size()+"");
        return mChilds;
    }*/

    @Override
    public int getGroupCount() {
        return 1;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return mChilds.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mData;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mChilds.get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupHolder holder = null;
        if (convertView == null) {
            holder = new GroupHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_courser_catalog_fragment,null);
            holder.mCourseTitle = (TextView) convertView.findViewById(R.id.course_title);
            holder.mCatalogArrow = (ImageView) convertView.findViewById(R.id.catalog_arrow);
            convertView.setTag(holder);
        } else {
            holder = (GroupHolder) convertView.getTag();
        }

        //判断是否已经打开列表
        if (isExpanded) {
            holder.mCatalogArrow.setImageResource(R.mipmap.cehua_arrow_small_up);
        } else {
            holder.mCatalogArrow.setImageResource(R.mipmap.cehua_arrow_small_down);
        }

        holder.mCourseTitle.setText(mData.getTitle());
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
       ChildHolder holder = null;

        if (convertView == null) {
            holder = new ChildHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_course_catalog_content_list,null);
            holder.mCataLogList = (RecyclerView) convertView.findViewById(R.id.catalog_list);
            convertView.setTag(holder);
        } else {
            holder = (ChildHolder) convertView.getTag();
        }
        holder.mCataLogList.setLayoutManager(new LinearLayoutManager(mContext));
        holder.mCataLogList.setAdapter(new CommonAdapter<SingleBuyResponse.Child>(mContext,R.layout.item_catalog_content,mChilds.get(groupPosition)) {
            @Override
            protected void convert(ViewHolder holder, SingleBuyResponse.Child mChild, int position) {
                holder.setText(R.id.content_title,(position+1)+"."+mChild.getTitle());
            }
        });
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {

        return true;
    }
    class GroupHolder {
        public TextView mCourseTitle;
        public ImageView mCatalogArrow;
    }
    class ChildHolder {
        public RecyclerView mCataLogList;
        public ImageView sound;
    }
}
