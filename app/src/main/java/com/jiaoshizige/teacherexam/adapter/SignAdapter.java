package com.jiaoshizige.teacherexam.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jiaoshizige.teacherexam.R;
import com.jiaoshizige.teacherexam.base.MBaseAdapter;
import com.jiaoshizige.teacherexam.model.SignDialogResponse;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/a1/9.
 * 签到 adapter
 */

public class SignAdapter extends MBaseAdapter<SignDialogResponse.mWeeks> {
    private int mDayNum = 0;
    public SignAdapter(Context context) {
        super(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
    ViewHolder viewHolder = null;
      /*  if(convertView == null){*/
            convertView = mInflater.inflate(R.layout.item_sign_day,null);
            viewHolder = new ViewHolder(convertView);
//            convertView.setTag(viewHolder);
      /*  }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }*/
        if(mList != null && mList.size() > 0){
            viewHolder.mAddOne.setText("+"+mList.get(position).getIntegral());
            viewHolder.mDayNum.setText("第"+(position+1)+"天");
            if(mList.get(position).getIs_sign().equals("1")){
                viewHolder.mAddOne.setTextColor(ContextCompat.getColor(mContext,R.color.white));
                viewHolder.mAddOne.setBackground(ContextCompat.getDrawable(mContext,R.mipmap.home_png_gold_pressed));

                viewHolder.mDayNum.setTextColor(ContextCompat.getColor(mContext,R.color.buy_color));
            }else{
                viewHolder.mAddOne.setTextColor(ContextCompat.getColor(mContext,R.color.text_color6));
                viewHolder.mAddOne.setBackground(ContextCompat.getDrawable(mContext,R.mipmap.home_png_gold_disabled));
            }
        /*    if(position == 6){
                if(mList.get(position).getIs_sign().equals("1")){
                    viewHolder.mAddOne.setText(mList.get(position).getIntegral());
                    viewHolder.mAddOne.setBackground(ContextCompat.getDrawable(mContext,R.mipmap.home_png_envelopes_selected));
                }else{
                    viewHolder.mAddOne.setText("");
                    viewHolder.mAddOne.setBackground(ContextCompat.getDrawable(mContext,R.mipmap.home_png_envelopes_disabled));
                }
            }*/

        }
        return convertView;
    }
    static class ViewHolder{
        @BindView(R.id.add_one)
        TextView mAddOne;
        @BindView(R.id.day_num)
        TextView mDayNum;
        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
