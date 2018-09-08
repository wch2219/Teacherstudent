package com.jiaoshizige.teacherexam.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jiaoshizige.teacherexam.R;
import com.jiaoshizige.teacherexam.base.MBaseAdapter;
import com.jiaoshizige.teacherexam.model.MyCouponResponse;
import com.jiaoshizige.teacherexam.utils.TimeUtils;
import com.jiaoshizige.teacherexam.utils.ToolUtils;

import retrofit2.http.POST;

/**
 * Created by Administrator on 2017/a1/22 0022.
 */

public class MyCouponAdapter extends MBaseAdapter<MyCouponResponse.mData> {
    private LayoutInflater mLayoutInflater;

    public MyCouponAdapter(Context context) {
        super(context);
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder mHolder;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.item_my_coupons, null);
            mHolder = new ViewHolder();
            mHolder.mCouponName = (TextView) convertView.findViewById(R.id.coupon_name);
            mHolder.mCouponMinPrice = (TextView) convertView.findViewById(R.id.coupon_min_price);
            mHolder.mCouponPrice = (TextView) convertView.findViewById(R.id.coupon_price);
            mHolder.mCouponEndTime = (TextView) convertView.findViewById(R.id.coupon_end_time);
            mHolder.mCouponUser = (TextView) convertView.findViewById(R.id.coupon_use);
            mHolder.mItem = convertView.findViewById(R.id.layout);
            mHolder.mOver = convertView.findViewById(R.id.img_over);
            convertView.setTag(mHolder);
        } else {
            mHolder = (ViewHolder) convertView.getTag();
        }
        Log.d("**********user", mList.get(position).getIs_use());
        if (mList.get(position).getIs_use().equals("0")){
            if (mList.get(position).getType().equals("1")) {
                mHolder.mItem.setBackgroundResource(R.mipmap.kjuan_bg_class);
                mHolder.mCouponName.setText("课程专享券");
                mHolder.mCouponName.setTextColor(ContextCompat.getColor(mContext, R.color.blue1));
                mHolder.mCouponMinPrice.setTextColor(ContextCompat.getColor(mContext, R.color.blue1));
                mHolder.mOver.setVisibility(View.GONE);
            } else if (mList.get(position).getType().equals("2")) {
                mHolder.mItem.setBackgroundResource(R.mipmap.kjuan_bg_book);
                mHolder.mCouponName.setText("图书专享券");
                mHolder.mCouponName.setTextColor(ContextCompat.getColor(mContext, R.color.red3));
                mHolder.mCouponMinPrice.setTextColor(ContextCompat.getColor(mContext, R.color.red4));
                mHolder.mOver.setVisibility(View.GONE);
            }else if (mList.get(position).getType().equals("0")){
                mHolder.mItem.setBackgroundResource(R.mipmap.kjuan_bg_currency);
                mHolder.mCouponName.setText("全场通用券");
                mHolder.mCouponName.setTextColor(ContextCompat.getColor(mContext, R.color.purple8));
                mHolder.mCouponMinPrice.setTextColor(ContextCompat.getColor(mContext, R.color.purple8));
                mHolder.mOver.setVisibility(View.GONE);
            }
            mHolder.mCouponUser.setText("未使用");
        }else {
            if (mList.get(position).getType().equals("1")) {
                mHolder.mCouponName.setText("课程专享券");
            } else if (mList.get(position).getType().equals("2")) {
                mHolder.mCouponName.setText("图书专享券");
            }else if (mList.get(position).getType().equals("0")){
                mHolder.mCouponName.setText("全场通用券");
            }
            mHolder.mCouponName.setTextColor(ContextCompat.getColor(mContext, R.color.normal7));
            mHolder.mCouponMinPrice.setTextColor(ContextCompat.getColor(mContext, R.color.normal8));
            mHolder.mItem.setBackgroundResource(R.mipmap.kjuan_bg_over);
            mHolder.mOver.setVisibility(View.VISIBLE);
            mHolder.mCouponUser.setText("已过期");
        }
        mHolder.mCouponEndTime.setText("使用期限:"+mList.get(position).getUse_time());
        mHolder.mCouponPrice.setText(mList.get(position).getPrice());
        mHolder.mCouponMinPrice.setText("订单满" + mList.get(position).getMin_price() + "元减" + mList.get(position).getPrice() + "元");
        return convertView;
    }

    class ViewHolder {
        TextView mCouponName;
        TextView mCouponMinPrice;
        TextView mCouponPrice;
        TextView mCouponEndTime;
        TextView mCouponUser;
        LinearLayout mItem;
        ImageView mOver;

    }
}
