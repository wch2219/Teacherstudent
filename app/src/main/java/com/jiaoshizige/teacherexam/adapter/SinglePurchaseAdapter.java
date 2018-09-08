package com.jiaoshizige.teacherexam.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.jiaoshizige.teacherexam.R;
import com.jiaoshizige.teacherexam.base.MBaseAdapter;
import com.jiaoshizige.teacherexam.model.SingleBuyResponse;
import com.jiaoshizige.teacherexam.widgets.CustomExpandableListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/3/15.
 */

public class SinglePurchaseAdapter extends MBaseAdapter<SingleBuyResponse.Data>{
    SingleBuyAdapter mAdapter;
    public SinglePurchaseAdapter(Context context) {
        super(context);
//        mAdapter = new SingleBuyAdapter(mContext);
        Log.e("TAG","ssss"+"sdsd");
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView == null){

            convertView=mInflater.inflate(R.layout.item_single_buy_ex,null);
            holder = new ViewHolder();
            holder.mList = (CustomExpandableListView) convertView.findViewById(R.id.expandable_list_view);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
     /*   //关键代码
        if (parent instanceof CustomExpandableListView) {
            if ((parent).isOnMeasure) {
                return convertView;
            }
        }*/
        Log.e("TAG","ssss"+position);
//        mAdapter.setData(mList.get(position));
//        mAdapter.setChili(mList.get(position).getmChild());
        List<List<SingleBuyResponse.Child>> allChild = new ArrayList<>();
        allChild.add(mList.get(0).getmChild());
        allChild.add(mList.get(1).getmChild());
        mAdapter = new SingleBuyAdapter(mContext,mList.get(position),allChild);
        holder.mList.setAdapter(mAdapter);
        return convertView;
    }
    class ViewHolder{
        CustomExpandableListView mList;
    }
}
