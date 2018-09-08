package com.jiaoshizige.teacherexam.adapter;

import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.ExpandableListAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jiaoshizige.teacherexam.R;
import com.jiaoshizige.teacherexam.activity.ErrorTopicDetailActivity;
import com.jiaoshizige.teacherexam.model.ErrorTopicResponse;

import java.util.List;

/**
 * Created by Administrator on 2018/7/31.
 */

public class ErrorTopicAdapter extends BaseExpandableListAdapter {
    private List<ErrorTopicResponse.mData> mData;
    private List<ErrorTopicResponse.mSon> mSon;
    private LayoutInflater mInflate;
    private Context mContext;


    public ErrorTopicAdapter(List<ErrorTopicResponse.mData> mData, List<ErrorTopicResponse.mSon> mSon, Context mContext) {
        this.mData = mData;
        this.mSon = mSon;
        this.mContext = mContext;
        this.mInflate = LayoutInflater.from(mContext);

        Log.d("***********data", mData.size() + "********-----" + mSon.size());
    }

    @Override
    public int getGroupCount() {
        return mData.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return mSon.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mData.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mData.get(groupPosition).getSon().get(childPosition);
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
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupHolder mHolder = null;
        if (convertView == null) {
            mHolder = new GroupHolder();
            convertView = mInflate.inflate(R.layout.error_topic_group_layout, null);
            mHolder.mNum = convertView.findViewById(R.id.number);
            mHolder.tvTitle = convertView.findViewById(R.id.name);
            convertView.setTag(mHolder);
        } else {
            mHolder = (GroupHolder) convertView.getTag();
        }
        mHolder.tvTitle.setText(mData.get(groupPosition).getName());
        mHolder.mNum.setText(mData.get(groupPosition).getTotle_question() + "道");

        if (!isExpanded) {
            Drawable drawable = ContextCompat.getDrawable(mContext, R.mipmap.ctlx_icon_down);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            mHolder.tvTitle.setCompoundDrawables(drawable, null, null, null);
        } else {
            Drawable drawable = ContextCompat.getDrawable(mContext, R.mipmap.ctlx_icon_up);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            mHolder.tvTitle.setCompoundDrawables(drawable, null, null, null);
        }
        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildHolder mChildHolder;
        if (convertView == null) {
            mChildHolder = new ChildHolder();
            convertView = mInflate.inflate(R.layout.error_topic_childer_layout, null);
            mChildHolder.mChildName = convertView.findViewById(R.id.child_name);
            mChildHolder.mChildNum = convertView.findViewById(R.id.child_number);
            mChildHolder.mItem = convertView.findViewById(R.id.item);
            convertView.setTag(mChildHolder);
        } else {
            mChildHolder = (ChildHolder) convertView.getTag();
        }
        mChildHolder.mChildName.setText(mData.get(groupPosition).getSon().get(childPosition).getName());
        mChildHolder.mChildNum.setText(mData.get(groupPosition).getSon().get(childPosition).getTotle_question() + "道");
        mChildHolder.mItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(mContext, ErrorTopicDetailActivity.class);
                intent.putExtra("id", mData.get(groupPosition).getSon().get(childPosition).getId());
                mContext.startActivity(intent);
            }
        });
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    class GroupHolder {
        TextView mNum;
        TextView tvTitle;
    }

    class ChildHolder {
        TextView mChildName;
        TextView mChildNum;
        RelativeLayout mItem;
    }
}
